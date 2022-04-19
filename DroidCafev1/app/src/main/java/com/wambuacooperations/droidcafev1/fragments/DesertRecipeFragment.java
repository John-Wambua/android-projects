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


public class DesertRecipeFragment extends Fragment {
    private RecyclerView dessertRecyclerView;
    private ArrayList<Recipe> dessertRecipeData;
    private RecipeAdapter dessertAdapter;

    public DesertRecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_desert_recipe, container, false);

        dessertRecyclerView=rootView.findViewById(R.id.recyclerDessert);
        dessertRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dessertRecipeData=new ArrayList<>();
        dessertAdapter=new RecipeAdapter(dessertRecipeData,getContext());
        dessertRecyclerView.setAdapter(dessertAdapter);
        initializeData();

        ItemTouchHelper helper=new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.RIGHT| ItemTouchHelper.LEFT | ItemTouchHelper.DOWN | ItemTouchHelper.UP,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        final int fromPos = viewHolder.getAdapterPosition();
                        final int toPos = target.getAdapterPosition();
                        // move item in `fromPos` to `toPos` in adapter.
                        Collections.swap(dessertRecipeData,fromPos,toPos);
                        dessertAdapter.notifyDataSetChanged();
                        return true;// true if moved, false otherwise
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        //remove card from adapter
                        dessertRecipeData.remove(viewHolder.getAdapterPosition());
                        dessertAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    }
                }
        );
        helper.attachToRecyclerView(dessertRecyclerView);

        return rootView;
    }

    private void initializeData() {
        String[] dessertTitles=getResources().getStringArray(R.array.dessert_title);
        String[] dessertDescription=getResources().getStringArray(R.array.dessert_description);
        TypedArray dessertImages=getResources().obtainTypedArray(R.array.dessert_images);

        //clear any existing data in the arraylist
        dessertRecipeData.clear();

        for(int i=0;i<dessertTitles.length;i++){
            dessertRecipeData.add(new Recipe(dessertImages.getResourceId(i,0),dessertTitles[i],dessertDescription[i]));

        }
        dessertImages.recycle();
        dessertAdapter.notifyDataSetChanged();
    }
}
