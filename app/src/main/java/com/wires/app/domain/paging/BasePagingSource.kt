package com.wires.app.domain.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState

const val DEFAULT_OFFSET = 0
const val DEFAULT_LIMIT = 15

abstract class BasePagingSource<T : Any> : PagingSource<Int, T>() {

    abstract suspend fun loadPage(offset: Int, limit: Int): List<T>

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val offset = params.key ?: DEFAULT_OFFSET
        val limit = params.loadSize

        return try {
            val result = loadPage(offset, limit)
            LoadResult.Page(
                data = result,
                prevKey = if (offset == DEFAULT_OFFSET) null else offset - 1,
                nextKey = if (result.size < limit) null else offset + result.size
            )
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        val nextOffset = page.nextKey
        val prevOffset = page.prevKey
        return if (nextOffset != null && prevOffset != null)
            nextOffset - prevOffset
        else
            null
    }
}

fun <T : Any> createPager(pagingSource: PagingSource<Int, T>, pageSize: Int = DEFAULT_LIMIT): Pager<Int, T> {
    return createPager({ pagingSource }, pageSize)
}

fun <T : Any> createPager(pagingSourceFactory: () -> PagingSource<Int, T>, pageSize: Int = DEFAULT_LIMIT): Pager<Int, T> {
    return Pager(
        config = PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = false,
            initialLoadSize = pageSize,
            prefetchDistance = pageSize / 2
        ),
        pagingSourceFactory = pagingSourceFactory
    )
}
