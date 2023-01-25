package com.example.volley

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException

class MainActivity : AppCompatActivity() {

    private var getRequest: StringRequest? = null
    private var postRequest: StringRequest? = null
    private var requestQueue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestQueue = Volley.newRequestQueue(this@MainActivity)

        btnGetRequest.setOnClickListener { getRequest() }

        btnPostRequest.setOnClickListener { postRequest() }
    }

    @SuppressLint("SetTextI18n")
    private fun getRequest() {
        progressBar.visibility = View.VISIBLE
        val url = "https://simplifiedcoding.net/demos/marvel"
        getRequest = StringRequest(Request.Method.GET, url,
            { response ->
                txtResultValue.text = response
                progressBar.visibility = View.GONE
            }) {
            txtResultValue.text = "Something went wrong, please try again later."
            progressBar.visibility = View.GONE
        }
        getRequest!!.tag = "getRequest"
        requestQueue!!.add(getRequest)
    }

    private fun postRequest() {
        progressBar.visibility = View.VISIBLE
        val url = "https://reqres.in/api/users"
        val requestBody: String
        try {
            val jsonBody = JSONObject()
            jsonBody.put("name", "IT wala")
            requestBody = jsonBody.toString()
            postRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    Log.d("TAG_Response", response)
                    txtResultValue.text = response
                    progressBar.visibility = View.GONE
                }, Response.ErrorListener { error ->
                    Log.d("TAG_Error", error.toString())
                    progressBar.visibility = View.GONE
                }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                override fun getBody(): ByteArray? {
                    return try {
                        requestBody.toByteArray(Charsets.UTF_8)
                    }catch (e : UnsupportedEncodingException) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8")
                        null
                    }
                }
            }
            getRequest!!.tag = "postRequest"
            requestQueue!!.add(postRequest)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onStop() {
        if (requestQueue != null) {
            requestQueue!!.cancelAll("getRequest")
            requestQueue!!.cancelAll("postRequest")
            progressBar.visibility = View.GONE
        }
        super.onStop()
    }
}
