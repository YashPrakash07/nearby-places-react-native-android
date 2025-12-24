package com.nearbyplaces.location
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
class LocationProvider(ctx:Context){
 private val client=LocationServices.getFusedLocationProviderClient(ctx)
 suspend fun getLocation():Location = suspendCancellableCoroutine{cont->
  client.lastLocation.addOnSuccessListener{if(it!=null)cont.resume(it) else cont.resumeWithException(Exception("LOCATION_UNAVAILABLE"))}.addOnFailureListener{cont.resumeWithException(it)}
 }
}