package devanmejia

import org.opencv.core.Mat

object CropService {

    fun cropImage(image: Mat, cropParameter: CropParameter): Mat {
        val croppedImage = Mat(cropParameter.height, cropParameter.width, image.type())
        val startPoint = cropParameter.starPoint
        for (i in 0 until cropParameter.height){
            for (j in 0 until cropParameter.width){
                val pixelColor = image[i + startPoint.y, j + startPoint.x][0]
                croppedImage.put(i, j, *ColorUtils.getColorFromGrayShade(pixelColor))
            }
        }
        return croppedImage
    }

}

data class CropParameter(val starPoint: Point, val width: Int, val height: Int)

data class Point(val x: Int, val y: Int)
