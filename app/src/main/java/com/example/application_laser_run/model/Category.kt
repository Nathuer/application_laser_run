package com.example.application_laser_run.model

data class Category(
    val id: String,
    val name: String,
    val initialDistance: Int,
    val lapDistance: Int,
    val lapCount: Int,
    val shootDistance: Int)
