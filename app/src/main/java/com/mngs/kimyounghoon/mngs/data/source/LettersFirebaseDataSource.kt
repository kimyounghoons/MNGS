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
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.ANSWER_USER_ID
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.HAS_ANSWER
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.LETTERS
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.LETTER_ID
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.LIMIT_PAGE
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.REANSWER
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.TIME
import com.mngs.kimyounghoon.mngs.data.Letter
import com.mngs.kimyounghoon.mngs.data.ReAnswer

object LettersFirebaseDataSource : LettersDataSource {

    private val lettersCollection = FirebaseFirestore.getInstance().collection(BuildConfig.BUILD_TYPE).document(LETTERS).collection(LETTERS)
    private lateinit var letterDocumentReference: DocumentReference

    private val loadLettersCollection = lettersCollection.whereEqualTo(HAS_ANSWER, false).orderBy(TIME, Query.Direction.DESCENDING).limit(LIMIT_PAGE)

    private val answerCollection = FirebaseFirestore.getInstance().collection(BuildConfig.BUILD_TYPE).document(ANSWER).collection(ANSWER)
    private lateinit var answerDocumentReference: DocumentReference

    private val reAnswerCollection = FirebaseFirestore.getInstance().collection(BuildConfig.BUILD_TYPE).document(REANSWER).collection(REANSWER)
    private lateinit var reAnswerDocumentReference: DocumentReference

    private lateinit var loadReAnswersQuery: Query
    private var reAnswersLoadMoreQuery: Query? = null

    private lateinit var loadAnswersQuery: Query
    private var answersLoadMoreQuery: Query? = null

