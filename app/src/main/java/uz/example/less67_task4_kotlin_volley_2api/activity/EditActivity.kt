package uz.example.less67_task4_kotlin_volley_2api.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import uz.example.less67_task4_kotlin_volley_2api.MainActivity
import uz.example.less67_task4_kotlin_volley_2api.R

class EditActivity : AppCompatActivity() {
    lateinit var et_name: EditText
    lateinit var et_salary: EditText
    lateinit var et_age: EditText
    lateinit var btn_edit: Button
    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        initViews()
    }

    fun initViews() {
        val extras = intent.extras
        et_name = findViewById(R.id.et_nameEdit)
        et_salary = findViewById(R.id.et_SalaryEdit)
        et_age = findViewById(R.id.et_ageEdit)
        btn_edit = findViewById(R.id.btn_submitEdit)
        if (extras != null) {
            Log.d("###", "extras not NULL - ")
            id = extras.getInt("id")
            et_name.setText(extras.getString("name"))
            et_salary.setText("" + extras.getInt("salary"))
            et_age.setText("" + extras.getInt("age"))
        }
        btn_edit.setOnClickListener(View.OnClickListener {
            val name = et_name.getText().toString()
            val salary = et_salary.getText().toString().trim { it <= ' ' }.toInt()
            val age = et_age.getText().toString().trim { it <= ' ' }.toInt()
            val intent = Intent(this@EditActivity, MainActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("salary", salary)
            intent.putExtra("age", age)
            intent.putExtra("id", id)
            startActivity(intent)
        })
    }
}