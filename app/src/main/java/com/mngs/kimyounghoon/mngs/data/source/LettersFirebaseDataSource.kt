package com.mngs.kimyounghoon.mngs.data.source

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mngs.kimyounghoon.mngs.BuildConfig
import com.mngs.kimyounghoon.mngs.data.Answer
import com.mngs.kimyounghoon.mngs.data.Constants
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.ANSWER
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.HAS_ANSWER
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.LETTERS
import com.mngs.kimyounghoon.mngs.data.Letter

object LettersFirebaseDataSource : LettersDataSource {

    private val lettersCollection = FirebaseFirestore.getInstance().collection(BuildConfig.BUILD_TYPE).document(LETTERS).collection(LETTERS)
    private lateinit var letterDocumentReference: DocumentReference

    private val loadLettersCollection = lettersCollection.whereEqualTo(HAS_ANSWER, false).limit(10)

    private val answerCollection = FirebaseFirestore.getInstance().collection(BuildConfig.BUILD_TYPE).document(ANSWER).collection(ANSWER)
    private lateinit var answerDocumentReference: DocumentReference

    private var loadMoreQuery: Query? = null

    private var inboxCollection: Query? = null
    private var loadMoreInboxQuery: Query? = null

    override fun getLetterId(): String {
        letterDocumentReference = lettersCollection.document()
        return letterDocumentReference.id
    }

    override fun getAnswerId(): String {
        answerDocumentReference = answerCollection.document()
        return answerDocumentReference.id
    }

    override fun answerLetter(answer: Answer, callBack: LettersDataSource.SendAnswerCallback) {
        answerDocumentReference.set(answer).addOnSuccessListener {
            updateHasAnswer(answer.letterId, callBack)
        }.addOnFailureListener {
            callBack.onFailedToSendAnswer()
        }
    }

    private fun updateHasAnswer(letterId: String, callBack: LettersDataSource.SendAnswerCallback) {
        lettersCollection.document(letterId).update(HAS_ANSWER, true).addOnSuccessListener {
            callBack.onAnswerSended()
        }.addOnFailureListener {
            callBack.onFailedToSendAnswer()
        }
    }

    override fun getLetter(letterId: String, callBack: LettersDataSource.GetLetterCallback) {

    }

    override fun sendLetter(letter: Letter, callBack: LettersDataSource.SendLetterCallback) {
        letterDocumentReference.set(letter).addOnSuccessListener {
            callBack.onLetterSended()
        }.addOnFailureListener {
            callBack.onFailedToSendLetter()
        }
    }

    override fun loadLetters(callback: LettersDataSource.LoadLettersCallback) {
        loadLettersCollection
                .get().addOnSuccessListener {

                    val letters: ArrayList<Letter> = ArrayList()
                    for (doc in it.documents) {
                        val letter = doc.toObject(Letter::class.java)
                        letter?.apply {
                            letters.add(this)
                        }
                    }
                    if (letters.size >= 0) {
                        callback.onLettersLoaded(letters)
                    }
                    if (letters.size > 0) {
                        val lastVisible: DocumentSnapshot = it.documents[it.size() - 1]
                        loadMoreQuery = loadLettersCollection.startAfter(lastVisible).limit(Constants.LIMIT_PAGE)
                    }
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
                loadMoreQuery = loadLettersCollection.startAfter(lastVisible).limit(Constants.LIMIT_PAGE)
            }
        }?.addOnFailureListener {
            callback.onFailedToLoadMoreLetters()
        }
    }

    override fun loadInBox(callback: LettersDataSource.LoadLettersCallback) {
        inboxCollection = FirebaseFirestore.getInstance().collection(BuildConfig.BUILD_TYPE).document("letters").collection("letters").whereEqualTo("userId", FirebaseAuth.getInstance().uid)
        inboxCollection!!.limit(10)
                .get().addOnSuccessListener {

                    val letters: ArrayList<Letter> = ArrayList()
                    for (doc in it.documents) {
                        val letter = doc.toObject(Letter::class.java)
                        letter?.apply {
                            letters.add(this)
                        }
                    }
                    if (letters.size >= 0) {
                        callback.onLettersLoaded(letters)
                    }
                    if (letters.size > 0) {
                        val lastVisible: DocumentSnapshot = it.documents[it.size() - 1]
                        loadMoreInboxQuery = inboxCollection!!.startAfter(lastVisible).limit(Constants.LIMIT_PAGE)
                    }
                }.addOnFailureListener {
                    callback.onFailedToLoadLetters()
                }
    }

    override fun loadMoreInBox(callback: LettersDataSource.LoadMoreLettersCallback) {
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

    override fun loadAnswers(callback: LettersDataSource.LoadLettersCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadMoreAnswers(callback: LettersDataSource.LoadMoreLettersCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}