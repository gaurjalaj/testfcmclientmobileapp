import React from "react"
import { Text, View } from "react-native"

export const App = () => {
  return (
    <View style={{backgroundColor: '#000', width: '100%', height: '100%', display: 'flex', justifyContent: 'center', alignItems: 'center'}}>
      <Text style={{color : '#fff', fontSize: 60, width: '70%'}}>FCM POC All States</Text>
    </View>
  )
}