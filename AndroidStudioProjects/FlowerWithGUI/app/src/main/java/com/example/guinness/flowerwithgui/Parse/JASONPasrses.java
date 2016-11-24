package com.example.guinness.flowerwithgui.Parse;

import com.example.guinness.flowerwithgui.Model.DATANAME;
import com.example.guinness.flowerwithgui.Model.Flower;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guinness on 22/11/16.
 */

public class JASONPasrses {
    public static List<Flower> parseFeed(String content) {

        try {
            JSONArray ar = new JSONArray(content);
            List<Flower> flowerList = new ArrayList<>();

            for (int i = 0; i < ar.length(); i++) {

                JSONObject obj = ar.getJSONObject(i);
                Flower flower = new Flower();

                flower.setProudctID(obj.getInt(DATANAME.ProductID));
                flower.setName(obj.getString(DATANAME.NAME));
                flower.setCatagory(obj.getString(DATANAME.Category));
                flower.setInstrutions(obj.getString(DATANAME.Instruction));
                flower.setPhoto(obj.getString(DATANAME.Photo));
                flower.setPrice(obj.getDouble(DATANAME.Price));

                flowerList.add(flower);
            }

            return flowerList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
