package com.forgeos.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM player_profile WHERE id = 1 LIMIT 1")
    fun getProfile(): Flow<PlayerProfile?>

    @Query("SELECT * FROM player_profile WHERE id = 1 LIMIT 1")
    suspend fun getProfileOnce(): PlayerProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(profile: PlayerProfile)

    @Update
    suspend fun update(profile: PlayerProfile)
}

@Dao
interface SessionDao {
    @Insert
    suspend fun insert(session: GameSession): Long

    @Query("SELECT * FROM game_session ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<GameSession>>

    @Query("SELECT * FROM game_session ORDER BY startTime DESC LIMIT :limit")
    suspend fun getRecentSessions(limit: Int = 20): List<GameSession>

    @Query("SELECT SUM(durationMinutes) FROM game_session WHERE completed = 1")
    suspend fun getTotalMinutes(): Long?

    @Query("SELECT COUNT(*) FROM game_session WHERE completed = 1")
    suspend fun getTotalSessions(): Int
}

@Dao
interface BadgeDao {
    @Query("SELECT * FROM badge")
    fun getAllBadges(): Flow<List<Badge>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(badges: List<Badge>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(badge: Badge)
}
