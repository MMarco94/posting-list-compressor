package docidReassigner

import java.io.File
import java.net.URL
import java.time.Duration
import java.time.Instant

val documentsRoot = File("docs")
val parser = SmartDocumentParser
val similarity = JaccardSimilarity
val clusterer = SinglePassClusterer(0.10, 2500)
val sources = sequenceOf(
    GzippedSource(File(documentsRoot, "doc1"), URL("http://www.ai.mit.edu/projects/jmlr/papers/volume5/lewis04a/a12-token-files/lyrl2004_tokens_test_pt0.dat.gz")),
    GzippedSource(File(documentsRoot, "doc2"), URL("http://www.ai.mit.edu/projects/jmlr/papers/volume5/lewis04a/a12-token-files/lyrl2004_tokens_test_pt1.dat.gz")),
    GzippedSource(File(documentsRoot, "doc3"), URL("http://www.ai.mit.edu/projects/jmlr/papers/volume5/lewis04a/a12-token-files/lyrl2004_tokens_test_pt2.dat.gz")),
    GzippedSource(File(documentsRoot, "doc4"), URL("http://www.ai.mit.edu/projects/jmlr/papers/volume5/lewis04a/a12-token-files/lyrl2004_tokens_test_pt3.dat.gz"))
)
val documents = sources.map {
    it.getDataStream()
}.flatMap {
    parser.parse(it)
}.toList().asSequence()
val encodings = setOf(VBCode, EliasGammaCode, EliasDeltaCode)

fun main() {
    //println("Documents count = ${documents.count()}") //Count = 781265
    println("Computing initial d-gap...")
    val initialSize = time {
        encodings.associateWith { e ->
            computePostings(IdentityRemapper, e).totalSize
        }
    }
    initialSize.forEach { (e, size) ->
        println("-${e.javaClass.simpleName} would use ${formatBit(size)}")
    }
    repeat(2) { println() }

    println("Computing clusters...")
    val clusters = time {
        clusterer.cluster(documents, similarity)
    }
    println("Found ${clusters.size} clusters")

    println("Computing TSP...")
    val tspRemapper = time { TSPRemapper(clusters, similarity) }
    println("Computing new d-gap...")
    time {
        encodings.forEach { e ->
            val postings = computePostings(tspRemapper, e)
            println("-${e.javaClass.simpleName} would use ${formatBit(postings.totalSize)}\t\tSaved ${formatBit(initialSize.getValue(e) - postings.totalSize)}")
        }
    }
}

private fun computePostings(idRemapper: DocumentIdRemapper, encoder: DGapEncoder): DgapPostingComputer {
    val posting = DgapPostingComputer(encoder)
    documents
        .map { idRemapper.remap(it) }
        .sortedBy { it.id }
        .flatMap { doc ->
            doc.words.distinct().map { term -> doc.id to term }.asSequence()
        }.forEach { (doc, term) ->
            posting.addPosting(doc, term)
        }
    return posting
}

private fun <T> time(f: () -> T): T {
    val start = Instant.now()
    val ret = f()
    val took = Duration.between(start, Instant.now())
    println("Took $took")
    return ret
}
