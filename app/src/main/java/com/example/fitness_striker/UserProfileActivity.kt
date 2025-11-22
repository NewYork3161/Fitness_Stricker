package com.example.fitness_striker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class UserProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        // Buttons
        val dietBtn = findViewById<Button>(R.id.dietPlanButton)
        val workoutBtn = findViewById<Button>(R.id.workoutRoutineButton)

        // Open Diet Plan Activity
        dietBtn.setOnClickListener {
            val intent = Intent(this, DietPlanActivity::class.java)
            startActivity(intent)
        }

        // Open Workout Routine Activity
        workoutBtn.setOnClickListener {
            val intent = Intent(this, WorkoutRoutineActivity::class.java)
            startActivity(intent)
        }
    }
}
