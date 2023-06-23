package com.example.myapplication.interfaces

interface Navigable {
    enum class Destination {
        List, Add, Edit, Paint, Map
    }

    fun navigate(to: Destination, id: Int? = null)
}