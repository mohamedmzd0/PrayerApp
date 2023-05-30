package com.app.usecase


import com.app.data.model.BaseEndPointResponse
import com.app.data.model.pray_times.Date
import com.app.data.model.pray_times.Meta
import com.app.data.model.pray_times.PrayerTimes
import com.app.data.model.pray_times.Timings
import com.app.data.model.qibla.QiblaDirection
import com.app.data.remote.AppApi
import com.app.data.repository.AppRepo
import com.app.data.utils.ErrorAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AppUseCaseTest {


    lateinit var api: AppApi

    lateinit var appRepository: AppRepo

    lateinit var appUseCase: AppUseCase



    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        api = mock(AppApi::class.java)
        appRepository = mock(AppRepo::class.java)
        appUseCase = AppUseCase(appRepository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `getPrayerTimes success with data`() = runBlocking {
        // Mock data
        val fakeData = getFakeData()
        val fakeResponse = Response.success(fakeData)
        val currentDate = appUseCase.getCurrentDate()

        // Mock repository response
        `when`(
            appRepository.getPrayerTimes(
                latitude = 31.12345,
                longitude = 31.12345,
                time = currentDate
            )
        ).thenReturn(
            flowOf(fakeResponse)
        )

        // Call the use case function
        val result = appUseCase.getPrayerTimes(
            latitude = 31.12345,
            longitude = 31.12345,
            time = currentDate
        ).single()

        assertEquals(1, result.size)
        assertEquals("05:00 AM", result.first().timingsArrayList.first().time)
    }

    @Test
    fun `getPrayerTimes failed with serverError`() = runBlocking {
        // Mock server error response
        val fakeErrorResponse = Response.error<BaseEndPointResponse<ArrayList<PrayerTimes>>>(
            500,
            "{\"message\":\"Internal Server Error\" }".toResponseBody()
        )
        val currentDate = appUseCase.getCurrentDate()

        // Mock repository response
        `when`(appRepository.getPrayerTimes(latitude = 31.12345, longitude = 31.12345, time = currentDate))
            .thenReturn(flowOf(fakeErrorResponse))

        // Call the use case function
        try {
            appUseCase.getPrayerTimes(latitude = 31.12345, longitude = 31.12345, time = currentDate).single()
            fail("Expected exception not thrown")
        } catch (e: Throwable) {
            assertEquals(ErrorAPI.SERVER_ERROR, e.message)
        }
    }



private fun getFakeData() = BaseEndPointResponse<ArrayList<PrayerTimes>>().apply {
    code = 200
    data = arrayListOf(PrayerTimes().apply {
        timings = Timings().apply {
            Fajr = "05:00 (EET)"
            Sunrise = "06:00 (EET)"
            Dhuhr = "12:00 (EET)"
            Asr = "15:00 (EET)"
            Maghrib = "18:00 (EET)"
            Isha = "20:00 (EET)"
        }
        date = Date().apply {
            readable = "05:00 AM"
        }

        meta = Meta().apply {
            timezone = "Africa/Cairo"
        }
    })
}


// create getqibladirection success with data
@Test
fun `getQiblaDirection success with data`() = runBlocking {
    // Mock data
    val fakeData = getFakeQiblaData()
    val fakeResponse = Response.success(fakeData)
    val latitude = 31.12345
    val longitude = 31.12345

    // Mock repository response
    `when`(appRepository.getQiblaDirection(latitude, longitude))
        .thenReturn(flowOf(fakeResponse))

    // Call the use case function
    val result = appUseCase.getQiblaDirection(latitude, longitude).single()

    assertNotNull(result)
    assertEquals(fakeData.data, result)
}


private fun getFakeQiblaData() = BaseEndPointResponse<QiblaDirection>().apply {
    code = 200
    data = QiblaDirection().apply {
        direction = 123.12345
        latitude = 31.12345
        longitude = 31.12345
    }
}
}