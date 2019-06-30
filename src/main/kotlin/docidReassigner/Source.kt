package docidReassigner

import java.io.File
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.GZIPInputStream

interface Source {
    fun getDataStream(): InputStream
}

class GzippedSource(
    val file: File,
    val url: URL
) : Source {
    override fun getDataStream(): InputStream {
        if (!file.exists()) {
            file.parentFile.mkdirs()
            println("Downloading $url...")
            val conn = url.openConnection() as HttpURLConnection
            GZIPInputStream(conn.inputStream).use { input ->
                file.outputStream().use { fileOut ->
                    input.copyTo(fileOut)
                }
            }
        }
        return file.inputStream()
    }

}