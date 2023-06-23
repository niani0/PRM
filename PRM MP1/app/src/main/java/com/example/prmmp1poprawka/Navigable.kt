package com.example.prmmp1poprawka

interface Navigable {
    enum class Destination {
        List, Add, Info
    }

    fun navigate(to: Destination)
}