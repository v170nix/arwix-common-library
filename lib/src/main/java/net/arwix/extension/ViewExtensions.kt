package net.arwix.extension

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

fun <T : View> T.visible(): T {
    visibility = View.VISIBLE
    return this
}

fun <T : View> T.gone(): T {
    visibility = View.GONE
    return this
}

fun <T : View> T.invisible(): T {
    visibility = View.INVISIBLE
    return this
}

fun View.setBackgroundDrawableCompat(idDrawable: Int) {
    this.background = DrawableCompat.wrap(ContextCompat.getDrawable(this.context, idDrawable)!!)
}

fun View.hideSoftInputFromWindow() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE)?.let {
        it as InputMethodManager
    } ?: return
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun TextView.setTextAppearanceCompat(resId: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.setTextAppearance(resId)
    } else {
        this.setTextAppearance(this.context, resId)
    }
}