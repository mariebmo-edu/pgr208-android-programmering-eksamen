package no.kristiania.reverseimagesearch.viewmodel.utils

import org.json.JSONArray

class JsonArrUtils {
    fun multipleJsonArraysToOne(vararg jarr: JSONArray?): JSONArray {
        val jsonResponse = JSONArray()

        fun addToResponse(arr: JSONArray) {
            println("in add to response: " + arr.length())
            for (i in 0 until arr.length()) {
                jsonResponse.put(arr[i])
            }
        }

        for (i in jarr.indices) {
            if (jarr[i] != null) {
                addToResponse(jarr[i]!!)
            }
        }

        println("AllProviders: $jsonResponse")
        return jsonResponse
    }
}