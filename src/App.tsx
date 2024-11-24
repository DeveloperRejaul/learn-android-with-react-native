import { StyleSheet, View } from 'react-native';
import React from 'react';
import TurboModule from './TurboModule';
import LegacyModule from './LegacyModule';
import TurboView from './TurboView';
import LegacyCustomTextView from './LegacyView';
import XmlCustomFragmentView from './XmlView';

export default function App() {
  return (
    <View style={{flex:1}}>
     {/* <TurboModule/> */}
     {/* <LegacyModule/> */}
     {/* <TurboView/> */}
     {/* <LegacyCustomTextView/> */}
     <XmlCustomFragmentView/>
    </View>
  );
}

