package devanmejia

import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.imgproc.Imgproc

object UltrasoundImageDetector {

    fun findUltraSoundImage(image: Mat): MatOfPoint {
        val grayImage = generateGrayImage(image)
        val mask = generateThresholdImage(grayImage)
        val points = mutableListOf<MatOfPoint>()
        Imgproc.findContours(mask, points, Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE)
        var maxIndex = -1
        var maxContourArea = -1.0
        for (i in points.indices) {
            val area = Imgproc.contourArea(points[i])
            if (area > maxContourArea) {
                maxContourArea = area
                maxIndex = i
            }
        }
        return points[maxIndex]
    }

    private fun generateGrayImage(image: Mat): Mat {
        val grayImage = Mat()
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_RGB2GRAY)
        return grayImage
    }

    private fun generateThresholdImage(image: Mat): Mat {
        val thresholdImage = Mat()
        Imgproc.threshold(image, thresholdImage, 0.0, 255.0, Imgproc.THRESH_TRIANGLE)
        return thresholdImage
    }

}
