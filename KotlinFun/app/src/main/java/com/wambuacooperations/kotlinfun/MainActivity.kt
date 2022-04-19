package com.wambuacooperations.kotlinfun

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var counter=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var textView=findViewById<TextView>(R.id.textView)

        textView.text="0"

    }

    fun addOne(view: View){
        counter++
        textView.text=counter.toString()

    }
    fun reset(view:View){
        counter=0
        textView.text=counter.toString()
    }
}
