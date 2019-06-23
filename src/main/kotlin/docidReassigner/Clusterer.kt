package docidReassigner

interface Clusterer {
    fun cluster(documents: Sequence<Document>, similarity: Similarity): Set<DocumentGroup>
}

data class DocumentGroup(val leader: Document, val allDocuments: Set<Document>)

class SinglePassClusterer(val minSimilarity: Double, val maxClusterCount: Int) : Clusterer {

    override fun cluster(documents: Sequence<Document>, similarity: Similarity): Set<DocumentGroup> {
        val ret = HashMap<Document, MutableSet<Document>>()
        documents
            .sortedByDescending { it.words.size }
            .forEachIndexed { index, d ->
                val closer = ret.keys.maxBy { similarity.getSimilarity(it, d) }
                when {
                    closer == null -> ret[d] = mutableSetOf(d)
                    ret.size < maxClusterCount && similarity.getSimilarity(closer, d) < minSimilarity -> {
                        ret[d] = mutableSetOf(d)
                        //println("$index) Creating ${ret.size}th cluster, since similarity is ${similarity.getSimilarity(closer, d)} < $minSimilarity")
                    }
                    else -> ret.getValue(closer).add(d)
                }
            }
        return ret.mapTo(HashSet()) { (leader, docs) -> DocumentGroup(leader, docs) }
    }

}