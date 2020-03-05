package com.example.hw3_todolist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val code = 123

    var masterTaskList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadData()

        btnSecondActivity.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivityForResult(intent, code)
        }


        val myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, masterTaskList)

        toDoListView.adapter = myAdapter

        toDoListView.setOnItemLongClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            masterTaskList.removeAt(position)
            myAdapter.notifyDataSetChanged()
            if(myAdapter.count == 0){
                Toast.makeText(this, "All tasks are completed!", Toast.LENGTH_SHORT).show()
            }
            return@setOnItemLongClickListener true
        }

        clearAll.setOnClickListener{
            myAdapter.clear()
            masterTaskList.clear()
            Toast.makeText(this, "List has been cleared!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, masterTaskList)

        if (requestCode == code && resultCode == Activity.RESULT_OK) {
            val itemList = data?.getStringArrayListExtra("items") //as Iterable<String>
            if (itemList != null) {
                for (item in itemList) {
                    masterTaskList.add(item)
                }

                toDoListView.adapter = myAdapter

                saveData()

                toDoListView.setOnItemLongClickListener { parent, view, position, id ->
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    masterTaskList.removeAt(position)
                    myAdapter.notifyDataSetChanged()
                    if(myAdapter.count == 0){
                        Toast.makeText(this, "All tasks are completed!", Toast.LENGTH_SHORT).show()
                    }
                    return@setOnItemLongClickListener true
                }
                clearAll.setOnClickListener{

                    myAdapter.clear()
                    masterTaskList.clear()
                    Toast.makeText(this, "List has been cleared!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun saveData(){
        //Below is the code which the taskList send to the SharedPreference and stored with Gson
        val sharedPreferences = getPreferences(MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val nonDestoryableTasksList = gson.toJson(masterTaskList)
        editor.putString("tasks", nonDestoryableTasksList)
        editor.apply()
    }

    fun loadData(){
        //Loads the sharedPreferences and Gson
        val sharedPreferences = getPreferences(MODE_PRIVATE)
        val nTasks = sharedPreferences.getString("tasks", "")
        val gson = Gson()
        val sType = object : TypeToken<List<String>>() { }.type
        masterTaskList = gson.fromJson<List<String>>(nTasks, sType) as MutableList<String>
    }
}