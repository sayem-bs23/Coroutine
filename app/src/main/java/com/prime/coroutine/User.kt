package com.prime.coroutine

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("email")
    val email:String,

    @SerializedName("username")
    val name:String,

    @SerializedName("image")
    val image:String){

}