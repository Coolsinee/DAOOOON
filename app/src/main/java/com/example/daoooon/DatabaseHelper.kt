package com.example.daoooon

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, SCHEMA) {

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL("CREATE TABLE IF NOT EXISTS students(id INTEGER PRIMARY KEY, name VARCHAR(30), birthday DATE, groupId VARCHAR(10))")
        db.execSQL("INSERT OR IGNORE INTO students(name, birthday, groupId) VALUES ('Эмиль', '11.06.2005', '09-253 (2)')")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS students")
        onCreate(db)
    }

    fun getStudents(): List<Student> {

        val students = ArrayList<Student>()
        val cursor = readableDatabase.rawQuery("select * from students", null)

        while (cursor.moveToNext()) {

            val id = cursor.getInt(0)
            val name = cursor.getString(1)
            val birthday = cursor.getString(2)
            val groupId = cursor.getString(3)

            students.add(Student(id, name, birthday, groupId))
        }

        cursor.close()
        return students
    }

    fun insert(name: String, birthday: String, groupId: String) {
        readableDatabase.execSQL("INSERT OR IGNORE INTO students(name, birthday, groupId) VALUES ('$name', '$birthday', '$groupId')")
    }

    fun clear() {
        readableDatabase.execSQL("DELETE FROM students")
    }

    companion object {
        const val DATABASE_NAME = "Students.db"
        private const val SCHEMA = 1
    }
}