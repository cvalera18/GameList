package com.example.gamelista.data

import android.content.Context
import android.content.SharedPreferences
import com.example.gamelista.model.Game
import com.example.gamelista.model.GameStatus
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class RepositoryTest {

    @RelaxedMockK
    private lateinit var repository: Repository
    private val sharedPreferences: SharedPreferences = mockk(relaxed = true)
    private val editor: SharedPreferences.Editor = mockk(relaxed = true)

    @Before
    fun setUp() {
        val context = mockk<Context>(relaxed = true)

    }

}