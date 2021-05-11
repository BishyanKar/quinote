package com.example.quinote.ui.user

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.quinote.NoteApp
import com.example.quinote.R
import com.example.quinote.models.User
import com.example.quinote.util.EmailValidator
import com.example.quinote.util.EncryptDecrypt
import com.example.quinote.util.MobileValidator
import com.example.quinote.util.PassowrdValidator
import com.example.quinote.viewmodel.UserViewModel
import com.example.quinote.viewmodel.UserViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var emailView: EditText
    private lateinit var passView: EditText
    private lateinit var mobileView: EditText
    private lateinit var nameView: EditText
    private lateinit var buttonNext: Button

    private lateinit var userViewModel: UserViewModel
    private lateinit var userViewModelFactory: UserViewModelFactory

    private lateinit var encryptDecrypt: EncryptDecrypt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        userViewModelFactory = UserViewModelFactory((application as NoteApp).userRepository)
        userViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)

        encryptDecrypt = EncryptDecrypt(this)

        emailView = findViewById(R.id.email_field_layout_SU)
        mobileView = findViewById(R.id.mobile_field_layout)
        passView = findViewById(R.id.pass_ET)
        nameView = findViewById(R.id.name_field_layout_SU)
        buttonNext = findViewById(R.id.button_signup)

        buttonNext.setOnClickListener {
            runValidations(emailView.text.toString(), passView.text.toString(), mobileView.text.toString(),nameView.text.toString())
        }
    }
    private fun runValidations(email: String?, password: String?, mobile: String?, name: String?){


        if(!EmailValidator.isEmailValid(email)){
            emailView.error = "Please enter a valid email with min 4 and max 25 characters"
            return
        }

        if(!PassowrdValidator.isValid(password,email,name)){
            passView.error = "Password must have min 8 and max 15 characters, should not contain your name, the first character should be lowercase, " +
                    "must contain at least 2 uppercase characters, 2 digits and 1 special character."
            return
        }

        if(!MobileValidator.isValid(mobile)){
            mobileView.error = "Mobile number should be of 10-digits only"
            return
        }


        val user = User(email, encryptDecrypt.getEncrypted(password), mobile)

        userViewModel.getUserByEmail(email).observe(this, Observer {
            if(it!=null){
                Toast.makeText(this, "User with this email already exists", Toast.LENGTH_SHORT).show()
            }
            else{
                userViewModel.insert(user)
                Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        })
    }
}