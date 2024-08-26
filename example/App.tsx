import { Button, StyleSheet, Text, View } from 'react-native';

import * as MultipleAlarms from 'multiplealarms';

export default function App() {
  return (
    <View style={styles.container}>
      <Button title='Set ---- Alarm' 
      onPress={()=>{
        let dateObj = new Date
        for (let i = 1; i < 3; i++) {
          MultipleAlarms.setAlarm(dateObj.getHours(),dateObj.getMinutes()+i,"*-----@>> "+String(i),i)
        }
      }}
       />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#999999',
    alignItems: 'center',
    justifyContent: 'center',
    padding:3,
    fontSize:60
  },
});
