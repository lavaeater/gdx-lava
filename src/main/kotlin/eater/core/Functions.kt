package eater.core

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.physics.box2d.World
import eater.injection.InjectionContext.Companion.inject

fun<T> selectedItemListOf(callBack: (T)-> Unit = {}, vararg items: T): SelectedItemList<T> {
    val list = SelectedItemList(callBack, items.toList())
    return list
}

fun<T> selectedItemListOf(vararg items: T): SelectedItemList<T> {
    val list = SelectedItemList({}, items.toList())
    return list
}

fun world(): World {
    return inject()
}

fun engine() : Engine {
    return inject()
}