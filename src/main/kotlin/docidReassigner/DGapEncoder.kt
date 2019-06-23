package docidReassigner

import kotlin.math.ceil
import kotlin.math.log2
import kotlin.math.pow

interface DGapEncoder {
    fun getBitTaken(gap: Long): Long
}

object FixedSize24 : DGapEncoder {
    val maxGap = 2.0.pow(24).toInt()

    override fun getBitTaken(gap: Long): Long {
        if (gap < 0) throw IllegalStateException()
        if (gap > maxGap) {
            throw UnsupportedOperationException("Cannot represent a gap larger than $maxGap")
        }
        return 24
    }
}

object VBCode : DGapEncoder {
    override fun getBitTaken(gap: Long): Long {
        if (gap < 0) throw IllegalStateException()
        if (gap == 0L) return 8
        val bitCount = ceil(log2(gap + 1.0)).toLong()
        val requiredBytes = ceil(bitCount / 7.0).toLong()
        return requiredBytes * 8
    }
}

object UnaryCode : DGapEncoder {
    override fun getBitTaken(gap: Long): Long {
        if (gap < 0) throw IllegalStateException()
        return if (gap == 0L) 0L
        else gap + 1
    }
}

abstract class AbstractEliasCode(
    val lengthEncoder: DGapEncoder
) : DGapEncoder {
    override fun getBitTaken(gap: Long): Long {
        if (gap < 0) throw IllegalStateException()
        if (gap == 0L) {
            throw UnsupportedOperationException("Cannot encode 0")
        }
        val offset = ceil(log2(gap + 1.0)).toInt() - 1
        val length = if (offset == 0) 0 else lengthEncoder.getBitTaken(offset.toLong())
        return offset + length
    }
}

object EliasGammaCode : AbstractEliasCode(UnaryCode)
object EliasDeltaCode : AbstractEliasCode(EliasGammaCode)