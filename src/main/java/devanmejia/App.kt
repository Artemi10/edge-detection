package devanmejia

import nu.pattern.OpenCV

fun main() {
    OpenCV.loadShared()
    println("Write image file name")
    val imagePath = readLine() ?: throw IllegalArgumentException("Invalid path")
    val image = ImageRepository.readImage(imagePath)
    val ultrasoundContour = UltrasoundImageDetector.findUltraSoundImage(image)
    val croppedImage = CropService.cropImage(image, ultrasoundContour)
    ImageRepository.writeImage(croppedImage, imagePath)
}
