package com.devjsr.inventario.ui

import androidx.lifecycle.*
import com.devjsr.inventario.data.Item
import com.devjsr.inventario.data.ItemDao
import kotlinx.coroutines.launch

class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {

    val allItems: LiveData<List<Item>> = itemDao.getAllItems().asLiveData()

    private fun insertItem( item: Item) {
        viewModelScope.launch {
            itemDao.insertItem(item)
        }
    }

    private fun getNewItemEntry( itemName: String, itemPrice: String, itemCount: String) : Item {
        return Item(
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
    }

    fun addNewItem( itemName: String, itemPrice: String, itemCount: String) {
        val newItem = getNewItemEntry(itemName, itemPrice, itemCount)
        insertItem(newItem)
    }

    //Verificar si los texts no estan vacios
    fun isValidEntry( itemName: String, itemPrice: String, itemCount: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }

    //Recuperar los Detalles del item por id
    fun retrieveditem( id: Int): LiveData<Item> = itemDao.getItemById(id).asLiveData()

    private fun updateItem( item: Item) {
        viewModelScope.launch { itemDao.updateItem(item) }
    }

    fun sellItem( item: Item) {
        if (item.quantityInStock > 0) {
            val newItem = item.copy( quantityInStock = item.quantityInStock - 1)
            updateItem(newItem)
        }
    }

    //Stock disponible?
    fun isStockAvailable( item: Item): Boolean = (item.quantityInStock > 0)

    //borrar una entidad
    fun deleteItem( item: Item) {
        viewModelScope.launch {
            itemDao.deleteItem(item)
        }
    }

    //Actualizar/Editar de una entidad
    private fun getUpdatedItemEntry(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemCount: String
    ): Item {
        return Item(
            id = itemId,
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
    }

    fun updateItem(
        id: Int,
        itemName: String,
        itemPrice: String,
        itemCount: String
    ) {
        val updateItem = getUpdatedItemEntry(id, itemName, itemPrice, itemCount)
        updateItem(updateItem)
    }

}

class InventoryViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}