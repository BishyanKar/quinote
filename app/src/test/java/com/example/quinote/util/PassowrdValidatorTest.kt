package com.example.quinote.util

import org.junit.Test

import org.junit.Assert.*

class PassowrdValidatorTest {

    @Test
    fun passValidator_CorrectPass_ReturnsTrue() {
        assertEquals(true,PassowrdValidator.isValid("abcAB98.","tom@gmail.com","tom"))
    }
    @Test
    fun passValidator_CorrectPass_ReturnsTrue1() {
        assertEquals(true,PassowrdValidator.isValid("abcAB987@$.","tom@gmail.com","tom"))
    }
    @Test
    fun passValidator_WrongPass_ReturnsFalse() {
        //must have 2 digits
        assertEquals(false,PassowrdValidator.isValid("abcdeeeAB8@","tom@gmail.com","tom"))
    }
    @Test
    fun passValidator_WrongPass_ReturnsFalse1() {
        //must start with lowercase
        assertEquals(false,PassowrdValidator.isValid("AB98abc@","tom@gmail.com","tom"))
    }
    @Test
    fun passValidator_WrongPass_ReturnsFalse2() {
        // must have at least 2 uppercase characters
        assertEquals(false,PassowrdValidator.isValid("abcAb98@","tom@gmail.com","tom"))
    }
    @Test
    fun passValidator_WrongPass_ReturnsFalse3() {
        // must have at least 1 special characters
        assertEquals(false,PassowrdValidator.isValid("abcAb98eer","tom@gmail.com","tom"))
    }

    @Test
    fun passValidator_WrongPass_ReturnsFalse4() {
        // must have min 8 characters
        assertEquals(false,PassowrdValidator.isValid("aAB98@","tom@gmail.com","tom"))
    }
    @Test
    fun passValidator_WrongPass_ReturnsFalse5() {
        // must have max 15 characters
        assertEquals(false,PassowrdValidator.isValid("abcAB987@121232323232323232323","tom@gmail.com","tom"))
    }

    @Test
    fun passValidator_WrongPass_ReturnsFalse6() {
        // must not contain name
        assertEquals(false,PassowrdValidator.isValid("abcAB987@tom","tom@gmail.com","tom"))
    }
    @Test
    fun passValidator_WrongPass_ReturnsFalse7() {
        // must not contain email
        assertEquals(false,PassowrdValidator.isValid("tomA98@gmail.com","tomA98@gmail.com","tom"))
    }
}