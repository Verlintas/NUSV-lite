package com.nusv.lite.repository

import com.nusv.lite.data.AppDatabase
import com.nusv.lite.model.Category
import com.nusv.lite.model.Doc
import com.nusv.lite.model.Item
import kotlinx.coroutines.flow.Flow

class AppRepository(private val db: AppDatabase) {

    val allItems: Flow<List<Item>> = db.itemDao().getAll()
    val featuredItems: Flow<List<Item>> = db.itemDao().getFeatured()
    val allCategories: Flow<List<Category>> = db.categoryDao().getAll()
    val allDocs: Flow<List<Doc>> = db.docDao().getAll()

    fun getItemsByCategory(categoryId: String): Flow<List<Item>> =
        db.itemDao().getByCategory(categoryId)

    fun getItemById(id: String): Flow<Item?> =
        db.itemDao().getById(id)

    fun getDocById(id: String): Flow<Doc?> =
        db.docDao().getById(id)

    fun search(query: String): Flow<List<Item>> =
        db.itemDao().search(query)
}
