package com.wires.app.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import com.github.dhaval2404.imagepicker.ImagePicker

private const val FILE_PATH_KEY = "extra.file_path"
private const val MIME_TYPE_JPG = "image/jpg"
private const val MIME_TYPE_JPEG = "image/jpeg"
private const val MIME_TYPE_PNG = "image/png"
private const val MAX_IMAGE_SIZE = 3072

fun Activity.hideSoftKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun Activity.hideSoftKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.showSoftKeyboard(view: View, showFlags: Int = 0) {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, showFlags)
}

fun Activity.restartApp() {
    val intent = packageManager.getLaunchIntentForPackage(packageName)
    val mainIntent = Intent.makeRestartActivityTask(intent?.component)
    finish()
    startActivity(mainIntent)
}

fun FragmentActivity.pickImage(launcher: ActivityResultLauncher<Intent>, needCrop: Boolean = false) {
    ImagePicker
        .with(this)
        .galleryMimeTypes(
            mimeTypes = arrayOf(
                MIME_TYPE_JPG,
                MIME_TYPE_JPEG,
                MIME_TYPE_PNG
            )
        )
        .compress(MAX_IMAGE_SIZE)
        .apply { if (needCrop) cropSquare() }
        .createIntent(launcher::launch)
}

fun FragmentActivity.handleImagePickerResult(
    result: ActivityResult,
    needPath: Boolean = false,
    onSuccess: (String) -> Unit = { },
    onCancel: () -> Unit = { },
    onFailure: () -> Unit = { },
    onComplete: () -> Unit = { }
) {
    if (result.resultCode == Activity.RESULT_OK) {
        val receivedString = if (needPath) result.data?.extras?.getString(FILE_PATH_KEY) else result.data?.data?.toString()
        receivedString?.let { string ->
            onSuccess(string)
        } ?: run {
            onFailure()
        }
    } else {
        onCancel()
    }
    onComplete()
}
