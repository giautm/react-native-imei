import {
  NativeModules,
  Platform,
} from 'react-native';

const INFO = {
  HAS_NATIVE: false,
  IMEI: [],
  SIM1_READY: null,
  SIM2_READY: null,
};

if (Platform.OS === 'android' && NativeModules.SimInfoAndroid) {
  const { SimInfoAndroid } = NativeModules;
  INFO.HAS_NATIVE = true;
  INFO.IMEI = SimInfoAndroid.IMEI;
  INFO.SIM1_READY = SimInfoAndroid.SIM1_READY;
  INFO.SIM2_READY = SimInfoAndroid.SIM2_READY;
}

export default INFO;
