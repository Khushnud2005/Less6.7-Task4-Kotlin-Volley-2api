package uz.example.less67_task4_kotlin_volley_2api

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject
import uz.example.less67_task4_kotlin_volley_2api.activity.CreateActivity
import uz.example.less67_task4_kotlin_volley_2api.adapter.EmployeeAdapter
import uz.example.less67_task4_kotlin_volley_2api.model.Employee
import uz.example.less67_task4_kotlin_volley_2api.network.VolleyHandler
import uz.example.less67_task4_kotlin_volley_2api.network.VolleyHttp

class MainActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    var employees = ArrayList<Employee>()
    lateinit var pb_loading: ProgressBar
    lateinit var floating: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    fun initViews() {
        pb_loading = findViewById<ProgressBar>(R.id.pb_loading)
        floating = findViewById<FloatingActionButton>(R.id.floating)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setLayoutManager(GridLayoutManager(this, 1))
        recyclerView.setItemAnimator(DefaultItemAnimator())
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        apiEmployeeList()
        floating.setOnClickListener(View.OnClickListener { openCreateActivity() })
        val extras = intent.extras
        if (extras != null) {
            Log.d("###", "extras not NULL - ")
            val edit_name = extras.getString("name")
            val edit_salary = extras.getInt("salary")
            val edit_age = extras.getInt("age")
            val edit_id = extras.getInt("id")
            val employee = Employee(edit_id, edit_name!!, edit_salary, edit_age)
            Toast.makeText(this@MainActivity, "Employee Prepared to Edit", Toast.LENGTH_LONG).show()
            apiEmployeeEdit(employee)
        }
    }
    var launchSomeActivity = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == 78) {
            val data = result.data
            if (data != null) {
                val edit_name = data.getStringExtra("name")
                val edit_salary = data.getStringExtra("salary")
                val edit_age = data.getStringExtra("age")
                val employee = Employee(edit_name!!, edit_salary!!.toInt(), edit_age!!.toInt())
                Toast.makeText(this@MainActivity, "Title modified", Toast.LENGTH_LONG).show()
                apiEmployeeCreate(employee)
            }
            // your operation....
        } else {
            Toast.makeText(this@MainActivity, "Operation canceled", Toast.LENGTH_LONG).show()
        }
    }

    fun refreshAdapter(employees: ArrayList<Employee>) {
        val adapter = EmployeeAdapter(this, employees)
        recyclerView.setAdapter(adapter)
    }

    fun openCreateActivity() {
        val intent = Intent(this@MainActivity, CreateActivity::class.java)
        launchSomeActivity.launch(intent)
    }
    fun dialogEmployee(employee: Employee) {
        AlertDialog.Builder(this)
            .setTitle("Delete Employee")
            .setMessage("Are you sure you want to delete this employee?")
            .setPositiveButton(
                android.R.string.yes
            ) { dialog, which ->
                apiEmployeeDelete(employee)
            }
            .setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    private fun apiEmployeeList() {
        pb_loading.visibility = View.VISIBLE
        VolleyHttp.get(VolleyHttp.API_LIST_EMPLOYEE,VolleyHttp.paramsEmpty(),object : VolleyHandler {
                override fun onSuccess(response: String) {
                    Log.d("@@@onResponse ", "" + response)
                    pb_loading.visibility = View.GONE
                    employees.clear()
                    try {
                        val jsonObject = JSONObject(response)
                        val jsonArray = jsonObject.getJSONArray("data")
                        if (jsonArray.length() > 0) {
                            for (i in 0 until jsonArray.length()) {
                                val jsonObject1 = jsonArray.getJSONObject(i)
                                val name = jsonObject1.getString("employee_name")
                                val salary = jsonObject1.getString("employee_salary")
                                val age = jsonObject1.getString("employee_age")
                                val id = jsonObject1.getString("id")
                                val employee =
                                    Employee(id.toInt(), name, salary.toInt(), age.toInt())
                                employees.add(employee)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    refreshAdapter(employees)
                }

                override fun onError(error: String) {
                    Log.d("@@@onErrorResponse ", error)
                    apiEmployeeList()
                }
            })
    }

    private fun apiEmployeeCreate(employee: Employee) {
        pb_loading.visibility = View.VISIBLE
        VolleyHttp.post(VolleyHttp.API_CREATE_EMPLOYEE,VolleyHttp.paramsCreate(employee),object : VolleyHandler {
                override fun onSuccess(response: String) {
                    Log.d("@@@onCreate ", "" + response)
                    Toast.makeText(this@MainActivity,employee.employee_name.toString() + " Created",Toast.LENGTH_LONG).show()
                    apiEmployeeList()
                }

                override fun onError(error: String) {
                    Log.d("@@@onErrorResponse ", error)
                    apiEmployeeCreate(employee)
                }
            })
    }

    private fun apiEmployeeEdit(employee: Employee) {
        pb_loading.visibility = View.VISIBLE
        VolleyHttp.put(VolleyHttp.API_UPDATE_EMPLOYEE + employee.id,VolleyHttp.paramsUpdate(employee),object : VolleyHandler {
                override fun onSuccess(response: String) {
                    Toast.makeText(this@MainActivity,employee.employee_name + " Edited",Toast.LENGTH_LONG).show()
                    Log.d("@@@onEdit ", response)
                    apiEmployeeList()
                }

                override fun onError(error: String) {
                    Log.d("@@@onErrorResponse ", error)
                    apiEmployeeEdit(employee)
                }
            })
    }

    private fun apiEmployeeDelete(employee: Employee) {
        pb_loading.visibility = View.VISIBLE
        VolleyHttp.del(VolleyHttp.API_DELETE_EMPLOYEE + employee.id, object : VolleyHandler {
            override fun onSuccess(response: String) {
                Log.d("@@@onDeleted", response)
                Toast.makeText(this@MainActivity,"Employer " + employee.id + " Deleted",Toast.LENGTH_LONG).show()
                apiEmployeeList()
                //pb_loading.setVisibility(View.GONE);
            }

            override fun onError(error: String) {
                Log.d("@@@onErrorResponse ", error)
                apiEmployeeDelete(employee)
            }
        })
    }


}