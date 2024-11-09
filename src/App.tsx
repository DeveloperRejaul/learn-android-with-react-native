import { View } from 'react-native';
import React from 'react';
import TurboModule from './TurboModule';
import LegacyModule from './LagacyModule';


export default function App() {
  return (
    <View style={{flex:1}}>
     <TurboModule/>
     <LegacyModule/>
    </View>
  );
}
