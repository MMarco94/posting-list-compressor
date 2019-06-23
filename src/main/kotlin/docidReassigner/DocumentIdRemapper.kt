package docidReassigner


interface DocumentIdRemapper {
    fun remap(document: Document): Document
}

object IdentityRemapper : DocumentIdRemapper {
    override fun remap(document: Document) = document
}

class TSPRemapper(
    val clusters: Set<DocumentGroup>,
    val similarity: Similarity
) : DocumentIdRemapper {

    private val oldIdToNewIdMap: Map<Document, Long> = run {
        val tsp = tspSolver {
            clusters.forEach {
                addVertex(it)
            }
            clusters.forEachIndexed { index, group ->
                clusters.drop(index + 1).forEach { otherGroup ->
                    setEdgeWeight(addEdge(group, otherGroup), similarity.getSimilarity(group.leader, otherGroup.leader))
                }
            }
        }
        val documentSequence = tsp.flatMap {
            it.allDocuments.asSequence()
        }
        documentSequence.withIndex().associate { (newIndex, docId) ->
            docId to newIndex.toLong()
        }
    }

    override fun remap(document: Document): Document {
        return Document(oldIdToNewIdMap.getValue(document), document.words)
    }
}