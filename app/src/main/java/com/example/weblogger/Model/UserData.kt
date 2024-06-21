package com.example.weblogger.Model

data class UserData(
    val name:String = "",
    val email:String = "",
    val profileUrl:String = ""
) {
    constructor():this ("","","")
}
