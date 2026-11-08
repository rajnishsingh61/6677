package com.forgeos.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [PlayerProfile::class, GameSession::class, Badge::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun sessionDao(): SessionDao
    abstract fun badgeDao(): BadgeDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "forgeos.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

class Converters {
    @androidx.room.TypeConverter
    fun fromRankTier(value: RankTier): String = value.name

    @androidx.room.TypeConverter
    fun toRankTier(value: String): RankTier = RankTier.valueOf(value)

    @androidx.room.TypeConverter
    fun fromSessionMode(value: SessionMode): String = value.name

    @androidx.room.TypeConverter
    fun toSessionMode(value: String): SessionMode = SessionMode.valueOf(value)
}
