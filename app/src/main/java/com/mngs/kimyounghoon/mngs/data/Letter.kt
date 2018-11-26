package com.mngs.kimyounghoon.mngs.data

import com.mngs.kimyounghoon.mngs.data.Constants.Companion.EMPTY
import com.mngs.kimyounghoon.mngs.utils.TimeHelper

class Letter(id: String = EMPTY, val userId: String = EMPTY, var hasAnswer: Boolean = false, var needBadge: Boolean = false,
             val title: String = EMPTY, val content: String = EMPTY, val time: Long = TimeHelper.getCurrentTime()) : AbstractId(id)