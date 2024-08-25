import * as React from 'react';

import { MultipleAlarmsViewProps } from './MultipleAlarms.types';

export default function MultipleAlarmsView(props: MultipleAlarmsViewProps) {
  return (
    <div>
      <span>{props.name}</span>
    </div>
  );
}
