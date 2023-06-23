package com.example.myapplication.data.model

import android.graphics.Path

data class PathWithSettings(
    val path: Path = Path(),
    val color: Int,
    val size: Float
)
