package com.forgeos.xp

import com.forgeos.data.Badge
import com.forgeos.data.PlayerProfile
import com.forgeos.data.RankProgress
import com.forgeos.data.RankTier

/**
 * Handles XP awarding, rank calculation and basic badge unlocking.
 */
class XpManager {

    fun addXp(currentProfile: PlayerProfile, amount: Int): PlayerProfile {
        val newXp = currentProfile.totalXp + amount
        return currentProfile.copy(totalXp = newXp)
    }

    fun getRankProgress(profile: PlayerProfile): RankProgress {
        return RankProgress.calculate(profile.totalXp)
    }

    fun checkAndUnlockBadges(
        profile: PlayerProfile,
        existingBadges: List<Badge>
    ): List<Badge> {
        val unlocked = mutableListOf<Badge>()
        val now = System.currentTimeMillis()

        // Example badges
        val candidates = listOf(
            Badge("first_session", "First Blood", "Complete your first Ranked session", "ic_badge_first", xpReward = 50),
            Badge("hour_grind", "1 Hour Grind", "Play for 60 minutes in one session", "ic_badge_hour", xpReward = 100),
            Badge("streak_3", "On Fire", "3-day session streak", "ic_badge_streak", xpReward = 150),
            Badge("streak_7", "Unstoppable", "7-day session streak", "ic_badge_streak7", xpReward = 400),
            Badge("rank_gold", "Gold Climber", "Reach Gold rank", "ic_badge_gold", xpReward = 200),
            Badge("rank_diamond", "Diamond Hands", "Reach Diamond rank", "ic_badge_diamond", xpReward = 500),
        )

        candidates.forEach { candidate ->
            val already = existingBadges.any { it.id == candidate.id && it.unlockedAt != null }
            if (!already) {
                val shouldUnlock = when (candidate.id) {
                    "first_session" -> profile.totalSessions >= 1
                    "hour_grind" -> profile.totalMinutesPlayed >= 60
                    "streak_3" -> profile.currentStreak >= 3
                    "streak_7" -> profile.currentStreak >= 7
                    "rank_gold" -> RankTier.fromXp(profile.totalXp).ordinal >= RankTier.GOLD.ordinal
                    "rank_diamond" -> RankTier.fromXp(profile.totalXp).ordinal >= RankTier.DIAMOND.ordinal
                    else -> false
                }
                if (shouldUnlock) {
                    unlocked.add(candidate.copy(unlockedAt = now))
                }
            }
        }
        return unlocked
    }
}
