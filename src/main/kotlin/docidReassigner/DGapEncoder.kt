package docidReassigner

import kotlin.math.ceil
import kotlin.math.log2
import kotlin.math.pow

/**
 * An interface that calculates how many bits it takes to encode a gap
 */
interface DGapEncoder {
    fun getBitTaken(gap: Long): Long
}

/**
 * An object that encodes gaps using a fixed size of 24 bits
 */
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

/**
 * An object that applies the VBCode to encode gaps
 */
object VBCode : DGapEncoder {
    override fun getBitTaken(gap: Long): Long {
        if (gap < 0) throw IllegalStateException()
        if (gap == 0L) return 8
        val bitCount = ceil(log2(gap + 1.0)).toLong()
        val requiredBytes = ceil(bitCount / 7.0).toLong()
        return requiredBytes * 8
    }
}

/**
 * An object that applies the unary code to encode gaps. Used in [EliasGammaCode]
 */
object UnaryCode : DGapEncoder {
    override fun getBitTaken(gap: Long): Long {
        if (gap < 0) throw IllegalStateException()
        return if (gap == 0L) 0L
        else gap + 1
    }
}

/**
 * An abstract class to perform both the Elias code
 */
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

/**
 * An object that applies the Elias Gamma code to encode gaps.
 */
object EliasGammaCode : AbstractEliasCode(UnaryCode)

/**
 * An object that applies the Elias Delta code to encode gaps.
 */
object EliasDeltaCode : AbstractEliasCode(EliasGammaCode)