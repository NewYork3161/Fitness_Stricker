package com.example.fitness_striker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class HealthSurveyActivity : AppCompatActivity() {

    // --- DATABASE/USER VARIABLES ---
    // Removed: private lateinit var dbHelper: FitnessDatabaseHelper
    private val currentUserId = "unique_user_id_123"
    // -------------------------------

    private lateinit var prefs: SharedPreferences

    // UI Elements
    private lateinit var consentScroll: ScrollView
    private lateinit var consentText: TextView
    private lateinit var agreeButton: Button
    private lateinit var disagreeButton: Button

    private lateinit var questionText: TextView
    private lateinit var yesButton: Button
    private lateinit var noButton: Button
    private lateinit var questionButtonsLayout: LinearLayout

    private val questions = listOf(
        "Do you have high blood pressure?",
        "Are you currently taking blood pressure medication?",
        "Do you experience back pain?",
        "Do you experience knee, hip, or ankle pain?",
        "Do you have any joint mobility issues?",
        "Have you had injuries in the last 12 months?",
        "Do you get shortness of breath during light activity?",
        "Do you have difficulty bending or lifting?",
        "Do you follow a sleep schedule?",
        "Do you sleep at least 6 hours per night?",
        "Do you feel low energy most days?",
        "Do you have a busy or irregular work schedule?",
        "Do you prefer home workouts?",
        "Do you have access to gym equipment?",
        "Are you comfortable trying new exercises?",
        "Are you interested in martial-arts conditioning?",
        "Do you eat meat regularly?",
        "Do you eat vegetables regularly?",
        "Do you drink soda or sugary drinks?",
        "Do you eat fast food often?",
        "Are you trying to lose weight?",
        "Are you trying to gain muscle?",
        "Are you trying to maintain your current weight?",
        "Are you open to a Mediterranean-style diet?",
        "Are you open to a high-protein fitness diet?",
        "Are you open to a vegetarian or plant-based diet?",
        "Are you open to a low-carb / low-sugar diet?",
        "Do you eat pasta or bread often?",
        "Do you eat rice often?",
        "Do you have food allergies?",
        "Do you consider yourself a beginner?",
        "Do you consider yourself intermediate?",
        "Do you consider yourself advanced?",
        "Would you like your routine to include stretching?",
        "Would you like cardio included weekly?"
    )

    private val answers = mutableMapOf<String, String>()
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_survey)

        prefs = getSharedPreferences("survey_prefs", MODE_PRIVATE)

        if (prefs.getBoolean("survey_completed", false)) {
            goToUserProfile()
            return
        }

        consentScroll = findViewById(R.id.consentScroll)
        consentText = findViewById(R.id.consentText)
        agreeButton = findViewById(R.id.agreeButton)
        disagreeButton = findViewById(R.id.disagreeButton)

        questionText = findViewById(R.id.questionText)
        yesButton = findViewById(R.id.yesButton)
        noButton = findViewById(R.id.noButton)
        questionButtonsLayout = findViewById(R.id.questionButtonsLayout)

        setupConsent()

        yesButton.setOnClickListener { handleAnswer("Yes") }
        noButton.setOnClickListener { handleAnswer("No") }
    }

    private fun setupConsent() {
        questionText.visibility = View.GONE
        questionButtonsLayout.visibility = View.GONE

        consentScroll.visibility = View.VISIBLE
        agreeButton.visibility = View.VISIBLE
        disagreeButton.visibility = View.VISIBLE

        agreeButton.setOnClickListener { showFirstQuestion() }

        disagreeButton.setOnClickListener {
            Toast.makeText(this, "You must agree to continue.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun showFirstQuestion() {
        consentScroll.visibility = View.GONE
        agreeButton.visibility = View.GONE
        disagreeButton.visibility = View.GONE

        questionText.visibility = View.VISIBLE
        questionButtonsLayout.visibility = View.VISIBLE

        questionText.text = questions[currentIndex]
    }

    private fun handleAnswer(answer: String) {
        answers[questions[currentIndex]] = answer

        // TODO — Save to SQLite (database removed for now)
        // insertAnswerToDatabase(currentUserId, questions[currentIndex], answer)

        val fadeOut = AlphaAnimation(1f, 0f).apply { duration = 200; fillAfter = true }
        val fadeIn = AlphaAnimation(0f, 1f).apply { duration = 250; fillAfter = true }

        questionText.startAnimation(fadeOut)

        fadeOut.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(animation: android.view.animation.Animation?) {}

            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                currentIndex++

                if (currentIndex < questions.size) {
                    questionText.text = questions[currentIndex]
                    questionText.startAnimation(fadeIn)
                } else {
                    triggerAIPush()
                    prefs.edit().putBoolean("survey_completed", true).apply()
                    goToUserProfile()
                }
            }

            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
        })
    }

    private fun goToUserProfile() {
        startActivity(Intent(this, UserProfileActivity::class.java))
        finish()
    }

    private fun triggerAIPush() {
        Toast.makeText(this, "Survey complete. Sending data to AI...", Toast.LENGTH_LONG).show()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // TODO — Replace with real AI call
                val fakeJson = """
                    {
                      "full_plan_text": "Sample plan text...",
                      "trigger_keywords": "sample,keywords"
                    }
                """.trimIndent()

                withContext(Dispatchers.Main) {
                    val planObject = JSONObject(fakeJson)

                    val fullPlanText = planObject.getString("full_plan_text")
                    val triggerKeywords = planObject.getString("trigger_keywords")

                    // TODO — Save plan to SQLite (database removed)
                    // dbHelper.insertAiPlan(currentUserId, fullPlanText, triggerKeywords)

                    Toast.makeText(
                        this@HealthSurveyActivity,
                        "Plan successfully generated!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@HealthSurveyActivity,
                        "Error generating plan.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
