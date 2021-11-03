package com.example.noteapp.event

interface IRegister {
    interface Password{
        fun onSuccessful()
        fun onFailure(mes: String)
    }

    interface Reset{

    }

    fun checkFirst(type: Int)
}