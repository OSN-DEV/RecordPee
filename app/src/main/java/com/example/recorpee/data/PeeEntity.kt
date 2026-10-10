package com.example.recorpee.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import java.time.Instant
import java.util.Date

@Entity(tableName ="pee")
data class PeeEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var date: Long,
    var amount: Int
)
