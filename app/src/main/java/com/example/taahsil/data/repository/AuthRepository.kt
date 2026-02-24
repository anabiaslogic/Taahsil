package com.example.taahsil.data.repository

import com.example.taahsil.data.local.dao.UserDao
import com.example.taahsil.data.local.entity.UserEntity
import com.example.taahsil.data.remote.AuthResponse
import com.example.taahsil.data.remote.LoginRequest
import com.example.taahsil.data.remote.TaahsilApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: TaahsilApi,
    private val userDao: UserDao
) {
    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = api.login(LoginRequest(email, password))
            userDao.insertUser(response.user)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(user: UserEntity): Result<AuthResponse> {
        return try {
            val response = api.register(user)
            userDao.insertUser(response.user)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUser(userId: String): Flow<UserEntity?> = userDao.getUserById(userId)
}
