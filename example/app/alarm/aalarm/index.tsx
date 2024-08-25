import * as MultipleAlarms from 'multiplealarms';
import { useEffect } from 'react';
import { Button, StyleSheet, Text, View, Linking } from 'react-native';
// import { Linking } from 'expo';

const AlarmScreen = () => {
  Linking.addEventListener('url', (event) => {
    
    console.log('Alarm triggered!; url ->',event);
  });

  return (
    <View style={{backgroundColor:'#cccff0', flex:1 }}  >
      <Text>AlarmAlarm----- screen</Text>
      <Button
        title="Set Alarm"
        onPress={() => {
          let dateObj = new Date();
          for (let i = 1; i < 3; i++) {
            MultipleAlarms.setAlarm(dateObj.getHours(), dateObj.getMinutes() + i, "*&#B*&B@", i);
            console.log(dateObj.getHours(),":", dateObj.getMinutes() + i, "--*&#B*&B@--", i);
            
          }
        }}
      />
    </View>
  );
};

export default AlarmScreen;