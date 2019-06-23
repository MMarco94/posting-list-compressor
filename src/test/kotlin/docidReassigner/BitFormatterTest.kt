package docidReassigner

import kotlin.test.Test
import kotlin.test.assertEquals

class BitFormatterTest {
    @Test
    fun testFormatter() {
        assertEquals("1 bit", formatBit(1))
        assertEquals("2 bits", formatBit(2))
        assertEquals("1 byte", formatBit(8))
        assertEquals("2 bytes", formatBit(16))
        assertEquals("98 bytes", formatBit(780))
        assertEquals("1 KB", formatBit(1024 * 8))
        assertEquals("7 KB", formatBit(7 * 1024 * 8))
        assertEquals("5 MB", formatBit(5 * 1024 * 1024 * 8))
        assertEquals("5 MB, 7 KB", formatBit(5 * 1024 * 1024 * 8 + 7 * 1024 * 8))
    }
}