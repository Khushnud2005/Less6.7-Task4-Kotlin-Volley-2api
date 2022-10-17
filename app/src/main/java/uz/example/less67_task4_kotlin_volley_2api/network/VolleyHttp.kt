package uz.example.less67_task4_kotlin_volley_2api.network

import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject
import uz.example.less67_task4_kotlin_volley_2api.App
import uz.example.less67_task4_kotlin_volley_2api.model.Employee

class VolleyHttp {
    companion object{
        val TAG = VolleyHttp::class.java.simpleName
        val IS_TESTER = true
        val SERVER_DEVELOPMENT = "https://dummy.restapiexample.com/api/v1/"
        val SERVER_PRODUCTION = "https://dummy.restapiexample.com/api/v1/"

        fun server(url:String):String{
            if (IS_TESTER){
                return SERVER_DEVELOPMENT + url
            }
            return SERVER_PRODUCTION + url
        }

        fun headers():HashMap<String,String>{
            val headers = HashMap<String,String>()
            headers["Content-type"] = "application/json; charset=UTF-8"
            return headers
        }

        fun get(api:String,params:HashMap<String,String>,volleyHandler: VolleyHandler){
            val stringRequest = object: StringRequest(
                Method.GET, server(api), Response.Listener { response ->

                    volleyHandler.onSuccess(response)
                },
                Response.ErrorListener { error ->

                    volleyHandler.onError(error.toString())
                }
            ){
                override fun getParams(): MutableMap<String, String> {
                    return params
                }
            }
            App.instance!!.addToRequestQueue(stringRequest)
        }

        fun post(api: String, body: HashMap<String, String>, handler: VolleyHandler) {
            val stringRequest: StringRequest =
                object : StringRequest(Method.POST, server(api),
                    Response.Listener { response ->
                        handler.onSuccess(response)
                    }, Response.ErrorListener { error ->
                        handler.onError(error.toString())
                    }) {
                    override fun getHeaders(): Map<String, String> {
                        return headers()
                    }
                    override fun getBody(): ByteArray {
                        return JSONObject(body as Map<*,*>).toString().toByteArray()
                    }
                }
            App.instance!!.addToRequestQueue(stringRequest)
        }

        fun put(api: String, body: HashMap<String, String>, handler: VolleyHandler) {
            val stringRequest: StringRequest =
                object : StringRequest(Method.PUT, server(api),
                    Response.Listener { response ->
                        handler.onSuccess(response)
                    }, Response.ErrorListener { error ->
                        handler.onError(error.toString())
                    }) {
                    override fun getHeaders(): Map<String, String> {
                        return headers()
                    }
                    override fun getBody(): ByteArray {
                        return JSONObject(body as Map<*, *>).toString().toByteArray()
                    }
                }
            App.instance!!.addToRequestQueue(stringRequest)
        }

        fun del(api: String, handler: VolleyHandler) {
            val stringRequest = object : StringRequest(Method.DELETE, server(api),
                Response.Listener { response ->
                    handler.onSuccess(response)
                }, Response.ErrorListener{ error ->
                    handler.onError(error.toString())
                }){}
            App.instance!!.addToRequestQueue(stringRequest)
        }

        /**
         * Request Api`s
         */
        var API_LIST_EMPLOYEE: String = "employees"
        var API_SINGLE_EMPLOYEE = "employee/" //id

        var API_CREATE_EMPLOYEE = "create"
        var API_UPDATE_EMPLOYEE = "update/" //id

        var API_DELETE_EMPLOYEE = "delete/" //id


        fun paramsEmpty(): HashMap<String, String> {
            return HashMap()
        }

        fun paramsCreate(employee: Employee): HashMap<String, String> {
            val params = java.util.HashMap<String, String>()
            params["employee_name"] = employee.employee_name
            params["employee_salary"] = employee.employee_salary.toString()
            params["employee_age"] = employee.employee_age.toString()
            return params
        }

        fun paramsUpdate(employee: Employee): HashMap<String, String> {
            val params = HashMap<String, String>()
            params["id"] = employee.id.toString()
            params["employee_name"] = employee.employee_name
            params["employee_salary"] = employee.employee_salary.toString()
            params["employee_age"] = employee.employee_age.toString()
            return params
        }
    }
}