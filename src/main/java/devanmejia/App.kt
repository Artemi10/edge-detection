package devanmejia

import nu.pattern.OpenCV

fun main() {
    OpenCV.loadShared()
    println("Write image file name")
    val imagePath = readLine() ?: throw IllegalArgumentException("Invalid path")
    val image = ImageRepository.readImage(imagePath)
    val crossFetalImage = FetalDetector.getFetalImage(image, AlgorithmType.MORPH_CROSS)
    val fetalImage = FetalDetector.getFetalImage(crossFetalImage, AlgorithmType.MORPH_OPEN)
    ImageRepository.writeImage(fetalImage, imagePath)
}
