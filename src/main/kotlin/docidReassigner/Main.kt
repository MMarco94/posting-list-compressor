package docidReassigner

import java.io.File
import java.net.URL
import java.time.Duration
import java.time.Instant

/**
 * The similarity algorithm to use
 */
val similarity = JaccardSimilarity
/**
 * The clustering algorithm to use
 */
val clusterer = SinglePassClusterer(0.10, 2500)
/**
 * The sources for the documents
 */
val sources = sequenceOf(
    GzippedSource(File("docs/doc1"), URL("http://www.ai.mit.edu/projects/jmlr/papers/volume5/lewis04a/a12-token-files/lyrl2004_tokens_test_pt0.dat.gz")),
    GzippedSource(File("docs/doc2"), URL("http://www.ai.mit.edu/projects/jmlr/papers/volume5/lewis04a/a12-token-files/lyrl2004_tokens_test_pt1.dat.gz")),
    GzippedSource(File("docs/doc3"), URL("http://www.ai.mit.edu/projects/jmlr/papers/volume5/lewis04a/a12-token-files/lyrl2004_tokens_test_pt2.dat.gz")),
    GzippedSource(File("docs/doc4"), URL("http://www.ai.mit.edu/projects/jmlr/papers/volume5/lewis04a/a12-token-files/lyrl2004_tokens_test_pt3.dat.gz"))
)
/**
 * The limit on how many document to consider.
 */
const val limit = Integer.MAX_VALUE
/**
 * All the documents
 */
val documents = sources.map {
    it.getDataStream()
}.flatMap {
    SmartDocumentParser.parse(it)
}.take(limit).toList().asSequence()
/**
 * The encodings to measure
 */
val encodings = setOf(VBCode, EliasGammaCode, EliasDeltaCode)

/**
 * This is the main. It downloads the documents (if needed) and computes the initial positing lists size.
 * Then performs the clustering, the TSP, and computes the new posting lists size.
 *
 * It may take several hours to complete. To try on a smaller number of documents, [limit] can be decreased
 */
fun main() {
    println("Computing initial d-gap...")
    val initialSize = time {
        encodings.associateWith { e ->
            computePostingListsSize(IdentityRemapper, e)
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
            val postingsSize = computePostingListsSize(tspRemapper, e)
            println("-${e.javaClass.simpleName} would use ${formatBit(postingsSize)}\t\tSaved ${formatBit(initialSize.getValue(e) - postingsSize)}")
        }
    }
}

/**
 * Given a [DocumentIdRemapper] and a [DGapEncoder], computes the size of the posting lists
 */
private fun computePostingListsSize(idRemapper: DocumentIdRemapper, encoder: DGapEncoder): Long {
    val posting = DgapPostingComputer(encoder)
    documents
        .map { idRemapper.remap(it) }
        .sortedBy { it.id }
        .flatMap { doc ->
            doc.words.distinct().map { term -> doc.id to term }.asSequence()
        }.forEach { (doc, term) ->
            posting.addPosting(doc, term)
        }
    return posting.totalSize
}

/**
 * Measures the time it takes to execute [f]
 */
private fun <T> time(f: () -> T): T {
    val start = Instant.now()
    val ret = f()
    val took = Duration.between(start, Instant.now())
    println("Took $took")
    return ret
}
