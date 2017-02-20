package de.fhg.fit.biomos.sensorplatform.restservices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by garagon on 17.02.2017.
 */
public class util {
    JSONArray array = new JSONArray();
    JSONObject obj2 = new JSONObject();

    JSONObject obj = new JSONObject();
 //   obj.put("status",array);

    Iterator<String> it = obj.keys();
   //     while(it.hasNext())

    {
        String keys = it.next();
        JSONObject innerJson = new JSONObject(obj.toString());
        JSONArray innerArray = innerJson.getJSONArray(keys);
        for (int i = 0; i < innerArray.length(); i++) {
            JSONObject innInnerObj = innerArray.getJSONObject(i);
            Iterator<String> InnerIterator = innInnerObj.keys();
            while (InnerIterator.hasNext()) {
                System.out.println("InnInnerObject value is :" + innInnerObj.get(InnerIterator.next()));


            }

        }
    }
}