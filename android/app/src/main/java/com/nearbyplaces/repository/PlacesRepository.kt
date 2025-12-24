package com.nearbyplaces.repository
import com.nearbyplaces.location.LocationProvider
import com.nearbyplaces.model.Place
import java.util.UUID
class PlacesRepository(private val provider:LocationProvider){
 suspend fun getNearby():List<Place>{
  val loc=provider.getLocation()
  return listOf(
   Place(UUID.randomUUID().toString(),"Cafe",loc.latitude+0.001,loc.longitude+0.001,120.0),
   Place(UUID.randomUUID().toString(),"Restaurant",loc.latitude+0.002,loc.longitude-0.001,240.0)
  )
 }
}