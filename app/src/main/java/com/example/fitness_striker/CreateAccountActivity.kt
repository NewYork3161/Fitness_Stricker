package com.example.fitness_striker

import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var db: LoginDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        db = LoginDatabase(this)

        // INPUT FIELDS
        val firstNameField = findViewById<EditText>(R.id.firstNameField)
        val lastNameField = findViewById<EditText>(R.id.lastNameField)
        val emailField = findViewById<EditText>(R.id.emailField)
        val passwordField = findViewById<EditText>(R.id.passwordField)
        val rePasswordField = findViewById<EditText>(R.id.rePasswordField)

        // BUTTONS
        val loginButton = findViewById<Button>(R.id.loginButton)
        val createAccountButton = findViewById<Button>(R.id.createAccountButton)

        // GO BACK TO LOGIN
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // CREATE ACCOUNT
        createAccountButton.setOnClickListener {
            val firstName = firstNameField.text.toString().trim()
            val lastName = lastNameField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString()
            val rePassword = rePasswordField.text.toString()

            // BASIC VALIDATION
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
                password.isEmpty() || rePassword.isEmpty()
            ) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 8) {
                Toast.makeText(this, "Password must be at least 8 characters.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != rePassword) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // INSERT USER INTO SQLITE
            try {
                val success = db.insertUser(firstName, lastName, email, password)
                if (success) {
                    Toast.makeText(this, "Account created successfully.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Could not create account. Try again.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: SQLiteConstraintException) {
                // Triggered by UNIQUE(email) violation
                Toast.makeText(this, "Email already exists. Try logging in.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
