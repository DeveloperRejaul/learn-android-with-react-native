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
        ToastModule.emitEvent("Hello World");
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
                 console.log(err)
                 return 
            };
           console.log(event);
        });
   };

     const handleTwoCallback = () => {
        ToastModule.twoCallbackFunction('hello', 'hello world', (error)=> {
            if(error){
                 console.log("error", error);
            };
        },
        (success)=> {
            console.log("success", success);
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
