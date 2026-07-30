package com.nusv.lite.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class ContentJson(
    val version: Int,
    val updatedAt: String,
    val categories: List<CategoryJson>,
    val items: List<ItemJson>
)

@Serializable
data class CategoryJson(
    val id: String,
    val name: String,
    val color: String
)

@Serializable
data class ItemJson(
    val id: String,
    val title: String,
    val description: String,
    val url: String,
    val categoryId: String,
    val tags: List<String> = emptyList(),
    val isFeatured: Boolean = false,
    val createdAt: String
)

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey val id: String,
    val name: String,
    val color: Long,
    val sortOrder: Int = 0
)

@Entity(tableName = "items")
data class Item(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val url: String,
    val categoryId: String,
    val tags: String,
    val isFeatured: Boolean,
    val createdAt: Long
)
