package devanmejia

import nu.pattern.OpenCV
import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc.*


fun main() {
    OpenCV.loadShared()
    println("Write image file name")
    val imagePath = readLine() ?: throw IllegalArgumentException("Invalid path")
    val image = ImageRepository.readImage(imagePath)
    val fetalImage = FetalDetector.getFetalImage(image)
    ImageRepository.writeImage(fetalImage, imagePath)
}

object FetalDetector {

    fun getFetalImage(image: Mat): Mat{
        val thresholdImage = generateThresholdImage(image)
        val morphologyImage = generateMorphologyImage(thresholdImage)
        val mask = generateMask(morphologyImage)
        return addMask(image, mask)
    }

    private fun generateThresholdImage(image: Mat): Mat {
        val thresholdImage = Mat()
        threshold(image, thresholdImage, 0.0, 255.0, THRESH_BINARY)
        return thresholdImage
    }

    private fun generateMorphologyImage(thresholdImage: Mat): Mat {
        val morphologyImage = Mat()
        val kernel = Mat(23, 23, 0)
        morphologyEx(thresholdImage, morphologyImage, MORPH_CROSS, kernel)
        return morphologyImage
    }

    private fun generateMask(morphologyImage: Mat): Mat {
        val detectedMask = Mat()
        val detectionKernel = Mat(112, 112, 0)
        erode(morphologyImage, detectedMask, detectionKernel)
        return detectedMask
    }

    private fun addMask(image: Mat, mask: Mat): Mat {
        val detectedImage = Mat(image.height(), image.width(), image.type())
        for (i in 0 until mask.height()){
            for (j in 0 until mask.width()){
                if (ColorUtils.isBlack(mask[i, j])) detectedImage.put(i, j, *ColorUtils.getBlackColor())
                else detectedImage.put(i, j, *ColorUtils.getColorFromGrayShade(image[i, j][0]))
            }
        }
        return detectedImage
    }
}

object ColorUtils {

    fun isBlack(color: DoubleArray) = color[0] == 0.0

    fun getBlackColor() = doubleArrayOf(0.0, 1.0, 0.0)

    fun getColorFromGrayShade(shade: Double) = doubleArrayOf(shade, shade, shade)
}

object ImageRepository {

    fun readImage(imageName: String): Mat =
        Imgcodecs.imread("res/images/$imageName")

    fun writeImage(image: Mat, imageName: String) =
        Imgcodecs.imwrite("res/detected/$imageName", image)
}

