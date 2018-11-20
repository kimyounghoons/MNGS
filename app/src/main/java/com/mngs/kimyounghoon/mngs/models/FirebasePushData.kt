package com.mngs.kimyounghoon.mngs.models

data class FirebasePushData(val to : String, val notification: HashMap<String,String>, val data : HashMap<String,String>)