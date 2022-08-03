package com.content.nsfw

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.content.nsfw.ml.SavedModel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Analyzer(context: Context) {
    private var mContext = context

    fun classify(bitmap: Bitmap, callback: (Classifier) -> Unit) {

        val shapedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, false)

        val model = SavedModel.newInstance(mContext)

        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)

        val byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intArray = IntArray(224 * 224)
        shapedBitmap.getPixels(intArray, 0, 224, 0, 0, 224, 224)

        var pixel = 0
        for (i in 0 until 224) {
            for (j in 0 until 224) {
                val p = intArray[pixel++]
                byteBuffer.putFloat((p shr 16 and 0xFF) * (1f / 255f))
                byteBuffer.putFloat((p shr 8 and 0xFF) * (1f / 255f))
                byteBuffer.putFloat((p and 0xFF) * (1f / 255f))
            }
        }

        inputFeature0.loadBuffer(byteBuffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        val confidences = outputFeature0.floatArray

        var max = 0f
        var arrayIndex = 0

        confidences.forEachIndexed { index, fl ->
            if (fl > max) {
                max = fl
                arrayIndex = index
            }
        }
        model.close()
        if (!shapedBitmap.isRecycled) {
            shapedBitmap.recycle()
        }
        when (arrayIndex) {
            0 -> callback(Classifier.DRAWINGS)
            1 -> callback(Classifier.HENTAI)
            2 -> callback(Classifier.NEUTRAL)
            3 -> callback(Classifier.SEXY)
            4 -> callback(Classifier.PORN)
        }
    }

    companion object {
        const val TAG = "AnalyzerTag"
    }
}