package com.mngs.kimyounghoon.mngs.data

import com.mngs.kimyounghoon.mngs.data.Constants.Companion.EMPTY

class User(id : String = EMPTY, val firebaseToken: String= EMPTY) : AbstractId(id)