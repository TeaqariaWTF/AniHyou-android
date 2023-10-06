package com.axiel7.anihyou.ui.screens.home.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.axiel7.anihyou.data.model.activity.text
import com.axiel7.anihyou.type.ActivityType
import com.axiel7.anihyou.ui.composables.pullrefresh.PullRefreshIndicator
import com.axiel7.anihyou.ui.composables.pullrefresh.pullRefresh
import com.axiel7.anihyou.ui.composables.pullrefresh.rememberPullRefreshState
import com.axiel7.anihyou.ui.screens.home.activity.composables.ActivityFeedItem
import com.axiel7.anihyou.ui.screens.home.activity.composables.ActivityFollowingChip
import com.axiel7.anihyou.ui.screens.home.activity.composables.ActivityTypeChip
import com.axiel7.anihyou.ui.theme.AniHyouTheme

@Composable
fun ActivityFeedView(
    modifier: Modifier = Modifier,
    navigateToActivityDetails: (Int) -> Unit,
    navigateToMediaDetails: (Int) -> Unit,
    navigateToUserDetails: (Int) -> Unit,
    navigateToFullscreenImage: (String) -> Unit,
) {
    val viewModel: ActivityFeedViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagingItems = viewModel.activities.collectAsLazyPagingItems()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = pagingItems.loadState.refresh is LoadState.Loading,
        onRefresh = { viewModel.setRefreshCache(true) }
    )

    Box(
        modifier = Modifier
            .clipToBounds()
            .pullRefresh(pullRefreshState)
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = modifier,
        ) {
            item {
                Row(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ActivityTypeChip(
                        value = uiState.type,
                        onValueChanged = viewModel::setType
                    )
                    ActivityFollowingChip(
                        value = uiState.isFollowing,
                        onValueChanged = viewModel::setIsFollowing
                    )
                }
            }
            items(
                count = pagingItems.itemCount,
                contentType = { it }
            ) { index ->
                pagingItems[index]?.let { item ->
                    item.onListActivity?.let {
                        ActivityFeedItem(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            type = ActivityType.MEDIA_LIST,
                            username = it.user?.name,
                            avatarUrl = it.user?.avatar?.medium,
                            createdAt = it.listActivityFragment.createdAt,
                            text = it.listActivityFragment.text(),
                            replyCount = it.listActivityFragment.replyCount,
                            likeCount = it.listActivityFragment.likeCount,
                            isLiked = it.listActivityFragment.isLiked,
                            mediaCoverUrl = it.listActivityFragment.media?.coverImage?.medium,
                            onClick = {
                                navigateToActivityDetails(it.listActivityFragment.id)
                            },
                            onClickUser = {
                                it.listActivityFragment.userId?.let(navigateToUserDetails)
                            },
                            onClickLike = {
                                viewModel.toggleLikeActivity(it.listActivityFragment.id)
                            },
                            onClickMedia = {
                                it.listActivityFragment.media?.id?.let(navigateToMediaDetails)
                            }
                        )
                        HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))
                    }
                    item.onTextActivity?.textActivityFragment?.let {
                        ActivityFeedItem(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            type = ActivityType.TEXT,
                            username = it.user?.name,
                            avatarUrl = it.user?.avatar?.medium,
                            createdAt = it.createdAt,
                            text = it.text ?: "",
                            replyCount = it.replyCount,
                            likeCount = it.likeCount,
                            isLiked = it.isLiked,
                            onClick = {
                                navigateToActivityDetails(it.id)
                            },
                            onClickUser = {
                                it.userId?.let(navigateToUserDetails)
                            },
                            onClickLike = {
                                viewModel.toggleLikeActivity(it.id)
                            },
                            navigateToFullscreenImage = navigateToFullscreenImage
                        )
                        HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))
                    }
                }
            }
        }//:LazyColumn
        PullRefreshIndicator(
            refreshing = pagingItems.loadState.refresh is LoadState.Loading,
            state = pullRefreshState,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.TopCenter)
        )
    }//:Box
}

@Preview
@Composable
fun ActivityFeedViewPreview() {
    AniHyouTheme {
        Surface {
            ActivityFeedView(
                navigateToActivityDetails = {},
                navigateToMediaDetails = {},
                navigateToUserDetails = {},
                navigateToFullscreenImage = {},
            )
        }
    }
}