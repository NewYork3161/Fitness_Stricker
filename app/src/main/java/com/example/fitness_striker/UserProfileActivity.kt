package com.example.fitness_striker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class UserProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val dietBtn = findViewById<Button>(R.id.dietPlanButton)
        val workoutBtn = findViewById<Button>(R.id.workoutRoutineButton)
        val profileImage = findViewById<ImageView>(R.id.profileImage)
        val editProfileImageBtn = findViewById<Button>(R.id.profileImageEditButton)

        // Diet Plan
        dietBtn.setOnClickListener {
            startActivity(Intent(this, DietPlanActivity::class.java))
        }

        // Workout Routine
        workoutBtn.setOnClickListener {
            startActivity(Intent(this, WorkoutRoutineActivity::class.java))
        }

        // Profile Image → Profile Settings
        profileImage.setOnClickListener {
            startActivity(Intent(this, UserProfileSettingsActivity::class.java))
        }

        // NEW: Edit button → Profile Settings
        editProfileImageBtn.setOnClickListener {
            startActivity(Intent(this, UserProfileSettingsActivity::class.java))
        }
    }
}
