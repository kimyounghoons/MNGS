package com.mngs.kimyounghoon.mngs.data

import com.mngs.kimyounghoon.mngs.utils.TimeHelper

data class Letter(val letterId: String = "", val userId: String = "", var hasAnswer: Boolean = false,
                  val title: String = "", val content: String = "", val time: Long = TimeHelper.getCurrentTime())