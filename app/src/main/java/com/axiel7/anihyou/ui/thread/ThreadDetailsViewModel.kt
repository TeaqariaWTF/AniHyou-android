package com.axiel7.anihyou.ui.thread

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.apollographql.apollo3.api.Optional
import com.axiel7.anihyou.ThreadCommentsQuery
import com.axiel7.anihyou.ThreadDetailsQuery
import com.axiel7.anihyou.ui.base.BaseViewModel

class ThreadDetailsViewModel : BaseViewModel() {

    var threadDetails by mutableStateOf<ThreadDetailsQuery.Thread?>(null)

    suspend fun getThreadDetails(threadId: Int) {
        isLoading = true
        val response = ThreadDetailsQuery(
            threadId = Optional.present(threadId)
        ).tryQuery()

        threadDetails = response?.data?.Thread
        isLoading = false
    }

    var threadComments = mutableStateListOf<ThreadCommentsQuery.ThreadComment>()
    var page = 1
    var hasNextPage = true

    suspend fun getThreadComments(threadId: Int) {
        isLoading = page == 1
        val response = ThreadCommentsQuery(
            page = Optional.present(page),
            perPage = Optional.present(25),
            threadId = Optional.present(threadId)
        ).tryQuery()

        response?.data?.Page?.threadComments?.filterNotNull()?.let { threadComments.addAll(it) }
        hasNextPage = response?.data?.Page?.pageInfo?.hasNextPage ?: false
        page = response?.data?.Page?.pageInfo?.currentPage?.plus(1) ?: page
        isLoading = false
    }
}