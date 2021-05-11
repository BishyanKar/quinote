package com.example.quinote.util

object PassowrdValidator {

    val passPattern = "^(?=[a-z])(?=.*[0-9][0-9])(?=.*[a-z])(?=.*[A-Z][A-Z])(?=.*[.@#$%^&+=])(?=\\S+$).{8,15}$"

    fun isValid(password: String?, email: String?, name: String?) : Boolean{
        if(password?.matches(passPattern.toRegex())==false){
            return false
        }

        if(password?.contains(email!!)==true || password?.contains(name!!)==true){
            return false
        }
        return true
    }
}