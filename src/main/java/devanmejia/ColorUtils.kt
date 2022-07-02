package devanmejia

object ColorUtils {

    fun isBlack(color: DoubleArray) = color[0] == 0.0

    fun getBlackColor() = doubleArrayOf(0.0, 1.0, 0.0)

    fun getColorFromGrayShade(shade: Double) = doubleArrayOf(shade, shade, shade)
}
