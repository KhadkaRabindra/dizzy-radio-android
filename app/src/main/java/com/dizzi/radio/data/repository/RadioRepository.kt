package com.dizzi.radio.data.repository

interface RadioRepository {

    fun getListenerCount(callback: (Int) -> Unit)

    fun updateListenerCount(newCount: Int)
}