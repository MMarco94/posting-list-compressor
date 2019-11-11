package docidReassigner

import kotlin.test.Test
import kotlin.test.assertEquals

class DGapEncoderTest {
    @Test
    fun test() {
        //VBCode
        assertEquals(8, VBCode.getBitTaken(0))
        assertEquals(8, VBCode.getBitTaken(1))
        assertEquals(8, VBCode.getBitTaken(127))
        assertEquals(16, VBCode.getBitTaken(128))
        assertEquals(24, VBCode.getBitTaken(214577))

        //Unary
        assertEquals(2, UnaryCode.getBitTaken(1))

        //Elias gamma
        assertEquals(1, EliasGammaCode.getBitTaken(1))
        assertEquals(3, EliasGammaCode.getBitTaken(2))
        assertEquals(3, EliasGammaCode.getBitTaken(3))
        assertEquals(5, EliasGammaCode.getBitTaken(4))
        assertEquals(7, EliasGammaCode.getBitTaken(9))
        assertEquals(7, EliasGammaCode.getBitTaken(13))
        assertEquals(9, EliasGammaCode.getBitTaken(24))
        assertEquals(17, EliasGammaCode.getBitTaken(511))
        assertEquals(21, EliasGammaCode.getBitTaken(1025))

        //Elias delta
        assertEquals(5, EliasGammaCode.getBitTaken(7))

    }
}