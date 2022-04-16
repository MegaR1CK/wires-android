package com.wires.app.data.mapper

import com.wires.app.data.model.Image
import com.wires.app.data.model.ImageSize
import com.wires.app.data.remote.response.ImageResponse
import com.wires.app.data.remote.response.ImageSizeResponse
import javax.inject.Inject

class ImagesMapper @Inject constructor() {

    fun fromResponseToModel(imageSizeResponse: ImageSizeResponse): ImageSize {
        return ImageSize(
            width = imageSizeResponse.width,
            height = imageSizeResponse.height
        )
    }

    fun fromResponseToModel(imageResponse: ImageResponse): Image {
        return Image(
            url = imageResponse.url,
            size = fromResponseToModel(imageResponse.size)
        )
    }
}
