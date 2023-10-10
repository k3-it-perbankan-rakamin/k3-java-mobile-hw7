package com.example.myrakamin

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class CekSaldoActivity : AppCompatActivity() {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cek_saldo)
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
    }
}