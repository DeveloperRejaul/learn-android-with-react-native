package com.webview

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView
import android.webkit.WebViewClient
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.events.Event
import com.facebook.react.TurboReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider
import com.facebook.react.uimanager.ViewManager


class ReactWebView: WebView {
    constructor(context: Context) : super(context) {
        configureComponent()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        configureComponent()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        configureComponent()
    }

    private fun configureComponent() {
        this.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        this.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                emitOnScriptLoaded(OnScriptLoadedEventResult.success)
            }
        }
    }

    fun emitOnScriptLoaded(result: OnScriptLoadedEventResult) {
        val reactContext = context as ReactContext
        val surfaceId = UIManagerHelper.getSurfaceId(reactContext)
        val eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, id)
        val payload =
            Arguments.createMap().apply {
                putString("result", result.name)
            }
        val event = OnScriptLoadedEvent(surfaceId, id, payload)

        eventDispatcher?.dispatchEvent(event)
    }

    enum class OnScriptLoadedEventResult() {
        success(),
        error()
    }

    inner class OnScriptLoadedEvent(
        surfaceId: Int,
        viewId: Int,
        private val payload: WritableMap
    ) : Event<OnScriptLoadedEvent>(surfaceId, viewId) {
        override fun getEventName() = "onScriptLoaded"

        override fun getEventData() = payload
    }
}

class ReactWebViewPackage : TurboReactPackage() {
    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return listOf(ReactWebViewManager(reactContext))
    }

    override fun getModule(s: String, reactApplicationContext: ReactApplicationContext): NativeModule? {
        when (s) {
            ReactWebViewManager.REACT_CLASS -> ReactWebViewManager(reactApplicationContext)
        }
        return null
    }

    override fun getReactModuleInfoProvider(): ReactModuleInfoProvider = ReactModuleInfoProvider {
        mapOf(ReactWebViewManager.REACT_CLASS to ReactModuleInfo(
            _name = ReactWebViewManager.REACT_CLASS,
            _className = ReactWebViewManager.REACT_CLASS,
            _canOverrideExistingModule = false,
            _needsEagerInit = false,
            isCxxModule = false,
            isTurboModule = true,
        )
        )
    }
}