package com.ifmo.rmp.ui.navigation

object Routes {
    const val LOGIN = "login"
    const val REGISTRATION = "registration"
    const val HOME = "home"
    const val ACTIVITIES = "activities"
    const val CLUBS = "clubs"
    const val REWARDS = "rewards"
    const val PROFILE = "profile"
    const val EDIT_PROFILE = "edit_profile"
    const val ANOTHER_PERSON = "another_person"
    const val ANOTHER_PERSON_WITH_ID = "another_person/{userId}"
    const val ADD_ACTIVITY = "add_activity"
    const val CREATE_CLUB = "create_club"
    const val MANAGE_CLUB = "manage_club"
    const val MANAGE_CLUB_WITH_ID = "manage_club/{clubId}"

    fun anotherPerson(userId: String): String {
        return "another_person/$userId"
    }
    
    fun manageClub(clubId: String?): String {
        return if (clubId != null) "manage_club/$clubId" else "manage_club"
    }
}