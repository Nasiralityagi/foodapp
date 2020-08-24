package com.android.tlb.home.data.model

data class Response(
    val `data`: List<Data>,
    val message: String,
    val status: Int
)