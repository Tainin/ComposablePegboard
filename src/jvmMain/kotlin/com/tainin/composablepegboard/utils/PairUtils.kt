package com.tainin.composablepegboard.utils

fun <T, R> Pair<T, T>.transform(transform: (T) -> R) = transform(first) to transform(second)
fun <A, B> Pair<A, B>.swap() = Pair(second, first)