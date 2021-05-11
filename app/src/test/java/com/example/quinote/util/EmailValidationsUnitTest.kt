package com.example.quinote.util

import com.example.quinote.util.EmailValidator
import org.junit.Test

import org.junit.Assert.*

class EmailValidationsUnitTest {
    @Test
    fun emailValidator_CorrectEmail_ReturnsTrue() {
        assertEquals(true, EmailValidator.isEmailValid("email@gmail.com"))
    }

    @Test
    fun emailValidator_CorrectEmail_ReturnsTrue1() {
        assertEquals(true,EmailValidator.isEmailValid("email.abc+123@gmail.com"))
    }

    @Test
    fun emailValidator_CorrectEmail_ReturnsTrue2() {
        assertEquals(true,EmailValidator.isEmailValid("email.123.com@gmail.com"))
    }


    @Test
    fun emailValidator_WrongEmail_ReturnsFalse() {
        assertEquals(false, EmailValidator.isEmailValid("email123@gmail..com"))
    }

    @Test
    fun emailValidator_WrongEmail_ReturnsFalse1() {
        assertEquals(false, EmailValidator.isEmailValid("email..123@gmail.com"))
    }

    @Test
    fun emailValidator_WrongEmail_ReturnsFalse2() {
        assertEquals(false, EmailValidator.isEmailValid("email/123@gmail..com"))
    }

    @Test
    fun emailValidator_WrongEmail_ReturnsFalseForLength() {
        assertEquals(false, EmailValidator.isEmailValid("email.abc.123.com@gmail.com"))
    }
}