package docidReassigner

import java.io.InputStream

/**
 * An interface that parses document that come from an [InputStream]
 */
interface DocumentParser {
    fun parse(input: InputStream): Sequence<Document>
}

/**
 * A [DocumentParser] that parses documents in SMART format
 */
object SmartDocumentParser : DocumentParser {
    override fun parse(input: InputStream): Sequence<Document> {
        return input.bufferedReader().readText().split("\n\n".toRegex())
            .asSequence()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { document ->
                val lines = document.split("\n".toRegex())
                if (lines.size < 3 || !lines[0].startsWith(".I ") || lines[1] != ".W") {
                    throw IllegalStateException("Invalid document $document")
                }
                Document(
                    id = lines[0].removePrefix(".I ").toLong(),
                    words = lines
                        .drop(2)//The first two lines don't contain words
                        .flatMap { it.split("\\s+".toRegex()) }
                        .filter { it.isNotBlank() }
                        .mapTo(HashSet()) { it.intern() }//Using the internal representation, so Strings are kept in memory only once
                )
            }
    }

}