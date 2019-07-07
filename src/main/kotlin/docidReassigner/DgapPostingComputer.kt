package docidReassigner

/**
 * A class that computes the size of the posting lists using the given [DGapEncoder]
 */
class DgapPostingComputer(
    val encoder: DGapEncoder
) {

    private val lastDocumentIdForTerm = HashMap<String, Long>()
    var totalSize = 0L
        private set

    fun addPosting(docId: Long, term: String) {
        val last = lastDocumentIdForTerm[term]
        if (last != null) {
            totalSize += encoder.getBitTaken(docId - last)
        }
        lastDocumentIdForTerm[term] = docId
    }
}