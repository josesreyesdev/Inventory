package com.devjsr.inventario.ui

import android.app.Application
import com.devjsr.inventario.data.ItemRoomDatabase

class InventoryApplication: Application() {
    val database: ItemRoomDatabase by lazy { ItemRoomDatabase.getDatabase(this) }
}