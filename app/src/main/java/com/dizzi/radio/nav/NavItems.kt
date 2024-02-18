package com.dizzi.radio.nav

import com.dizzi.radio.R


enum class Screens(val route: String) {
    RadioPlayerScreen("radio_player"),
    AboutScreen("about"),
    TermsAndConditionsScreen("terms_and_conditions"),
    PrivacyPolicyScreen("privacy_policy"),
    RequestASongScreen("request_a_song"),
}

data class NavItems(
    val title: Int,
    val icon: Int,
    val route: String
)

val listOfNavItems = listOf(
    NavItems(
        title = R.string.radio_player,
        icon = R.drawable.mic,
        route = Screens.RadioPlayerScreen.route
    ),
    NavItems(
        title = R.string.about,
        icon = R.drawable.mic,
        route = Screens.AboutScreen.route
    ),
    NavItems(
        title = R.string.terms_and_conditions,
        icon = R.drawable.mic,
        route = Screens.TermsAndConditionsScreen.route
    ),
    NavItems(
        title = R.string.privacy_policy,
        icon = R.drawable.mic,
        route = Screens.PrivacyPolicyScreen.route
    ),
    NavItems(
        title = R.string.request_a_song,
        icon = R.drawable.mic,
        route = Screens.RequestASongScreen.route
    ),
    )