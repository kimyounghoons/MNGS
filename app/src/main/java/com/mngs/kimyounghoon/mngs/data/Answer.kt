package com.mngs.kimyounghoon.mngs.data

import com.mngs.kimyounghoon.mngs.utils.TimeHelper

data class Answer(val answerId: String = "", val letterId: String = "", val originUserId: String = "", val answerUserId: String = "", val content: String = "", val time: Long = TimeHelper.getCurrentTime())