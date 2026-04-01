package etf.ri.rma.newsfeedapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import etf.ri.rma.newsfeedapp.data.local.dao.NewsDao
import etf.ri.rma.newsfeedapp.data.local.dao.TagDao
import etf.ri.rma.newsfeedapp.data.local.entity.NewsEntity
import etf.ri.rma.newsfeedapp.data.local.entity.NewsTagCrossRef
import etf.ri.rma.newsfeedapp.data.local.entity.TagEntity

@Database(
    entities = [NewsEntity::class, TagEntity::class, NewsTagCrossRef::class],
    version = 6,
    exportSchema = false
)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun savedNewsDAO(): SavedNewsDAO
    abstract fun tagDao(): TagDao
    abstract fun newsDao(): NewsDao

    companion object {
        private const val DB_NAME = "news-db"
        @Volatile private var INSTANCE: NewsDatabase? = null

        fun getInstance(context: Context): NewsDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    NewsDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}