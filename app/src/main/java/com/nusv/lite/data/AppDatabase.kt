package com.nusv.lite.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import com.nusv.lite.model.Category
import com.nusv.lite.model.Doc
import com.nusv.lite.model.Item
import kotlinx.coroutines.flow.Flow

@Database(entities = [Item::class, Category::class, Doc::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun categoryDao(): CategoryDao
    abstract fun docDao(): DocDao
}

@Dao
interface DocDao {
    @Query("SELECT * FROM docs ORDER BY createdAt DESC")
    fun getAll(): Flow<List<Doc>>

    @Query("SELECT * FROM docs WHERE id = :id")
    fun getById(id: String): Flow<Doc?>

    @Query("SELECT COUNT(*) FROM docs")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(docs: List<Doc>)

    @Query("DELETE FROM docs")
    suspend fun deleteAll()
}

@Dao
interface ItemDao {
    @Query("SELECT * FROM items ORDER BY createdAt DESC")
    fun getAll(): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE isFeatured = 1 ORDER BY createdAt DESC")
    fun getFeatured(): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE categoryId = :categoryId ORDER BY createdAt DESC")
    fun getByCategory(categoryId: String): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE id = :id")
    fun getById(id: String): Flow<Item?>

    @Query(
        "SELECT * FROM items WHERE title LIKE '%' || :query || '%' " +
        "OR description LIKE '%' || :query || '%' ORDER BY createdAt DESC"
    )
    fun search(query: String): Flow<List<Item>>

    @Query("SELECT COUNT(*) FROM items")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Item>)

    @Query("DELETE FROM items")
    suspend fun deleteAll()
}

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY sortOrder")
    fun getAll(): Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE id = :id")
    fun getById(id: String): Flow<Category?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<Category>)

    @Query("DELETE FROM categories")
    suspend fun deleteAll()
}
