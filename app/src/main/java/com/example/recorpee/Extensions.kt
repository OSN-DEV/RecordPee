package com.example.recorpee

import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.ZonedDateTime

fun String.toUnixTime(formatter: DateTimeFormatter): Int {
    val localDate = LocalDate.parse(this, formatter)
    val zonedDateTime = localDate.atStartOfDay(ZoneOffset.ofHours(+9))
    return zonedDateTime.toEpochSecond().toInt()
}

/**
 * 現在日時に指定された日数を足したUnixタイム（ミリ秒）を取得する
 *
 * @param days 加算する日数（マイナスを指定すれば過去の計算も可能）
 */
fun ZonedDateTime.plusDaysToEpochMillis(days: Long): Long {
    return this.plusDays(days).toInstant().toEpochMilli()
}

/**
 * 現在日時に指定された日数を足したUnixタイム（秒）を取得する
 */
fun ZonedDateTime.plusDaysToEpochSeconds(days: Long): Long {
    return this.plusDays(days).toEpochSecond()
}