package com.nusv.lite.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class DocsJson(
    val version: Int,
    val updatedAt: String,
    val docs: List<DocJson>
)

@Serializable
data class DocJson(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: String
)

@Entity(tableName = "docs")
data class Doc(
    @PrimaryKey val id: String,
    val title: String,
    val content: String,
    val createdAt: Long
)
