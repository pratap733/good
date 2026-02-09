package com.example.good

import android.content.Context
import org.json.JSONObject

class McqStorage(context: Context) {

    private val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun save(question: McqQuestion) {
        val payload = JSONObject().apply {
            put(KEY_QUESTION, question.question)
            put(KEY_OPTION_A, question.options[0])
            put(KEY_OPTION_B, question.options[1])
            put(KEY_OPTION_C, question.options[2])
            put(KEY_OPTION_D, question.options[3])
            put(KEY_CORRECT_OPTION, question.correctOption)
        }

        prefs.edit().putString(KEY_MCQ_JSON, payload.toString()).apply()
    }

    fun load(): McqQuestion? {
        val raw = prefs.getString(KEY_MCQ_JSON, null) ?: return null
        val payload = JSONObject(raw)

        return McqQuestion(
            question = payload.getString(KEY_QUESTION),
            options = listOf(
                payload.getString(KEY_OPTION_A),
                payload.getString(KEY_OPTION_B),
                payload.getString(KEY_OPTION_C),
                payload.getString(KEY_OPTION_D)
            ),
            correctOption = payload.getString(KEY_CORRECT_OPTION)
        )
    }

    companion object {
        private const val PREF_NAME = "mcq_practice_storage"
        private const val KEY_MCQ_JSON = "current_mcq"
        private const val KEY_QUESTION = "question"
        private const val KEY_OPTION_A = "option_a"
        private const val KEY_OPTION_B = "option_b"
        private const val KEY_OPTION_C = "option_c"
        private const val KEY_OPTION_D = "option_d"
        private const val KEY_CORRECT_OPTION = "correct_option"
    }
}
