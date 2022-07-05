package eater.injection

import com.badlogic.ashley.core.Entity

sealed class ContactType {
    class FishAndCity(val fish: Entity, val city: Entity): ContactType()
}