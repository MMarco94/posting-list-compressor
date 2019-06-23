package docidReassigner

class Document(val id: Long, val words: Set<String>) {

    fun intersect(another: Document): Int = if (another.words.size < words.size) {
        another.intersect(this)
    } else {
        words.count { another.words.contains(it) }
    }

    fun union(another: Document) = words.size + another.words.size - intersect(another)

    override fun equals(other: Any?): Boolean {
        return other is Document && id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
