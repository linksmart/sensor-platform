package de.fhg.fit.biomos.sensorplatform.system;

import java.io.IOException;

import de.fhg.fit.biomos.sensorplatform.util.SignalQualityBean;

/**
 * Created by garagon on 15.12.2016.
 */
public interface Surfstick extends Runnable {

  public void setHardwarePlatform(HardwarePlatform hwPlatform);

  public String getConnectCommand();

  public void setupSerialPort() throws IOException, InterruptedException;

  public int getOverallRSSI();

  public SignalQualityBean getSQB();

  public void queryCSNR();

  public void querySYSINFO();

  public boolean isAttached();

  public boolean isRunning();

  public int getConnectionDuration();

  public float getMeasuredUploadSpeed_bps();

  public float getMeasuredDownloadSpeed_bps();

  public long getTotalSentData();

  public long getTotalReceivedData();

  public int getMaxUploadSpeed_pbs();

  public int getMaxDownloadSpeed_bbs();

}
