package com.example.crazybottle2

interface textStorageInterface {
    fun write(listStrings : List<String>)
    fun read() : List<String>
}