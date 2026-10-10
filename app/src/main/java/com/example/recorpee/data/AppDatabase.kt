package com.example.recorpee.data

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.recorpee.plusDaysToEpochMillis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

@Database(entities = [PeeEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun peeDao(): PeeDao
}

// ダミーデータの作成
fun AppDatabase.seed(scope: CoroutineScope) {
    scope.launch(Dispatchers.IO) {
        val dao = peeDao()
        if (dao.queryAll().first().isNotEmpty()) {
            return@launch
        }
        val now = ZonedDateTime.now()
        for(i in 1..20) {
            PeeEntity(
                date=now.plusDaysToEpochMillis(i.toLong()),
                amount = i
            ).also{
                dao.insert(it)
            }
        }
    }
}