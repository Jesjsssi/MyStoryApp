package com.dicoding.mystoryapp.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.mystoryapp.R

class MyEditText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    AppCompatEditText(context, attrs) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                when (id) {
                    R.id.ed_register_email, R.id.ed_login_email -> {
                        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                            error = context.getString(R.string.error_invalid_email)
                        } else {
                            error = null
                        }
                    }

                    R.id.ed_register_password, R.id.ed_login_password -> {
                        if (s.length < 8) {
                            error = context.getString(R.string.error_short_password)
                        } else {
                            error = null
                        }
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })
    }
}