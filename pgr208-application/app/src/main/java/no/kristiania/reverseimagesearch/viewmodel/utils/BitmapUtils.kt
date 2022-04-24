package no.kristiania.reverseimagesearch.viewmodel.utils

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import android.util.Log
import androidx.core.content.ContextCompat
import java.io.*
import java.util.*
import java.util.zip.Deflater
import java.util.zip.Inflater
import kotlin.math.roundToInt
import kotlin.math.sqrt

class BitmapUtils {

    companion object {

        //CODE FROM BORIS MOCIALOV
        fun VectorDrawableToBitmap(context: Context, id: Int?, uri: String?): Bitmap {
            val drawable = (ContextCompat.getDrawable(context!!, id!!) as VectorDrawable)
            val image = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(image)
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
            drawable.draw(canvas)

            return image
        }

        fun UriToBitmap(context: Context, id: Int?, uri: String?): Bitmap {
            val image: Bitmap =
                MediaStore.Images.Media.getBitmap(context.contentResolver, Uri.parse(uri))
            return image
        }

        fun getBitmap(
            context: Context,
            id: Int?,
            uri: String?,
            decoder: (Context, Int?, String?) -> Bitmap
        ): Bitmap {
            return decoder(context, id, uri)
        }

        fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }

        fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
            val outputStream = ByteArrayOutputStream()

            val compressedBitmap = compressBitmap(bitmap, (640*480))

            compressedBitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
            return outputStream.toByteArray()
        }

        //From https://github.com/Vysh01/AndroidImageResizer/blob/master/ImageResizer.java
        fun compressBitmap(bitmap: Bitmap, MAX_SIZE : Int): Bitmap {

            val ratioSquare = ((bitmap.height * bitmap.width)/ MAX_SIZE).toDouble()

            if(ratioSquare <= 1){
                return bitmap
            }

            val ratio = sqrt(ratioSquare)
            Log.d("RATIO", ratio.toString())

            return Bitmap.createScaledBitmap(bitmap, (bitmap.width / ratio).roundToInt(), (bitmap.height/ratio).roundToInt(), true)
        }


        //FROM https://stackoverflow.com/questions/7769806/convert-bitmap-to-file
        fun bitmapToFile(bitmap: Bitmap, filename: String, context: Context): File {

            val file = File(context.getCacheDir(), filename)
            file.createNewFile()

            val bitmapdata = bitmapToByteArray(bitmap)

            val fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(bitmapdata)
            fileOutputStream.flush()
            fileOutputStream.close()

            return file
        }


    }
}