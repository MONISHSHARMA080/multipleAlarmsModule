import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { Linking } from 'expo';
import AlarmScreen from './alarm';

const Stack = createNativeStackNavigator();

const App = () => {
  return (
      <Stack.Navigator>
        <Stack.Screen name="alarm" component={AlarmScreen} />
        {/* Add other routes here */}
      </Stack.Navigator>
  );
};

export default App;