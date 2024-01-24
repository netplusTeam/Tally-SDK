@file:OptIn(DelicateCoroutinesApi::class)

package com.netplus.coremechanism.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatEditText
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.util.Calendar
import java.util.regex.Pattern

fun base64ToBitmap(base64String: String): Bitmap? {
    try {
        val decodedBytes: ByteArray = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

fun bitmapToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun AppCompatEditText.addTextChangedListenerAfterTextChanged(action: (String) -> Unit): TextWatcher {
    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            action.invoke(s.toString())
        }
    }

    this.addTextChangedListener(textWatcher)
    return textWatcher
}

fun AppCompatEditText.setTextWithoutTriggerListener(text: CharSequence, textWatcher: TextWatcher) {
    this.removeTextChangedListener(textWatcher)
    this.setText(text)
    this.addTextChangedListener(textWatcher)
}

val listOfCardSchemes = listOf("Visa", "MasterCard", "American Express", "Discover", "Verve")

fun isValidCardNumber(cardNumber: String, cardType: String): Boolean {
    val lengthMap = mapOf(
        "Visa" to setOf(13, 16),
        "MasterCard" to setOf(16),
        "American Express" to setOf(15),
        "Discover" to setOf(16),
        "Verve" to setOf(16) // Update if the actual length is different
    )

    return lengthMap[cardType]?.contains(cardNumber.length) == true
}

fun getCardScheme(cardNumber: String): String {
    val visaPattern = "^4[0-9]{6,}$"
    val mastercardPattern = "^5[1-5][0-9]{5,}$"
    val amexPattern = "^3[47][0-9]{5,}$"
    val discoverPattern = "^6(?:011|5[0-9]{2})[0-9]{3,}$"
    val vervePattern = "^5[0-9]{6,}$" // Assuming Verve uses a pattern similar to MasterCard

    val patterns = mapOf(
        "Visa" to visaPattern,
        "MasterCard" to mastercardPattern,
        "American Express" to amexPattern,
        "Discover" to discoverPattern,
        "Verve" to vervePattern
    )

    for ((scheme, pattern) in patterns) {
        if (Pattern.matches(pattern, cardNumber)) {
            return scheme
        }
    }

    return "Unknown"
}

fun getCardType(cardNumber: String): String {
    return if (Pattern.matches("^4[0-9]{6,}$", cardNumber)) {
        "Visa"
    } else if (Pattern.matches("^5[1-5][0-9]{5,}$", cardNumber)) {
        "MasterCard"
    } else if (Pattern.matches("^3[47][0-9]{5,}$", cardNumber)) {
        "American Express"
    } else if (Pattern.matches("^6(?:011|5[0-9]{2})[0-9]{3,}$", cardNumber)) {
        "Discover"
    } else if (Pattern.matches("^5[0-9]{6,}$", cardNumber)) {
        "Verve"
    } else {
        "Unknown"
    }
}

fun formatCardNumber(cardNumber: String): String {
    val spaceIndices = calculateSpaceIndices(cardNumber.length)
    val formattedCardNumber = StringBuilder()

    for (i in cardNumber.indices) {
        if (i in spaceIndices) {
            formattedCardNumber.append(" - ")
        }
        formattedCardNumber.append(cardNumber[i])
    }

    return formattedCardNumber.toString()
}

fun calculateSpaceIndices(cardNumberLength: Int): List<Int> {
    val spaceIndices = mutableListOf<Int>()

    // Calculate hyphen insertion points for groups of four digits
    for (i in 4 until cardNumberLength step 4) {
        spaceIndices.add(i)
    }

    return spaceIndices
}

fun isValidExpiryDate(month: Int, year: Int): Boolean {
    val currentDate = Calendar.getInstance()
    val currentYear =
        currentDate.get(Calendar.YEAR) % 100 // Get last two digits of the current year
    val currentMonth = currentDate.get(Calendar.MONTH) + 1 // Month is 0-indexed

    return year > currentYear || (year == currentYear && month >= currentMonth)
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun setEditTextListener(editText: AppCompatEditText, nextEditText: AppCompatEditText?) {
    editText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            if (s?.length == 1) {
                nextEditText?.requestFocus()
            }
        }
    })

    editText.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            nextEditText?.requestFocus()
            return@setOnEditorActionListener true
        }
        false
    }
}

fun decodeBase64ToBitmap(base64String: String): Bitmap? {
    try {
        val byteData = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(byteData, 0, byteData.size)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

private fun getBitmapFromImageView(imageView: ImageView): Bitmap? {
    imageView.isDrawingCacheEnabled = true
    imageView.buildDrawingCache()
    val bitmap = Bitmap.createBitmap(imageView.drawingCache)
    imageView.isDrawingCacheEnabled = false
    return bitmap
}


fun saveImageToGallery(context: Context, imageView: ImageView) {
    val bitmap = getBitmapFromImageView(imageView)

    if (bitmap != null) {
        GlobalScope.launch(Dispatchers.IO) {
            val uri = saveImageToMediaStore(context, bitmap)
            // You can perform any additional actions here, like showing a toast or updating UI
            //Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show()
        }
    } else {
        // Handle the case when the bitmap is null (e.g., if the ImageView doesn't have an image)
        //Toast.makeText(context, "Unable to find QRCODE", Toast.LENGTH_SHORT).show()
    }
}

private fun saveImageToMediaStore(context: Context, bitmap: Bitmap): String? {
    val contentResolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "barcode_image")
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    }

    val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    try {
        if (uri != null) {
            val outputStream: OutputStream? = contentResolver.openOutputStream(uri)
            if (outputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }

    return uri?.toString()
}