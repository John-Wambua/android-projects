package com.wambuacooperations.droidcafev1.models;

public class Store {
    private int storeImage;
    private String storeName;
    private String StoreDescription;
    private float storeRating;

    public Store(int storeImage, String storeName, String storeDescription, float storeRating) {
        this.storeImage = storeImage;
        this.storeName = storeName;
        StoreDescription = storeDescription;
        this.storeRating = storeRating;
    }

    public int getStoreImage() {
        return storeImage;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreDescription() {
        return StoreDescription;
    }

    public float getStoreRating() {
        return storeRating;
    }
}
