package com.example.fitness_striker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class EditUserProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_profile)

        // Buttons from XML
        val editButton = findViewById<Button>(R.id.saveEditButton)   // BLUE "Edit" button
        val cancelButton = findViewById<Button>(R.id.cancelEditButton)

        // 🔹 Edit button → Go to EditUserInfoActivity
        editButton.setOnClickListener {
            val intent = Intent(this, EditUserInfoActivity::class.java)
            startActivity(intent)
        }

        // 🔹 Cancel → Return to profile settings screen
        cancelButton.setOnClickListener {
            val intent = Intent(this, UserProfileSettingsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
