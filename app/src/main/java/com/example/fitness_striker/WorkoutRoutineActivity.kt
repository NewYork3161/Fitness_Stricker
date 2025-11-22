package com.example.fitness_striker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WorkoutRoutineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_routine)

        val viewVideoBtn = findViewById<Button>(R.id.viewWorkoutBtn)
        val backBtn = findViewById<Button>(R.id.backToProfileBtn)

        // Open YouTube link
        viewVideoBtn.setOnClickListener {
            val url = "https://youtube.com/shorts/wSwo6mKHgZI?si=sYj9buLCNOaE8Gyr"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        // Go back to profile
        backBtn.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java))
            finish()
        }
    }
}
