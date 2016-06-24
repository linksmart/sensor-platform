// package de.fhg.fit.biomos.sensorplatform.tools;
//
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.regex.Matcher;
// import java.util.regex.Pattern;
//
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
//
// import de.fhg.fit.biomos.sensorplatform.util.AddressType;
// import de.fhg.fit.biomos.sensorplatform.util.SensorDescription;
// import net.sf.expectit.Expect;
// import net.sf.expectit.ExpectBuilder;
// import net.sf.expectit.matcher.Matchers;
//
/// **
// * @author Daniel Pyka
// */
// public class HcitoolImpl implements Hcitool {
//
// private static final Logger LOG = LoggerFactory.getLogger(HcitoolImpl.class);
//
// private static final String headerScan = "Scanning ...";
// private static final String headerLEscan = "LE Scan ...";
//
// // use online tester or QuickREx eclipse plugin for messing with regex
// private static final Pattern PATTERN_VERSION = Pattern.compile("(\\d\\.\\d+)");
// private static final Pattern PATTERN_DEVICE = Pattern.compile("(\\D{3}\\d{1})\\s");
// private static final Pattern PATTERN_BDADDRESS = Pattern.compile("(\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2})");
// private static final Pattern PATTERN_SCAN_BT_DEVICE = Pattern.compile("\\s+(\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2})\\s+(\\S.+)");
// private static final Pattern PATTERN_LESCAN_BT_DEVICE = Pattern.compile("(\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2})\\s(\\S.+)");
//
// // 00:23:4D:E8:21:8B Dell Wireless 370 Bluetooth Mini-card
// // 28:F0:76:25:E2:91 imac1511
// // EC:FE:7E:15:D5:EB FAROS-1620509
// // 11:AA:22:BB:33:CC (unknown)
// // FF:FF:FF:FF:FF:FF n/a
//
// // EC:FE:7E:15:D5:EB (unknown)
// // EC:FE:7E:15:D5:EB FAROS-1620509
// // D2:66:6F:08:A8:B5 (unknown)
// // D2:66:6F:08:A8:B5 One
//
// // sudo timeout -s SIGINT 5s hcitool -i hci0 lescan
// // sudo timeout 5 hcitool lescan BLOCKING -> IO Error
//
// private static final String SHELL = "/bin/sh";
// private static final String CMD_VERSION = "sudo hcitool";
// private static final String CMD_SCAN = "sudo hcitool scan";
// private static final String CMD_LESCAN = "sudo timeout -s SIGINT 5s hcitool lescan";
// private static final String CMD_DEVICE = "hcitool dev";
// private static final String EXIT = "exit";
//
// private Expect expect;
// private Process process;
// private String device = ""; // eg hci0
// private String bdAddress = ""; // eg B8:27:EB:26:A5:C4
// private String version = "";
//
// public HcitoolImpl() {
// try {
// this.process = Runtime.getRuntime().exec(SHELL);
// this.expect = new ExpectBuilder().withInputs(this.process.getInputStream()).withOutput(this.process.getOutputStream()).withExceptionOnFailure().build();
// } catch (IOException e) {
// this.process = null;
// this.expect = null;
// e.printStackTrace();
// }
// }
//
// public String getDevice() {
// return this.device;
// }
//
// public String getBdAddress() {
// return this.bdAddress;
// }
//
// public void exit() throws IOException, InterruptedException {
// this.expect.sendLine(EXIT);
// this.expect.expect(Matchers.eof());
// this.process.waitFor();
// this.expect.close();
// }
//
// @Override
// public void collectLocalDeviceAndAddress() throws IOException {
// this.expect.sendLine(CMD_DEVICE);
// this.device = this.expect.expect(Matchers.regexp(PATTERN_DEVICE)).group(1);
// this.bdAddress = this.expect.expect(Matchers.regexp(PATTERN_BDADDRESS)).group(1);
// LOG.info("Device is " + this.device);
// LOG.info("Bluetooth address is " + this.bdAddress);
// this.expect.sendLine(CMD_VERSION);
// this.version = this.expect.expect(Matchers.regexp(PATTERN_VERSION)).group(1);
// LOG.info("Bluez version is " + this.version);
// }
//
// @Override
// public List<SensorDescription> scan() throws IOException {
// final List<SensorDescription> result = new ArrayList<SensorDescription>();
// LOG.info("Bluetooth scan in progress... (blocking)");
// this.expect.sendLine(CMD_SCAN);
// this.expect.expect(Matchers.contains(headerScan));
// String rawOutput = this.expect.expect(Matchers.regexp("\n$")).getBefore();
// LOG.info("Bluetooth scan finished. Results:");
// Matcher matcher = PATTERN_SCAN_BT_DEVICE.matcher(rawOutput);
// System.out.println();
// while (matcher.find()) {
// SensorDescription sd = new SensorDescription(matcher.group(2), matcher.group(1), AddressType.PUBLIC);
// result.add(sd);
// System.out.println(sd);
// }
// System.out.println();
// return result;
// }
//
// @Override
// public List<SensorDescription> lescan(int duration) throws IOException {
// List<SensorDescription> result = new ArrayList<SensorDescription>();
// LOG.info("Bluetooth low energy scan in progress... (blocking)");
// this.expect.sendLine(CMD_LESCAN);
//
// // expect.expect(Matchers.contains("LE Scan ..."));
// String test1 = this.expect.expect(Matchers.anyString()).getBefore();
// System.out.println(test1);
// String test2 = this.expect.expect(Matchers.anyString()).getBefore();
// System.out.println(test2);
// String test3 = this.expect.expect(Matchers.anyString()).getBefore();
// System.out.println(test3);
//
// String rawOutput = this.expect.expect(Matchers.regexp("\n$")).getBefore();
// LOG.info("Bluetooth low energy scan finished. Results:");
// Matcher matcher = PATTERN_LESCAN_BT_DEVICE.matcher(rawOutput);
// System.out.println();
// while (matcher.find()) {
// SensorDescription sd = new SensorDescription(matcher.group(2), matcher.group(1), AddressType.PUBLIC);
// result.add(sd);
// System.out.println(sd);
// }
// System.out.println();
//
// return result;
// }
//
// }
