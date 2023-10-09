package com.example.myrakamin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var editTextUsername: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextAddress: EditText
    private lateinit var saveButton: Button
    private lateinit var logoutButton: Button


    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        editTextUsername = findViewById(R.id.editTextUsername)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPhone = findViewById(R.id.editTextPhone)
        editTextAddress = findViewById(R.id.editTextAddress)
        saveButton = findViewById(R.id.saveButton)
        logoutButton = findViewById(R.id.logoutButton)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        retrieveUserData()

        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            editTextEmail.setText(currentUser.email)
        }

        saveButton.setOnClickListener {
            updateUserData()
        }


        logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun retrieveUserData() {
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            val userDocRef = firestore.collection("users").document(userId)

            userDocRef.get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val username = documentSnapshot.getString("username")
                            val phone = documentSnapshot.getString("phone")
                            val address = documentSnapshot.getString("address")

                            editTextUsername.setText(username)
                            editTextPhone.setText(phone)
                            editTextAddress.setText(address)
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error retrieving user data: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
        }
    }

    private fun updateUserData() {
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            val userDocRef = firestore.collection("users").document(userId)

            val updatedUsername = editTextUsername.text.toString().trim()
            val updatedEmail = editTextEmail.text.toString().trim()
            val updatedPhone = editTextPhone.text.toString().trim()
            val updatedAddress = editTextAddress.text.toString().trim()

            val updatedData = hashMapOf(
                    "username" to updatedUsername,
                    "email" to updatedEmail,
                    "phone" to updatedPhone,
                    "address" to updatedAddress
            )

            // Explicit cast to Map<String, Any>
            val updatedDataMap: Map<String, Any> = updatedData

            userDocRef.update(updatedDataMap)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error updating user data: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
        }
    }
}
