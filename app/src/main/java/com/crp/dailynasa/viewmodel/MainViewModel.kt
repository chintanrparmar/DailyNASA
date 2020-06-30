package com.crp.dailynasa.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crp.dailynasa.data.api.NasaAPI
import com.crp.dailynasa.data.model.ResponseData
import com.crp.dailynasa.data.model.State
import com.crp.dailynasa.utils.NetworkHelper
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class MainViewModel(
    private val nasaAPI: NasaAPI,
    private val networkHelper: NetworkHelper
) : ViewModel(), KoinComponent {

    private val _imageData = MutableLiveData<State<ResponseData>>()
    val imageData: LiveData<State<ResponseData>>
        get() = _imageData



     fun fetchData(dateString: String) {
        viewModelScope.launch {
            _imageData.postValue(State.loading())
            if (networkHelper.isNetworkConnected()) {
                nasaAPI.getImageResponse("DEMO_KEY", dateString).let {
                    if (it.url.isNotEmpty()) {
                        _imageData.postValue(State.success(it))
                    } else _imageData.postValue(State.error("No Data Found"))
                }
            } else _imageData.postValue(State.error("No internet connection"))
        }
    }
}