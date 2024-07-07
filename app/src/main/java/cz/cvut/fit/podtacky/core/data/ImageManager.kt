package cz.cvut.fit.podtacky.core.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun createImageFile(context: Context): Uri {
    val storageDir: File? = context.getExternalFilesDir(null)
    File.createTempFile(
        "JPEG_${System.currentTimeMillis()}_",
        ".jpg",
        storageDir
    ).apply {
        return FileProvider.getUriForFile(
            context,
            "cz.cvut.cz.tonda2.podtacky.provider",
            this
        )
    }
}

fun compressImage(
    context: Context,
    originalImageUri: Uri,
    compressRate: Double = 0.25
): Uri {
    val compressedImageUri = createImageFile(context)

    val inputStream = context.contentResolver.openInputStream(originalImageUri)
    val originalBitmap = BitmapFactory.decodeStream(inputStream)
    inputStream?.close()

    val rotatedBitmap = fixBitmapRotation(context, originalImageUri, originalBitmap)
    val newW = rotatedBitmap.width * compressRate
    val newH = rotatedBitmap.height * compressRate
    val resizedBitmap = Bitmap.createScaledBitmap(rotatedBitmap, newW.toInt(), newH.toInt(), true)

    val outputStream = context.contentResolver.openOutputStream(compressedImageUri)!!
    outputStream.use {
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
    }
    resizedBitmap.recycle()
    originalBitmap.recycle()
    rotatedBitmap.recycle()

    context.contentResolver.delete(originalImageUri, null, null)

    return compressedImageUri
}

private fun fixBitmapRotation(
    context: Context,
    originalImageUri: Uri,
    originalBitmap: Bitmap
): Bitmap {
    val exifInputStream = context.contentResolver.openInputStream(originalImageUri)
    val exif = ExifInterface(exifInputStream!!)
    val orientation =
        exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
    exifInputStream.close()

    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(originalBitmap, 90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(originalBitmap, 180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(originalBitmap, 270f)
        else -> originalBitmap
    }
}

private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
    val matrix = Matrix().apply {
        postRotate(degrees)
    }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}