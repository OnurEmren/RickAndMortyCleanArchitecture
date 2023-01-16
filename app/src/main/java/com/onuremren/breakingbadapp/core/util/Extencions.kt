package com.onuremren.breakingbadapp.core.util

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.onuremren.breakingbadapp.R



typealias InflateFragmentView<T> = (LayoutInflater, ViewGroup?, Boolean) -> T



internal interface ViewModelContract<EVENT> {
    fun process(viewEvent: EVENT)
}

class NoObserverAttachedException(message: String) : Exception(message)



fun ImageView.loadWithGlide(url: String?) {
    Glide.with(this.context)
        .load(url)
        .centerInside()
        .error(R.drawable.ic_launcher_foreground)
        .into(this)
}

fun ImageView.downloadFromUrl(url: String?){
    Glide.with(context)
        .load(url)
        .into(this)

}

fun ImageView.loadWithGlide(@DrawableRes drawable: Int) {
    Glide.with(this.context)
        .load(drawable)
        .centerInside()
        .into(this)
}

fun RadioGroup.getTextButtonChecked(): String {
    val selectedId: Int = this.checkedRadioButtonId
    return if(selectedId > -1) findViewById<RadioButton>(selectedId).text.toString() else " "
}

fun RadioGroup.setButtonChecked(selectedId: Int) {
    if(selectedId > 0) findViewById<RadioButton>(selectedId).isChecked = true
}
fun ChipGroup.getTextChipChecked(): String{
    val selectedId: Int = this.checkedChipId
    return if(selectedId > -1) findViewById<Chip>(selectedId).text.toString() else " "
}

fun ChipGroup.setChipChecked(selectedId: Int){
    if(selectedId > 0) this.findViewById<Chip>(selectedId).isChecked = true
}

/**
 * use it with when, to add all remaining branches
 */
val <T> T.exhaustive: T
    get() = this
