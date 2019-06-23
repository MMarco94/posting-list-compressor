package docidReassigner

import org.jgrapht.graph.DefaultUndirectedWeightedGraph
import org.jgrapht.graph.DefaultWeightedEdge
import kotlin.random.Random
import kotlin.test.Test

class GraphTest {

    private class Node

    @Test
    fun instantiationTest() {
        val nodes = Array(5000) { Node() }
        DefaultUndirectedWeightedGraph<Node, DefaultWeightedEdge>(DefaultWeightedEdge::class.java).apply {
            nodes.forEach { addVertex(it) }
            nodes.forEachIndexed { index, n1 ->
                nodes.drop(index + 1).forEach { n2 ->
                    setEdgeWeight(addEdge(n1, n2), Random.nextDouble() * 1000)
                }
            }
        }
    }
}