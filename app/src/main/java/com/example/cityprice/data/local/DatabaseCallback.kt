package com.example.cityprice.data.local

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cityprice.data.DemoData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatabaseCallback(
    private val scope: CoroutineScope,
    private val databaseProvider: () -> CityPriceDatabase
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        scope.launch(Dispatchers.IO) {
            val dao = databaseProvider().productDao()
            if (dao.count() == 0) {
                dao.insertAll(DemoData.products.map { ProductEntity.fromDomain(it) })
            }
        }
    }
}
