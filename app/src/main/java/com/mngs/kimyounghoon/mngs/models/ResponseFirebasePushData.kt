package com.mngs.kimyounghoon.mngs.models

data class ResponseFirebasePushData(val multicast_id : Long,val success : Int, val failure : Int, val canonical_ids : Int,val result: HashMap<String,String>)