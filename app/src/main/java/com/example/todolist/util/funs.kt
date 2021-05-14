package com.example.todolist.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

fun showToast(string: String) {
    Toast.makeText(APP_ACTIVITY, string, Toast.LENGTH_SHORT).show()
}

fun hideKeyboard() {
    val imm: InputMethodManager =
        APP_ACTIVITY.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(APP_ACTIVITY.window.decorView.windowToken, 0)
}