import { Link, Stack } from 'expo-router';
import { useEffect } from 'react';
import { Linking } from 'react-native';

export default function Layout() {
  useEffect(() => {
    // Handle deep links
    const handleDeepLink = ({url}: {url: string}) => {
      // You can add custom logic here if needed
      console.log('Received deep link:', url);
    };
    
     <Link href={''}  />
    // Add event listener for deep links
    Linking.addEventListener('url', handleDeepLink);

    // Check for initial URL
    Linking.getInitialURL().then((url) => {
      if (url) {
        console.log('Initial URL:', url);
        // Handle the initial URL if needed
      }
    });

    // Cleanup
   
  }, []);

  return <Stack />;
}