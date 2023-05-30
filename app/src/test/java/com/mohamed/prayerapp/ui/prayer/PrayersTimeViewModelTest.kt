package com.mohamed.prayerapp.ui.prayer

import app.cash.turbine.test
import com.app.data.model.pray_times.PrayItem
import com.app.data.model.pray_times.Timings
import com.app.data.model.qibla.QiblaDirection
import com.app.data.remote.AppApi
import com.app.data.remote.NetWorkState
import com.app.data.repository.AppRepo
import com.app.data.utils.ErrorAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PrayersTimeViewModelTest {


    lateinit var useCase: com.app.usecase.AppUseCase

    lateinit var viewModel: PrayersTimeViewModel


    @get:Rule
    val coroutineRule = CoroutineTestRule()

    @org.junit.Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        useCase = mock(com.app.usecase.AppUseCase::class.java)
        viewModel = PrayersTimeViewModel(useCase)
    }

    @org.junit.After
    fun tearDown() {
    }

    @org.junit.Test
    fun `getPrayersTime with valid response`() = coroutineRule.runBlockingTest {
        val fakeData = arrayListOf(getFakeObject())
        `when`(
            useCase.getPrayerTimes(
                0.0, 0.0
            )
        ).thenReturn(flowOf(fakeData))

        viewModel.prayersFlow.test {
            viewModel.getPrayersTime(0.0, 0.0)
            assertEquals(awaitItem(), NetWorkState.Loading)
            assertEquals(awaitItem(), NetWorkState.Success(fakeData))
            assertEquals(awaitItem(), NetWorkState.DismissLoading)
            expectNoEvents()
        }
    }

    @org.junit.Test
    fun `getPrayersTime with bad request`() = coroutineRule.runBlockingTest {
        val error= Throwable(ErrorAPI.BAD_REQUEST)
        `when`(
            useCase.getPrayerTimes(
                0.0, 0.0
            )
        ).thenReturn(flow { throw error })

        viewModel.prayersFlow.test {
            viewModel.getPrayersTime(0.0, 0.0)
            assertEquals(awaitItem(), NetWorkState.Loading)

            assertEquals(awaitItem(), NetWorkState.Success(null))
            val result = awaitItem() as NetWorkState.Error
            assertEquals(error, result.th)
            assertEquals(awaitItem(), NetWorkState.DismissLoading)
            expectNoEvents()
        }
    }


    @org.junit.Test
    fun getQiblaDirection() = coroutineRule.runBlockingTest {
        val fakeData = QiblaDirection(0.0, 0.0)
        `when`(
            useCase.getQiblaDirection(
                0.0, 0.0
            )
        ).thenReturn(flowOf(fakeData))

        viewModel.qiblaFlow.test {
            viewModel.getQiblaDirection(0.0, 0.0)
            assertEquals(awaitItem(), NetWorkState.Loading)
            assertEquals(awaitItem(), NetWorkState.Success(fakeData))
            assertEquals(awaitItem(), NetWorkState.DismissLoading)
            expectNoEvents()
        }
    }

    private fun getFakeObject(): com.app.data.model.pray_times.PrayerTimes {
        return com.app.data.model.pray_times.PrayerTimes(
            timings = Timings(
                Fajr = "04:00",
                Sunrise = "05:00",
                Dhuhr = "12:00",
                Asr = "15:00",
                Sunset = "18:00",
                Maghrib = "18:00",
                Isha = "20:00",
            ), date = com.app.data.model.pray_times.Date(
                readable = "12/12/2021",
            ), meta = com.app.data.model.pray_times.Meta(
                timezone = "UTC",
            ), timingsArrayList = arrayListOf(
                PrayItem("Fajr", "04:00"),
                PrayItem("Sunrise", "05:00"),
                PrayItem("Dhuhr", "12:00"),
            )

        )
    }
}