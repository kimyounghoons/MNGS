package com.mngs.kimyounghoon.mngs.data.source

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mngs.kimyounghoon.mngs.data.Letter

object LettersFirebaseDataSource : LettersDataSource {

    private val lettersCollection = FirebaseFirestore.getInstance().collection("letters")
    private lateinit var documentReference: DocumentReference

    override fun getId(): String {
        documentReference = lettersCollection.document()
        return documentReference.id
    }

    override fun getLetter(letterId: String, callBack: LettersDataSource.GetLetterCallback) {

    }

    override fun sendLetter(letter: Letter, callBack: LettersDataSource.SendLetterCallback) {
        documentReference.set(letter).addOnSuccessListener {
            callBack.onLetterSended()
        }.addOnFailureListener {
            callBack.onFailedToSendLetter()
        }
    }

    override fun loadLetters(callback: LettersDataSource.LoadLettersCallback) {
        lettersCollection.limit(10)
                .get().addOnSuccessListener {
                    val letters: ArrayList<Letter> = ArrayList()
                    for (doc in it.documents) {
                        val letter = doc.toObject(Letter::class.java)
                        letter?.apply {
                            letters.add(this)
                        }
                    }
                    if (letters.size > 0) {
                        callback.onLettersLoaded(letters)
                    }
                }.addOnFailureListener {
                    callback.onFailedToLoadLetters()
                }
    }

}