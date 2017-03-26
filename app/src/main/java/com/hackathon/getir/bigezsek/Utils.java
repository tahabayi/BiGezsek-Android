package com.hackathon.getir.bigezsek;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by taha on 25/03/17.
 */

public class Utils {

    private static final String TAG = "Utils";
    public static  final ArrayList<Place> places = new ArrayList<>();

    public static void loadProfiles(final Context context, final SwipePlaceHolderView mSwipeView, final String location, final String date){
        try{

            RequestQueue queue = Volley.newRequestQueue(context);
            String url ="http://bigezsek-backend.herokuapp.com/getUsers?place="+location+"&date="+date+"&hour=aksam&id=2";
            Log.d("asd",location);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            GsonBuilder builder = new GsonBuilder();
                            Gson gson = builder.create();
                            JSONArray array = null;
                            try {
                                array = new JSONArray(response);
                                for(int i=0;i<array.length();i++){
                                    Profile profile = gson.fromJson(array.getString(i), Profile.class);
                                    mSwipeView.addView(new Card(context, profile, mSwipeView));
                                }
                                if(array.length()==0){
                                    Toast.makeText(context,"There is nobody wanna go here!",Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(stringRequest);

        }catch (Exception e){
        }
    }


    public static void loadPlaces(final Context context, final GoogleMap map, final double lat , final double lng, final double radius ){

        try{
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = "http://bigezsek-backend.herokuapp.com/getNearbyPlaces?location=" + lat + "," + lng + "&radius=" + radius;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            GsonBuilder builder = new GsonBuilder();
                            Gson gson = builder.create();
                            JSONArray array = null;
                            try {
                                array = new JSONArray(response);
                                for(int i=0;i<array.length();i++){
                                    Place p = gson.fromJson(array.getString(i), Place.class);
                                    map.addMarker(new MarkerOptions()
                                            .position(new LatLng(p.getLat(), p.getLng()))
                                            .title(p.getName()));

                                }
                                if(array.length()==0){
                                    Toast.makeText(context,"There is nobody wanna go here!",Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(stringRequest);

        }catch (Exception e){
        }
    }



}