package com.example.mp3test.interfaces

interface Navigable {
    enum class Destination {
        Login, List, Signup
    }

    fun navigate(to: Destination)
}