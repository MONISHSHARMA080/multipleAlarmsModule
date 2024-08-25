import MultipleAlarmsModule from './MultipleAlarmsModule';

export function hello(): string {
  return MultipleAlarmsModule.hello();
}

export function setAlarm(hour:Number,minutes:number,message:String,requestCode:number){
  return MultipleAlarmsModule.setAlarm(hour,minutes,message,requestCode)
}
