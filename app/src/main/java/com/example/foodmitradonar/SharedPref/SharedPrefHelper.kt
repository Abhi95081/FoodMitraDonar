package com.example.foodmitradonar.SharedPref

import android.content.Context
import android.content.SharedPreferences

class SharedPrefHelper(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("donor_prefs", Context.MODE_PRIVATE)

    fun saveDonor(name: String, mobile: String, imageUri: String) {
        prefs.edit().apply {
            putString("name", name)
            putString("mobile", mobile)
            putString("imageUri", imageUri)
            apply()
        }
    }

    fun getDonorName() = prefs.getString("name", "") ?: ""
    fun getDonorMobile() = prefs.getString("mobile", "") ?: ""
    fun getDonorImageUri() = prefs.getString("imageUri", "") ?: ""

    fun isDonorRegistered(): Boolean {
        val name = prefs.getString("name", null)
        val mobile = prefs.getString("mobile", null)
        val imageUri = prefs.getString("imageUri", null)
        return !name.isNullOrBlank() && !mobile.isNullOrBlank() && !imageUri.isNullOrBlank()
    }

    fun clearDonorData() {
        prefs.edit().clear().apply()
    }
}
