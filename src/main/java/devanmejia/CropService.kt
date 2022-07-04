package devanmejia

import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc

object CropService {

    fun cropImage(image: Mat, contour: MatOfPoint): Mat {
        val croppedImage = Mat(image.height(), image.width(), image.type())
        val mask = createCropMask(image, listOf(contour))
        for (i in 0 until image.height()){
            for (j in 0 until image.width()){
                val pixelColor = mask[i, j]
                if (ColorUtils.isWhite(pixelColor)) {
                    croppedImage.put(i, j, *image[i, j])
                }
            }
        }
        return croppedImage
    }

    private fun createCropMask(image: Mat, contours: List<MatOfPoint>): Mat {
        val mask = Mat(image.height(), image.width(), image.type())
        val color = Scalar(ColorUtils.getWhiteColor())
        Imgproc.drawContours(mask, contours, -1, color, -1)
        return mask
    }
}
