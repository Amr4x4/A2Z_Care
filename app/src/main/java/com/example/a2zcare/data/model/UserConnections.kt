package com.example.a2zcare.data.model

data class SaveUserRequest(
    val userId: String,
    val targetUserId: String
)

data class SearchUserResponse(
    val users: List<UserSearchResult>
)

data class UserSearchResult(
    val id: String,
    val name: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String? = null
)