package com.wambuacooperations.thearttic.posts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wambuacooperations.thearttic.R;
import com.wambuacooperations.thearttic.auth.RegisterActivity;
import com.wambuacooperations.thearttic.models.Attic;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference likesRef;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    Boolean likeChecker=false;
    private FirebaseRecyclerAdapter adapter;
    String currentUserID=null;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        recyclerView=findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);;

        //Display most recent post at the top.
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        likesRef= FirebaseDatabase.getInstance().getReference().child("Likes");
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent loginIntent=new Intent(MainActivity.this, RegisterActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if (currentUser!=null){
            updateUI(currentUser);
            adapter.startListening();
        }
    }

    private void updateUI(FirebaseUser currentUser) {
        Query query=FirebaseDatabase.getInstance().getReference().child("Posts");
        FirebaseRecyclerOptions<Attic> options=new FirebaseRecyclerOptions.Builder<Attic>()
                .setQuery(query, new SnapshotParser<Attic>() {
                    @NonNull
                    @Override
                    public Attic parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new Attic(snapshot.child("title").getValue().toString(),
                                snapshot.child("desc").getValue().toString(),
                                snapshot.child("postImage").getValue().toString(),
                                snapshot.child("displayName").getValue().toString(),
                                snapshot.child("profilePhoto").getValue().toString(),
                                snapshot.child("time").getValue().toString(),
                                snapshot.child("date").getValue().toString());
                    }
                }).build();
        adapter=new FirebaseRecyclerAdapter<Attic, AtticViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AtticViewHolder holder, int i, @NonNull final Attic attic) {

                final String post_key=getRef(i).getKey();
                holder.setTitle(attic.getTitle());
                holder.setDesc(attic.getDesc());
                holder.setPostImage(getApplicationContext(),attic.getPostImage());
                holder.setUserName(attic.getDisplayName());
                holder.setProfilePhoto(getApplicationContext(),attic.getProfilePhoto());
                holder.setTime(attic.getTime());
                holder.setDate(attic.getDate());
                holder.setLikesButtonStatus(post_key);

                //To allow opening the post on a different screen
                holder.post_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Launch the single post activity on clicking a particular cardview item
                        Intent singleActivity=new Intent(MainActivity.this, SinglePostActivity.class);
                        singleActivity.putExtra("PostID",post_key);
                        startActivity(singleActivity);

                    }
                });
                holder.sharePostButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(MainActivity.this, "Share Post", Toast.LENGTH_SHORT).show();
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, attic.getPostImage());
                            shareIntent.setType("image/jpeg");
                            startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));

                    }
                });

                //set onclick listener on the button for liking a post
                holder.likePostButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        likeChecker=!likeChecker;
                        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                        if (user!=null){
                            currentUserID=user.getUid();
                        }else{
                            Toast.makeText(MainActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                        }
                        likesRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (likeChecker){
                                    //disliking post
                                    if (snapshot.child(post_key).hasChild(currentUserID)){
                                        likesRef.child(post_key).child(currentUserID).removeValue();
                                        likeChecker=false;
                                    }else {
                                        //liking post
                                        likesRef.child(post_key).child(currentUserID).setValue(true);
                                        likeChecker=true;
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }


            @NonNull
            @Override
            public AtticViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_items,parent,false);
                return new AtticViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();

        FirebaseUser currentUser=mAuth.getCurrentUser();
        if (currentUser!=null){
            adapter.stopListening();
        }
    }

    public class AtticViewHolder extends RecyclerView.ViewHolder{
        public TextView post_title;
        public TextView post_desc;
        public ImageView post_image;
        public TextView postUserName;
        public ImageView user_image;
        public TextView postTime;
        public TextView postDate;
        public LinearLayout post_layout;
        public ImageButton likePostButton,commentPostButton,sharePostButton;
        public TextView displayLikes;

        int countLikes;

        String currentUserID;

        DatabaseReference likesRef;

        public AtticViewHolder(@NonNull View itemView) {
            super(itemView);

            post_title=itemView.findViewById(R.id.post_title_txtview);
            post_desc=itemView.findViewById(R.id.post_desc_txtview);
            post_image=itemView.findViewById(R.id.post_image);
            postUserName=itemView.findViewById(R.id.post_user);
            user_image=itemView.findViewById(R.id.userImage);
            postTime=itemView.findViewById(R.id.time);
            postDate=itemView.findViewById(R.id.date);
            post_layout=itemView.findViewById(R.id.linear_layout_post);
            likePostButton=itemView.findViewById(R.id.like_button);
            commentPostButton=itemView.findViewById(R.id.comment);
            displayLikes=itemView.findViewById(R.id.likes_display);
            sharePostButton=itemView.findViewById(R.id.share_button);

            likesRef=FirebaseDatabase.getInstance().getReference().child("Likes");
        }

        public void setTitle(String title) {
            post_title.setText(title);
        }

        public void setDesc(String desc) {
            post_desc.setText(desc);
        }

        public void setPostImage(Context context, String postImage) {

            Glide.with(context).load(postImage).into(post_image);
        }

        public void setUserName(String userName) {
            postUserName.setText(userName);
        }

        public void setProfilePhoto(Context context,String profilePhoto) {
            Glide.with(context).load(profilePhoto).into(user_image);
        }

        public void setTime(String time) {
            postTime.setText(time);
        }

        public void setDate(String date) {
            postDate.setText(date);
        }
        public void setLikesButtonStatus(final String post_key){
            //To find out who has liked a particular post

            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
            if (user!=null){
                currentUserID=user.getUid();
            }else{
                Toast.makeText(MainActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
            }
            likesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(post_key).hasChild(currentUserID)){
                        countLikes= (int) snapshot.child(post_key).getChildrenCount();
                        likePostButton.setImageResource(R.drawable.like);
                        displayLikes.setText(Integer.toString(countLikes));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.action_settings:
                Log.i("Item selected","Settings");
                return true;

            case R.id.action_add:
            Intent postIntent=new Intent(this, PostActivity.class);
            startActivity(postIntent);
                return true;

            case R.id.logout:
                mAuth.signOut();
                Intent logoutIntent=new Intent(MainActivity.this,RegisterActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logoutIntent);

                return true;

            default:return false;
        }
    }
}
