package com.example.fitness_striker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var db: LoginDatabase
    private lateinit var prefs: SharedPreferences

    private val MAX_ATTEMPTS = 5
    private val LOCKOUT_DURATION_MS = 5 * 60 * 1000L // 5 minutes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = LoginDatabase(this)
        prefs = getSharedPreferences("login_security", MODE_PRIVATE)

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)

        val createAccountBtn = findViewById<Button>(R.id.createAccountBtn)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val updateUserBtn = findViewById<Button>(R.id.updateUserBtn)

        // CREATE ACCOUNT
        createAccountBtn.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
        }

        // UPDATE USER
        updateUserBtn.setOnClickListener {
            startActivity(Intent(this, UpdateUserActivity::class.java))
        }

        // LOGIN BUTTON
        loginBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()

            // CHECK LOCKOUT
            if (isLockedOut()) {
                Toast.makeText(this, "Too many attempts. Try again later.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // BASIC VALIDATION
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email format.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // CHECK CREDENTIALS AGAINST SQLITE
            val isValid = db.validateUser(email, password)
            if (isValid) {
                resetLockout()
                Toast.makeText(this, "Login successful.", Toast.LENGTH_SHORT).show()

                // ⭐ THIS IS WHERE YOU NAVIGATE AFTER LOGIN ⭐
                val intent = Intent(this, HealthSurveyActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                recordFailedAttempt()
                val remaining = MAX_ATTEMPTS - getAttempts()

                if (remaining <= 0) {
                    startLockout()
                    Toast.makeText(this, "Too many attempts. Locked for 5 minutes.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Invalid credentials. Attempts left: $remaining", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // ==============================================
    // SECURITY / LOCKOUT FUNCTIONS
    // ==============================================
    private fun getAttempts(): Int {
        return prefs.getInt("failed_attempts", 0)
    }

    private fun recordFailedAttempt() {
        val current = getAttempts() + 1
        prefs.edit().putInt("failed_attempts", current).apply()
    }

    private fun resetLockout() {
        prefs.edit()
            .putInt("failed_attempts", 0)
            .putLong("lockout_until", 0L)
            .apply()
    }

    private fun startLockout() {
        val lockoutUntil = System.currentTimeMillis() + LOCKOUT_DURATION_MS
        prefs.edit()
            .putInt("failed_attempts", 0)
            .putLong("lockout_until", lockoutUntil)
            .apply()
    }

    private fun isLockedOut(): Boolean {
        val lockoutUntil = prefs.getLong("lockout_until", 0L)
        if (lockoutUntil == 0L) return false
        return System.currentTimeMillis() < lockoutUntil
    }
}
