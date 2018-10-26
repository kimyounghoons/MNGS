package com.mngs.kimyounghoon.mngs.data

import com.google.firebase.database.ServerValue

data class Letter(val letterId: String = "",val userId: String="",val hasAnswer: Boolean=false,
                  val title: String = "", val content: String = "",val time:Map<String,String> = ServerValue.TIMESTAMP)