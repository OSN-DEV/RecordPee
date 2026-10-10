package com.example.recorpee.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recorpee.data.PeeDao
import com.example.recorpee.data.PeeEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PeeViewModel @Inject constructor(val peeDao: PeeDao): ViewModel() {
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    fun changeDate(newDate: LocalDate) {
        _selectedDate.value = newDate
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val peeList: StateFlow<List<PeeEntity>> = _selectedDate
        .flatMapLatest { date ->
            val baseDateTime = date.atTime(6, 0)
            val from = baseDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val to = baseDateTime.plusDays(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            peeDao.query(from, to)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(3000),
            initialValue = emptyList()
        )


    fun getPeeList(baseDate: LocalDate, zoneId: ZoneId =  ZoneId.systemDefault()): StateFlow<List<PeeEntity>> {
        val baseDateTime = baseDate.atTime(6,0)
        val from = baseDateTime.atZone(zoneId).toInstant().toEpochMilli()
        val to = baseDateTime.plusDays(1).atZone(zoneId).toInstant().toEpochMilli()
        return peeDao.query(from, to)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(3000),
                initialValue=  emptyList<PeeEntity>()
            )
    }

    suspend fun insert(peeData: PeeEntity) {
        peeDao.insert(peeData)
    }

    suspend fun update(peeData: PeeEntity) {
        peeDao.update(peeData)
    }

    suspend fun delete(peeData: PeeEntity) {
        peeDao.delete(peeData)
    }
}