    private lateinit var loadSentAnswersQuery: Query
    private var loadMoreSentAnswersQuery: Query? = null

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
        lettersCollection.whereEqualTo(LETTER_ID, letterId).get().addOnSuccessListener {

            val currentUid = FirebaseAuth.getInstance().currentUser!!.uid
            for (doc in it.documents) {
                val letter = doc.toObject(Letter::class.java)
                letter?.apply {
                    callBack.onLetterLoaded(letter)
                }
            }
        }.addOnFailureListener {
            callBack.onFailedToLoadLetters()
        }
    }

    override fun sendLetter(letter: Letter, callBack: LettersDataSource.SendLetterCallback) {
        letterDocumentReference.set(letter).addOnSuccessListener {
            callBack.onLetterSended()
        }.addOnFailureListener {
            callBack.onFailedToSendLetter()
        }
    }

    override fun sendReAnswer(reAnswer: ReAnswer, callback: LettersDataSource.SendLetterCallback) {
        reAnswerDocumentReference.set(reAnswer).addOnSuccessListener {
            callback.onLetterSended()
        }.addOnFailureListener {
            callback.onFailedToSendLetter()
        }
    }

    override fun getReAnswerId(): String {
        reAnswerDocumentReference = reAnswerCollection.document()
        return reAnswerDocumentReference.id
    }

    override fun loadLetters(callback: LettersDataSource.LoadLettersCallback) {
        loadLettersCollection.get().addOnSuccessListener {

            val letters: ArrayList<Letter> = ArrayList()
            val currentUid = FirebaseAuth.getInstance().currentUser!!.uid
            for (doc in it.documents) {
                val letter = doc.toObject(Letter::class.java)
                letter?.apply {
                    if (letter.userId != currentUid)
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
        loadMoreQuery?.orderBy(TIME, Query.Direction.DESCENDING)?.get()?.addOnSuccessListener {

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
        inboxCollection!!.orderBy(TIME, Query.Direction.DESCENDING).limit(10)
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
        loadMoreInboxQuery?.orderBy(TIME, Query.Direction.DESCENDING)?.get()?.addOnSuccessListener {

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
        loadAnswersQuery = answerCollection.whereEqualTo(LETTER_ID, letterId).limit(Constants.LIMIT_PAGE)
        loadAnswersQuery.orderBy(TIME, Query.Direction.DESCENDING).get().addOnSuccessListener {

            val answers: ArrayList<Answer> = ArrayList()
            for (doc in it.documents) {
                val answer = doc.toObject(Answer::class.java)
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
        answersLoadMoreQuery?.orderBy(TIME, Query.Direction.DESCENDING)?.get()?.addOnSuccessListener {

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


    override fun loadReAnswers(letterId: String, callback: LettersDataSource.LoadReAnswersCallback) {
        loadReAnswersQuery = reAnswerCollection.whereEqualTo(LETTER_ID, letterId).orderBy(TIME, Query.Direction.ASCENDING).limit(Constants.LIMIT_PAGE)
        loadReAnswersQuery.get().addOnSuccessListener {

            val reAnswers: ArrayList<ReAnswer> = ArrayList()
            for (doc in it.documents) {
                val reAnswer = doc.toObject(ReAnswer::class.java)
                reAnswer?.apply {
                    reAnswers.add(this)
                }
            }
            if (reAnswers.size >= 0) {
                callback.onReAnswersLoaded(reAnswers)
            }

            if (it.documents.size > 0) {
                val lastVisible: DocumentSnapshot = it.documents.get(it.size() - 1)
                reAnswersLoadMoreQuery = loadReAnswersQuery.startAfter(lastVisible).limit(LIMIT_PAGE)
            }
        }.addOnFailureListener {
            callback.onFailedToLoadReAnswers()
        }
    }

    override fun loadMoreReAnswers(callback: LettersDataSource.LoadMoreReAnswersCallback) {
        reAnswersLoadMoreQuery?.orderBy(TIME, Query.Direction.ASCENDING)?.get()?.addOnSuccessListener {

            val reAnswers: ArrayList<ReAnswer> = ArrayList()
            for (doc in it.documents) {
                val reAnswer = doc.toObject(ReAnswer::class.java)
                reAnswer?.apply {
                    reAnswers.add(this)
                }
            }
            if (reAnswers.size >= 0) {
                callback.onReAnswersMoreLoaded(reAnswers)
            }

            if (it.documents.size > 0) {
                val lastVisible: DocumentSnapshot = it.documents.get(it.size() - 1)
                reAnswersLoadMoreQuery = reAnswersLoadMoreQuery!!.startAfter(lastVisible).limit(LIMIT_PAGE)
            }
        }?.addOnFailureListener {
            callback.onFailedToLoadMoreReAnswers()
        }
    }

    override fun loadSentAnswers(callback: LettersDataSource.LoadAnswersCallback) {
        loadSentAnswersQuery = answerCollection.whereEqualTo(ANSWER_USER_ID, FirebaseAuth.getInstance().currentUser!!.uid).limit(Constants.LIMIT_PAGE)
        loadSentAnswersQuery.orderBy(TIME, Query.Direction.DESCENDING).get().addOnSuccessListener {

            val answers: ArrayList<Answer> = ArrayList()
            for (doc in it.documents) {
                val answer = doc.toObject(Answer::class.java)
                answer?.apply {
                    answers.add(this)
                }
            }
            if (answers.size >= 0) {
                callback.onAnswersLoaded(answers)
            }

            if (it.documents.size > 0) {
                val lastVisible: DocumentSnapshot = it.documents.get(it.size() - 1)
                loadMoreSentAnswersQuery = loadSentAnswersQuery.startAfter(lastVisible).limit(LIMIT_PAGE)
            }
        }.addOnFailureListener {
            callback.onFailedToLoadAnswers()
        }
    }

    override fun loadMoreSentAnswers(callback: LettersDataSource.LoadMoreAnswersCallback) {
        loadMoreSentAnswersQuery?.orderBy(TIME, Query.Direction.DESCENDING)?.get()?.addOnSuccessListener {

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
                loadMoreSentAnswersQuery = loadMoreSentAnswersQuery!!.startAfter(lastVisible).limit(LIMIT_PAGE)
            }
        }?.addOnFailureListener {
            callback.onFailedToLoadMoreAnswers()
        }
    }
}