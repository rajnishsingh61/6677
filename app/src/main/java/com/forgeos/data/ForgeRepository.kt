package com.forgeos.data

import android.content.Context
import com.forgeos.xp.XpManager
import kotlinx.coroutines.flow.Flow

class ForgeRepository(context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val profileDao = db.profileDao()
    private val sessionDao = db.sessionDao()
    private val badgeDao = db.badgeDao()
    private val xpManager = XpManager()

    fun observeProfile(): Flow<PlayerProfile?> = profileDao.getProfile()
    fun observeSessions(): Flow<List<GameSession>> = sessionDao.getAllSessions()
    fun observeBadges(): Flow<List<Badge>> = badgeDao.getAllBadges()

    suspend fun getOrCreateProfile(): PlayerProfile {
        val existing = profileDao.getProfileOnce()
        if (existing != null) return existing
        val fresh = PlayerProfile()
        profileDao.upsert(fresh)
        return fresh
    }

    suspend fun saveSessionAndAwardXp(session: GameSession): PlayerProfile {
        sessionDao.insert(session)
        val profile = getOrCreateProfile()
        val updated = xpManager.addXp(profile, session.xpEarned)
            .copy(
                totalSessions = profile.totalSessions + 1,
                totalMinutesPlayed = profile.totalMinutesPlayed + session.durationMinutes
            )
        profileDao.upsert(updated)

        // Badge check
        val currentBadges = badgeDao.getAllBadges() // this is Flow, so we need a one-shot in real code
        // For skeleton we skip full badge flow here

        return updated
    }

    suspend fun updateProfile(profile: PlayerProfile) {
        profileDao.upsert(profile)
    }
}
