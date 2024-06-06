package com.example.yandexmaptest.source

import com.yandex.mapkit.geometry.Point
import java.io.Serializable

data class AddressData(
    val name: String,
    val description: String,
    val imageResId: Int,
    val location: Point
) : Serializable