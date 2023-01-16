package com.onuremren.breakingbadapp.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.viewbinding.ViewBinding
import com.onuremren.breakingbadapp.core.util.AuthError
import com.onuremren.breakingbadapp.core.util.InflateFragmentView
import com.onuremren.breakingbadapp.core.util.InternetConnectionError

abstract class BaseFragment<VB : ViewBinding, STATE, EFFECT, EVENT, ViewModel : BaseViewModel<STATE, EFFECT, EVENT>>(
    private val inflateFragmentView: InflateFragmentView<VB>
) : Fragment() {
    private var loadingDialog: LoadingDialog? = null
    abstract val viewModel: ViewModel
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    abstract fun init()

    //Bu kod parçası, bir sınıfın renderViewState() metodunu tanımlar.
    //Bu metodun içinde, bir View nesnesinin görünümü değiştirilebilir.
    //Uygulamadaki tüm fragmentlarda uygulanmasını istediğimiz için abstract olarak tanımlıyoruz.
    abstract fun renderViewState(viewState: STATE)

    //renderViewEffect() metodu, bir View nesnesinin davranışını değiştirmek için kullanılır.
    abstract fun renderViewEffect(viewEffect: EFFECT)


    private val viewStateObserver = Observer<STATE> {
        renderViewState(it)
    }

    private val viewEffectObserver = Observer<EFFECT> {
        renderViewEffect(it)
    }

    protected fun navigateTo(actionId: Int) {
        Navigation.findNavController(binding.root).navigate(actionId)
    }

    protected fun navigateTo(directions: NavDirections) {
        Navigation.findNavController(binding.root).navigate(directions)
    }

    protected fun navigateBack() {
        Navigation.findNavController(binding.root).popBackStack()
    }

    private fun showToast(text: String) {
        context?.let {
            Toast.makeText(it, text, Toast.LENGTH_SHORT).show()
        }
    }

    protected open fun hideLoadingDialog() {
        loadingDialog?.dismissAllowingStateLoss()
        loadingDialog = null
    }

    protected open fun showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog(
                args = LoadingDialog.Args(
                    "Yükleniyor...",
                    ""
                )
            )
        }

        //Bu kod parçası, eğer bir LoadingDialog adlı fragment zaten eklenmişse,
        // yeni bir tane eklememesi için kontrol ediyor. Eğer LoadingDialog adlı fragment zaten eklenmemişse,
        // o zaman loadingDialog değişkenini kullanarak yeni bir LoadingDialog fragment'ı oluşturup ekliyor.
        val isAlreadyAdded = childFragmentManager.findFragmentByTag(LoadingDialog.TAG)
        if (isAlreadyAdded == null) {
            loadingDialog?.showNow(childFragmentManager, LoadingDialog.TAG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflateFragmentView.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewStates().observe(viewLifecycleOwner, viewStateObserver)
        viewModel.viewEffects().observe(viewLifecycleOwner, viewEffectObserver)
        init()
        observeShowLoading()
        observeNetworkError()
    }


    private fun observeNetworkError() {
        viewModel.networkError.observe(viewLifecycleOwner) { error ->
            when (error) {
                is AuthError -> {
                    showToast("Auth 401 ")
                }
                is UnknownError -> {
                    showToast("Unknown")
                }
                is InternetConnectionError -> {
                    showToast("Internet connection error ")
                }
                else -> {
                    showToast("Some thing else")
                }
            }
        }
    }

    private fun observeShowLoading() {
        viewModel.showLoading.observe(viewLifecycleOwner) { shouldShow ->
            if (shouldShow) {
                showLoadingDialog()
            } else {
                hideLoadingDialog()
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
