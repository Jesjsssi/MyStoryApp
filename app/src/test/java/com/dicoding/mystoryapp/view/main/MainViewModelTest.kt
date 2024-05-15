package com.dicoding.mystoryapp.view.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.mystoryapp.adapter.StoryAdapter
import com.dicoding.mystoryapp.data.preference.TokenPreferences
import com.dicoding.mystoryapp.data.remote.StoryListItem
import com.dicoding.mystoryapp.repository.StoryRepository
import com.dicoding.mystoryapp.utils.DataDummy
import com.dicoding.mystoryapp.utils.MainDispatcherRule
import com.dicoding.mystoryapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import com.dicoding.mystoryapp.Result

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcher = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Mock
    private lateinit var preferences: TokenPreferences
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel(storyRepository, preferences)
    }

    @Test
    fun `when Get Stories Should Not Null and Return Data`() = runTest {
        val dummyStory = DataDummy.generateDummyResponse()
        val data: PagingData<StoryListItem> = StoryPagingSourceTest.snapshot(dummyStory)
        val expectedStory = MutableLiveData<Result<PagingData<StoryListItem>>>()
        expectedStory.value = Result.Success(data)
        Mockito.`when`(storyRepository.getStoriesPaging(viewModel.viewModelScope))
            .thenReturn(expectedStory)

        val actualStory = viewModel.getStories.getOrAwaitValue()
        val successResult = actualStory as Result.Success
        val pagingData = successResult.data

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(pagingData)

        Assert.assertNotNull(actualStory)
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Stories is Empty Should Return No Data`() = runTest {
        val data: PagingData<StoryListItem> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<Result<PagingData<StoryListItem>>>()
        expectedStory.value = Result.Success(data)
        Mockito.`when`(storyRepository.getStoriesPaging(viewModel.viewModelScope))
            .thenReturn(expectedStory)

        val actualStory = viewModel.getStories.getOrAwaitValue()
        val successResult = actualStory as Result.Success
        val pagingData = successResult.data

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(pagingData)
        Assert.assertEquals(0, differ.snapshot().size)
    }
}

class StoryPagingSourceTest : PagingSource<Int, LiveData<List<StoryListItem>>>() {

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryListItem>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryListItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

    companion object {
        fun snapshot(items: List<StoryListItem>): PagingData<StoryListItem> {
            return PagingData.from(items)
        }
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}