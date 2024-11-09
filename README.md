This is a new [**React Native**](https://reactnative.dev) project, bootstrapped using [`@react-native-community/cli`](https://github.com/react-native-community/cli).

# Getting Started

>**Note**: Make sure you have completed the [React Native - Environment Setup](https://reactnative.dev/docs/environment-setup) instructions till "Creating a new application" step, before proceeding.

## Step 1: Start the Metro Server

First, you will need to start **Metro**, the JavaScript _bundler_ that ships _with_ React Native.

To start Metro, run the following command from the _root_ of your React Native project:

```bash
# using npm
npm start

# OR using Yarn
yarn start
```

## Step 2: Start your Application

Let Metro Bundler run in its _own_ terminal. Open a _new_ terminal from the _root_ of your React Native project. Run the following command to start your _Android_ or _iOS_ app:

### For Android

```bash
# using npm
npm run android

# OR using Yarn
yarn android
```

### For iOS

```bash
# using npm
npm run ios

# OR using Yarn
yarn ios
```

## Implements of native module legacy and new architecture on android


### New architecture
[React Native Doc](https://reactnative.dev/docs/turbo-native-modules-introduction)
#### Step 01
```js
// create file like this path in root dir : specs/NativeLocalStorage.ts
// and write interface and register module
import type {TurboModule} from 'react-native';
import {TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
  setItem(value: string, key: string): void;
  getItem(key: string): string | null;
  removeItem(key: string): void;
  clear(): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>(
  'NativeLocalStorage',
);
```


#### Step 02:  Configure Codegen to run
```json
// add this code in package.json file
 "codegenConfig": {
     "name": "NativeLocalStorageSpec",
     "type": "modules",
     "jsSrcsDir": "specs",
     "android": {
       "javaPackageName": "com.nativelocalstorage"
     }
  },
```
#### Step 03: Run this command
```
cd android
./gradlew generateCodegenArtifactsFromSchema

```

#### Step 04
```js
// Write Application Code using the Turbo Native Module
import React from 'react';
import {
  SafeAreaView,
  StyleSheet,
  Text,
  TextInput,
  Button,
} from 'react-native';

import NativeLocalStorage from './specs/NativeLocalStorage';

const EMPTY = '<empty>';

function App(): React.JSX.Element {
  const [value, setValue] = React.useState<string | null>(null);

  const [editingValue, setEditingValue] = React.useState<
    string | null
  >(null);

  React.useEffect(() => {
    const storedValue = NativeLocalStorage?.getItem('myKey');
    setValue(storedValue ?? '');
  }, []);

  function saveValue() {
    NativeLocalStorage?.setItem(editingValue ?? EMPTY, 'myKey');
    setValue(editingValue);
  }

  function clearAll() {
    NativeLocalStorage?.clear();
    setValue('');
  }

  function deleteValue() {
    NativeLocalStorage?.removeItem(editingValue ?? EMPTY);
    setValue('');
  }

  return (
    <SafeAreaView style={{flex: 1}}>
      <Text style={styles.text}>
        Current stored value is: {value ?? 'No Value'}
      </Text>
      <TextInput
        placeholder="Enter the text you want to store"
        style={styles.textInput}
        onChangeText={setEditingValue}
      />
      <Button title="Save" onPress={saveValue} />
      <Button title="Delete" onPress={deleteValue} />
      <Button title="Clear" onPress={clearAll} />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  text: {
    margin: 10,
    fontSize: 20,
  },
  textInput: {
    margin: 10,
    height: 40,
    borderColor: 'black',
    borderWidth: 1,
    paddingLeft: 5,
    paddingRight: 5,
    borderRadius: 5,
  },
});

export default App;
```

#### Step 05 : Write your Native Platform code

```kotlin
// Create File this path : android/app/src/main/java/com/nativelocalstorage/NativeLocalStorageModule.kt

// past this code 
package com.nativelocalstorage

import android.content.Context
import android.content.SharedPreferences
import com.nativelocalstorage.NativeLocalStorageSpec
import com.facebook.react.bridge.ReactApplicationContext

class NativeLocalStorageModule(reactContext: ReactApplicationContext) : NativeLocalStorageSpec(reactContext) {

  override fun getName() = NAME

  override fun setItem(value: String, key: String) {
    val sharedPref = getReactApplicationContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    val editor = sharedPref.edit()
    editor.putString(key, value)
    editor.apply()
  }

  override fun getItem(key: String): String? {
    val sharedPref = getReactApplicationContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    val username = sharedPref.getString(key, null)
    return username.toString()
  }

  override fun removeItem(key: String) {
    val sharedPref = getReactApplicationContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    val editor = sharedPref.edit()
    editor.remove(key)
    editor.apply()
  }

  override fun clear() {
    val sharedPref = getReactApplicationContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    val editor = sharedPref.edit()
    editor.clear()
    editor.apply()
  }

  companion object {
    const val NAME = "NativeLocalStorage"
  }
}

// Create anther file this path : android/app/src/main/java/com/nativelocalstorage/NativeLocalStoragePackage.kt
// past this code 
package com.nativelocalstorage

import com.facebook.react.TurboReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider

class NativeLocalStoragePackage : TurboReactPackage() {

  override fun getModule(name: String, reactContext: ReactApplicationContext): NativeModule? =
    if (name == NativeLocalStorageModule.NAME) {
      NativeLocalStorageModule(reactContext)
    } else {
      null
    }

  override fun getReactModuleInfoProvider() = ReactModuleInfoProvider {
    mapOf(
      NativeLocalStorageModule.NAME to ReactModuleInfo(
        _name = NativeLocalStorageModule.NAME,
        _className = NativeLocalStorageModule.NAME,
        _canOverrideExistingModule = false,
        _needsEagerInit = false,
        isCxxModule = false,
        isTurboModule = true
      )
    )
  }
}
```

#### 06  : add module in MainApplication file 
```kotlin
import com.nativelocalstorage.NativeLocalStoragePackage // import our package 

 override val reactNativeHost: ReactNativeHost =
      object : DefaultReactNativeHost(this) {
        override fun getPackages(): List<ReactPackage> =
            PackageList(this).packages.apply {
              // Packages that cannot be autolinked yet can be added manually here, for example:
              // add(MyReactNativePackage())
              add(NativeLocalStoragePackage()) // add package  
            }

        override fun getJSMainModuleName(): String = "index"

        override fun getUseDeveloperSupport(): Boolean = BuildConfig.DEBUG

        override val isNewArchEnabled: Boolean = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
        override val isHermesEnabled: Boolean = BuildConfig.IS_HERMES_ENABLED
}


```

### Legacy Architecture 
[React Native Doc](https://reactnative.dev/docs/legacy/native-modules-android)

#### Step 01 : 
```ts
// create file like this path in root dir : legacyModule/tost.ts
// and write interface and register module

import { NativeModules } from 'react-native';
const { Toast } = NativeModules;

interface IToastModule { 
    nonPromiseFunction: (text: string) => void;
    promiseFunction: (eventName: string, location: string) => Promise<string>; 
    callbackFunction: (eventName: string, location: string, cb: (event:string)=> void) => void; 
    callbackFunctionWithError: (eventName: string, location: string, cb: (error: any, event:string)=> void) => void; 
    twoCallbackFunction: (eventName: string, location: string, rej: (event:string)=> void, res: (event:string)=> void, ) => void; 
    emitEvent: (value:string)=> void
}
export const ToastModule: IToastModule = Toast;

```

#### Step 02 : Write Application Code using the Legacy Native Module
```jsx
import { DeviceEventEmitter, Text, View } from 'react-native';
import React, { useEffect } from 'react';
import {  ToastModule } from '../legacyModule/tost';


export default function LegacyModule() {

    useEffect(() => {
        const subscription = DeviceEventEmitter.addListener('EventReminder', (event) => {
          console.log('Received event:', event);
        });

        // Clean up the event listener on component unmount
        return () => {
          subscription.remove();
        };
      }, []);

      const triggerNativeEvent = () => {
        // Call the emitEvent function from the native module
        ToastModule.emitEvent('Hello World');
      };



    const handleNonPromise = () => {
        ToastModule.nonPromiseFunction('hello LegacyModule');
    };

    const handlePromise = async () => {
     const res = await ToastModule.promiseFunction('hello', 'hello world');
     console.log(res);
    };

    const handleCallback = () => {
         ToastModule.callbackFunction('hello', 'hello world', (event)=> {
            console.log(event);
         });
    };

    const handleCallbackWithError = () => {
        ToastModule.callbackFunction('hello', 'hello world', (err, event)=> {
            if(err){
                 console.log(err);
                 return;
            }
           console.log(event);
        });
   };

     const handleTwoCallback = () => {
        ToastModule.twoCallbackFunction('hello', 'hello world', (error)=> {
            if(error){
                 console.log('error', error);
            }
        },
        (success)=> {
            console.log('success', success);
        }
    );
   };



  return (
    <View style={{
        justifyContent:'center',
        flex:1,
        alignItems:'center',
    }}>
    <Text
      onPress={handleNonPromise}
      style={{textAlign:'center'}}>
        Press LegacyModule Non Promise
    </Text>
    <Text
      onPress={handlePromise}
      style={{textAlign:'center'}}>
        Press LegacyModule Promise
    </Text>

    <Text
      onPress={handleCallback}
      style={{textAlign:'center'}}>
        Press LegacyModule Callback
    </Text>

    <Text
      onPress={handleCallbackWithError}
      style={{textAlign:'center'}}>
        Press LegacyModule Callback with error
    </Text>

    <Text
      onPress={handleTwoCallback}
      style={{textAlign:'center'}}>
        Press LegacyModule Two Callback
    </Text>


    <Text
      onPress={triggerNativeEvent}
      style={{textAlign:'center'}}>
        Emit Event
    </Text>
    </View>
  );
}
```

#### Step 03 : Create Custom Native Module File in 
``` kotlin
// crate kotlin file like this path : LearnAndroidWithReactNative/android/app/src/main/java/com/learnandroidwithreactnative


// create file like this name : ToastModule.kt
// and past this code 
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

// create anther file in this dir 
// and past this code 
package com.learnandroidwithreactnative
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager

class ToastPackage:ReactPackage {
    override fun createNativeModules(
        reactContext: ReactApplicationContext
    ): MutableList<NativeModule> = listOf(ToastModule(reactContext)).toMutableList()

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return listOf()
    }
} 
```


#### Step 04 : Register and add Module in MainApplication.kt
``` kotlin
// like this 
 override val reactNativeHost: ReactNativeHost =
      object : DefaultReactNativeHost(this) {
        override fun getPackages(): List<ReactPackage> =
            PackageList(this).packages.apply {
              // Packages that cannot be autolinked yet can be added manually here, for example:
              // add(MyReactNativePackage())
                add(NativeLocalStoragePackage()) // add turbo module
                add(ToastPackage()) // add legacy module
            }

        override fun getJSMainModuleName(): String = "index"

        override fun getUseDeveloperSupport(): Boolean = BuildConfig.DEBUG

        override val isNewArchEnabled: Boolean = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
        override val isHermesEnabled: Boolean = BuildConfig.IS_HERMES_ENABLED
}

```