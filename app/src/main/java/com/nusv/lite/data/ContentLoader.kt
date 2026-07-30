package com.nusv.lite.data

import android.content.Context
import android.content.SharedPreferences
import com.nusv.lite.model.Category
import com.nusv.lite.model.ContentJson
import com.nusv.lite.model.Doc
import com.nusv.lite.model.DocsJson
import com.nusv.lite.model.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.time.Instant

class ContentLoader(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }
    private val prefs: SharedPreferences =
        context.getSharedPreferences("sync_meta", Context.MODE_PRIVATE)

    suspend fun loadIfNeeded(db: AppDatabase) = withContext(Dispatchers.IO) {
        if (db.itemDao().count() > 0 && db.docDao().count() > 0) return@withContext

        loadContent(db)
        loadDocs(db)
    }

    private suspend fun loadContent(db: AppDatabase) {
        if (db.itemDao().count() > 0) return

        val jsonString = context.assets.open("content.json")
            .bufferedReader()
            .use { it.readText() }

        val data = json.decodeFromString<ContentJson>(jsonString)

        db.categoryDao().insertAll(
            data.categories.mapIndexed { i, c ->
                Category(
                    id = c.id,
                    name = c.name,
                    color = c.color.removePrefix("#").toLong(16),
                    sortOrder = i
                )
            }
        )

        db.itemDao().insertAll(
            data.items.map { item ->
                Item(
                    id = item.id,
                    title = item.title,
                    description = item.description,
                    url = item.url,
                    categoryId = item.categoryId,
                    tags = item.tags.joinToString(","),
                    isFeatured = item.isFeatured,
                    createdAt = try {
                        Instant.parse(item.createdAt).toEpochMilli()
                    } catch (_: Exception) {
                        System.currentTimeMillis()
                    }
                )
            }
        )

        prefs.edit().putInt("content_version", data.version).apply()
    }

    private suspend fun loadDocs(db: AppDatabase) {
        if (db.docDao().count() > 0) return

        try {
            val jsonString = context.assets.open("docs.json")
                .bufferedReader()
                .use { it.readText() }

            val data = json.decodeFromString<DocsJson>(jsonString)

            db.docDao().insertAll(
                data.docs.map { doc ->
                    Doc(
                        id = doc.id,
                        title = doc.title,
                        content = doc.content,
                        createdAt = try {
                            Instant.parse(doc.createdAt).toEpochMilli()
                        } catch (_: Exception) {
                            System.currentTimeMillis()
                        }
                    )
                }
            )

            prefs.edit().putInt("docs_version", data.version).apply()
        } catch (_: Exception) {
        }
    }
}
