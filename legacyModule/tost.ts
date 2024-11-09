import { NativeModules } from 'react-native';
const { Toast } = NativeModules;

interface IToastModule { 
    nonPromiseFunction: (text: string) => void;
    promiseFunction: (eventName: string, location: string) => Promise<string>; 
    callbackFunction: (eventName: string, location: string, cb: (event:string)=> void) => void; 
    callbackFunctionWithError: (eventName: string, location: string, cb: (error: any, event:string)=> void) => void; 
    twoCallbackFunction: (eventName: string, location: string, rej: (event:string)=> void, res: (event:string)=> void, ) => void; 
    emitEvent: (value:string)=> void
}
export const ToastModule: IToastModule = Toast;