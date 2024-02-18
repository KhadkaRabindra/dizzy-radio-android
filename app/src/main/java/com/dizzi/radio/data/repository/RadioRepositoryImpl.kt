package com.dizzi.radio.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class RadioRepositoryImpl @Inject constructor(
    private var listenersReference: DatabaseReference,
) : RadioRepository {

    override fun getListenerCount(callback: (Int) -> Unit) {
        listenersReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.getValue(Int::class.java) ?: 0
                callback(count)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(0)
            }
        })
    }

    override fun updateListenerCount(newCount: Int) {
        listenersReference.setValue(newCount)

    }
}