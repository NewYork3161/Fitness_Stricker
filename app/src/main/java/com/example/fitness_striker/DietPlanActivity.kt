package com.example.fitness_striker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DietPlanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diet_plan)

        val backBtn = findViewById<Button>(R.id.backToProfileBtn)
        backBtn.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java))
            finish()
        }
    }
}
