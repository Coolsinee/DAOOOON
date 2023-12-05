package com.example.daoooon

class Student(
    private var id: Int,
    private var name: String,
    private var birthday: String,
    private var groupId: String
) {

    override fun toString(): String {
        return "$id $name $birthday $groupId"
    }
}
