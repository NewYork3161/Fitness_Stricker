package com.example.fitness_striker

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.MessageDigest
import java.security.SecureRandom

class LoginDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "fitness_striker.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_USERS = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_FIRSTNAME = "firstname"
        private const val COLUMN_LASTNAME = "lastname"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "passwordHash"
        private const val COLUMN_SALT = "salt"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_FIRSTNAME TEXT,
                $COLUMN_LASTNAME TEXT,
                $COLUMN_EMAIL TEXT UNIQUE,
                $COLUMN_PASSWORD TEXT,
                $COLUMN_SALT TEXT
            )
        """.trimIndent()

        db?.execSQL(createUsersTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // =============================
    // INSERT NEW USER
    // =============================
    fun insertUser(firstname: String, lastname: String, email: String, password: String): Boolean {
        val db = writableDatabase
        val salt = generateSalt()
        val hashedPassword = sha256(salt + password)

        val values = ContentValues().apply {
            put(COLUMN_FIRSTNAME, firstname)
            put(COLUMN_LASTNAME, lastname)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, hashedPassword)
            put(COLUMN_SALT, salt)
        }

        return db.insert(TABLE_USERS, null, values) != -1L
    }

    // =============================
    // CHECK USER LOGIN
    // =============================
    fun validateUser(email: String, password: String): Boolean {
        val db = readableDatabase

        val cursor: Cursor = db.rawQuery(
            "SELECT $COLUMN_PASSWORD, $COLUMN_SALT FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ?",
            arrayOf(email)
        )

        if (!cursor.moveToFirst()) {
            cursor.close()
            return false
        }

        val storedHash = cursor.getString(0)
        val salt = cursor.getString(1)
        cursor.close()

        val inputHash = sha256(salt + password)

        return storedHash == inputHash
    }

    // =============================
    // CHECK IF NEW PASSWORD == OLD
    // =============================
    fun isSamePassword(email: String, newPassword: String): Boolean {
        val db = readableDatabase

        val cursor: Cursor = db.rawQuery(
            "SELECT $COLUMN_PASSWORD, $COLUMN_SALT FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ?",
            arrayOf(email)
        )

        if (!cursor.moveToFirst()) {
            cursor.close()
            return false  // user not found → treat as not same
        }

        val storedHash = cursor.getString(0)
        val salt = cursor.getString(1)
        cursor.close()

        val newHash = sha256(salt + newPassword)
        return storedHash == newHash
    }

    // =============================
    // UPDATE USER PASSWORD
    // =============================
    fun updatePassword(email: String, newPassword: String): Boolean {
        val db = writableDatabase

        val cursor: Cursor = db.rawQuery(
            "SELECT $COLUMN_SALT FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ?",
            arrayOf(email)
        )

        if (!cursor.moveToFirst()) {
            cursor.close()
            return false // no such user
        }

        val salt = cursor.getString(0)
        cursor.close()

        val newHash = sha256(salt + newPassword)

        val values = ContentValues().apply {
            put(COLUMN_PASSWORD, newHash)
        }

        return db.update(TABLE_USERS, values, "$COLUMN_EMAIL = ?", arrayOf(email)) > 0
    }

    // =============================
    // SALT + SHA-256 HELPERS
    // =============================
    private fun generateSalt(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
