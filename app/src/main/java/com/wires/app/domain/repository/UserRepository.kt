package com.wires.app.domain.repository

import com.google.gson.Gson
import com.wires.app.data.database.LocalStorage
import com.wires.app.data.mapper.UserMapper
import com.wires.app.data.model.User
import com.wires.app.data.model.UserInterest
import com.wires.app.data.model.UserPreview
import com.wires.app.data.preferences.PreferenceStorage
import com.wires.app.data.remote.WiresApiService
import com.wires.app.data.remote.params.UserPasswordChangeParams
import com.wires.app.data.remote.params.UserUpdateParams
import com.wires.app.extensions.toMultipartPart
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    private val apiService: WiresApiService,
    private val localStorage: LocalStorage,
    private val userMapper: UserMapper,
    private val gson: Gson
) {

    companion object {
        private const val AVATAR_PART_NAME = "avatar"
    }

    val isSignedIn: Boolean
        get() = !preferenceStorage.accessToken.isNullOrEmpty()

    fun getStoredUser(): User? {
        return localStorage.currentUser
    }

    fun storeUser(user: User) {
        localStorage.currentUser = user
    }

    fun clearUser() {
        localStorage.clearStorage()
    }

    suspend fun getCurrentUser(): User {
        return userMapper.fromResponseToModel(apiService.getCurrentUser().data)
    }

    suspend fun getUser(userId: Int): User {
        return userMapper.fromResponseToModel(apiService.getUser(userId).data)
    }

    suspend fun updateUser(
        firstName: String?,
        lastName: String?,
        email: String?,
        username: String?,
        interests: List<UserInterest>,
        avatarPath: String?
    ) {
        apiService.updateUser(
            gson.toJson(UserUpdateParams(username, email, firstName, lastName, interests)).toRequestBody(),
            avatarPath?.let { File(it).toMultipartPart(AVATAR_PART_NAME) }
        )
    }

    suspend fun changeUserPassword(oldPasswordHash: String, newPasswordHash: String) {
        apiService.changeUserPassword(UserPasswordChangeParams(oldPasswordHash, newPasswordHash))
    }

    suspend fun searchUsers(query: String): List<UserPreview> {
        return apiService.searchUsers(query).data.map(userMapper::fromResponseToModel)
    }
}
