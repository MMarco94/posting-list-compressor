package docidReassigner

/**
 * A class that represents a document. A document has an id and a set of words
 */
class Document(val id: Long, val words: Set<String>) {

    /**
     * Compute how many terms appear on both documents
     */
    fun intersect(another: Document): Int = if (another.words.size < words.size) {
        another.intersect(this)
    } else {
        words.count { another.words.contains(it) }
    }

    /**
     * Compute how many terms appear on at least one of the two documents
     */
    fun union(another: Document) = words.size + another.words.size - intersect(another)

    override fun equals(other: Any?): Boolean {
        return other is Document && id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
