package com.kl3jvi.animity.ui.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kl3jvi.animity.R

class EliteNavView(
    context: Context,
    attributeSet: AttributeSet,
) : BottomNavigationView(context, attributeSet) {
    private var cornerRadius = 0f

    init {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.EliteNavView)
        cornerRadius = typedArray.getDimension(R.styleable.EliteNavView_cornerRadius, 0f)
        typedArray.recycle()

        background = createRoundedBg()
    }

    private fun createRoundedBg(): Drawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.cornerRadius = cornerRadius
        gradientDrawable.setColor(
            ContextCompat.getColor(
                context,
                R.color.darkBlue,
            ),
        )
        return gradientDrawable
    }

    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int,
    ) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val marginHorizontal = resources.getDimension(R.dimen.default_margin_horizontal).toInt()
        val marginBottom = resources.getDimension(R.dimen.default_margin_bottom).toInt()
        val marginTop = resources.getDimension(R.dimen.default_margin_top).toInt()

        // Apply margins
        val layoutParams = layoutParams as MarginLayoutParams
        layoutParams.setMargins(marginHorizontal, marginTop, marginHorizontal, marginBottom)
    }
}
