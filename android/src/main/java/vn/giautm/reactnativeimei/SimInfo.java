package vn.giautm.reactnativeimei;

/**
 * Created by giautm on 2/27/17.
 */

import java.lang.reflect.Method;

import android.content.Context;
import android.telephony.TelephonyManager;

public final class SimInfo {

  private static SimInfo simInfo;
  private String imsiSIM1;
  private String imsiSIM2;
  private boolean isSIM1Ready;
  private boolean isSIM2Ready;

  public String getImsiSIM1() {
    return imsiSIM1;
  }

  public String getImsiSIM2() {
    return imsiSIM2;
  }

  public boolean isSIM1Ready() {
    return isSIM1Ready;
  }

  public boolean isSIM2Ready() {
    return isSIM2Ready;
  }

  public boolean isDualSIM() {
    return imsiSIM2 != null;
  }

  private SimInfo() {
  }

  public static SimInfo getInstance(Context context) {
    if (simInfo == null) {
      simInfo = new SimInfo();

      TelephonyManager telephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));

      simInfo.imsiSIM1 = telephonyManager.getDeviceId();
      simInfo.imsiSIM2 = null;

      try {
        simInfo.imsiSIM1 = getDeviceIdBySlot(context, "getDeviceIdGemini", 0);
        simInfo.imsiSIM2 = getDeviceIdBySlot(context, "getDeviceIdGemini", 1);
      } catch (GeminiMethodNotFoundException e) {
        e.printStackTrace();

        try {
          simInfo.imsiSIM1 = getDeviceIdBySlot(context, "getDeviceId", 0);
          simInfo.imsiSIM2 = getDeviceIdBySlot(context, "getDeviceId", 1);
        } catch (GeminiMethodNotFoundException e1) {
          //Call here for next manufacturer's predicted method name if you wish
          e1.printStackTrace();
        }
      }

      simInfo.isSIM1Ready = telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
      simInfo.isSIM2Ready = false;

      try {
        simInfo.isSIM1Ready = getSIMStateBySlot(context, "getSimStateGemini", 0);
        simInfo.isSIM2Ready = getSIMStateBySlot(context, "getSimStateGemini", 1);
      } catch (GeminiMethodNotFoundException e) {
        e.printStackTrace();

        try {
          simInfo.isSIM1Ready = getSIMStateBySlot(context, "getSimState", 0);
          simInfo.isSIM2Ready = getSIMStateBySlot(context, "getSimState", 1);
        } catch (GeminiMethodNotFoundException e1) {
          //Call here for next manufacturer's predicted method name if you wish
          e1.printStackTrace();
        }
      }
    }

    return simInfo;
  }

  private static String getDeviceIdBySlot(Context context, String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {
    String imsi = null;

    TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    try {
      Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

      Class<?>[] parameter = new Class[1];
      parameter[0] = int.class;
      Method getSimID = telephonyClass.getMethod(predictedMethodName, parameter);

      Object[] obParameter = new Object[1];
      obParameter[0] = slotID;
      Object ob_phone = getSimID.invoke(telephony, obParameter);

      if (ob_phone != null) {
        imsi = ob_phone.toString();
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new GeminiMethodNotFoundException(predictedMethodName);
    }

    return imsi;
  }

  private static boolean getSIMStateBySlot(Context context, String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {
    boolean isReady = false;

    TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    try {
      Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

      Class<?>[] parameter = new Class[1];
      parameter[0] = int.class;
      Method getSimStateGemini = telephonyClass.getMethod(predictedMethodName, parameter);

      Object[] obParameter = new Object[1];
      obParameter[0] = slotID;
      Object ob_phone = getSimStateGemini.invoke(telephony, obParameter);

      if (ob_phone != null) {
        int simState = Integer.parseInt(ob_phone.toString());
        if (simState == TelephonyManager.SIM_STATE_READY) {
          isReady = true;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new GeminiMethodNotFoundException(predictedMethodName);
    }

    return isReady;
  }


  private static class GeminiMethodNotFoundException extends Exception {
    private static final long serialVersionUID = -996812356902545308L;

    public GeminiMethodNotFoundException(String info) {
      super(info);
    }
  }
}
