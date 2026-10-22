package com.example.cityprice.di

import android.content.Context
import androidx.room.Room
import com.example.cityprice.data.local.CartDao
import com.example.cityprice.data.local.CityPriceDatabase
import com.example.cityprice.data.local.DatabaseCallback
import com.example.cityprice.data.local.ProductDao
import com.example.cityprice.data.repository.ProductRepository
import com.example.cityprice.data.repository.ProductRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CityPriceDatabase {
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        lateinit var db: CityPriceDatabase
        db = Room.databaseBuilder(
            context,
            CityPriceDatabase::class.java,
            CityPriceDatabase.NAME
        )
            .addCallback(DatabaseCallback(scope) { db })
            .fallbackToDestructiveMigration()
            .build()
        return db
    }

    @Provides
    fun provideProductDao(db: CityPriceDatabase): ProductDao = db.productDao()

    @Provides
    fun provideCartDao(db: CityPriceDatabase): CartDao = db.cartDao()

    @Provides
    @Singleton
    fun provideProductRepository(
        productDao: ProductDao,
        cartDao: CartDao
    ): ProductRepository = ProductRepositoryImpl(productDao, cartDao)
}
