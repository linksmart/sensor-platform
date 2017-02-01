package de.fhg.fit.biomos.sensorplatform.system;

import de.fhg.fit.biomos.sensorplatform.util.HuaweiUtils;
import de.fhg.fit.biomos.sensorplatform.util.SignalQualityBean;

import java.io.IOException;

/**
 * Created by garagon on 15.12.2016.
 */
public interface Surfstick extends Runnable{

    public void setHardwarePlatform(HardwarePlatform hwPlatform);

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
