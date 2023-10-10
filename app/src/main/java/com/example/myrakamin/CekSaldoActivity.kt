package com.example.myrakamin

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CekSaldoActivity : AppCompatActivity() {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var imgbutton: ImageButton

    private lateinit var username : TextView
    private lateinit var balance : TextView
    private lateinit var noAccount : TextView

    private  var firebaseAuth = FirebaseAuth.getInstance()
    private  var firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cek_saldo)

        username= findViewById(R.id.usernameMain)
        balance= findViewById(R.id.balanceMain)
        noAccount= findViewById(R.id.noAccountMain)

        getUser()

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        drawer = findViewById(R.id.ceksaldo_drawer)
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
        var navigationView:NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_dashboard -> {
                    val intent = Intent(this@CekSaldoActivity, CekSaldoActivity::class.java)
                    startActivity(intent)
                }

                R.id.nav_editprofile -> {
                    val intent1 = Intent(this@CekSaldoActivity, EditProfileActivity::class.java)
                    startActivity(intent1)
                }

                R.id.nav_logout -> {
                    val intent2 = Intent(this@CekSaldoActivity, SignInActivity::class.java)
                    startActivity(intent2)
                }
            }
            false
        }
        imgbutton = findViewById(R.id.imageButton2)
        var balnce = findViewById<TextView>(R.id.balanceMain)
        balnce.visibility = View.GONE
        imgbutton.setOnClickListener{

               if (balnce.visibility == View.GONE){
                   balnce.visibility = View.VISIBLE
                   imgbutton.setImageResource(R.drawable.view)
               }else{
                   balnce.visibility = View.GONE
                   imgbutton.setImageResource(R.drawable.hidden)
               }
        }
    }

    private fun getUser() {

        val uid = firebaseAuth.currentUser!!.uid
        val ref = firestore.collection("users").document(uid)
        ref.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val getUsername = document.data?.get("username")?.toString()
                val getBalance = document.data?.get("balance")?.toString()
                val getNoAccount = document.data?.get("account_number").toString()
                username.text= getUsername
                balance.text= getBalance
                noAccount.text = getNoAccount
            } else {
                Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(
                this, "Failed Retrieve Data User : $exception", Toast.LENGTH_SHORT
            ).show()
        }
    }
}