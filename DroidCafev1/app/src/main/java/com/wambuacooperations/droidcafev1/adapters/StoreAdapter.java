package com.wambuacooperations.droidcafev1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.wambuacooperations.droidcafev1.R;
import com.wambuacooperations.droidcafev1.models.Store;

import java.util.ArrayList;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {

    private ArrayList<Store> storeArrayList;
    private Context myContext;

    public StoreAdapter(ArrayList<Store> storeArrayList, Context context) {
        this.storeArrayList = storeArrayList;
        this.myContext=context;
    }

    @NonNull
    @Override
    public StoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(myContext).inflate(R.layout.stores_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull StoreAdapter.ViewHolder holder, int position) {
        Store currentStore=storeArrayList.get(position);
        holder.bindTo(currentStore);
    }

    @Override
    public int getItemCount() {
        return storeArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView storeImage;
        private TextView storeName;
        private TextView storeDescription;
        private RatingBar storeRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storeImage=itemView.findViewById(R.id.store_image);
            storeName=itemView.findViewById(R.id.store_name);
            storeDescription=itemView.findViewById(R.id.store_description);
            storeRating=itemView.findViewById(R.id.store_rating);
        }

        public void bindTo(Store currentStore) {
            Glide.with(myContext)
                    .load(currentStore.getStoreImage())
//                    .transform(new CircleCrop())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(storeImage);

            storeName.setText(currentStore.getStoreName());
            storeDescription.setText(HtmlCompat.fromHtml(currentStore.getStoreDescription(), HtmlCompat.FROM_HTML_MODE_LEGACY));
            storeRating.setRating(currentStore.getStoreRating());
        }
    }
}
