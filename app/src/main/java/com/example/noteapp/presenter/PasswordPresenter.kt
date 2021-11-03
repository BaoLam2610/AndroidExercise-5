package com.example.noteapp.presenter

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.noteapp.event.IRegister
import com.example.noteapp.until.Constant.KEY_PASSWORD
import com.example.noteapp.until.Constant.PASSWORD

class PasswordPresenter {
    private var context: Context? = null
    private var iRegisterPassword: IRegister.Password? = null
    private var iRegister: IRegister? = null
    private val sharedPref: SharedPreferences
    private val editor: SharedPreferences.Editor

    constructor(context: Context, iRegisterPassword: IRegister.Password, iRegister: IRegister) {
        this.context = context
        this.iRegisterPassword = iRegisterPassword
        this.iRegister = iRegister
        sharedPref = context.getSharedPreferences(KEY_PASSWORD, MODE_PRIVATE)
        editor = sharedPref.edit()
    }

    fun check() {
        val firstCheck = sharedPref.getString(PASSWORD, null)
        if (firstCheck == null) {   // lan dau dang nhap
            iRegister?.checkFirst(0)
        } else {
            iRegister?.checkFirst(1)
        }
    }

    fun register(pass: String, confirmPass: String) {
        if (pass.isEmpty()) {
            iRegisterPassword?.onFailure("Mật khẩu không được bỏ trống")
            return
        }
        if (pass.lowercase() != confirmPass.lowercase()) {
            iRegisterPassword?.onFailure("Xác nhận mật khẩu không chính xác")
            return
        }
        editor.putString(PASSWORD, pass.trim())?.apply()
        iRegisterPassword?.onSuccessful()
    }

    fun checkPassword(password: String) {
        if (password.isEmpty()) {
            iRegisterPassword?.onFailure("Mật khẩu không được bỏ trống")
            return
        }
        val check = sharedPref.getString(PASSWORD, null)
        if (password.lowercase() != check?.lowercase())
            iRegisterPassword?.onFailure("Sai mật khẩu")
        else
            iRegisterPassword?.onSuccessful()
    }

    fun resetPassword(){
        editor.clear().commit()
    }

}