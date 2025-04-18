package com.example.quizapp

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var submitButton: Button
    private lateinit var scoreTextView: TextView

    private val questions = listOf(
        Question("What is the capital of France?", listOf("Berlin", "Madrid", "Paris", "Rome"), "Paris"),
        Question("What is 2 + 2?", listOf("3", "4", "5", "6"), "4"),
        Question("Which planet is known as the Red Planet?", listOf("Earth", "Mars", "Jupiter", "Saturn"), "Mars")
    )

    private var currentQuestionIndex = 0
    private var score = 0
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        questionTextView = findViewById(R.id.textViewQuestion)
        radioGroup = findViewById(R.id.radioGroupAnswers)
        submitButton = findViewById(R.id.buttonSubmit)
        scoreTextView = findViewById(R.id.textViewScore)

        sharedPreferences = getSharedPreferences("quizAppPrefs", MODE_PRIVATE)

        // Retrieve previous score (if any)
        score = sharedPreferences.getInt("quiz_score", 0)

        // Initially hide the score TextView
        scoreTextView.visibility = View.INVISIBLE

        // Display the first question
        displayQuestion()

        submitButton.setOnClickListener {
            // Check answer for current question
            checkAnswer()

            // Move to the next question
            currentQuestionIndex++

            // If all questions are answered, show the final score
            if (currentQuestionIndex < questions.size) {
                displayQuestion() // Display the next question
            } else {
                // After all questions are answered, show the final score
                showFinalScore()

                // Save score using SharedPreferences
                sharedPreferences.edit().putInt("quiz_score", score).apply()

                // Optionally, show a toast
                Toast.makeText(this, "Quiz Finished! Final Score: $score", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Display question and possible answers
    private fun displayQuestion() {
        val question = questions[currentQuestionIndex]
        questionTextView.text = question.text

        radioGroup.removeAllViews()

        for (answer in question.answers) {
            val radioButton = RadioButton(this)
            radioButton.text = answer
            radioGroup.addView(radioButton)
        }
    }

    // Check answer and update score
    private fun checkAnswer() {
        val selectedRadioButtonId = radioGroup.checkedRadioButtonId
        if (selectedRadioButtonId != -1) {
            val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
            val selectedAnswer = selectedRadioButton.text.toString()

            val correctAnswer = questions[currentQuestionIndex].correctAnswer
            if (selectedAnswer == correctAnswer) {
                score++  // Increase score for correct answer
            }
        }
    }

    // Show the final score after all questions have been answered
    private fun showFinalScore() {
        scoreTextView.visibility = View.VISIBLE // Make score visible at the end
        scoreTextView.text = "Your Final Score: $score"
    }

    // Data class to hold question details
    data class Question(val text: String, val answers: List<String>, val correctAnswer: String)
}
