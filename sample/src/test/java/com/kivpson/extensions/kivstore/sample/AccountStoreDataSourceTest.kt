package com.kivpson.extensions.kivstore.sample

import com.kivpson.extensions.kivstore.KivStore
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class AccountStoreDataSourceTest  {

    @Before
    fun setup() {
        initializeKivStore()
        clearAllData()
    }

    @After
    fun tearDown() {
        clearAllData()
    }

    @Test
    fun `test set and get values`() {
        val testId = 42
        val testToken = "abcd1234"

        println("Initial data auth: ${AccountStoreDataSource.auth} id: ${AccountStoreDataSource.id}, token: ${AccountStoreDataSource.token}")
        println("Set data in store")

        AccountStoreDataSource.auth = true
        AccountStoreDataSource.id = testId
        AccountStoreDataSource.token = testToken
        println("Current data auth: ${AccountStoreDataSource.auth} id: ${AccountStoreDataSource.id}, token: ${AccountStoreDataSource.token}")

        println("Evaluation")
        assertTrue(AccountStoreDataSource.auth)
        assertEquals(testId, AccountStoreDataSource.id)
        assertEquals(testToken, AccountStoreDataSource.token)
    }

    @Test
    fun `test data persistence`() {
        runBlocking {
            val testId = 42
            val testToken = "abcd1234"

            println("Initial data auth: ${AccountStoreDataSource.auth} id: ${AccountStoreDataSource.id}, token: ${AccountStoreDataSource.token}")

            println("Set data in store")
            AccountStoreDataSource.auth = true
            AccountStoreDataSource.id = testId
            AccountStoreDataSource.token = testToken
            println("Current data auth: ${AccountStoreDataSource.auth} id: ${AccountStoreDataSource.id}, token: ${AccountStoreDataSource.token}")

            println("Restore app")
            // Restore app
            reinitializeKivStore()

            println("Current data auth: ${AccountStoreDataSource.auth} id: ${AccountStoreDataSource.id}, token: ${AccountStoreDataSource.token}")
            println("Evaluation")
            assertTrue(AccountStoreDataSource.auth)
            assertEquals(testId, AccountStoreDataSource.id)
            assertEquals(testToken, AccountStoreDataSource.token)
        }
    }

    @Test
    fun `test concurrent access`() = runBlocking {
        val n = 10
        (1..n).map { i ->
            async {
                println("Id: $i")
                AccountStoreDataSource.id = i
            }
        }.awaitAll()
        println("Stored id: ${AccountStoreDataSource.id}")
        println("Restore app")
        // Restore app
        reinitializeKivStore()
        delay(100)
        println("Stored in DataStore id: ${AccountStoreDataSource.id}")

        assertNotEquals(0, AccountStoreDataSource.id)
    }

    @Test
    fun `test auth flow emissions`() = runBlocking {
        println("Initial data auth: ${AccountStoreDataSource.auth} id: ${AccountStoreDataSource.id}, token: ${AccountStoreDataSource.token}")
        val results = mutableListOf<Boolean>()

        val job = launch {
            AccountStoreDataSource.authFlow.stateIn(
                scope = this,
                started = SharingStarted.Eagerly,
                initialValue = false
            ).collect {
                println("Flow auth: $it")
                results.add(it)
            }
        }

        delay(100)

        AccountStoreDataSource.auth = true
        delay(100)
        AccountStoreDataSource.auth = false
        delay(100)

        assertEquals(listOf(false, true, false), results)
        job.cancel()
    }

    //
    // ------------------------------------------------------------------
    //

    private fun initializeKivStore() {
        println("Initialize store")
        KivStore.init(RuntimeEnvironment.getApplication())
    }

    private fun reinitializeKivStore() {
        println("Reinitialize store")
        initializeKivStore()
        runBlocking { delay(100) }
    }

    private fun clearAllData() = runBlocking {
        println("Clear store")
        AccountStore.clear()
    }

}