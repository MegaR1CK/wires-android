package com.wires.app.data.database

import com.wires.app.data.model.User
import io.paperdb.Paper
import javax.inject.Inject

class LocalStorage @Inject constructor() {

    companion object {
        private const val CURRENT_USER_KEY = "current_user"
    }

    var currentUser: User?
        get() = Paper.book().read<User>(CURRENT_USER_KEY)
        set(value) {
            value?.let { Paper.book().write(CURRENT_USER_KEY, value) }
        }

    fun clearStorage() = Paper.book().destroy()
}
