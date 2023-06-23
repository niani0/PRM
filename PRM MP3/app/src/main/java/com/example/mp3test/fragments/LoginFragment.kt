package com.example.mp3test.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mp3test.data.DataSource
import com.example.mp3test.data.UserDatabase
import com.example.mp3test.data.UserEntity
import com.example.mp3test.databinding.FragmentLoginBinding
import com.example.mp3test.interfaces.Navigable
import com.google.firebase.auth.FirebaseAuth
import kotlin.concurrent.thread

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: UserDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        db = UserDatabase.open(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentLoginBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {

        binding.loginBtn.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Signed in successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            DataSource.email = email
                            thread {
                                addUserIfUnique(email)
                            }
                            (activity as? Navigable)?.navigate(Navigable.Destination.List)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                it.exception?.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.createAccount.setOnClickListener {
            (activity as? Navigable)?.navigate(Navigable.Destination.Signup)
        }
    }
    private fun doesUserExist(username: String): Boolean {
        val existingUser = db.users.getUserByLogin(username)
        return existingUser != null
    }
    private fun addUserIfUnique(username: String) {
        if (!doesUserExist(username)) {
            val user = UserEntity(login = username, articleTitles = ArrayList())
            db.users.addUser(user)
        }
    }

}