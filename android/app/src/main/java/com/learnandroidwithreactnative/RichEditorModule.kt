package com.learnandroidwithreactnative

import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import jp.wasabeef.richeditor.RichEditor

class RichEditorModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {


    override fun getName(): String {
        return "RichEditorModule"
    }

    // Initialize the editor (Optional: You might handle initialization in the view manager)
    @ReactMethod
    fun setHtml(html: String, editorTag: Int) {
        Log.d("RichEditorModule", "HTML content fetched successfully")
    }

    @ReactMethod
    fun getHtml () {
        Log.d("RichEditorModule", "HTML content fetched successfully Get")
    }

}
