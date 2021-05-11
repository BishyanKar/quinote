package com.example.quinote.util

object MobileValidator {
    val mobilePattern = "^([6-9]{1})([0-9]{9})$"
    fun isValid(mobile: String?): Boolean
    {
        if(mobile?.matches(mobilePattern.toRegex())==false)
        {
            return false
        }
        return true
    }
}