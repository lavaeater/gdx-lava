package eater.core

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family




class SelectedItemList<T>(val listUpdatedCallback: (T)-> Unit, items: List<T>) : ArrayList<T>() {
    init {
        this.addAll(items)
    }

    val withSelectedItemFirst: List<T> get() {
        val item = get(selectedIndex)
        this.sortBy { it.toString() }
        val newList = mutableListOf<T>()
        var indexToAdd = indexOf(item)
        for(i in 0 until this.size) {
            indexToAdd = when {
                indexToAdd < 0 -> lastIndex
                indexToAdd > lastIndex -> 0
                else -> indexToAdd
            }
            newList.add(this[indexToAdd])
            indexToAdd += 1
        }
        return newList
    }

    private var selectedIndex: Int = 0
        private set(value) {
            field = when {
                value < 0 -> this.lastIndex
                value > this.lastIndex -> 0
                else -> value
            }
        }
    val selectedItem get () = this[selectedIndex]
    fun nextItem() : T {
        selectedIndex++
        listUpdatedCallback(selectedItem)
        return selectedItem
    }
    fun previousItem() : T {
        selectedIndex--
        listUpdatedCallback(selectedItem)
        return selectedItem
    }
}