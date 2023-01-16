package com.onuremren.breakingbadapp.core.util

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T> : MutableLiveData<T>() {
    //Atomic Boolean, Java dilinde bir boolean değişken türüdür.
    // Bu değişken, birden fazla thread (iş parçacığı) tarafından aynı anda kullanılabilecek şekilde tasarlandı
    // ve bu nedenle, thread-safe (iş parçacığı güvenli) bir değişkendir.
    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner){t ->
            if (pending.compareAndSet(true, false))
                observer.onChanged(t)
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }
}