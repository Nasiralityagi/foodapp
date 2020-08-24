package com.android.tlb.store

import android.content.Context
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import java.util.regex.Pattern

class VssValidation {
    fun isCheckFirstName(context: Context, edtFirstName: EditText): Boolean {
        var result = true
        if (TextUtils.isEmpty(edtFirstName.text)) {
           Toast.makeText(context,"FirstName is required",Toast.LENGTH_SHORT).show()
            result = false
        }
        return result
    }

    fun isCheckLastName(context: Context, edtLastName: EditText): Boolean {
        var result = true
        if (TextUtils.isEmpty(edtLastName.text)) {
            Toast.makeText(context,"LastName is required",Toast.LENGTH_SHORT).show()
            result = false
        }
        return result
    }

    fun isCheckEmail(context: Context, edtEmail: EditText): Boolean {
        var result = true
        if (TextUtils.isEmpty(edtEmail.text)) {
            Toast.makeText(context,"Email is required",Toast.LENGTH_SHORT).show()
            result = false
        } else if (!instance!!.isEmailValid(edtEmail.text.toString())) {
            edtEmail.error = "Email is invalid"
            result = false
        }
        return result
    }

    fun isCheckPassword(
        context: Context,
        edtPassword: EditText
    ): Boolean {
        var result = true
        if (TextUtils.isEmpty(edtPassword.text)) {
            edtPassword.error = "Password is required"
            result = false
        } else if (!instance!!.isPasswordValid(edtPassword.text.toString())) {
            edtPassword.error = "Password Invalid"
            result = false
        }
        return result
    }

    fun isCheckPhoneNumber(context: Context, edtNumber: EditText): Boolean {
        var result = true
        if (TextUtils.isEmpty(edtNumber.text)) {
            Toast.makeText(context,"Phone number is required",Toast.LENGTH_SHORT).show()
            result = false
        }
        return result
    }

    fun isCheckBirthDate(context: Context, edtBirthDate: EditText): Boolean
    {
        var result = true
        if (TextUtils.isEmpty(edtBirthDate.text)) {
            Toast.makeText(context,"BirthDate is required",Toast.LENGTH_SHORT).show()
            result = false
        }
        return result
    }
    fun isCheckAddress(context: Context, edtAddress: EditText): Boolean
    {
        var result = true
        if (TextUtils.isEmpty(edtAddress.text)) {
            Toast.makeText(context,"Address is required",Toast.LENGTH_SHORT).show()
            result = false
        }
        return result
    }

     fun isPasswordValid(password: String): Boolean { //TODO: Replace this with your own logic
        return password.length > 6
    }

     fun isEmailValid(email: String?): Boolean {
        val pattern =
            Pattern.compile(EMAIL_PATTERN)
        return pattern.matcher(email).matches()
    }

    companion object {
        const val EMAIL_PATTERN = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        private var mInstance: VssValidation? = null
        @get:Synchronized
        val instance: VssValidation?
            get() {
                if (mInstance == null) {
                    synchronized(VssValidation::class.java) {
                        if (mInstance == null) {
                            mInstance = VssValidation()
                        }
                    }
                }
                return mInstance
            }
    }
}