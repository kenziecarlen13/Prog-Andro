package com.example.watertracker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "WaterTracker.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_COMPLIANCE = "weekly_compliance"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_DAYS = "days_compliant"
        private const val COLUMN_URI = "profile_image_uri"
        private const val COLUMN_IS_ME = "is_me"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE " + TABLE_COMPLIANCE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_DAYS + " INTEGER,"
                + COLUMN_URI + " TEXT,"
                + COLUMN_IS_ME + " INTEGER" + ")")
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLIANCE)
        onCreate(db)
    }

    fun insertDummyTiers() {
        val db = this.writableDatabase
        
        // Cek jika data sudah ada agar tidak duplikat
        val countQuery = "SELECT count(*) FROM $TABLE_COMPLIANCE"
        val cursor = db.rawQuery(countQuery, null)
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()

        if (count == 0) {
            val dummies = listOf(
                TierUser(0, "Kenzie", 7, "content://media/external/images/media/invalid", true),
                TierUser(0, "Budi", 6, "", false),
                TierUser(0, "Andi", 5, null, false),
                TierUser(0, "Siti", 2, "android.resource://com.example.watertracker/drawable/avatar_placeholder_circle", false),
                TierUser(0, "Dewi", 0, "invalid_uri_here", false)
            )

            for (user in dummies) {
                val values = ContentValues()
                values.put(COLUMN_NAME, user.name)
                values.put(COLUMN_DAYS, user.daysCompliant)
                values.put(COLUMN_URI, user.profileImageUri)
                values.put(COLUMN_IS_ME, if (user.isMe) 1 else 0)
                db.insert(TABLE_COMPLIANCE, null, values)
            }
        }
        db.close()
    }

    fun getComplianceData(isUserVisible: Boolean): List<TierUser> {
        val tierList = mutableListOf<TierUser>()
        val db = this.readableDatabase
        val selectQuery = if (isUserVisible) {
            "SELECT * FROM $TABLE_COMPLIANCE ORDER BY $COLUMN_DAYS DESC"
        } else {
            "SELECT * FROM $TABLE_COMPLIANCE WHERE $COLUMN_IS_ME = 0 ORDER BY $COLUMN_DAYS DESC"
        }

        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val user = TierUser(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    daysCompliant = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DAYS)),
                    profileImageUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URI)),
                    isMe = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_ME)) == 1
                )
                tierList.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return tierList
    }

    // Task 4 (Modul 7): Dynamic INSERT operation
    fun insertTierUser(name: String, days: Int, uri: String?, isMe: Boolean) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_DAYS, days)
            put(COLUMN_URI, uri)
            put(COLUMN_IS_ME, if (isMe) 1 else 0)
        }
        db.insert(TABLE_COMPLIANCE, null, values)
        db.close()
    }

    // Task 4 (Modul 7): Dynamic DELETE operation
    fun deleteTierUser(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_COMPLIANCE, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }
}

