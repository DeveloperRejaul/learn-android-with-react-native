package com.learnandroidwithreactnative

import android.content.Context
import com.facebook.react.bridge.ReactMethod
import jp.wasabeef.richeditor.RichEditor
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

class RichEditorViewManager : SimpleViewManager<RichEditor>() {

    override fun getName(): String {
        return "RichEditorView"
    }

    override fun createViewInstance(reactContext: ThemedReactContext): RichEditor {
        val editor = RichEditor(reactContext)
        editor.setEditorHeight(200) // Set default height
        editor.setPadding(10, 10, 10, 10) // Default padding
        editor.setPlaceholder("Start typing...")
        return editor
    }

    @ReactProp(name = "html")
    fun setHtml(view: RichEditor, html: String?) {
        view.html = html
    }

    @ReactProp(name = "placeholder")
    fun setPlaceholder(view: RichEditor, placeholder: String?) {
        view.setPlaceholder(placeholder)
    }
}
