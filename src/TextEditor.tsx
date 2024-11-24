import React from 'react';
import { requireNativeComponent, NativeModules, View, Text } from 'react-native';
const { RichEditorModule } = NativeModules;
const RichEditorView = requireNativeComponent("RichEditorView")


export default function TextEditor () { 

  const handleGetHtml = () => {
    RichEditorModule.getHtml();
  }   
  const handleSettHtml = () => {
    RichEditorModule.getHtml();
  } 


return (
  <View style={{flex:1}}>
    <RichEditorView style={{ flex: 1 }} html="<h1> Rezaul Karim <h1/>" placeholder="Wright something.."/>
    <Text  onPress={handleGetHtml}>get</Text>
    <Text  onPress={handleSettHtml}>set</Text>
  </View>
);
}