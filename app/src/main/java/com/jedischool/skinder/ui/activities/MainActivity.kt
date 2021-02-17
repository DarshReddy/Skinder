package com.jedischool.skinder.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.jedischool.skinder.R
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val sharedPreferences = getSharedPreferences("AUTH", MODE_PRIVATE)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val headerLayout = navView.getHeaderView(0)

        val navImage:CircleImageView = headerLayout.findViewById(R.id.nav_imageView)
        val navUsername: TextView = headerLayout.findViewById(R.id.nav_username)
        val navPoints:TextView = headerLayout.findViewById(R.id.nav_points)
        navUsername.text = sharedPreferences.getString("username","User")
        navPoints.text = "Points: " + sharedPreferences.getInt("points",0)
        Glide.with(this).load(sharedPreferences.getString("profile_image","null")).into(navImage)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_profile, R.id.nav_posts, R.id.nav_leaderboard, R.id.nav_trending
        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_add_post -> {
                val intent = Intent(this, AddPostActivity::class.java)
                startActivity(intent)
            }
            R.id.action_logout -> Toast.makeText(this,"logout",Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}