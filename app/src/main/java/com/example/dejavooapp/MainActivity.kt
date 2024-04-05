package com.example.dejavooapp

import okhttp3.*
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val saleButton: Button = findViewById(R.id.sale_button)
        val cancelButton: Button = findViewById(R.id.cancel_button)
        val refundButton: Button = findViewById(R.id.refund_button)
        saleButton.setOnClickListener {
        GlobalScope.launch (Dispatchers.IO)  {
                makeSale(60.0)
            }
        }
        cancelButton.setOnClickListener{
            GlobalScope.launch (Dispatchers.IO){
                cancelSale(50.0)
            }
        }
        refundButton.setOnClickListener {
            GlobalScope.launch (Dispatchers.IO){
                refundSale(50.0)
            }
        }

        }
    }

    fun generateAlphanumericString(length: Int): String {
        require(length in 1..50)
        val alphanumericCharacters = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { alphanumericCharacters.random() }
            .joinToString("")
    }
    private suspend fun makeSale(amount: Double) {
        withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val mediaType = "application/json".toMediaTypeOrNull()
            val requestBody = RequestBody.create(
                mediaType, """
                      {
                  "Amount": $amount,
                  "TipAmount": 2,
                  "ExternalReceipt": "",
                  "PaymentType": "Credit",
                  "ReferenceId": ${generateAlphanumericString(15)},
                  "PrintReceipt": "No",
                  "GetReceipt": "No",
                  "MerchantNumber": null,
                  "InvoiceNumber": "",
                  "CaptureSignature": false,
                  "GetExtendedData": true,
                  "CallbackInfo": {
                    "Url": ""
                  },
                  "Tpn": "901224304820",
                  "Authkey": "aMiWUUhUvr",
                  "SPInProxyTimeout": null,
                  "CustomFields": {}
                }
                    """.trimIndent()
            )

            val request = Request.Builder()
                .url("https://test.spinpos.net/spin/v2/Payment/Sale")
                .method("POST", requestBody)
                .addHeader("Content-Type", "application/json")
                .build()

            Log.d("request", request.toString())
            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code $response")
                }
                val responseData = response.body?.string()
                println(responseData)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }

        private suspend fun cancelSale(amount: Double) {
            withContext(Dispatchers.IO) {
                val client = OkHttpClient()
                val mediaType = "application/json".toMediaTypeOrNull()
                val requestBody = RequestBody.create(
                    mediaType, """
                      {
                  "Amount": $amount,
                  "TipAmount": 2,
                  "ExternalReceipt": "",
                  "PaymentType": "Credit",
                  "ReferenceId": "d6c8ncsr55a580",
                  "PrintReceipt": "No",
                  "GetReceipt": "No",
                  "MerchantNumber": null,
                  "InvoiceNumber": "",
                  "CaptureSignature": false,
                  "GetExtendedData": true,
                  "CallbackInfo": {
                    "Url": ""
                  },
                  "Tpn": "901224304820",
                  "Authkey": "aMiWUUhUvr",
                  "SPInProxyTimeout": null,
                  "CustomFields": {}
                }
                    """.trimIndent()
                )

                val request = Request.Builder()
                    .url("https://test.spinpos.net/spin/v2/Payment/Void")
                    .method("POST", requestBody)
                    .addHeader("Content-Type", "application/json")
                    .build()

                Log.d("cancel request", request.toString())
                try {
                    val response = client.newCall(request).execute()
                    if (!response.isSuccessful) {
                        throw IOException("Unexpected code $response")
                    }
                    val responseData = response.body?.string()
                    println(responseData)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

            private suspend fun refundSale(amount: Double) {
                withContext(Dispatchers.IO) {
                    val client = OkHttpClient()
                    val mediaType = "application/json".toMediaTypeOrNull()
                    val requestBody = RequestBody.create(
                        mediaType, """
                      {
                  "Amount": $amount,
                  "TipAmount": 2,
                  "ExternalReceipt": "",
                  "PaymentType": "Credit",
                  "ReferenceId": "d6c871jgyysx55b6a580",
                  "PrintReceipt": "No",
                  "GetReceipt": "No",
                  "MerchantNumber": null,
                  "InvoiceNumber": "",
                  "CaptureSignature": false,
                  "GetExtendedData": true,
                  "CallbackInfo": {
                    "Url": ""
                  },
                  "Tpn": "901224304820",
                  "Authkey": "aMiWUUhUvr",
                  "SPInProxyTimeout": null,
                  "CustomFields": {}
                }
                    """.trimIndent()
                    )

                    val request = Request.Builder()
                        .url("https://test.spinpos.net/spin/v2/Payment/Return")
                        .method("POST", requestBody)
                        .addHeader("Content-Type", "application/json")
                        .build()

                    Log.d("cancel request", request.toString())
                    try {
                        val response = client.newCall(request).execute()
                        if (!response.isSuccessful) {
                            throw IOException("Unexpected code $response")
                        }
                        val responseData = response.body?.string()
                        println(responseData)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
        }
