package com.wambuacooperations.volleyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wambuacooperations.volleyapp.adapters.DevelopersAdapter;
import com.wambuacooperations.volleyapp.models.DeveloperList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String URL_DATA="https://api.github.com/search/users?q=language:java+location:nairobi";
    RecyclerView recyclerView;
    DevelopersAdapter developersAdapter;
    List<DeveloperList> developerLists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        developerLists=new ArrayList<>();

        loadUrlData();
    }

    private void loadUrlData() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("...Loading");
        progressDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray array=jsonObject.getJSONArray("items");
                            for (int i=0;i<array.length();i++){
                                JSONObject jo=array.getJSONObject(i);
                                DeveloperList developers=new DeveloperList(jo.getString("login"),jo.getString("avatar_url"),jo.getString("html_url"));
                                developerLists.add(developers);
                                developersAdapter=new DevelopersAdapter(developerLists,getApplicationContext());
                                recyclerView.setAdapter(developersAdapter);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);



    }
}
