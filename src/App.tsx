import { StyleSheet, Text, View } from 'react-native';
import React from 'react';
import TurboModule from './TurboModule';
import LegacyModule from './LegacyModule';
import TurboView from './TurboView';
import LegacyCustomTextView from './LegacyView';
import TextEditor from './TextEditor'

export default function App() {
  return (
    <View style={{flex:1}}>
     {/* <TurboModule/> */}
     {/* <LegacyModule/> */}
     {/* <TurboView/> */}
     {/* <LegacyCustomTextView/> */}
     <Text>Hello World</Text>
     <TextEditor/>
    </View>
  );
}

