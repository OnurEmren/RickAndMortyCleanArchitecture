package com.onuremren.breakingbadapp.core.base

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onuremren.breakingbadapp.core.util.NoObserverAttachedException
import com.onuremren.breakingbadapp.core.util.Resource
import com.onuremren.breakingbadapp.core.util.SingleLiveEvent
import com.onuremren.breakingbadapp.core.util.ViewModelContract
import com.onuremren.breakingbadapp.model.CharacterList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

open class BaseViewModel<STATE, EFFECT, EVENT> :
    ViewModel(), ViewModelContract<EVENT> {

    //İnternetin olup olmaması durumu SingleLiveEvent sınıfında değerlendirildiği için
    //Bu değişken SLE sınıfına bağlanmıştır. Bu sayede kontrolü yapacak SLE sınıfının metodları
    //override edilerek ilgili alan kolayca gözlemlenebilir.
    private val _showLoading: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val showLoading: SingleLiveEvent<Boolean> = _showLoading

    private val _networkError: SingleLiveEvent<Throwable?> = SingleLiveEvent()
    val networkError: SingleLiveEvent<Throwable?> = _networkError

    var loadingCount = 0

    private val _viewStates: MutableLiveData<STATE> = MutableLiveData()
    fun viewStates(): LiveData<STATE> = _viewStates

    private var _viewState: STATE? = null
    protected var viewState: STATE
        get() = _viewState
            ?: throw UninitializedPropertyAccessException("\"viewState\" was queried before being initialized")
        set(value) {
            _viewState = value
            _viewStates.value = value!!
        }


    private val _viewEffects: SingleLiveEvent<EFFECT> = SingleLiveEvent()
    fun viewEffects(): SingleLiveEvent<EFFECT> = _viewEffects

    private var _viewEffect: EFFECT? = null
    protected var viewEffect: EFFECT
        get() = _viewEffect
            ?: throw UninitializedPropertyAccessException("\"viewEffect\" was queried before being initialized")
        set(value) {
            _viewEffect = value
            _viewEffects.value = value!!
        }

    @CallSuper
    override fun process(viewEvent: EVENT) {
        if (!viewStates().hasObservers()) {
            throw NoObserverAttachedException("No observer attached. In case of custom View \"startObserving()\" function needs to be called manually.")
        }
    }

    fun <T> makeApiCall(
        onFailure: (Throwable?) -> Unit,
        onLoading: () -> Unit,
        onSuccess: (T?) -> Unit,
        showLoading: Boolean = true,
        request: suspend () -> Flow<Resource<T>>,
    ) {
        viewModelScope.launch {
            request().collect {
                when (val response = it) {
                    is Resource.Failure -> {
                        hideLoading()
                        _networkError.postValue(response.throwable)
                        onFailure.invoke(response.throwable)
                    }
                    is Resource.Loading -> {
                        if (showLoading) {
                            showLoading()
                        }
                        onLoading.invoke()
                    }
                    is Resource.Success -> {
                        hideLoading()
                        onSuccess.invoke(response.value)
                    }
                }
            }
        }
    }
    //loadingCount değişkeni, bir sayaç gibi kullanılmaktadır ve bu değişkenin değeri,
    // yüklenme işlemlerini takip etmek için kullanılır.
    // Örneğin, bir Activity veya Fragment içinde birden fazla yüklenme işlemi gerçekleştirildiğinde,
    // loadingCount değişkeninin değeri arttırılır ve bu sayede, yüklenme işlemlerinin sayısı takip edilebilir.

    private fun showLoading() {
        loadingCount++
        if (loadingCount > 0) {
            _showLoading.value = true
        }
    }

    private fun hideLoading() {
        loadingCount--
        if (loadingCount == 0) {
            _showLoading.value = false
        }
    }
}
