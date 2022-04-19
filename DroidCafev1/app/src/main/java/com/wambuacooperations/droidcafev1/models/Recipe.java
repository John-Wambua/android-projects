package com.wambuacooperations.droidcafev1.models;

public class Recipe {
    private int recipeImage;
    private String recipeTitle;
    private String recipeDescription;

    public Recipe(int recipeImage, String recipeTitle, String recipeDescription){
        this.recipeImage=recipeImage;
        this.recipeTitle=recipeTitle;
        this.recipeDescription=recipeDescription;
    }

    public int getRecipeImage() {
        return recipeImage;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public String getRecipeDescription() {
        return recipeDescription;
    }
}
