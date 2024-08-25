import { requireNativeViewManager } from 'expo-modules-core';
import * as React from 'react';

import { MultipleAlarmsViewProps } from './MultipleAlarms.types';

const NativeView: React.ComponentType<MultipleAlarmsViewProps> =
  requireNativeViewManager('MultipleAlarms');

export default function MultipleAlarmsView(props: MultipleAlarmsViewProps) {
  return <NativeView {...props} />;
}
