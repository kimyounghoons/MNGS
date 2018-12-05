package com.mngs.kimyounghoon.mngs.data.source

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.GsonBuilder
import com.mngs.kimyounghoon.mngs.BuildConfig
import com.mngs.kimyounghoon.mngs.MngsApp.Companion.context
import com.mngs.kimyounghoon.mngs.ModelManager
import com.mngs.kimyounghoon.mngs.data.*
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.ANSWER
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.ANSWER_USER_ID
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.CONTENT_AVAILABLE
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.FIREBASE_TOKEN
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.HAS_ANSWER
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.HIGH
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.ID
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.JSON_ANSWER
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.JSON_LETTER
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.LETTERS
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.LETTER_ID
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.LIMIT_PAGE
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.PRIORITY
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.REANSWER
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.TIME
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.USERS
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.VERSION
import com.mngs.kimyounghoon.mngs.firebases.FirebasePushDAO
import com.mngs.kimyounghoon.mngs.models.FirebasePushData

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

    override fun sendRefreshToken(token: String) {
        FirebaseFirestore.getInstance().collection(BuildConfig.BUILD_TYPE).document(USERS).collection(USERS).document(FirebaseAuth.getInstance().uid!!).update(FIREBASE_TOKEN, token).addOnSuccessListener {
            Log.d("kyh!!!", token)
        }.addOnFailureListener {
            Log.d("kyh!!!", "fail")
        }
    }

    override fun checkVersion(callback: LettersDataSource.VersionCallback) {
        FirebaseFirestore.getInstance().collection(BuildConfig.BUILD_TYPE).document(VERSION).get().addOnSuccessListener {
            val version = it.toObject(Version::class.java)
            version?.apply {
                callback.onSuccess(version)
            }
        }.addOnFailureListener {
            callback.onFailedToGetVersion()
        }
    }

    override fun getUser(userId: String, callback: LettersDataSource.UserCallback) {
        FirebaseFirestore.getInstance().collection(BuildConfig.BUILD_TYPE).document(USERS).collection(USERS).whereEqualTo(ID, userId).get().addOnSuccessListener {
            if (it.documents.size == 0) {
                callback.onFailToGetUser()
            } else {
                for (doc in it.documents) {
                    val user = doc.toObject(User::class.java)
                    user?.apply {
                        callback.onSuccess(user)
                    }
                }
            }
        }.addOnFailureListener {
            callback.onFailToGetUser()
        }
    }

    override fun signup(signupCallBack: LettersDataSource.SignupCallback) {
        val uid = FirebaseAuth.getInstance().uid!!
        FirebaseFirestore.getInstance().collection(BuildConfig.BUILD_TYPE).document(USERS).collection(USERS).document(uid).set(User(uid, FirebaseInstanceId.getInstance().token!!)).addOnSuccessListener {
            signupCallBack.onSuccess()
        }.addOnFailureListener {
            signupCallBack.onFail()
        }
    }

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
            getUser(answer.originUserId, object : LettersDataSource.UserCallback {
                override fun onSuccess(user: User) {
                    postFirebasePush(user.firebaseToken, answer)
                }

                override fun onFailToGetUser() {

                }

            })
        }.addOnFailureListener {
            callBack.onFailedToSendAnswer()
        }
    }

    private fun postFirebasePush(firebaseToken: String, answer: Answer) {
        val notification = HashMap<String, String>()
        notification.put("title", "답장이 도착했습니다.")
        notification.put("body", answer.content)

        val data = HashMap<String, Any>()
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonLetter = gson.toJson(ModelManager.letters[answer.letterId])
        val jsonAnswer = gson.toJson(answer)

        data[JSON_LETTER] = jsonLetter
        data[JSON_ANSWER] = jsonAnswer
        data[CONTENT_AVAILABLE] = true
        data[PRIORITY] = HIGH

        FirebasePushDAO(context).postPushAnswer(FirebasePushData(firebaseToken, notification, data))
    }

    private fun postFirebasePush(firebaseToken: String, reAnswer: ReAnswer) {
        val notification = HashMap<String, String>()
        notification.put("title", "답장이 도착했습니다.")
        notification.put("body", reAnswer.content)

        val data = HashMap<String, Any>()

        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonAnswer = gson.toJson(ModelManager.answers[reAnswer.answerId])

        data[JSON_ANSWER] = jsonAnswer
        data[CONTENT_AVAILABLE] = true
        data[PRIORITY] = HIGH

        FirebasePushDAO(context).postPushAnswer(FirebasePushData(firebaseToken, notification, data))
    }

    private fun updateHasAnswer(letterId: String, callBack: LettersDataSource.SendAnswerCallback) {
        lettersCollection.document(letterId).update(HAS_ANSWER, true).addOnSuccessListener {
            callBack.onAnswerSended()
        }.addOnFailureListener {
            callBack.onFailedToSendAnswer()
        }
    }

    override fun getLetter(letterId: String, callBack: LettersDataSource.GetLetterCallback) {
        lettersCollection.whereEqualTo(ID, letterId).get().addOnSuccessListener {

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
            val userId: String = if (reAnswer.originUserId == FirebaseAuth.getInstance().uid) {
                reAnswer.answerUserId
            } else {
                reAnswer.originUserId
            }
            getUser(userId, object : LettersDataSource.UserCallback {

                override fun onSuccess(user: User) {
                    postFirebasePush(user.firebaseToken, reAnswer)
                }

                override fun onFailToGetUser() {

                }

            })
        }.addOnFailureListener {
            callback.onFailedToSendLetter()
        }
    }

    override fun getReAnswerId(): String {
        reAnswerDocumentReference = reAnswerCollection.document()
        return reAnswerDocumentReference.id
    }

    override fun loadLetters(callback: LettersDataSource.LoadItemsCallback) {
        loadLettersCollection.get().addOnSuccessListener {

            val letters: ArrayList<Letter> = ArrayList()
            val currentUid = FirebaseAuth.getInstance().currentUser!!.uid
            for (doc in it.documents) {
                val letter = doc.toObject(Letter::class.java)
                letter?.apply {
                    if (letter.userId != currentUid) {
                        letters.add(this)
                        ModelManager.letters[letter.id] = this
                    }
                }
            }
            if (letters.size >= 0) {
                callback.onLoaded(letters)
            }
            if (letters.size > 0) {
                val lastVisible: DocumentSnapshot = it.documents[it.size() - 1]
                loadMoreQuery = loadLettersCollection.startAfter(lastVisible).limit(LIMIT_PAGE)
            }
        }.addOnFailureListener {
            callback.onFailedToLoadItems()
        }
    }

    override fun loadMoreLetters(callback: LettersDataSource.LoadMoreItemsCallback) {
        loadMoreQuery?.get()?.addOnSuccessListener {

            val letters: ArrayList<Letter> = ArrayList()
            for (doc in it.documents) {
                val letter = doc.toObject(Letter::class.java)
                letter?.apply {
                    letters.add(this)
                    ModelManager.letters[letter.id] = this
                }
            }
            if (letters.size >= 0) {
                callback.onMoreLoaded(letters)
            }

            if (it.documents.size > 0) {
                val lastVisible: DocumentSnapshot = it.documents.get(it.size() - 1)
                loadMoreQuery = loadLettersCollection.startAfter(lastVisible).limit(LIMIT_PAGE)
            }
        }?.addOnFailureListener {
            callback.onFailedToLoadMoreItems()
        }
    }

    override fun loadInBox(callback: LettersDataSource.LoadItemsCallback) {
        inboxCollection = FirebaseFirestore.getInstance().collection(BuildConfig.BUILD_TYPE).document("letters").collection("letters").whereEqualTo("userId", FirebaseAuth.getInstance().uid)
        inboxCollection!!.orderBy(TIME, Query.Direction.DESCENDING).limit(10)
                .get().addOnSuccessListener {

                    val letters: ArrayList<Letter> = ArrayList()
                    for (doc in it.documents) {
                        val letter = doc.toObject(Letter::class.java)
                        letter?.apply {
                            letters.add(this)
                            ModelManager.letters[letter.id] = this
                        }
                    }
                    if (letters.size >= 0) {
                        callback.onLoaded(letters)
                    }
                    if (letters.size > 0) {
                        val lastVisible: DocumentSnapshot = it.documents[it.size() - 1]
                        loadMoreInboxQuery = inboxCollection!!.startAfter(lastVisible).limit(LIMIT_PAGE)
                    }
                }.addOnFailureListener {
                    callback.onFailedToLoadItems()
                }
    }

    override fun loadMoreInBox(callback: LettersDataSource.LoadMoreItemsCallback) {
        loadMoreInboxQuery?.get()?.addOnSuccessListener {

            val letters: ArrayList<Letter> = ArrayList()
            for (doc in it.documents) {
                val letter = doc.toObject(Letter::class.java)
                letter?.apply {
                    letters.add(this)
                    ModelManager.letters[letter.id] = this
                }
            }
            if (letters.size >= 0) {
                callback.onMoreLoaded(letters)
            }

            if (it.documents.size > 0) {
                val lastVisible: DocumentSnapshot = it.documents.get(it.size() - 1)
                loadMoreInboxQuery = inboxCollection!!.startAfter(lastVisible).limit(LIMIT_PAGE)
            }
        }?.addOnFailureListener {
            callback.onFailedToLoadMoreItems()
        }
    }

    override fun loadAnswers(letterId: String, callback: LettersDataSource.LoadItemsCallback) {
        loadAnswersQuery = answerCollection.whereEqualTo(LETTER_ID, letterId).limit(Constants.LIMIT_PAGE)
        loadAnswersQuery.orderBy(TIME, Query.Direction.DESCENDING).get().addOnSuccessListener {

            val answers: ArrayList<Answer> = ArrayList()
            for (doc in it.documents) {
                val answer = doc.toObject(Answer::class.java)
                answer?.apply {
                    answers.add(this)
                    ModelManager.answers[answer.id] = this
                }
            }
            if (answers.size >= 0) {
                callback.onLoaded(answers)
            }

            if (it.documents.size > 0) {
                val lastVisible: DocumentSnapshot = it.documents.get(it.size() - 1)
                answersLoadMoreQuery = loadAnswersQuery.startAfter(lastVisible).limit(LIMIT_PAGE)
            }
        }.addOnFailureListener {
            callback.onFailedToLoadItems()
        }
    }

    override fun loadMoreAnswers(callback: LettersDataSource.LoadMoreItemsCallback) {
        answersLoadMoreQuery?.get()?.addOnSuccessListener {

            val answers: ArrayList<Answer> = ArrayList()
            for (doc in it.documents) {
                val answer = doc.toObject(Answer::class.java)
                answer?.apply {
                    answers.add(this)
                    ModelManager.answers[answer.id] = this
                }
            }
            if (answers.size >= 0) {
                callback.onMoreLoaded(answers)
            }

            if (it.documents.size > 0) {
                val lastVisible: DocumentSnapshot = it.documents.get(it.size() - 1)
                answersLoadMoreQuery = answersLoadMoreQuery!!.startAfter(lastVisible).limit(LIMIT_PAGE)
            }
        }?.addOnFailureListener {
            callback.onFailedToLoadMoreItems()
        }
    }


    override fun loadReAnswers(letterId: String, callback: LettersDataSource.LoadItemsCallback) {
        loadReAnswersQuery = reAnswerCollection.whereEqualTo(LETTER_ID, letterId).orderBy(TIME, Query.Direction.DESCENDING).limit(Constants.LIMIT_PAGE)
        loadReAnswersQuery.get().addOnSuccessListener {

            val reAnswers: ArrayList<ReAnswer> = ArrayList()
            for (doc in it.documents) {
                val reAnswer = doc.toObject(ReAnswer::class.java)
                reAnswer?.apply {
                    reAnswers.add(this)
                    ModelManager.reAnswers[reAnswer.id] = this
                }
            }
            if (reAnswers.size >= 0) {
                callback.onLoaded(reAnswers)
            }

            if (it.documents.size > 0) {
                val lastVisible: DocumentSnapshot = it.documents.get(it.size() - 1)
                reAnswersLoadMoreQuery = loadReAnswersQuery.startAfter(lastVisible).limit(LIMIT_PAGE)
            }
        }.addOnFailureListener {
            callback.onFailedToLoadItems()
        }
    }

    override fun loadMoreReAnswers(callback: LettersDataSource.LoadMoreItemsCallback) {
        reAnswersLoadMoreQuery?.get()?.addOnSuccessListener {

            val reAnswers: ArrayList<ReAnswer> = ArrayList()
            for (doc in it.documents) {
                val reAnswer = doc.toObject(ReAnswer::class.java)
                reAnswer?.apply {
                    reAnswers.add(this)
                    ModelManager.reAnswers[reAnswer.id] = this
                }
            }
            if (reAnswers.size >= 0) {
                callback.onMoreLoaded(reAnswers)
            }

            if (it.documents.size > 0) {
                val lastVisible: DocumentSnapshot = it.documents.get(it.size() - 1)
                reAnswersLoadMoreQuery = reAnswersLoadMoreQuery!!.startAfter(lastVisible).limit(LIMIT_PAGE)
            }
        }?.addOnFailureListener {
            callback.onFailedToLoadMoreItems()
        }
    }

    override fun loadSentAnswers(callback: LettersDataSource.LoadItemsCallback) {
        loadSentAnswersQuery = answerCollection.whereEqualTo(ANSWER_USER_ID, FirebaseAuth.getInstance().currentUser!!.uid).limit(Constants.LIMIT_PAGE)
        loadSentAnswersQuery.orderBy(TIME, Query.Direction.DESCENDING).get().addOnSuccessListener {

            val answers: ArrayList<Answer> = ArrayList()
            for (doc in it.documents) {
                val answer = doc.toObject(Answer::class.java)
                answer?.apply {
                    answers.add(this)
                    ModelManager.answers[answer.id] = this
                }
            }
            if (answers.size >= 0) {
                callback.onLoaded(answers)
            }

            if (it.documents.size > 0) {
                val lastVisible: DocumentSnapshot = it.documents.get(it.size() - 1)
                loadMoreSentAnswersQuery = loadSentAnswersQuery.startAfter(lastVisible).limit(LIMIT_PAGE)
            }
        }.addOnFailureListener {
            callback.onFailedToLoadItems()
        }
    }

    override fun loadMoreSentAnswers(callback: LettersDataSource.LoadMoreItemsCallback) {
        loadMoreSentAnswersQuery?.get()?.addOnSuccessListener {

            val answers: ArrayList<Answer> = ArrayList()
            for (doc in it.documents) {
                val answer = doc.toObject(Answer::class.java)
                answer?.apply {
                    answers.add(this)
                    ModelManager.answers[answer.id] = this
                }
            }
            if (answers.size >= 0) {
                callback.onMoreLoaded(answers)
            }

            if (it.documents.size > 0) {
                val lastVisible: DocumentSnapshot = it.documents.get(it.size() - 1)
                loadMoreSentAnswersQuery = loadMoreSentAnswersQuery!!.startAfter(lastVisible).limit(LIMIT_PAGE)
            }
        }?.addOnFailureListener {
            callback.onFailedToLoadMoreItems()
        }
    }
}