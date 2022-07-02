package devanmejia

import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs

object ImageRepository {

    fun readImage(imageName: String): Mat =
        Imgcodecs.imread("res/images/$imageName")

    fun writeImage(image: Mat, imageName: String) =
        Imgcodecs.imwrite("res/detected/$imageName", image)
}
