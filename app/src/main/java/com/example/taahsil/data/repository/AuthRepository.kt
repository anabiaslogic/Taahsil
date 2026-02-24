package com.example.taahsil.data.repository

import com.example.taahsil.data.local.dao.UserDao
import com.example.taahsil.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val userDao: UserDao
) {
    /**
     * Offline login: checks Room database directly.
     * Matches by email. In a real app you'd hash and compare passwords.
     */
    suspend fun login(email: String, password: String): Result<UserEntity> {
        return try {
            val users = userDao.getAllUsers().firstOrNull() ?: emptyList()
            val user = users.find { it.email.equals(email, ignoreCase = true) }
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("No account found with this email. Please sign up first."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Offline registration: saves user directly to Room.
     */
    suspend fun register(user: UserEntity): Result<UserEntity> {
        return try {
            // Check if email already exists
            val existing = userDao.getAllUsers().firstOrNull() ?: emptyList()
            if (existing.any { it.email.equals(user.email, ignoreCase = true) }) {
                return Result.failure(Exception("An account with this email already exists."))
            }
            userDao.insertUser(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUser(userId: String): Flow<UserEntity?> = userDao.getUserById(userId)

    fun getAllUsers(): Flow<List<UserEntity>> = userDao.getAllUsers()
}
