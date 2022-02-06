package com.example.ict602_grpproject.ui.news;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NewsFragment extends Fragment {

    private NewsViewModel newsViewModel;
    private FragmentNewsBinding binding;

    ListView list;

    Geocoder geocoder;
    List<Address> addresses;
    String address, URL = "http://www.ict602.ml/getReports.php";
    RequestQueue getHazards;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);

        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        geocoder = new Geocoder(getContext(), Locale.getDefault());
        getHazards = Volley.newRequestQueue(getContext());

        makeRequest();

        return root;
    }

    public void makeRequest() {
        StringRequest send = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //addresses = geocoder.getFromLocation(latitude, longitude, 1);
                //address = addresses.get(0).getAddressLine(0);

                String[] maintitle = {
                        "Jalan Impian Emas 17","Jalan Kempas Lama",
                        "Jalan Mewah Ria 2","Jalan Besar",
                };

                String[] subtitle = {
                        "5 Feb 2022, 08:30 AM","5 Feb 2022, 09:40 PM",
                        "6 Feb 2022, 12:53 PM","6 Feb 2022, 01:55 PM",
                };

                Integer[] imgid = {
                        R.drawable.c1_roadobstructioncircle,R.drawable.c2_slipperyroadcircle,
                        R.drawable.c3_potholecircle,R.drawable.c4_trafficaccidentcircle,
                };

                ListViewNews adapter = new ListViewNews(getContext(), maintitle, subtitle, imgid);
                list = (ListView) binding.newsDashboard;
                list.setAdapter(adapter);

            }
        }, errorListener) {
            @Override
            protected Map<String, String> getParams () {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };
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