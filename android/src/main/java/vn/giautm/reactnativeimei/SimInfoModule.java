package vn.giautm.reactnativeimei;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by giautm on 2/27/17.
 */

public class SimInfoModule extends ReactContextBaseJavaModule {
  private static final String IMEI_KEY = "IMEI";
  private static final String SIM1_READY_KEY = "SIM1_READY";
  private static final String SIM2_READY_KEY = "SIM2_READY";

  private final SimInfo simInfo;

  public SimInfoModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.simInfo = SimInfo.getInstance(reactContext);
  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();

    List<String> imei = new ArrayList<>();
    imei.add(simInfo.getImsiSIM1());
    if (simInfo.isDualSIM()) {
      imei.add(simInfo.getImsiSIM2());
    }

    constants.put(IMEI_KEY, imei);
    constants.put(SIM1_READY_KEY, simInfo.isSIM1Ready());
    constants.put(SIM2_READY_KEY, simInfo.isSIM2Ready());

    return constants;
  }

  @Override
  public String getName() {
    return "SimInfoAndroid";
  }
}
