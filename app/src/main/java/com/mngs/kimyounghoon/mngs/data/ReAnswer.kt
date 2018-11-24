package com.mngs.kimyounghoon.mngs.data

import com.mngs.kimyounghoon.mngs.data.Constants.Companion.EMPTY
import com.mngs.kimyounghoon.mngs.utils.TimeHelper

class ReAnswer(id: String = EMPTY, val answerId: String = EMPTY, val letterId: String = EMPTY, val originUserId: String = EMPTY, val answerUserId: String = EMPTY, val content: String = EMPTY, val time: Long = TimeHelper.getCurrentTime()) : AbstractId(id)