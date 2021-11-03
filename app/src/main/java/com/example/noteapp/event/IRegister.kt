package com.example.noteapp.event

interface IRegister {
    interface Password{
        fun onSuccessful()
        fun onFailure(mes: String)
    }
    fun checkFirst(type: Int)
}