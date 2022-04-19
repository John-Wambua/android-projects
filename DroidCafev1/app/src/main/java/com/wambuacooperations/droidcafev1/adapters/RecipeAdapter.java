package com.wambuacooperations.droidcafev1.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wambuacooperations.droidcafev1.DessertsActivity;
import com.wambuacooperations.droidcafev1.R;
import com.wambuacooperations.droidcafev1.models.Recipe;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    ArrayList<Recipe> recipeData;
     Context myContext;

    public RecipeAdapter(ArrayList<Recipe> mRecipeData, Context context){
        this.recipeData=mRecipeData;
        this.myContext=context;
    }


    @NonNull
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(myContext).inflate(R.layout.recipe_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, int position) {
        Recipe currentRecipe=recipeData.get(position);
        holder.bindTo(currentRecipe);
    }

    @Override
    public int getItemCount() {
        return recipeData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView myRecipeImage;
        private TextView myRecipeTitle;
        private TextView myRecipeDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            myRecipeImage=itemView.findViewById(R.id.recipe_image);
            myRecipeTitle=itemView.findViewById(R.id.recipe_title);
            myRecipeDescription=itemView.findViewById(R.id.recipe_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int dessertPosition=getAdapterPosition();
                    Recipe currentDessert=recipeData.get(dessertPosition);
                    Intent intent =new Intent(myContext, DessertsActivity.class);
                    intent.putExtra("title",currentDessert.getRecipeTitle());
                    intent.putExtra("image",currentDessert.getRecipeImage());
                    intent.putExtra("description",currentDessert.getRecipeDescription());
                    myContext.startActivity(intent);
                }
            });
        }

        public void bindTo(Recipe currentRecipe) {
            Glide.with(myContext)
                    .load(currentRecipe.getRecipeImage())
                    .fitCenter()
                    .into(myRecipeImage);
            myRecipeTitle.setText(currentRecipe.getRecipeTitle());
            myRecipeDescription.setText(currentRecipe.getRecipeDescription());

        }
    }
}
