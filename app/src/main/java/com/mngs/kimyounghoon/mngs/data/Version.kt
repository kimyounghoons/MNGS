package com.mngs.kimyounghoon.mngs.data

import com.mngs.kimyounghoon.mngs.data.Constants.Companion.EMPTY

data class Version(val currentVersion: String = EMPTY, val minVersion: String = EMPTY)