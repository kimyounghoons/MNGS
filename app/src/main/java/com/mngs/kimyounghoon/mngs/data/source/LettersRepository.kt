package com.mngs.kimyounghoon.mngs.data.source

import com.mngs.kimyounghoon.mngs.data.Letter

class LettersRepository(val lettersDataSource: LettersDataSource) : LettersDataSource {

    override fun getId() : String {
        return lettersDataSource.getId()
    }
    override fun loadLetters(callback: LettersDataSource.LoadLettersCallback) {
        lettersDataSource.loadLetters(callback)
    }

    override fun loadMoreLetters(callback: LettersDataSource.LoadMoreLettersCallback) {
        lettersDataSource.loadMoreLetters(callback)
    }

    override fun loadInBox(callback: LettersDataSource.LoadInBoxCallback) {
        lettersDataSource.loadInBox(callback)
    }

    override fun loadMoreInBox(callback: LettersDataSource.LoadMoreInBoxCallback) {
        lettersDataSource.loadMoreInBox(callback)
    }

    override fun getLetter(letterId: String, callBack: LettersDataSource.GetLetterCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendLetter(letter: Letter, callback: LettersDataSource.SendLetterCallback) {
        lettersDataSource.sendLetter(letter, callback)
    }

    companion object {

        private var INSTANCE: LettersRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.

         * @param tasksRemoteDataSource the backend data source
         * *
         * @param tasksLocalDataSource  the device storage data source
         * *
         * @return the [TasksRepository] instance
         */
        @JvmStatic
        fun getInstance(lettersDataSource: LettersDataSource) =
                INSTANCE ?: synchronized(LettersRepository::class.java) {
                    INSTANCE ?: LettersRepository(lettersDataSource)
                            .also { INSTANCE = it }
                }


        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }

}