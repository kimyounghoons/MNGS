package com.mngs.kimyounghoon.mngs.data.source

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mngs.kimyounghoon.mngs.data.Constants
import com.mngs.kimyounghoon.mngs.data.Letter

object LettersFirebaseDataSource : LettersDataSource {

    private val lettersCollection = FirebaseFirestore.getInstance().collection("letters")
    private lateinit var documentReference: DocumentReference
    private var loadMoreQuery: Query? = null
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

                    val lastVisible: DocumentSnapshot = it.documents.get(it.size() - 1)
                    loadMoreQuery = lettersCollection.startAfter(lastVisible).limit(Constants.LIMIT_PAGE)

                }.addOnFailureListener {
                    callback.onFailedToLoadLetters()
                }
    }

    override fun loadMoreLetters(callback: LettersDataSource.LoadMoreLettersCallback) {
        loadMoreQuery?.get()?.addOnSuccessListener {

            val letters: ArrayList<Letter> = ArrayList()
            for (doc in it.documents) {
                val letter = doc.toObject(Letter::class.java)
                letter?.apply {
                    letters.add(this)
                }
            }
            if (letters.size > 0) {
                callback.onLettersMoreLoaded(letters)
            }

            if (it.documents.size > 0) {
                val lastVisible: DocumentSnapshot = it.documents.get(it.size() - 1)
                loadMoreQuery = lettersCollection.startAfter(lastVisible).limit(Constants.LIMIT_PAGE)

            }
        }?.addOnFailureListener {
            callback.onFailedToLoadMoreLetters()
        }
    }

}