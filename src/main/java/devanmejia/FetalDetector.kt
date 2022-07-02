package devanmejia

import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc.*


enum class AlgorithmType {
    MORPH_OPEN,
    MORPH_CROSS
}

object FetalDetector {

    fun getFetalImage(image: Mat, type: AlgorithmType): Mat{
        val thresholdImage = generateThresholdImage(image)
        val morphologyImage = generateMorphologyImage(thresholdImage, type)
        val mask = generateMask(morphologyImage)
        return CropService.cropImage(image, mask.getCropParameter())
    }

    private fun generateThresholdImage(image: Mat): Mat {
        val thresholdImage = Mat()
        threshold(image, thresholdImage, 0.0, 255.0, THRESH_BINARY)
        return thresholdImage
    }

    private fun generateMorphologyImage(thresholdImage: Mat, type: AlgorithmType): Mat {
        val morphologyImage = Mat()
        val kernel = Mat(23, 23, 0)
        when (type) {
            AlgorithmType.MORPH_OPEN ->  morphologyEx(thresholdImage, morphologyImage, MORPH_OPEN, kernel)
            AlgorithmType.MORPH_CROSS -> morphologyEx(thresholdImage, morphologyImage, MORPH_CROSS, kernel)
        }
        morphologyEx(thresholdImage, morphologyImage, MORPH_OPEN, kernel)
        return morphologyImage
    }

    private fun generateMask(morphologyImage: Mat): Mat {
        val detectedMask = Mat()
        val detectionKernel = Mat(112, 112, 0)
        erode(morphologyImage, detectedMask, detectionKernel)
        return detectedMask
    }

}

private fun Mat.getCropParameter(): CropParameter {
    val (startX, width) = getXCropParameter()
    val (startY, height) = getYCropParameter()
    return CropParameter(Point(startX, startY), width, height)
}

private fun Mat.getXCropParameter(): Pair<Int, Int> {
    var maxXNeighborsAmount = 0
    var startX = 0
    for (i in 0 until height()){
        var tmpNeighborsAmount = 0
        for (j in 0 until width()){
            if (ColorUtils.isBlack(this[i, j])) {
                if (maxXNeighborsAmount < tmpNeighborsAmount) {
                    maxXNeighborsAmount = tmpNeighborsAmount
                    startX = j - maxXNeighborsAmount
                }
                else {
                    tmpNeighborsAmount = 0
                }
            }
            else {
                tmpNeighborsAmount++
            }
        }
        if (maxXNeighborsAmount < tmpNeighborsAmount) {
            maxXNeighborsAmount = tmpNeighborsAmount
            startX = width() - maxXNeighborsAmount
        }
    }
    return Pair(startX, maxXNeighborsAmount)
}

private fun Mat.getYCropParameter(): Pair<Int, Int> {
    var maxYNeighborsAmount = 0
    var startY = 0
    for (j in 0 until width()){
        var tmpNeighborsAmount = 0
        for (i in 0 until height()){
            if (ColorUtils.isBlack(this[i, j])) {
                if (maxYNeighborsAmount < tmpNeighborsAmount) {
                    maxYNeighborsAmount = tmpNeighborsAmount
                    startY = i - maxYNeighborsAmount
                }
                else {
                    tmpNeighborsAmount = 0
                }
            }
            else {
                tmpNeighborsAmount++
            }
        }
        if (maxYNeighborsAmount < tmpNeighborsAmount) {
            maxYNeighborsAmount = tmpNeighborsAmount
            startY = height() - maxYNeighborsAmount
        }
    }
    return Pair(startY, maxYNeighborsAmount)
}

