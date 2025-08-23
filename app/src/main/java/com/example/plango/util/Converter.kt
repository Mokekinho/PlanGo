package com.example.plango.util

import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

fun Money(
    value : Double
):String {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())

    return currencyFormat.format(value)
}

fun  Date(
    data: LocalDate
): String{
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
        .withLocale(Locale.getDefault())
    return data.format(formatter)
}