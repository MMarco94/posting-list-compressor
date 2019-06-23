package docidReassigner

interface Similarity {
    fun getSimilarity(a: Document, b: Document): Double
}

object JaccardSimilarity : Similarity {
    override fun getSimilarity(a: Document, b: Document): Double {
        return a.intersect(b).toDouble() / a.union(b)
    }

}