package de.fhg.fit.biomos.sensorplatform.sensor;

import de.fhg.fit.biomos.sensorplatform.util.FloatUtils;

import static de.fhg.fit.biomos.sensorplatform.util.FloatUtils.fromHexString;


/**
 * Created by garagon on 08.12.2016.
 */



public class Main {
   // String value=3 2 3 1 2 e 3 0 3 7 2  5  2  b  3  2  3  4  2  e  3  3  5  4  3  1  3  0  3  1  3  0  5  0
    //String value=0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33

 //00 02 00 08 03 25 02 03 06 50 01 00 01 00 00


    public static void main1(String[] args) {
        String hex = "323130";
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hex.length(); i+=2) {
            String str = hex.substring(i, i+2);
            output.append((char)Integer.parseInt(str, 16));
        }
        System.out.println(output.toString());
        System.out.println("Que pasa?");
    }



    public static void main(String[] args) {
        float respTemp, respOxy, respPress, res1, res2, res3;
        String value="20.64%+24.1T1010P";
        String value1="0002000803%020306T01000100";
        String value2="303139353425323437543130303850";
        String prueba, prueba1, prueba2, prueba10,prueba11,prueba12,prueba13;
        int prueba4,prueba5;
        float prueba6;
        byte[] array={0x32,0x31,0x2e,0x30,0x37,0x25,0x2b,0x32,0x34,0x2e,0x33,0x54,0x31,0x30,0x31,0x30,0x50};

        respTemp=FloatUtils.valueTemp(value);
        respOxy=FloatUtils.valueOxyg(value);
        respPress=FloatUtils.valuePress(value);

       // return ((short) Integer.parseInt(data.substring(18, 20) + data.substring(16, 18), 16)) * 1.0f / (32768 / ACCELERATION_RESOLUTION);

        prueba=value1.substring(0,10);
        prueba1=value1.substring(0,10);
        prueba4=(int)Long.parseLong(value1.substring(0,10));
        prueba1=Integer.toHexString(22);
        prueba1=FloatUtils.toHexString(array);
        prueba12=FloatUtils.fromHexString(value2).substring(0,5);
        prueba6= Integer.parseInt(FloatUtils.fromHexString(value2).substring(10,14))*1f;
        System.out.printf("Test11 : %.2f\r\n",prueba6);
        System.out.println("Test11: "+prueba6);


        value1.substring(0,1);


        prueba10=Byte.toString(array[0]);
        System.out.println("Test10: "+prueba10);
        //res1=FloatUtils.valueOxyg(array);
        System.out.println("Test: "+prueba);
        System.out.println("Test1: "+prueba1);
       System.out.println("Test4: "+prueba4);

        System.out.println("Respuesta Oxy: "+respOxy);
        System.out.println("Respuesta Temp: "+respTemp);
        System.out.println("Respuesta Press: "+respPress);

    }



}
