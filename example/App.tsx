import { StyleSheet, Text, View } from 'react-native';

import * as MultipleAlarms from 'multiplealarms';

export default function App() {
  return (
    <View style={styles.container}>
      <Text>{MultipleAlarms.hello()}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
