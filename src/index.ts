import { NativeModulesProxy, EventEmitter, Subscription } from 'expo-modules-core';

// Import the native module. On web, it will be resolved to MultipleAlarms.web.ts
// and on native platforms to MultipleAlarms.ts
import MultipleAlarmsModule from './MultipleAlarmsModule';
import MultipleAlarmsView from './MultipleAlarmsView';
import { ChangeEventPayload, MultipleAlarmsViewProps } from './MultipleAlarms.types';

// Get the native constant value.
export const PI = MultipleAlarmsModule.PI;

export function hello(): string {
  return MultipleAlarmsModule.hello();
}

export async function setValueAsync(value: string) {
  return await MultipleAlarmsModule.setValueAsync(value);
}

const emitter = new EventEmitter(MultipleAlarmsModule ?? NativeModulesProxy.MultipleAlarms);

export function addChangeListener(listener: (event: ChangeEventPayload) => void): Subscription {
  return emitter.addListener<ChangeEventPayload>('onChange', listener);
}

export { MultipleAlarmsView, MultipleAlarmsViewProps, ChangeEventPayload };
