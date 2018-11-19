package com.mngs.kimyounghoon.mngs.data

import com.google.protobuf.Empty
import com.mngs.kimyounghoon.mngs.data.Constants.Companion.EMPTY

data class User(val userId: String = EMPTY, val firebaseToken: String= EMPTY)