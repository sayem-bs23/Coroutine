package com.prime.coroutine

object ExampleSingleton{

    val singletonUser: User by lazy{
        User("abc", "sayem", "image.png")

    }
}