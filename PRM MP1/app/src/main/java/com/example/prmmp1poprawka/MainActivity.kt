package com.example.prmmp1poprawka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.prmmp1poprawka.Navigable.Destination.*

class MainActivity : AppCompatActivity(), Navigable {
    private lateinit var listFragment: ListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listFragment = ListFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.container, listFragment, listFragment.javaClass.name)
            .commit()
    }


    override fun navigate(to: Navigable.Destination) {
        supportFragmentManager.beginTransaction().apply {
            when (to) {
                List -> {
                    replace(R.id.container, listFragment, listFragment.javaClass.name)
                    addToBackStack(listFragment.javaClass.name)
                }
                Add -> {
                    replace(R.id.container, EditFragment(), EditFragment::class.java.name)
                    addToBackStack(EditFragment::class.java.name)
                }
                Info -> {
                    replace(R.id.container, InfoFragment(), InfoFragment::class.java.name)
                    addToBackStack(InfoFragment::class.java.name)
                }

            }.commit()
        }
    }

}