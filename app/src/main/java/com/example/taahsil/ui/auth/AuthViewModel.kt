package com.example.taahsil.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taahsil.data.SessionManager
import com.example.taahsil.data.local.entity.UserEntity
import com.example.taahsil.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class AuthState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val userRole: String? = null,
    val error: String? = null,
    val user: UserEntity? = null,
    // Login fields
    val email: String = "",
    val password: String = "",
    // Signup fields
    val name: String = "",
    val role: String = "Customer",
    val companyName: String = "",
    val contactPerson: String = "",
    val country: String = ""
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    init {
        // Check if already logged in
        if (sessionManager.isLoggedIn()) {
            _state.value = _state.value.copy(
                isLoggedIn = true,
                userRole = sessionManager.getUserRole()
            )
        }
    }

    fun onEmailChange(email: String) { _state.value = _state.value.copy(email = email) }
    fun onPasswordChange(pw: String) { _state.value = _state.value.copy(password = pw) }
    fun onNameChange(name: String) { _state.value = _state.value.copy(name = name) }
    fun onRoleChange(role: String) { _state.value = _state.value.copy(role = role) }
    fun onCompanyChange(company: String) { _state.value = _state.value.copy(companyName = company) }
    fun onContactChange(contact: String) { _state.value = _state.value.copy(contactPerson = contact) }
    fun onCountryChange(country: String) { _state.value = _state.value.copy(country = country) }

    fun login() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = authRepository.login(_state.value.email, _state.value.password)
            result.fold(
                onSuccess = { user ->
                    sessionManager.saveSession(user.userId, user.role, user.name)
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        userRole = user.role,
                        user = user
                    )
                },
                onFailure = { e ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = e.message ?: "Login failed"
                    )
                }
            )
        }
    }

    fun register() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val user = UserEntity(
                userId = UUID.randomUUID().toString(),
                name = _state.value.name,
                email = _state.value.email,
                role = _state.value.role,
                companyName = _state.value.companyName,
                contactPerson = _state.value.contactPerson,
                country = _state.value.country
            )
            val result = authRepository.register(user)
            result.fold(
                onSuccess = { registeredUser ->
                    sessionManager.saveSession(registeredUser.userId, registeredUser.role, registeredUser.name)
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        userRole = registeredUser.role,
                        user = registeredUser
                    )
                },
                onFailure = { e ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = e.message ?: "Registration failed"
                    )
                }
            )
        }
    }

    fun logout() {
        sessionManager.clearSession()
        _state.value = AuthState()
    }

    fun clearError() { _state.value = _state.value.copy(error = null) }
}
