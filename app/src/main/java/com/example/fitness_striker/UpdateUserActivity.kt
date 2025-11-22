package com.example.fitness_striker

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class UpdateUserActivity : AppCompatActivity() {

    private lateinit var db: LoginDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)

        db = LoginDatabase(this)

        // MATCHES YOUR XML IDs EXACTLY
        val emailInput = findViewById<TextInputEditText>(R.id.emailInput)
        val newPasswordInput = findViewById<TextInputEditText>(R.id.newPasswordInput)
        val rePasswordInput = findViewById<TextInputEditText>(R.id.rePasswordInput)

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val updateUserBtn = findViewById<Button>(R.id.updateUserBtn)

        // GO BACK TO LOGIN
        loginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // UPDATE USER PASSWORD
        updateUserBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val newPassword = newPasswordInput.text.toString()
            val rePassword = rePasswordInput.text.toString()

            if (email.isEmpty() || newPassword.isEmpty() || rePassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email format.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword.length < 8) {
                Toast.makeText(this, "Password must be at least 8 characters.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != rePassword) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // prevent user from reusing password
            if (db.isSamePassword(email, newPassword)) {
                Toast.makeText(this, "New password cannot be the same as the old one.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updated = db.updatePassword(email, newPassword)

            if (updated) {
                Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "No user found with that email.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
