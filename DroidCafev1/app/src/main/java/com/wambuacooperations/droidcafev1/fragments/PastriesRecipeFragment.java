package com.wambuacooperations.droidcafev1.fragments;

import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wambuacooperations.droidcafev1.R;
import com.wambuacooperations.droidcafev1.adapters.RecipeAdapter;
import com.wambuacooperations.droidcafev1.models.Recipe;

import java.util.ArrayList;
import java.util.Collections;


public class PastriesRecipeFragment extends Fragment {

    private RecyclerView pastriesRecyclerView;
    private ArrayList<Recipe> pastriesRecipeData;
    private RecipeAdapter dessertAdapter;

    public PastriesRecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_pastries_recipe, container, false);

        pastriesRecyclerView=view.findViewById(R.id.recyclerPastries);
        pastriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pastriesRecipeData=new ArrayList<>();
        dessertAdapter=new RecipeAdapter(pastriesRecipeData,getContext());
        pastriesRecyclerView.setAdapter(dessertAdapter);

        loadData();

        ItemTouchHelper helper=new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.RIGHT| ItemTouchHelper.LEFT | ItemTouchHelper.DOWN | ItemTouchHelper.UP,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        final int fromPos = viewHolder.getAdapterPosition();
                        final int toPos = target.getAdapterPosition();
                        // move item in `fromPos` to `toPos` in adapter.
                        Collections.swap(pastriesRecipeData,fromPos,toPos);
                        dessertAdapter.notifyDataSetChanged();
                        return true;// true if moved, false otherwise
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        //remove card from adapter
                        pastriesRecipeData.remove(viewHolder.getAdapterPosition());
                        dessertAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    }
                }
        );
        helper.attachToRecyclerView(pastriesRecyclerView);


        return view;
    }

    private void loadData() {
        String[] pastriesTitles=getResources().getStringArray(R.array.pastries_title);
        String[] pastriesDescription=getResources().getStringArray(R.array.pastries_description);
        TypedArray pastriesImages=getResources().obtainTypedArray(R.array.pastries_images);

        //clear any existing data in the arraylist
        pastriesRecipeData.clear();

        for(int i=0;i<pastriesTitles.length;i++){
            pastriesRecipeData.add(new Recipe(pastriesImages.getResourceId(i,0),pastriesTitles[i],pastriesDescription[i]));

        }
        pastriesImages.recycle();
        dessertAdapter.notifyDataSetChanged();
    }

}
