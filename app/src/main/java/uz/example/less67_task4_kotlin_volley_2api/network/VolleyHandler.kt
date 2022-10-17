package uz.example.less67_task4_kotlin_volley_2api.network

interface VolleyHandler {
    fun onSuccess(response: String)
    fun onError(error: String)
}