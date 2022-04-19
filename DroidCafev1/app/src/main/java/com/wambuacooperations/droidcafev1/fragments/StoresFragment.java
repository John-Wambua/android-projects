package com.wambuacooperations.droidcafev1.fragments;

import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wambuacooperations.droidcafev1.R;
import com.wambuacooperations.droidcafev1.adapters.StoreAdapter;
import com.wambuacooperations.droidcafev1.models.Store;

import java.util.ArrayList;


public class StoresFragment extends Fragment {
    private RecyclerView storeRecyclerView;
    private ArrayList<Store> storeArrayList;
    private StoreAdapter storeAdapter;

    public StoresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_stores, container, false);

        storeRecyclerView=view.findViewById(R.id.recyclerStores);
        storeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        storeArrayList=new ArrayList<>();
        storeAdapter=new StoreAdapter(storeArrayList,getContext());
        storeRecyclerView.setAdapter(storeAdapter);

        loadData();

        return view;
    }

    private void loadData() {
        TypedArray storeImages=getResources().obtainTypedArray(R.array.store_images);
        String[] storeNames=getResources().getStringArray(R.array.store_names);
        String[] storeDescriptions=getResources().getStringArray(R.array.store_description);
        String[] storeRatings=getResources().getStringArray(R.array.store_ratings);


        storeArrayList.clear();

        for (int i=0;i<storeImages.length();i++){
            storeArrayList.add(new Store(storeImages.getResourceId(i,0),storeNames[i],storeDescriptions[i],Float.parseFloat(storeRatings[i])));
        }
        storeImages.recycle();
        storeAdapter.notifyDataSetChanged();
    }
}
