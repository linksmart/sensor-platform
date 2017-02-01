package de.fhg.fit.biomos.sensorplatform.system;

import de.fhg.fit.biomos.sensorplatform.util.SignalQualityBean;

import java.io.IOException;

/**
 * Created by garagon on 15.12.2016.
 */
public class SurfstickCol implements Surfstick {

    private HardwarePlatform hwPlatform;

    public SurfstickCol() {
    }

    @Override
    public void setHardwarePlatform(HardwarePlatform hwPlatform) {
        this.hwPlatform = hwPlatform;
    }

    @Override
    public void setupSerialPort() throws IOException, InterruptedException {

    }

    @Override
    public int getOverallRSSI() {
        return 0;
    }

    @Override
    public SignalQualityBean getSQB() {
        return null;
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