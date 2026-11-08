package com.forgeos.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Rank ladder for ForgeOS
 * Iron → Radiant
 */
enum class RankTier(val displayName: String, val minXp: Int, val colorHex: String) {
    IRON("Iron", 0, "#8B8B8B"),
    BRONZE("Bronze", 500, "#CD7F32"),
    SILVER("Silver", 1500, "#C0C0C0"),
    GOLD("Gold", 3500, "#FFD700"),
    PLATINUM("Platinum", 7000, "#E5E4E2"),
    DIAMOND("Diamond", 12000, "#B9F2FF"),
    MASTER("Master", 20000, "#9B59B6"),
    GRANDMASTER("Grandmaster", 35000, "#E74C3C"),
    RADIANT("Radiant", 55000, "#00F5FF");

    companion object {
        fun fromXp(xp: Int): RankTier {
            return values().lastOrNull { xp >= it.minXp } ?: IRON
        }
    }
}

@Entity(tableName = "player_profile")
data class PlayerProfile(
    @PrimaryKey val id: Int = 1,
    val displayName: String = "Player",
    val mainGame: String = "BGMI",          // "BGMI" or "Free Fire"
    val totalXp: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalSessions: Int = 0,
    val totalMinutesPlayed: Long = 0,
    val avatarUri: String? = null,
    val rankGoal: RankTier = RankTier.GOLD
)

@Entity(tableName = "game_session")
data class GameSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startTime: Long,
    val endTime: Long? = null,
    val durationMinutes: Int = 0,
    val xpEarned: Int = 0,
    val mode: SessionMode = SessionMode.RANKED,
    val primaryGame: String = "BGMI",
    val completed: Boolean = false
)

enum class SessionMode {
    RANKED,     // Full lockdown + XP
    CASUAL,     // Light focus
    PRACTICE    // No XP, just tools
}

@Entity(tableName = "badge")
data class Badge(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val iconRes: String,
    val unlockedAt: Long? = null,
    val xpReward: Int = 0
)

data class RankProgress(
    val currentTier: RankTier,
    val currentXp: Int,
    val xpForNext: Int,
    val progressPercent: Float
) {
    companion object {
        fun calculate(totalXp: Int): RankProgress {
            val current = RankTier.fromXp(totalXp)
            val next = RankTier.values().getOrNull(current.ordinal + 1)
            val xpForNext = if (next != null) next.minXp - totalXp else 0
            val rangeStart = current.minXp
            val rangeEnd = next?.minXp ?: (current.minXp + 10000)
            val percent = if (rangeEnd > rangeStart) {
                ((totalXp - rangeStart).toFloat() / (rangeEnd - rangeStart)) * 100f
            } else 100f
            return RankProgress(current, totalXp, xpForNext, percent.coerceIn(0f, 100f))
        }
    }
}
