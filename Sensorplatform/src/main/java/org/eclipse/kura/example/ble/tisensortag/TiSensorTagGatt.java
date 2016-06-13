package org.eclipse.kura.example.ble.tisensortag;

import java.util.UUID;

public class TiSensorTagGatt {
	
	// These values are for TI CC2541 and TI CC2650
	// Refer to http://processors.wiki.ti.com/images/archive/a/a8/20130111154127!BLE_SensorTag_GATT_Server.pdf for the CC2541
	// and http://www.ti.com/ww/en/wireless_connectivity/sensortag2015/tearDown.html#main for the CC2560
	
	// Firmware revision
	public static final String HANDLE_FIRMWARE_REVISION_2541        = "0x0018";
	public static final String HANDLE_FIRMWARE_REVISION_2650        = "0x0014";
	
	// CC 2650
	public static final String HANDLE_TEMP_SENSOR_VALUE_2650		= "0x0021";
	public static final String HANDLE_TEMP_SENSOR_NOTIFICATION_2650	= "0x0022";
	public static final String HANDLE_TEMP_SENSOR_ENABLE_2650		= "0x0024";
	public static final String HANDLE_TEMP_SENSOR_PERIOD_2650		= "0x0026";
	
	public static final UUID UUID_TEMP_SENSOR_VALUE			    = UUID.fromString("f000aa01-0451-4000-b000-000000000000");
	public static final UUID UUID_TEMP_SENSOR_ENABLE		    = UUID.fromString("f000aa02-0451-4000-b000-000000000000");
	public static final UUID UUID_TEMP_SENSOR_PERIOD		    = UUID.fromString("f000aa03-0451-4000-b000-000000000000");
	
	// CC2650
	public static final String HANDLE_HUM_SENSOR_VALUE_2650			= "0x0029";
	public static final String HANDLE_HUM_SENSOR_NOTIFICATION_2650	= "0x002a";
	public static final String HANDLE_HUM_SENSOR_ENABLE_2650	    = "0x002c";
	public static final String HANDLE_HUM_SENSOR_PERIOD_2650	    = "0x002E";

	public static final UUID UUID_HUM_SENSOR_VALUE			    = UUID.fromString("f000aa21-0451-4000-b000-000000000000");
	public static final UUID UUID_HUM_SENSOR_ENABLE		        = UUID.fromString("f000aa22-0451-4000-b000-000000000000");
	public static final UUID UUID_HUM_SENSOR_PERIOD		        = UUID.fromString("f000aa23-0451-4000-b000-000000000000");
	
	// CC2650
	public static final String HANDLE_PRE_SENSOR_VALUE_2650     	= "0x0031";
	public static final String HANDLE_PRE_SENSOR_NOTIFICATION_2650	= "0x0032";
	public static final String HANDLE_PRE_SENSOR_ENABLE_2650		= "0x0034";
	public static final String HANDLE_PRE_SENSOR_PERIOD_2650		= "0x0036";
	
	public static final UUID UUID_PRE_SENSOR_VALUE			    = UUID.fromString("f000aa41-0451-4000-b000-000000000000");
	public static final UUID UUID_PRE_SENSOR_ENABLE		        = UUID.fromString("f000aa42-0451-4000-b000-000000000000");
	public static final UUID UUID_PRE_SENSOR_CALIBRATION        = UUID.fromString("f000aa43-0451-4000-b000-000000000000");
	public static final UUID UUID_PRE_SENSOR_PERIOD             = UUID.fromString("f000aa44-0451-4000-b000-000000000000");
	
	// CC2650
	public static final String HANDLE_KEYS_STATUS_2650    			= "0x0049";
	public static final String HANDLE_KEYS_NOTIFICATION_2650		= "0x004A";
	
	public static final UUID UUID_KEYS_STATUS   			    = UUID.fromString("f000ffe1-0451-4000-b000-000000000000");

	// Ambient Light sensor
	// CC2650
	public static final String HANDLE_OPTO_SENSOR_VALUE_2650		= "0x0041";
	public static final String HANDLE_OPTO_SENSOR_NOTIFICATION_2650	= "0x0042";
	public static final String HANDLE_OPTO_SENSOR_ENABLE_2650	    = "0x0044";
	public static final String HANDLE_OPTO_SENSOR_PERIOD_2650	    = "0x0046";
	
	public static final UUID UUID_OPTO_SENSOR_VALUE			    = UUID.fromString("f000aa71-0451-4000-b000-000000000000");
	public static final UUID UUID_OPTO_SENSOR_ENABLE		    = UUID.fromString("f000aa72-0451-4000-b000-000000000000");
	public static final UUID UUID_OPTO_SENSOR_PERIOD		    = UUID.fromString("f000aa73-0451-4000-b000-000000000000");
	
	// Movement sensor (accelerometer, gyroscope and magnetometer)
	// CC2560
	public static final String HANDLE_MOV_SENSOR_VALUE_2650			= "0x0039";
	public static final String HANDLE_MOV_SENSOR_NOTIFICATION_2650	= "0x003A";
	public static final String HANDLE_MOV_SENSOR_ENABLE_2650	    = "0x003C";
	public static final String HANDLE_MOV_SENSOR_PERIOD_2650	    = "0x003E";
	
	public static final UUID UUID_MOV_SENSOR_VALUE			    = UUID.fromString("f000aa81-0451-4000-b000-000000000000");
	public static final UUID UUID_MOV_SENSOR_ENABLE		        = UUID.fromString("f000aa82-0451-4000-b000-000000000000");
	public static final UUID UUID_MOV_SENSOR_PERIOD		        = UUID.fromString("f000aa83-0451-4000-b000-000000000000");	
}
