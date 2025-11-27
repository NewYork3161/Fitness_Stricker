package com.example.fitness_striker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class UserProfileSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile_settings)

        // --- Buttons ---
        val editBtn = findViewById<Button>(R.id.editProfileButton)
        val backBtn = findViewById<Button>(R.id.backToProfileButton)

        // Go to Edit User Profile screen
        editBtn.setOnClickListener {
            startActivity(Intent(this, EditUserProfileActivity::class.java))
        }

        // Go back to User Profile screen
        backBtn.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java))
        }
    }
}
