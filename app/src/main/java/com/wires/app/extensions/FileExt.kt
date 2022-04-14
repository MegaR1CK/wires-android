package com.wires.app.extensions

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

private const val PART_MEDIA_TYPE = "multipart/form-data"

fun File.toMultipartPart(partName: String) =
    MultipartBody.Part.createFormData(partName, name, asRequestBody(PART_MEDIA_TYPE.toMediaType()))
