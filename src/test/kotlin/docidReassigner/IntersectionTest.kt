package docidReassigner

import org.junit.Test
import kotlin.random.Random
import kotlin.test.assertEquals

class IntersectionTest {

    private data class Doc(
        val words: Set<String>
    ) {
        val sortedWords: List<String> = words.sorted()
    }

    private fun intersection1(a: Doc, b: Doc): Int {
        return if (b.words.size < a.words.size) {
            intersection1(b, a)
        } else {
            a.words.count { b.words.contains(it) }
        }
    }

    private fun intersection2(a: Doc, b: Doc): Int {
        var aIndex = 0
        var bIndex = 0
        var ret = 0
        while (aIndex < a.sortedWords.size && bIndex < b.sortedWords.size) {
            val aw = a.sortedWords[aIndex]
            val bw = b.sortedWords[bIndex]
            val compareTo = aw.compareTo(bw)
            when {
                compareTo == 0 -> {
                    aIndex++
                    bIndex++
                    ret++
                }
                compareTo < 0 -> aIndex++
                else -> bIndex++
            }
        }
        return ret
    }


    private val dictionary =
        ("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Metus vulputate eu scelerisque felis imperdiet proin fermentum. Quis eleifend quam adipiscing vitae. Cras sed felis eget velit aliquet. Porta non pulvinar neque laoreet suspendisse interdum. Nec feugiat in fermentum posuere. Non tellus orci ac auctor augue mauris augue neque. Augue ut lectus arcu bibendum at varius vel pharetra vel. Libero id faucibus nisl tincidunt. Eu mi bibendum neque egestas congue quisque egestas diam in.\n" +
                "Dolor sit amet consectetur adipiscing elit ut aliquam. Pellentesque elit eget gravida cum sociis. Vitae elementum curabitur vitae nunc sed velit dignissim. Arcu risus quis varius quam quisque id. In hendrerit gravida rutrum quisque non. Non curabitur gravida arcu ac. Enim neque volutpat ac tincidunt vitae semper. Faucibus scelerisque eleifend donec pretium vulputate sapien nec sagittis. Sed euismod nisi porta lorem mollis aliquam ut porttitor. Integer eget aliquet nibh praesent. Metus aliquam eleifend mi in. In mollis nunc sed id semper risus in hendrerit. Convallis tellus id interdum velit laoreet id donec ultrices tincidunt. Est ultricies integer quis auctor elit sed vulputate mi sit. Ultrices gravida dictum fusce ut placerat. Eget nunc scelerisque viverra mauris in. Fermentum iaculis eu non diam phasellus vestibulum lorem sed.\n" +
                "Sed enim ut sem viverra aliquet eget sit amet. Condimentum id venenatis a condimentum. Vestibulum rhoncus est pellentesque elit ullamcorper. At urna condimentum mattis pellentesque id. Nunc pulvinar sapien et ligula ullamcorper. Dignissim convallis aenean et tortor at. Augue ut lectus arcu bibendum at varius. Laoreet id donec ultrices tincidunt arcu non. Sit amet mattis vulputate enim nulla aliquet porttitor lacus. Elit duis tristique sollicitudin nibh sit amet commodo. Ac feugiat sed lectus vestibulum mattis ullamcorper. Sapien et ligula ullamcorper malesuada proin libero nunc. Vel quam elementum pulvinar etiam non quam. Venenatis lectus magna fringilla urna. Vitae tortor condimentum lacinia quis vel eros donec.\n" +
                "Leo urna molestie at elementum. Phasellus egestas tellus rutrum tellus pellentesque eu tincidunt. Eget magna fermentum iaculis eu non diam phasellus vestibulum lorem. Ultrices neque ornare aenean euismod elementum nisi. Viverra aliquet eget sit amet tellus. Amet aliquam id diam maecenas ultricies mi. Nunc non blandit massa enim nec dui nunc mattis. Posuere morbi leo urna molestie at. Turpis egestas integer eget aliquet nibh praesent tristique. Nec ultrices dui sapien eget mi proin sed libero enim. Nunc faucibus a pellentesque sit amet. Vivamus arcu felis bibendum ut tristique. Scelerisque varius morbi enim nunc faucibus a pellentesque sit. Elementum nibh tellus molestie nunc non blandit. Nisl pretium fusce id velit ut. Facilisi morbi tempus iaculis urna id. Rhoncus mattis rhoncus urna neque viverra.\n" +
                "Ac auctor augue mauris augue neque gravida. Odio tempor orci dapibus ultrices in iaculis. Cursus in hac habitasse platea. Proin fermentum leo vel orci porta non. Nec sagittis aliquam malesuada bibendum arcu vitae elementum curabitur vitae. Sed enim ut sem viverra aliquet eget sit amet tellus. Egestas tellus rutrum tellus pellentesque eu tincidunt tortor aliquam nulla. Neque convallis a cras semper auctor neque vitae. Lacus vel facilisis volutpat est. Tristique senectus et netus et.\n" +
                "Auctor augue mauris augue neque. In hendrerit gravida rutrum quisque non tellus. Ipsum faucibus vitae aliquet nec ullamcorper sit. Ullamcorper sit amet risus nullam eget felis eget. Amet venenatis urna cursus eget nunc scelerisque viverra mauris. Eget mi proin sed libero enim sed faucibus turpis. Accumsan tortor posuere ac ut consequat semper viverra. Non diam phasellus vestibulum lorem sed risus ultricies tristique nulla. Sit amet purus gravida quis blandit turpis cursus in hac. Ut faucibus pulvinar elementum integer enim neque volutpat ac. Consequat interdum varius sit amet mattis vulputate. Varius duis at consectetur lorem. Arcu non odio euismod lacinia at quis.\n" +
                "Consectetur lorem donec massa sapien faucibus et. A diam maecenas sed enim ut sem viverra aliquet. Nec nam aliquam sem et tortor consequat. Bibendum enim facilisis gravida neque convallis a cras semper auctor. Adipiscing tristique risus nec feugiat in fermentum. Sed arcu non odio euismod lacinia at quis. Ac tincidunt vitae semper quis lectus. Aliquet enim tortor at auctor urna nunc. Neque sodales ut etiam sit amet. Aenean et tortor at risus viverra. Gravida dictum fusce ut placerat. Risus sed vulputate odio ut enim blandit volutpat maecenas volutpat. Sed faucibus turpis in eu mi bibendum. Sit amet dictum sit amet justo donec enim diam vulputate. Egestas diam in arcu cursus.\n" +
                "Sit amet risus nullam eget felis eget. Scelerisque eu ultrices vitae auctor eu augue ut lectus arcu. Quis varius quam quisque id diam vel quam elementum. In hac habitasse platea dictumst vestibulum rhoncus est pellentesque elit. Netus et malesuada fames ac. Mauris in aliquam sem fringilla ut morbi tincidunt augue. Donec enim diam vulputate ut pharetra sit amet. Vulputate dignissim suspendisse in est ante in nibh. Egestas purus viverra accumsan in nisl nisi scelerisque. Vel fringilla est ullamcorper eget nulla. Praesent tristique magna sit amet.\n" +
                "Et molestie ac feugiat sed lectus vestibulum mattis. Sit amet porttitor eget dolor morbi. Adipiscing elit duis tristique sollicitudin nibh sit amet commodo nulla. Sed risus ultricies tristique nulla aliquet enim tortor. Id consectetur purus ut faucibus pulvinar. Odio ut sem nulla pharetra. Rhoncus dolor purus non enim praesent. Porttitor lacus luctus accumsan tortor posuere ac. Mi sit amet mauris commodo quis. Sit amet nisl suscipit adipiscing bibendum est ultricies. Nulla porttitor massa id neque aliquam vestibulum morbi blandit cursus. Dictumst vestibulum rhoncus est pellentesque elit ullamcorper. Tempor id eu nisl nunc mi ipsum. Magnis dis parturient montes nascetur ridiculus mus mauris. Turpis in eu mi bibendum neque egestas. Ligula ullamcorper malesuada proin libero nunc consequat interdum varius sit. Massa massa ultricies mi quis hendrerit. Mauris in aliquam sem fringilla ut. Condimentum id venenatis a condimentum vitae sapien pellentesque habitant.\n" +
                "Quam id leo in vitae. Lectus urna duis convallis convallis tellus id interdum. Nibh praesent tristique magna sit amet purus gravida. Nec feugiat in fermentum posuere. Porttitor leo a diam sollicitudin. Urna porttitor rhoncus dolor purus non enim praesent elementum facilisis. Mi proin sed libero enim sed. Tincidunt augue interdum velit euismod in pellentesque massa placerat duis. Orci dapibus ultrices in iaculis. Cursus turpis massa tincidunt dui ut ornare lectus sit amet.\n"
                )
            .split("\\s+".toRegex())

    private inline fun time(f: () -> Unit): Long {
        val start = System.nanoTime()
        f()
        return System.nanoTime() - start
    }

    @Test
    fun intersectionSpeed() {
        var time1 = 0L
        var time2 = 0L
        repeat(100000) {
            val doc1 = Doc(dictionary.shuffled().subList(0, Random.nextInt(20, dictionary.size)).toSet())
            val doc2 = Doc(dictionary.shuffled().subList(0, Random.nextInt(20, dictionary.size)).toSet())
            assertEquals(intersection1(doc1, doc2), intersection2(doc1, doc2))
            time1 += time { intersection1(doc1, doc2) }
            time2 += time { intersection2(doc1, doc2) }
        }
        println("1 took $time1")
        println("2 took $time2")
    }
}
