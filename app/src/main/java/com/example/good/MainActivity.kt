package com.example.good

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val questions = mutableListOf<McqQuestion>()
    private var currentQuestionIndex = 0
    private var score = 0

    private lateinit var etQuestion: EditText
    private lateinit var etOptionA: EditText
    private lateinit var etOptionB: EditText
    private lateinit var etOptionC: EditText
    private lateinit var etOptionD: EditText
    private lateinit var etCorrectOption: EditText
    private lateinit var tvCount: TextView
    private lateinit var tvPracticeQuestion: TextView
    private lateinit var rgPracticeOptions: RadioGroup
    private lateinit var rbOptionA: RadioButton
    private lateinit var rbOptionB: RadioButton
    private lateinit var rbOptionC: RadioButton
    private lateinit var rbOptionD: RadioButton
    private lateinit var btnCheckAnswer: Button
    private lateinit var btnNext: Button
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()

        findViewById<Button>(R.id.btnAddMcq).setOnClickListener { addQuestion() }
        btnCheckAnswer.setOnClickListener { checkAnswer() }
        btnNext.setOnClickListener { nextQuestion() }

        updateQuestionCount()
        updatePracticeUi()
    }

    private fun bindViews() {
        etQuestion = findViewById(R.id.etQuestion)
        etOptionA = findViewById(R.id.etOptionA)
        etOptionB = findViewById(R.id.etOptionB)
        etOptionC = findViewById(R.id.etOptionC)
        etOptionD = findViewById(R.id.etOptionD)
        etCorrectOption = findViewById(R.id.etCorrectOption)
        tvCount = findViewById(R.id.tvCount)
        tvPracticeQuestion = findViewById(R.id.tvPracticeQuestion)
        rgPracticeOptions = findViewById(R.id.rgPracticeOptions)
        rbOptionA = findViewById(R.id.rbOptionA)
        rbOptionB = findViewById(R.id.rbOptionB)
        rbOptionC = findViewById(R.id.rbOptionC)
        rbOptionD = findViewById(R.id.rbOptionD)
        btnCheckAnswer = findViewById(R.id.btnCheckAnswer)
        btnNext = findViewById(R.id.btnNext)
        tvResult = findViewById(R.id.tvResult)
    }

    private fun addQuestion() {
        val question = etQuestion.text.toString().trim()
        val optionA = etOptionA.text.toString().trim()
        val optionB = etOptionB.text.toString().trim()
        val optionC = etOptionC.text.toString().trim()
        val optionD = etOptionD.text.toString().trim()
        val correctOption = etCorrectOption.text.toString().trim().uppercase()

        if (question.isEmpty() || optionA.isEmpty() || optionB.isEmpty() || optionC.isEmpty() || optionD.isEmpty()) {
            showToast("Please fill all MCQ fields")
            return
        }

        if (correctOption !in setOf("A", "B", "C", "D")) {
            showToast("Correct option must be A, B, C, or D")
            return
        }

        questions.add(McqQuestion(question, listOf(optionA, optionB, optionC, optionD), correctOption))

        etQuestion.text.clear()
        etOptionA.text.clear()
        etOptionB.text.clear()
        etOptionC.text.clear()
        etOptionD.text.clear()
        etCorrectOption.text.clear()

        showToast("MCQ added. Keep pasting from ChatGPT and practice!")
        updateQuestionCount()

        if (questions.size == 1) {
            updatePracticeUi()
        }
    }

    private fun checkAnswer() {
        if (questions.isEmpty()) {
            showToast("Add at least one MCQ first")
            return
        }

        val selectedId = rgPracticeOptions.checkedRadioButtonId
        if (selectedId == -1) {
            showToast("Select an option")
            return
        }

        val selectedOption = when (selectedId) {
            R.id.rbOptionA -> "A"
            R.id.rbOptionB -> "B"
            R.id.rbOptionC -> "C"
            else -> "D"
        }

        val current = questions[currentQuestionIndex]
        if (selectedOption == current.correctOption) {
            score++
            tvResult.text = "Correct! Score: $score/${currentQuestionIndex + 1}"
        } else {
            tvResult.text = "Wrong. Correct answer: ${current.correctOption}. Score: $score/${currentQuestionIndex + 1}"
        }

        btnCheckAnswer.isEnabled = false
    }

    private fun nextQuestion() {
        if (questions.isEmpty()) {
            showToast("No MCQs to practice yet")
            return
        }

        currentQuestionIndex = (currentQuestionIndex + 1) % questions.size
        updatePracticeUi()
    }

    private fun updateQuestionCount() {
        tvCount.text = "Saved MCQs: ${questions.size}"
    }

    private fun updatePracticeUi() {
        if (questions.isEmpty()) {
            tvPracticeQuestion.text = "No MCQ yet. Add your first question from ChatGPT above."
            rbOptionA.text = "Option A"
            rbOptionB.text = "Option B"
            rbOptionC.text = "Option C"
            rbOptionD.text = "Option D"
            tvResult.text = ""
            btnCheckAnswer.isEnabled = true
            rgPracticeOptions.clearCheck()
            return
        }

        val current = questions[currentQuestionIndex]
        tvPracticeQuestion.text = "Q${currentQuestionIndex + 1}: ${current.question}"
        rbOptionA.text = "A. ${current.options[0]}"
        rbOptionB.text = "B. ${current.options[1]}"
        rbOptionC.text = "C. ${current.options[2]}"
        rbOptionD.text = "D. ${current.options[3]}"

        tvResult.text = ""
        btnCheckAnswer.isEnabled = true
        rgPracticeOptions.clearCheck()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

data class McqQuestion(
    val question: String,
    val options: List<String>,
    val correctOption: String,
)
