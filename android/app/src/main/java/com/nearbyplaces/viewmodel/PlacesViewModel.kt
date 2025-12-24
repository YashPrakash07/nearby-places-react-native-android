package com.nearbyplaces.viewmodel
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import com.nearbyplaces.repository.PlacesRepository
class PlacesViewModel(private val repo:PlacesRepository):ViewModel(){
 val places=MutableLiveData<Result<List<com.nearbyplaces.model.Place>>>()
 fun load(){ viewModelScope.launch{ try{places.value=Result.success(repo.getNearby())}catch(e:Exception){places.value=Result.failure(e)} } }
}