package com.example.good

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var storage: McqStorage

    private var savedQuestion: McqQuestion? = null
    private var attempted = false
    private var score = 0

    private lateinit var etQuestion: EditText
    private lateinit var etOptionA: EditText
    private lateinit var etOptionB: EditText
    private lateinit var etOptionC: EditText
    private lateinit var etOptionD: EditText
    private lateinit var etCorrectOption: EditText
    private lateinit var tvStatus: TextView
    private lateinit var tvPracticeQuestion: TextView
    private lateinit var rgPracticeOptions: RadioGroup
    private lateinit var rbOptionA: RadioButton
    private lateinit var rbOptionB: RadioButton
    private lateinit var rbOptionC: RadioButton
    private lateinit var rbOptionD: RadioButton
    private lateinit var btnCheckAnswer: Button
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        storage = McqStorage(this)
        bindViews()

        savedQuestion = storage.load()

        findViewById<Button>(R.id.btnSaveMcq).setOnClickListener { saveOrReplaceMcq() }
        btnCheckAnswer.setOnClickListener { checkAnswer() }

        updateStatus()
        updatePracticeUi()
    }

    private fun bindViews() {
        etQuestion = findViewById(R.id.etQuestion)
        etOptionA = findViewById(R.id.etOptionA)
        etOptionB = findViewById(R.id.etOptionB)
        etOptionC = findViewById(R.id.etOptionC)
        etOptionD = findViewById(R.id.etOptionD)
        etCorrectOption = findViewById(R.id.etCorrectOption)
        tvStatus = findViewById(R.id.tvStatus)
        tvPracticeQuestion = findViewById(R.id.tvPracticeQuestion)
        rgPracticeOptions = findViewById(R.id.rgPracticeOptions)
        rbOptionA = findViewById(R.id.rbOptionA)
        rbOptionB = findViewById(R.id.rbOptionB)
        rbOptionC = findViewById(R.id.rbOptionC)
        rbOptionD = findViewById(R.id.rbOptionD)
        btnCheckAnswer = findViewById(R.id.btnCheckAnswer)
        tvResult = findViewById(R.id.tvResult)
    }

    private fun saveOrReplaceMcq() {
        val question = etQuestion.text.toString().trim()
        val optionA = etOptionA.text.toString().trim()
        val optionB = etOptionB.text.toString().trim()
        val optionC = etOptionC.text.toString().trim()
        val optionD = etOptionD.text.toString().trim()
        val correctOption = etCorrectOption.text.toString().trim().uppercase()

        if (question.isEmpty() || optionA.isEmpty() || optionB.isEmpty() || optionC.isEmpty() || optionD.isEmpty()) {
            showToast("Please fill all fields")
            return
        }

        if (correctOption !in setOf("A", "B", "C", "D")) {
            showToast("Correct option must be A, B, C, or D")
            return
        }

        val mcq = McqQuestion(question, listOf(optionA, optionB, optionC, optionD), correctOption)
        storage.save(mcq)
        savedQuestion = mcq
        attempted = false

        etQuestion.text.clear()
        etOptionA.text.clear()
        etOptionB.text.clear()
        etOptionC.text.clear()
        etOptionD.text.clear()
        etCorrectOption.text.clear()

        showToast("MCQ saved. Existing MCQ was replaced.")
        updateStatus()
        updatePracticeUi()
    }

    private fun checkAnswer() {
        val current = savedQuestion ?: run {
            showToast("Save one MCQ first")
            return
        }

        if (attempted) {
            showToast("This MCQ is already checked. Save another or edit and replace.")
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

        if (selectedOption == current.correctOption) {
            score++
            tvResult.text = "✅ Correct! Total score: $score"
        } else {
            tvResult.text = "❌ Wrong. Correct answer: ${current.correctOption}. Total score: $score"
        }

        attempted = true
        btnCheckAnswer.isEnabled = false
    }

    private fun updateStatus() {
        tvStatus.text = if (savedQuestion == null) {
            "Stored MCQ: none"
        } else {
            "Stored MCQ: 1 (saved in local app storage)"
        }
    }

    private fun updatePracticeUi() {
        val current = savedQuestion
        if (current == null) {
            tvPracticeQuestion.text = "No MCQ saved yet. Paste from ChatGPT and tap Save MCQ."
            rbOptionA.text = "Option A"
            rbOptionB.text = "Option B"
            rbOptionC.text = "Option C"
            rbOptionD.text = "Option D"
            tvResult.text = ""
            btnCheckAnswer.isEnabled = true
            rgPracticeOptions.clearCheck()
            return
        }

        tvPracticeQuestion.text = current.question
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
