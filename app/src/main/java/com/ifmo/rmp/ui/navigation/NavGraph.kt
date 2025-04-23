package com.ifmo.rmp.ui.navigation

object Routes {
    const val HOME = "home"
    const val ACTIVITIES = "activities"
    const val CLUBS = "clubs"
    const val REWARDS = "rewards"
    const val PROFILE = "profile"
    const val EDIT_PROFILE = "edit_profile"
    const val ANOTHER_PERSON = "another_person"
    const val ANOTHER_PERSON_WITH_ID = "another_person/{userId}"

    fun anotherPerson(userId: String): String {
        return "another_person/$userId"
    }
}