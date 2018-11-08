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
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.LETTER_ID
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.LIMIT_PAGE
import com.mngs.kimyounghoon.mngs.data.Letter

object LettersFirebaseDataSource : LettersDataSource {

    private val lettersCollection = FirebaseFirestore.getInstance().collection(BuildConfig.BUILD_TYPE).document(LETTERS).collection(LETTERS)
    private lateinit var letterDocumentReference: DocumentReference

    private val loadLettersCollection = lettersCollection.whereEqualTo(HAS_ANSWER, false).limit(LIMIT_PAGE)

    private val answerCollection = FirebaseFirestore.getInstance().collection(BuildConfig.BUILD_TYPE).document(ANSWER).collection(ANSWER)
    private lateinit var answerDocumentReference: DocumentReference

    private lateinit var loadAnswersQuery : Query
    private var answersLoadMoreQuery: Query? = null

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
                        loadMoreQuery = loadLettersCollection.startAfter(lastVisible).limit(LIMIT_PAGE)
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
            if (letters.size >= 0) {
                callback.onLettersMoreLoaded(letters)
            }

            if (it.documents.size > 0) {
                val lastVisible: DocumentSnapshot = it.documents.get(it.size() - 1)
                loadMoreQuery = loadLettersCollection.startAfter(lastVisible).limit(LIMIT_PAGE)
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
                        loadMoreInboxQuery = inboxCollection!!.startAfter(lastVisible).limit(LIMIT_PAGE)
                    }
                }.addOnFailureListener {
                    callback.onFailedToLoadLetters()
                }
    }

    override fun loadMoreInBox(callback: LettersDataSource.LoadMoreLettersCallback) {
        loadMoreInboxQuery?.get()?.addOnSuccessListener {

            val letters: ArrayList<Letter> = ArrayList()
            for (doc in it.documents) {
                val letter = doc.toObject(Letter::class.java)
                letter?.apply {
                    letters.add(this)
                }
            }
            if (letters.size >= 0) {
                callback.onLettersMoreLoaded(letters)
            }

            if (it.documents.size > 0) {
                val lastVisible: DocumentSnapshot = it.documents.get(it.size() - 1)
                loadMoreInboxQuery = inboxCollection!!.startAfter(lastVisible).limit(LIMIT_PAGE)
            }
        }?.addOnFailureListener {
            callback.onFailedToLoadMoreLetters()
        }
    }

    override fun loadAnswers(letterId: String, callback: LettersDataSource.LoadAnswersCallback) {
        loadAnswersQuery = answerCollection.whereEqualTo(LETTER_ID, letterId).limit(10)
        loadAnswersQuery.get().addOnSuccessListener {

            val answers: ArrayList<Answer> = ArrayList()
            for (doc in it.documents) {
                val answer= doc.toObject(Answer::class.java)
                answer?.apply {
                    answers.add(this)
                }
            }
            if (answers.size >= 0) {
                callback.onAnswersLoaded(answers)
            }

            if (it.documents.size > 0) {
                val lastVisible: DocumentSnapshot = it.documents.get(it.size() - 1)
                answersLoadMoreQuery = loadAnswersQuery.startAfter(lastVisible).limit(LIMIT_PAGE)
            }
        }.addOnFailureListener {
            callback.onFailedToLoadAnswers()
        }
    }

    override fun loadMoreAnswers(callback: LettersDataSource.LoadMoreAnswersCallback) {
        answersLoadMoreQuery?.get()?.addOnSuccessListener {

            val answers: ArrayList<Answer> = ArrayList()
            for (doc in it.documents) {
                val answer = doc.toObject(Answer::class.java)
                answer?.apply {
                    answers.add(this)
                }
            }
            if (answers.size >= 0) {
                callback.onAnswersMoreLoaded(answers)
            }

            if (it.documents.size > 0) {
                val lastVisible: DocumentSnapshot = it.documents.get(it.size() - 1)
                answersLoadMoreQuery = answersLoadMoreQuery!!.startAfter(lastVisible).limit(LIMIT_PAGE)
            }
        }?.addOnFailureListener {
            callback.onFailedToLoadMoreAnswers()
        }
    }

}