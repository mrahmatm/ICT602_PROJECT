package com.example.ict602_grpproject.ui.news;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ict602_grpproject.LoginPage;
import com.example.ict602_grpproject.MainActivity;
import com.example.ict602_grpproject.R;
import com.example.ict602_grpproject.databinding.FragmentNewsBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class NewsFragment extends Fragment {

    private NewsViewModel newsViewModel;
    private FragmentNewsBinding binding;

    ListView list;

    Geocoder geocoder;
    List<Address> addresses;
    String address, URL = "http://www.ict602.ml/getReports.php", nHazard, nTime;
    RequestQueue getHazards;

    Double lat, longn;
    SimpleDateFormat dateFormatIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat dateFormatOut = new SimpleDateFormat("d MMM yyyy hh:mm a");
    Date dateIn;

    ProgressBar progress;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);

        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        geocoder = new Geocoder(getContext(), Locale.getDefault());
        getHazards = Volley.newRequestQueue(getContext());
        progress = (ProgressBar) binding.newsProgressBar;

        ((Activity) getContext()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progress.setVisibility(View.VISIBLE);

        makeRequest();

        return root;
    }

    public void makeRequest() {
        StringRequest send = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    String[] subtitle = new String[jsonArray.length()];
                    String[] maintitle = new String[jsonArray.length()];
                    Integer[] imgid = new Integer[jsonArray.length()];

                    if(jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if (jsonObject.getString("Hazard").equals("Road Obstruction")) {
                                imgid[i] = R.drawable.c1_roadobstructioncircle;
                            }
                            else if (jsonObject.getString("Hazard").equals("Slippery Road")) {
                                imgid[i] = R.drawable.c2_slipperyroadcircle;
                            }
                            else if (jsonObject.getString("Hazard").equals("Dangerous Pothole")) {
                                imgid[i] = R.drawable.c3_potholecircle;
                            }
                            else if (jsonObject.getString("Hazard").equals("Accident")) {
                                imgid[i] = R.drawable.c4_trafficaccidentcircle;
                            }
                            else {
                                imgid[i] = R.drawable.c4_trafficaccidentcircle;
                            }

                            dateIn = dateFormatIn.parse(jsonObject.getString("Time"));
                            subtitle[i] = String.valueOf(dateFormatOut.format(dateIn));

                            lat = jsonObject.getDouble("Latitude");
                            longn = jsonObject.getDouble("Longitude");
                            addresses = geocoder.getFromLocation(lat, longn, 1);
                            //Log.d("Address " + jsonObject.getString("ReportID"), addresses.get(0).getAddressLine(0));
                            maintitle[i] = addresses.get(0).getAddressLine(0);
                        }
                    }

                    ListViewNews adapter = new ListViewNews(getContext(), maintitle, subtitle, imgid);
                    list = (ListView) binding.newsDashboard;
                    list.setAdapter(adapter);

                    ((Activity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    progress.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, errorListener);
        getHazards.add(send);
    }

    public Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}