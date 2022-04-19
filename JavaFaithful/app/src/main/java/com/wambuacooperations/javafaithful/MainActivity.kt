package com.wambuacooperations.javafaithful

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val john = Person("John", 2000, "Student")
        println("Age is " + john.calcAge())
    }
}