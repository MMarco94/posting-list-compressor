package docidReassigner

/**
 * An interface that computes the similarity between two documents
 */
interface Similarity {
    fun getSimilarity(a: Document, b: Document): Double
}

/**
 * An object that computes the Jaccard similarity between two documents
 */
object JaccardSimilarity : Similarity {
    override fun getSimilarity(a: Document, b: Document): Double {
        return a.intersect(b).toDouble() / a.union(b)
    }

}