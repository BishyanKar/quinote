package com.example.quinote.util

import org.junit.Test

import org.junit.Assert.*

class MobileValidatorTest {

    @Test
    fun isValidTrue() {
        assertEquals(true,MobileValidator.isValid("9308076289"))
    }
    @Test
    fun isValidTrue1() {
        assertEquals(true,MobileValidator.isValid("7308076289"))
    }
    @Test
    fun isValidTrue2() {
        assertEquals(true,MobileValidator.isValid("6308076289"))
    }

    @Test
    fun isValidFalse()
    {
        assertEquals(false,MobileValidator.isValid("5308076289"))
    }

    @Test
    fun isValidFalse1()
    {
        assertEquals(false,MobileValidator.isValid("1308076289"))
    }
    @Test
    fun isValidFalse2()
    {
        assertEquals(false,MobileValidator.isValid("3308076289"))
    }
    @Test
    fun isValidFalse3()
    {
        assertEquals(false,MobileValidator.isValid("930807628912331"))
    }
}