package com.dms.flip.domain.model.community

data class Paged<T>(
    val items: List<T>,
    val nextCursor: String?
)
