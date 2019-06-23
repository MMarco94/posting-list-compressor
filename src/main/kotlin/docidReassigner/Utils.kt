package docidReassigner

import org.jgrapht.alg.tour.ChristofidesThreeHalvesApproxMetricTSP
import org.jgrapht.graph.DefaultUndirectedWeightedGraph
import org.jgrapht.graph.DefaultWeightedEdge
import kotlin.math.ceil

internal fun tspSolver(nodeSetupper: DefaultUndirectedWeightedGraph<DocumentGroup, DefaultWeightedEdge>.() -> Unit): Sequence<DocumentGroup> {
    val graph = DefaultUndirectedWeightedGraph<DocumentGroup, DefaultWeightedEdge>(DefaultWeightedEdge::class.java).apply(nodeSetupper)
    return ChristofidesThreeHalvesApproxMetricTSP<DocumentGroup, DefaultWeightedEdge>()
        .getTour(graph)
        .vertexList
        .asSequence()
}

private val units = listOf("bytes", "KB", "MB", "GB")
internal fun formatBit(bits: Long): String {
    if (bits < 0) return "-" + formatBit(-bits)
    if (bits == 1L) return "1 bit"
    if (bits < 8) return "$bits bits"
    var bytes = ceil(bits / 8.0).toLong()

    val ret = mutableListOf<String>()
    for (unit in units) {
        val quantity = bytes % 1024
        if (quantity > 0) {
            val u = if (quantity == 1L && unit.endsWith('s')) {
                unit.removeSuffix("s")
            } else unit
            ret.add(0, "$quantity $u")
        }
        if (bytes < 1024) {
            break
        }
        bytes /= 1024
    }
    return ret.joinToString(", ")
}
