package devanmejia

object ColorUtils {

    fun isWhite(color: DoubleArray) =
        color[0] == 255.0 && color[1] == 255.0 && color[2] == 255.0

    fun getWhiteColor() = doubleArrayOf(255.0, 255.0, 255.0)
}
