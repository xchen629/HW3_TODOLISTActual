package com.example.hw3_todolist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity() {

    val taskList = ArrayList<String>()
    val myIntent = Intent()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        btnReturnToMain.setOnClickListener {
            finish()
        }
    }

    fun saveAndAdd(view: View) {
        taskList.add(enterTask.text.toString())
        myIntent.putExtra("items", taskList)
        setResult(Activity.RESULT_OK, myIntent)
        enterTask.text.clear()
    }

    fun saveAndBack(view: View){
        taskList.add(enterTask.text.toString())
        myIntent.putExtra("items", taskList)
        setResult(Activity.RESULT_OK, myIntent)
        finish()
    }
}
