package com.example.fitness_striker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditUserInfoActivity : AppCompatActivity() {

    private lateinit var db: LoginDatabase
    private val PREFS = "UserSession"
    private val KEY_EMAIL = "logged_in_email"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_info)

        db = LoginDatabase(this)

        // UI
        val firstName = findViewById<EditText>(R.id.newFirstName)
        val lastName = findViewById<EditText>(R.id.newLastName)
        val email = findViewById<EditText>(R.id.newEmail)
        val saveBtn = findViewById<Button>(R.id.saveUserInfoButton)
        val cancelBtn = findViewById<Button>(R.id.cancelUserInfoButton)

        // 🔥 Get current logged in email from SharedPreferences
        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)
        val oldEmail = prefs.getString(KEY_EMAIL, null)

        if (oldEmail == null) {
            Toast.makeText(this, "Error: No active session!", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // -----------------------------------------------------------------------
        // SAVE — Updates database & returns to settings screen
        // -----------------------------------------------------------------------
        saveBtn.setOnClickListener {

            val f = firstName.text.toString().trim()
            val l = lastName.text.toString().trim()
            val e = email.text.toString().trim()

            if (f.isEmpty() || l.isEmpty() || e.isEmpty()) {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (db.updateUserInfo(oldEmail, f, l, e)) {

                // Save updated email into session
                prefs.edit().putString(KEY_EMAIL, e).apply()

                Toast.makeText(this, "Profile Updated Successfully!", Toast.LENGTH_LONG).show()

                startActivity(Intent(this, UserProfileSettingsActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Error updating user", Toast.LENGTH_SHORT).show()
            }
        }

        // -----------------------------------------------------------------------
        // CANCEL — Return without saving
        // -----------------------------------------------------------------------
        cancelBtn.setOnClickListener {
            startActivity(Intent(this, UserProfileSettingsActivity::class.java))
            finish()
        }
    }
}
