package com.example.quinote.ui.user

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.quinote.MainActivity
import com.example.quinote.NoteApp
import com.example.quinote.R
import com.example.quinote.models.User
import com.example.quinote.util.EmailValidator
import com.example.quinote.util.EncryptDecrypt
import com.example.quinote.util.MobileValidator
import com.example.quinote.util.PassowrdValidator
import com.example.quinote.viewmodel.UserViewModel
import com.example.quinote.viewmodel.UserViewModelFactory
import javax.crypto.EncryptedPrivateKeyInfo

class LoginActivity : AppCompatActivity() {

    private lateinit var emailView: EditText
    private lateinit var passView: EditText
    private lateinit var buttonNext: Button
    private lateinit var buttonCreate: Button

    private lateinit var userViewModel: UserViewModel
    private lateinit var userViewModelFactory: UserViewModelFactory

    private lateinit var encryptDecrypt: EncryptDecrypt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userViewModelFactory = UserViewModelFactory((application as NoteApp).userRepository)
        userViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)

        encryptDecrypt = EncryptDecrypt(this)

        emailView = findViewById(R.id.email_field_layout)
        passView = findViewById(R.id.password_ET)
        buttonNext = findViewById(R.id.button_login)
        buttonCreate = findViewById(R.id.button_create)

        buttonNext.setOnClickListener {
            runValidations(emailView.text.toString(), passView.text.toString())
        }

        buttonCreate.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

    }

    private fun runValidations(email: String?, password: String?){

        if(!EmailValidator.isEmailValid(email)){
            emailView.error = "Please enter a valid email with min 4 and max 25 characters"
            return
        }


    val sharedPref = getSharedPreferences(getString(R.string.Preference_key_file_name_1110), Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        userViewModel.getUserByEmail(email).observe(this, {

            if(it==null) {
                Toast.makeText(this, "Email does not exist", Toast.LENGTH_SHORT).show()
                return@observe
            }
            val decrptedPass = encryptDecrypt.getDecrypted(it.password)

            if(password?.compareTo(decrptedPass)==0){
                editor.putInt("UID",it.id)
                editor.apply()
                Toast.makeText(this, "Login Sucess", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })
    }
}