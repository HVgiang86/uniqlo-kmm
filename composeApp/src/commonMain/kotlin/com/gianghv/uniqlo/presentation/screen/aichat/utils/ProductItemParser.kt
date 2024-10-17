package com.gianghv.uniqlo.presentation.screen.aichat.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle


fun String.replaceProduct(list: List<Pair<Long, String>>): AnnotatedString = buildAnnotatedString {
    var lastIndex = 0
    val regex = """\{\{(\d+)\}\}\[([^\]]+)\]""".toRegex()
    regex.findAll(this@replaceProduct).forEach { matchResult ->
        val (id, name) = matchResult.destructured
        append(this@replaceProduct.substring(lastIndex, matchResult.range.first))
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(name)
        }
        lastIndex = matchResult.range.last + 1
    }
    append(this@replaceProduct.substring(lastIndex))
}

fun String.trimMessage(): String {
    return this.trim().removePrefix(" ").removeSuffix(" ").removeSuffix("\n")
}

fun String.parseProductItems(): List<Pair<Long, String>> {
    val regex = """\{\{(\d+)\}\}\[([^\]]+)\]""".toRegex()
    return regex.findAll(this).map { matchResult ->
        val (id, name) = matchResult.destructured
        id.toLong() to name
    }.toList()
}

