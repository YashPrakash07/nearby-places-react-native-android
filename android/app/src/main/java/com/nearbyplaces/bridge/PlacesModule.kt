package com.nearbyplaces.bridge
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.facebook.react.bridge.*
import com.nearbyplaces.viewmodel.PlacesViewModel
import com.nearbyplaces.repository.PlacesRepository
import com.nearbyplaces.location.LocationProvider
class PlacesModule(ctx:ReactApplicationContext):ReactContextBaseJavaModule(ctx){
 override fun getName()="PlacesModule"
 @ReactMethod fun fetchNearbyPlaces(promise:Promise){
  if(ContextCompat.checkSelfPermission(reactApplicationContext,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
   promise.reject("PERMISSION_DENIED","Location permission required"); return
  }
  val vm=PlacesViewModel(PlacesRepository(LocationProvider(reactApplicationContext)))
  vm.places.observeForever{res->res.onSuccess{list->
    val arr=Arguments.createArray()
    list.forEach{ val m=Arguments.createMap(); m.putString("id",it.id);m.putString("name",it.name);m.putDouble("latitude",it.latitude);m.putDouble("longitude",it.longitude);m.putDouble("distance",it.distanceMeters);arr.pushMap(m)}
    promise.resolve(arr)
  }.onFailure{promise.reject("ERROR",it.message)}}
  vm.load()
 }
}