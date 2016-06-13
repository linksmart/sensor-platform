//package de.fhg.fit.biomos.sensorplatform.deprecated;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.ScheduledFuture;
//
//import org.eclipse.kura.bluetooth.BluetoothAdapter;
//import org.eclipse.kura.bluetooth.BluetoothDevice;
//import org.eclipse.kura.bluetooth.BluetoothGatt;
//import org.eclipse.kura.bluetooth.BluetoothGattService;
//import org.eclipse.kura.bluetooth.BluetoothLeScanListener;
//import org.eclipse.kura.bluetooth.BluetoothService;
//import org.eclipse.kura.linux.bluetooth.BluetoothDeviceImpl;
//import org.eclipse.kura.linux.bluetooth.BluetoothServiceImpl;
//import org.eclipse.kura.linux.bluetooth.le.BluetoothGattImpl;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import de.fhg.fit.biomos.sensorplatform.data.Measurements;
//
///**
// * @author Daniel Pyka
// */
//public class Crap implements BluetoothLeScanListener {
//
//  private static BluetoothService btService = new BluetoothServiceImpl();
//  private static BluetoothAdapter btAdapter;
//  private static BluetoothDeviceImpl btDevice = null; // do not use!!!
//  private static List<BluetoothGattService> gatts;
//  private static ScheduledExecutorService m_worker;
//  private static ScheduledFuture<?> m_handle;
//
//  private static int m_period = 10;
//  private static int m_scantime = 5;
//  private static long m_startTime = 0;
//
//  private static final Logger LOG = LoggerFactory.getLogger(Crap.class);
//
//  public static void main(String[] args) throws IOException, InterruptedException {
//    
//    //Kategorie -> Partnumber:Measurements
//    
//    Measurements m1 = new Measurements("FarosEmotion360", "EC:FE:7E:15:D5:EB");
//    Measurements m2 = new Measurements("TempSensor", "11:22:33:44:55:66");
//    
//    HashMap<String, LinkedHashMap<Integer, Measurements>> test = new HashMap<String, LinkedHashMap<Integer,Measurements>>();
//    test.put("HRM", new LinkedHashMap<Integer, Measurements>());
//    test.put("Temp", new LinkedHashMap<Integer, Measurements>());
//    
//    m1.addSampleToList("12345");
//    m1.addSampleToList("43646");
//    m1.addSampleToList("21341");
//    Thread.sleep(1000);
//    m1.addSampleToList("54665");
//    m1.addSampleToList("78585");
//    m1.addSampleToList("99999");
//    
//    m2.addSampleToList("35");
//    m2.addSampleToList("37");
//    m2.addSampleToList("38");
//    Thread.sleep(1000);
//    m2.addSampleToList("39");
//    m2.addSampleToList("36");
//    m2.addSampleToList("33");
//    
//    test.get("HRM").put(new Integer(1), m1);
//    test.get("Temp").put(new Integer(1), m2);
//    
//    
//    System.out.println(test.get("HRM").get(1));
//    System.out.println(test.get("Temp").get(1));
//    
//    // Main main = new Main();
//    // main.activate();
//    // main.test();
//    // main.deactivate();
//    // Thread.sleep(20000);
//    // main.deactivate();
//  }
//
//  public Crap() {
//    //
//  }
//
//  private void test() {
//    // BluetoothConnector btConnector = btDevice.getBluetoothConnector();
//    BluetoothGatt btGatt = new BluetoothGattImpl("EC:FE:7E:15:D5:EB");
//    btGatt.connect();
//    btGatt.getServices();
//    btGatt.disconnect();
//  }
//
//  private void activate() {
//    m_worker = Executors.newSingleThreadScheduledExecutor();
//    btAdapter = btService.getBluetoothAdapter("hci0");
//    if (btAdapter != null) {
//      LOG.info("Bluetooth adapter address => " + btAdapter.getAddress());
//      LOG.info("Bluetooth adapter le enabled => " + btAdapter.isLeReady());
//
//      if (!btAdapter.isEnabled()) {
//        LOG.info("Enabling bluetooth adapter...");
//        btAdapter.enable();
//        LOG.info("Bluetooth adapter address => " + btAdapter.getAddress());
//      }
//      checkScan();
//    }
//  }
//
//  private void checkScan() {
//    if (btAdapter.isScanning()) {
//      LOG.info("m_bluetoothAdapter.isScanning");
//      if ((System.currentTimeMillis() - m_startTime) >= (m_scantime * 1000)) {
//        btAdapter.killLeScan();
//      }
//    } else {
//      if ((System.currentTimeMillis() - m_startTime) >= (m_period * 1000)) {
//        LOG.info("startLeScan");
//        btAdapter.startLeScan(this);
//        m_startTime = System.currentTimeMillis();
//      }
//    }
//
//  }
//
//  private void deactivate() {
//    LOG.debug("Deactivating BluetoothLe...");
//    if (btAdapter.isScanning()) {
//      LOG.debug("m_bluetoothAdapter.isScanning");
//      btAdapter.killLeScan();
//    }
//    // cancel a current worker handle if one if active
//    if (m_handle != null) {
//      m_handle.cancel(true);
//    }
//
//    // shutting down the worker and cleaning up the properties
//    m_worker.shutdown();
//
//    // cancel bluetoothAdapter
//    btAdapter = null;
//
//    LOG.debug("Deactivating BluetoothLe... Done.");
//  }
//
//  @Override
//  public void onScanFailed(int errorCode) {
//    LOG.error("Error during scan");
//  }
//
//  @Override
//  public void onScanResults(List<BluetoothDevice> scanResults) {
//    String name = "SensorTag";
//    for (BluetoothDevice bluetoothDevice : scanResults) {
//      LOG.info("Address " + bluetoothDevice.getAdress() + " Name " + bluetoothDevice.getName());
//
//      if (bluetoothDevice.getName().contains(name)) {
//        LOG.info(name + " " + bluetoothDevice.getAdress() + " found.");
//      } else {
//        LOG.info("Found device = " + bluetoothDevice.getAdress());
//      }
//    }
//
//  }
//}
