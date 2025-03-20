package com.example.pgdquiz.ui

data class Question (
    val id: Int,
    val question: String,
    val optionOne: String,
    val optionTwo: String,
    val optionThree: String,
    val optionFour: String,
    val optionFive: String,
    val correctAnswer: Int
)
