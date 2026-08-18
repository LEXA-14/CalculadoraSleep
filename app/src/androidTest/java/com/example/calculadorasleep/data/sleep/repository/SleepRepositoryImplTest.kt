package com.example.calculadorasleep.data.sleep.repository

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.calculadorasleep.data.sleep.local.SleepDatabase
import com.example.calculadorasleep.data.sleep.local.sleep.SleepEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class SleepRepositoryImplTest {
    private lateinit var db: SleepDatabase
    private lateinit var repository: SleepRepositoryImpl
    @Before
    fun setUp() {
       db=Room.inMemoryDatabaseBuilder(
           ApplicationProvider.getApplicationContext(),
           SleepDatabase::class.java
       ).allowMainThreadQueries().build()
    }
    @After
    fun tearDown(){
        db.close()
    }

    @Test
    fun getAll() {
    }

    @Test
    fun getSince() {
    }

    @Test
    fun getById() {
    }

    @Test
    fun upsert() {
    }

    @Test
    fun delete() {
    }
//    @Test
//    fun getById_devuelveElRegistroInsertado() = runTest {
//        val entity = SleepEntity(
//            sleepId = 1,
//            dormirTiempo = 1000L,
//            despertarTiempo = 2000L,
//            ciclos = 4,
//            calidadSleep = 80
//        )
//        dao.upsert(entity)
//
//        val resultado = dao.getById(1)
//
//        assertEquals(entity, resultado)
//    }

}