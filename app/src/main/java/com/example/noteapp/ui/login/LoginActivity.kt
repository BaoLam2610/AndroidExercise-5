package com.example.noteapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.noteapp.ui.main.MainActivity
import com.example.noteapp.R
import com.example.noteapp.event.IRegister
import com.example.noteapp.presenter.PasswordPresenter
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), IRegister.Password, IRegister {

    lateinit var presenter: PasswordPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        presenter = PasswordPresenter(this, this, this)

        etPassword.setOnFocusChangeListener { view, b ->
            ivShow1.visibility = View.VISIBLE
            ivShow2.visibility = View.VISIBLE

            ivHide1.visibility = View.GONE
            ivHide2.visibility = View.GONE
        }

        etConfirmPassword.setOnFocusChangeListener { view, b ->
            ivShow1.visibility = View.GONE
            ivShow2.visibility = View.GONE

            ivHide1.visibility = View.VISIBLE
            ivHide2.visibility = View.VISIBLE
        }
        presenter.check()


    }

    override fun onSuccessful() {
        Intent(this, MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }

    override fun onFailure(mes: String) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Lỗi")
            .setMessage(mes)
            .setNegativeButton("Cancel",null)
            .create()
        dialog.show()
    }

    override fun checkFirst(type: Int) {
        when (type) {
            0 -> {
                tvForget.visibility = View.GONE
                etConfirmPassword.visibility = View.VISIBLE
                btnRegister.setOnClickListener {
                    presenter.register(etPassword.text.toString(),
                        etConfirmPassword.text.toString())
                }
            }
            1 -> {
                tvForget.visibility = View.VISIBLE
                etConfirmPassword.visibility = View.GONE
                btnRegister.setOnClickListener {
                    presenter.checkPassword(etPassword.text.toString())
                }
                tvForget.setOnClickListener {
                    presenter.resetPassword()
                    presenter.check()
                }
            }
        }

    }
}