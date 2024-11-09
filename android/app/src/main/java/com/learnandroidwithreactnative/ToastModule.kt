package com.learnandroidwithreactnative
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import android.widget.Toast
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.Callback

import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.modules.core.DeviceEventManagerModule

class ToastModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName() = "Toast"
 

    @ReactMethod
    fun nonPromiseFunction(text: String) {
        Toast.makeText(reactApplicationContext,text,Toast.LENGTH_SHORT).show();
    }

    @ReactMethod
    fun promiseFunction(name: String, location: String, promise: Promise) {
        try {
            val eventId = "$name $location"
            promise.resolve(eventId)
        } catch (e: Throwable) {
            promise.reject("Create Event Error", e)
        }
    }

    @ReactMethod
    fun callbackFunction(name: String, location: String, callback: Callback) {
        val eventId = "$name $location"
        callback.invoke(eventId)
    }

    @ReactMethod
    fun callbackFunctionWithError(name: String, location: String, callback: Callback) {
        val eventId = "$name $location"
        callback.invoke(null, eventId)
    }


    @ReactMethod
    fun twoCallbackFunction(name: String, location: String, failureCallback: Callback, successCallback: Callback) {
        val eventId = "$name $location"
        failureCallback.invoke(eventId)
        successCallback.invoke(eventId)
    }

    // Event emitter function
    private fun sendEvent(reactContext: ReactContext, eventName: String, params: WritableMap?) {
        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(eventName, params)
    }

    // Track the number of active listeners
    private var listenerCount = 0

    @ReactMethod
    fun addListener(eventName: String) {
        if (listenerCount == 0) {
            // Set up any upstream listeners or background tasks as necessary
        }
        listenerCount += 1
    }

    @ReactMethod
    fun removeListeners(count: Int) {
        listenerCount -= count
        if (listenerCount == 0) {
            // Remove upstream listeners, stop unnecessary background tasks
        }
    }

    @ReactMethod
    fun emitEvent(value:String ) {
        val params = Arguments.createMap().apply {
            putString("eventProperty", value)
        }
        sendEvent(reactApplicationContext, "EventReminder", params)
    }

}