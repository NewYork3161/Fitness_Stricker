package com.example.fitness_striker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class UserAgreementActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_agreement)

        prefs = getSharedPreferences("user_flags", MODE_PRIVATE)

        // If user already agreed → skip permanently
        if (prefs.getBoolean("consent_given", false)) {
            startActivity(Intent(this, HealthSurveyActivity::class.java))
            finish()
            return
        }

        val agreementText = findViewById<TextView>(R.id.agreementText)
        val agreeBtn = findViewById<Button>(R.id.agreeButton)
        val disagreeBtn = findViewById<Button>(R.id.disagreeButton)

        // Load the long agreement text
        agreementText.text = getAgreementText()

        // User selects AGREE
        agreeBtn.setOnClickListener {
            prefs.edit().putBoolean("consent_given", true).apply()
            startActivity(Intent(this, HealthSurveyActivity::class.java))
            finish()
        }

        // User selects DO NOT AGREE
        disagreeBtn.setOnClickListener {
            showMustAgreeDialog()
        }
    }

    private fun showMustAgreeDialog() {
        AlertDialog.Builder(this)
            .setTitle("Agreement Required")
            .setMessage("To use Fitness Striker, you must accept the User Consent and Safety Agreement.")
            .setPositiveButton("Go Back") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    private fun getAgreementText(): String {
        return """
            FITNESS STRIKER — USER CONSENT & SAFETY AGREEMENT
            
            • You agree that all health information you provide is voluntary.
            • Fitness Striker is NOT a medical app and cannot replace a physician.
            • Always consult a doctor before beginning any new workout plan.
            • We do NOT share or sell your personal data.
            • Your identity is never transmitted anywhere.
            • All data you enter is stored ONLY on your device.
            • You may NOT hack, reverse-engineer, or tamper with this software.
            • All technology, algorithms, and logic are © Fitness Striker Inc.
            
            By tapping “I Agree”, you consent to:
            • answering health questions,
            • receiving AI-generated workout and diet suggestions,
            • storing your progress on your device only,
            • participating in the personalized fitness program.
            
            If you do not agree, you cannot use this application.
        """.trimIndent()
    }
}
