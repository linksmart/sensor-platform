package de.fhg.fit.biomos.sensorplatform.system;

import de.fhg.fit.biomos.sensorplatform.util.SignalQualityBean;

import java.io.IOException;

/**
 * Created by garagon on 15.12.2016.
 */
public class SurfstickCol implements Surfstick {

    private HardwarePlatform hwPlatform;
    
    private static final String SAKIS3G = "/home/administrator/3g/sakis3g SIM\\_PIN=\"1983\" --sudo \"connect\"";

    public SurfstickCol() {
    }

    @Override
    public void setHardwarePlatform(HardwarePlatform hwPlatform) {
        this.hwPlatform = hwPlatform;
    }

    @Override
    public String getConnectCommand() {
      return SAKIS3G;
    }
    
    @Override
    public void setupSerialPort() throws IOException, InterruptedException {

    }

    @Override
    public int getOverallRSSI() {
        return -113;
    }

    @Override
    public SignalQualityBean getSQB() {
        return new SignalQualityBean(-145,-32);
    }

    @Override
    public void queryCSNR() {

    }

    @Override
    public void querySYSINFO() {

    }

    @Override
    public boolean isAttached() {
        return false;
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public int getConnectionDuration() {
        return 0;
    }

    @Override
    public float getMeasuredUploadSpeed_bps() {
        return 0;
    }

    @Override
    public float getMeasuredDownloadSpeed_bps() {
        return 0;
    }

    @Override
    public long getTotalSentData() {
        return 0;
    }

    @Override
    public long getTotalReceivedData() {
        return 0;
    }

    @Override
    public int getMaxUploadSpeed_pbs() {
        return 0;
    }

    @Override
    public int getMaxDownloadSpeed_bbs() {
        return 0;
    }

    @Override
    public void run() {

    }
}
