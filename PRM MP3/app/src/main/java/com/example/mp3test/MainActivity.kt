package com.example.mp3test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mp3test.fragments.ListFragment
import com.example.mp3test.fragments.LoginFragment
import com.example.mp3test.fragments.SignupFragment
import com.example.mp3test.interfaces.Navigable

class MainActivity : AppCompatActivity(),Navigable {
    private lateinit var loginFragment: LoginFragment
    private lateinit var listFragment: ListFragment
    private lateinit var signupFragment: SignupFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginFragment = LoginFragment()
        listFragment = ListFragment()
        signupFragment = SignupFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.container, loginFragment, loginFragment.javaClass.name)
            .commit()
    }

    override fun navigate(to: Navigable.Destination) {
        supportFragmentManager.beginTransaction().apply {
            when (to) {
                Navigable.Destination.Login -> {
                    replace(R.id.container, loginFragment, loginFragment.javaClass.name)
                    addToBackStack(loginFragment.javaClass.name)
                }
                Navigable.Destination.List -> {
                    replace(R.id.container, listFragment, listFragment.javaClass.name)
                    addToBackStack(listFragment.javaClass.name)
                }
                Navigable.Destination.Signup -> {
                    replace(R.id.container, signupFragment, signupFragment.javaClass.name)
                    addToBackStack(signupFragment.javaClass.name)
                }
            }.commit()
        }
    }
}