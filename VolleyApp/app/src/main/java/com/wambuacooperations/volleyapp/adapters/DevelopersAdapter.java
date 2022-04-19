package com.wambuacooperations.volleyapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wambuacooperations.volleyapp.ProfileActivity;
import com.wambuacooperations.volleyapp.R;
import com.wambuacooperations.volleyapp.models.DeveloperList;

import java.util.List;

public class DevelopersAdapter extends RecyclerView.Adapter<DevelopersAdapter.ViewHolder> {

    public static final String KEY_NAME = "name";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_URL = "url";

    private List<DeveloperList> developerLists;
    private Context mContext;

    public DevelopersAdapter(List<DeveloperList> developerLists, Context mContext) {
        this.developerLists = developerLists;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public DevelopersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.developers_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DevelopersAdapter.ViewHolder holder, final int position) {
        DeveloperList currentDeveloper=developerLists.get(position);
        holder.login.setText(currentDeveloper.getLogin());
        holder.html_url.setText(currentDeveloper.getHtml_url());

        Glide.with(mContext)
                .load(currentDeveloper.getAvatar_url())
                .fitCenter()
                .into(holder.avatar_url);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeveloperList developerList1=developerLists.get(position);
                Intent intent=new Intent(v.getContext(), ProfileActivity.class);

                intent.putExtra(KEY_NAME,developerList1.getLogin());
                intent.putExtra(KEY_IMAGE,developerList1.getAvatar_url());
                intent.putExtra(KEY_URL,developerList1.getHtml_url());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return developerLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView login;
        public TextView html_url;
        public ImageView avatar_url;
        public LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            login=itemView.findViewById(R.id.username);
            html_url=itemView.findViewById(R.id.html_url);
            avatar_url=itemView.findViewById(R.id.avatar_image);
            linearLayout=itemView.findViewById(R.id.linearLayout);


        }
    }
}
