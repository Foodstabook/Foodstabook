package com.example.foodstabook

class UserClass (newUsername: String="", newPassword: String ="", newFirst: String = "", newLast: String =""){

    private var userUsername: String = newUsername
        get() = field
        set(value) {
            field = value
        }
    private var userPassword: String = newPassword
        get() = field
        set(value) {
            field = value
        }
    private var userFirstName: String = newFirst
        get() = field
        set(value) {
            field = value
        }
    private var userLastName: String = newLast
        get() = field
        set(value) {
            field = value
        }

    fun userlastname(): String{
        return userFirstName + userLastName
    }

}