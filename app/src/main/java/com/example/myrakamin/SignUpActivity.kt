package com.example.myrakamin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myrakamin.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.signInTextView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()
            val username = binding.usernameEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty() && username.isNotEmpty()) {
                if (pass == confirmPass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener { authTask ->
                                if (authTask.isSuccessful) {
                                    val userId = firebaseAuth.currentUser?.uid ?: ""
                                    val initialBalance = 10_000_000
                                    val accountNumber = generateAccountNumber()

                                    updateUserData(userId, username, initialBalance, accountNumber)
                                    val intent = Intent(this, SignInActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this, authTask.exception.toString(), Toast.LENGTH_SHORT).show()
                                }
                            }
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUserData(uid: String, username: String, balance: Int, accountNumber: Long) {
        val userDocRef = firestore.collection("users").document(uid)

        val userData = hashMapOf(
                "username" to username,
                "balance" to balance,
                "account_number" to accountNumber

        )

        userDocRef.set(userData)
                .addOnSuccessListener {

                }
                .addOnFailureListener { e ->

                    Toast.makeText(this, "Error updating user data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
    }

    private fun generateAccountNumber(): Long {
        return (1..12).joinToString("") { Random.nextInt(0, 10).toString() }.toLong()
    }
}
