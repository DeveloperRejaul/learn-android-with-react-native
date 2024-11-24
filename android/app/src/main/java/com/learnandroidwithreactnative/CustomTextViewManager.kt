package com.learnandroidwithreactnative

import android.graphics.Color
import android.widget.TextView
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

class CustomTextViewManager : SimpleViewManager<TextView>() {
    override fun getName(): String {
        return "CustomTextView"
    }

    override fun createViewInstance(reactContext: ThemedReactContext): TextView {
        return TextView(reactContext)
    }

    @ReactProp(name = "text")
    fun setText(view: TextView, text: String) {
        view.text = text
    }

     // Set the text color
     @ReactProp(name = "textColor")
     fun setTextColor(view: TextView, color: String?) {
         try {
             if (color != null) {
                 view.setTextColor(Color.parseColor(color)) // Convert color from hex string
             }
         } catch (e: IllegalArgumentException) {
             view.setTextColor(Color.BLACK) // Default to black if the color is invalid
         }
     }
 
     // Set the font size
     @ReactProp(name = "fontSize")
     fun setFontSize(view: TextView, size: Float) {
         view.textSize = size
     }
}
