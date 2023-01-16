package com.onuremren.breakingbadapp.core.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.onuremren.breakingbadapp.R
import com.onuremren.breakingbadapp.databinding.FragmentLoadingBinding
import kotlinx.parcelize.Parcelize


class LoadingDialog(private val args: Args) :
    DialogFragment(R.layout.fragment_loading) {

    private lateinit var binding: FragmentLoadingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    //Bu kod parçası, bir Activity veya Fragment sınıfının getTheme() metodunu override ederek,
    // o sınıfın temasını değiştirir.
    override fun getTheme(): Int {
        return R.style.FullScreenDialog
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireContext(),theme) {
            override fun onBackPressed() {
                return
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvLoadingTitle.text = args.title
        binding.tvLoadingDescription.text = args.description
    }

    //Bu kod parçası, dismiss() metodunu override ederek,
    // bir DialogFragment'ın kapatılmasını sağlar.
    override fun dismiss() {
        super.dismissAllowingStateLoss()
    }

    //onActivityCreated() metodu, DialogFragment nesnesinin eşlik ettiği Activity'nin onCreate() metodu çalıştıktan sonra çağrılır.
    // Bu metodun içinde, DialogFragment nesnesinin özellikleri (örneğin arka plan rengi, animasyonlar vb.) ayarlanabilir.
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.attributes?.windowAnimations = R.style.DialogFragmentAnimation
    }

    companion object {
        const val TAG = "LoadingDialog"
    }

    //Veri taşınabilmesi için @Parcelize annotationı kullanılmıştır.
    @Parcelize
    data class Args(
        var title: String? = "",
        var description: String? = "",
    ) : Parcelable
}