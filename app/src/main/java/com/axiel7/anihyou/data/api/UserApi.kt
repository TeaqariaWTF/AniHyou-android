package com.axiel7.anihyou.data.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.axiel7.anihyou.FollowersQuery
import com.axiel7.anihyou.FollowingsQuery
import com.axiel7.anihyou.SearchUserQuery
import com.axiel7.anihyou.ToggleFollowMutation
import com.axiel7.anihyou.UnreadNotificationCountQuery
import com.axiel7.anihyou.UpdateUserMutation
import com.axiel7.anihyou.UserActivityQuery
import com.axiel7.anihyou.UserBasicInfoQuery
import com.axiel7.anihyou.UserOptionsQuery
import com.axiel7.anihyou.UserStatsAnimeOverviewQuery
import com.axiel7.anihyou.UserStatsMangaOverviewQuery
import com.axiel7.anihyou.ViewerQuery
import com.axiel7.anihyou.type.ActivitySort
import com.axiel7.anihyou.type.MediaListOptionsInput
import com.axiel7.anihyou.type.ScoreFormat
import com.axiel7.anihyou.type.UserStaffNameLanguage
import com.axiel7.anihyou.type.UserTitleLanguage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserApi @Inject constructor(
    private val client: ApolloClient
) {
    fun searchUserQuery(
        query: String,
        page: Int,
        perPage: Int,
    ) = client
        .query(
            SearchUserQuery(
                page = Optional.present(page),
                perPage = Optional.present(perPage),
                search = Optional.present(query)
            )
        )

    fun unreadNotificationCountQuery() = client.query(UnreadNotificationCountQuery())

    fun userOptionsQuery() = client.query(UserOptionsQuery())

    fun updateUserMutation(
        displayAdultContent: Boolean?,
        titleLanguage: UserTitleLanguage?,
        staffNameLanguage: UserStaffNameLanguage?,
        scoreFormat: ScoreFormat?,
        airingNotifications: Boolean?,
        animeListOptions: MediaListOptionsInput?,
        mangaListOptions: MediaListOptionsInput?,
    ) = client
        .mutation(
            UpdateUserMutation(
                displayAdultContent = Optional.presentIfNotNull(displayAdultContent),
                titleLanguage = Optional.presentIfNotNull(titleLanguage),
                staffNameLanguage = Optional.presentIfNotNull(staffNameLanguage),
                scoreFormat = Optional.presentIfNotNull(scoreFormat),
                airingNotifications = Optional.presentIfNotNull(airingNotifications),
                animeListOptions = Optional.presentIfNotNull(animeListOptions),
                mangaListOptions = Optional.presentIfNotNull(mangaListOptions),
            )
        )

    fun viewerQuery() = client.query(ViewerQuery())

    fun userBasicInfoQuery(
        userId: Int?,
        username: String?,
    ) = client
        .query(
            UserBasicInfoQuery(
                userId = Optional.presentIfNotNull(userId),
                name = Optional.presentIfNotNull(username)
            )
        )

    fun toggleFollowMutation(userId: Int) = client
        .mutation(
            ToggleFollowMutation(
                userId = Optional.present(userId)
            )
        )

    fun userActivityQuery(
        userId: Int,
        sort: List<ActivitySort>,
        page: Int,
        perPage: Int,
    ) = client
        .query(
            UserActivityQuery(
                page = Optional.present(page),
                perPage = Optional.present(perPage),
                userId = Optional.present(userId),
                sort = Optional.present(sort)
            )
        )

    fun userStatsAnimeOverviewQuery(userId: Int) = client
        .query(
            UserStatsAnimeOverviewQuery(
                userId = Optional.present(userId)
            )
        )

    fun userStatsMangaOverviewQuery(userId: Int) = client
        .query(
            UserStatsMangaOverviewQuery(
                userId = Optional.present(userId)
            )
        )

    fun followersQuery(
        userId: Int,
        page: Int = 1,
        perPage: Int = 25,
    ) = client
        .query(
            FollowersQuery(
                userId = userId,
                page = Optional.present(page),
                perPage = Optional.present(perPage)
            )
        )

    fun followingsQuery(
        userId: Int,
        page: Int = 1,
        perPage: Int = 25,
    ) = client
        .query(
            FollowingsQuery(
                userId = userId,
                page = Optional.present(page),
                perPage = Optional.present(perPage)
            )
        )
}