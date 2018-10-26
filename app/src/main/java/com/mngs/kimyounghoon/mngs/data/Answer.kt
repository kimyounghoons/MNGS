package com.mngs.kimyounghoon.mngs.data

import com.google.firebase.database.ServerValue

data class Answer(val answerId: String="", val letterId: String="" ,val originUserId: String="", val answerUserId: String="", val title: String="", val content: String="", val time: Map<String,String> = ServerValue.TIMESTAMP)