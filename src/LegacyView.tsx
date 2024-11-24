import React from 'react';
import { View, StyleSheet } from 'react-native';
import TextView from '../legacyModule/CustomView';

export default function LegacyCustomTextView(){
    return (
        <View style={styles.container}>
            <TextView
              style={{width: "100%",height: 50,}}
              text="Hello from Kotlin!" 
              textColor="red"
              fontSize={30}
             />
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#f5f5f5',
    },
});
