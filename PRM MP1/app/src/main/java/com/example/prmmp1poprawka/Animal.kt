package com.example.prmmp1poprawka

import android.support.annotation.DrawableRes

data class Animal(
    val id: Int,
    val name: String,
    val latName: String,
    @DrawableRes
    val resId: Int,
    val info: String
)
