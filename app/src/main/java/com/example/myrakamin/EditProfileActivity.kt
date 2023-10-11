package com.example.myrakamin

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle


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


        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        drawer = findViewById(R.id.edit_profile_drawer)
        toggle = ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        )

        drawer.addDrawerListener(toggle)
        toggle.syncState()

        toolbar.setNavigationOnClickListener {
            if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                drawer.closeDrawer(Gravity.RIGHT)
            } else {
                drawer.openDrawer(Gravity.RIGHT)

            }
        }
        var navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_dashboard -> {
                    val intent = Intent(this@EditProfileActivity, CekSaldoActivity::class.java)
                    startActivity(intent)
                }

                R.id.nav_editprofile -> {
                    val intent1 = Intent(this@EditProfileActivity, EditProfileActivity::class.java)
                    startActivity(intent1)
                }

                R.id.nav_logout -> {
                    val intent2 = Intent(this@EditProfileActivity, SignInActivity::class.java)
                    startActivity(intent2)
                }
            }
            false
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
