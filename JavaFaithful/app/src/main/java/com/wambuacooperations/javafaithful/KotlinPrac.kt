package com.wambuacooperations.javafaithful

import java.util.*

class Person(val name:String,val yob:Int,var profession:String){
    fun calcAge():Int{
        var year= Calendar.getInstance().get(Calendar.YEAR)
        return year-this.yob
    }
}

