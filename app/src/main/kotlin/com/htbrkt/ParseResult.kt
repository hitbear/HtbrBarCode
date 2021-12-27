package com.htbrkt;

import android.content.Intent

public class ParseResult(parsedString: String, intent: Intent){
    private val parsedString: String = parsedString;
    private val intent : Intent = intent;

    fun getParsedString(): String{
        return parsedString
    }

    fun getIntent(): Intent{
        return intent
    }

}