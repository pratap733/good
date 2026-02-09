package com.example.good

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val username = findViewById<EditText>(R.id.etUsername)
        val password = findViewById<EditText>(R.id.etPassword)
        val loginBtn = findViewById<Button>(R.id.btnLogin)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val rememberMe = findViewById<CheckBox>(R.id.cbRemember)

        loginBtn.setOnClickListener {

            val user = username.text.toString()
            val pass = password.text.toString()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass.length < 4) {
                Toast.makeText(this, "Password too short", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (radioGroup.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Select Gender", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE

            progressBar.postDelayed({
                progressBar.visibility = View.GONE

                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                Toast.makeText(this, "Welcome $user", Toast.LENGTH_SHORT).show()


            }, 2000)
        }
    }
}
