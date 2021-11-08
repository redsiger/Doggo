package com.example.androidschool.moviePaging.util.CustomViews.ExpandableTextView

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.widget.TextView
import com.example.androidschool.moviePaging.R

@SuppressLint("AppCompatCustomView")
class ExpandableTextViewKotlin(context: Context): TextView(context) {

    companion object {
        const val DEFAULT_TRIM_LENGTH: Int = 200
        const val ELLIPSIS: String = "..."
    }

    private lateinit var originalText: CharSequence
    private lateinit var trimmedText: CharSequence
    private lateinit var bufferType: BufferType
    private var trim: Boolean = true
    private var trimLength: Int = 0
        set(value) {
            field = value
            trimmedText = trimText(originalText)
            setText()
        }

    constructor(context: Context, attrs: AttributeSet) : this(context) {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView)
        this.trimLength = typedArray.getInt(R.styleable.ExpandableTextView_trimLength, DEFAULT_TRIM_LENGTH)
        typedArray.recycle()

        setOnClickListener {
            trim = !trim
            setText()
            requestFocusFromTouch()
        }
    }

    private fun setText() {
        super.setText(getDisplayableText(), bufferType)
    }

    private fun getDisplayableText(): CharSequence? {
        return if (trim) trimmedText else originalText
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        originalText = text!!
        trimmedText = trimText(text)
        bufferType = type!!
        setText()
    }

    private fun trimText(text: CharSequence): CharSequence {
        return if (originalText.length > trimLength) {
            SpannableStringBuilder(originalText, 0, trimLength + 1).append(ELLIPSIS)
        } else {
            originalText
        }
    }
}