package com.example.orderflowersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.orderflowersapp.Interface.ItemClickListener;
import com.example.orderflowersapp.Model.Flower;
import com.example.orderflowersapp.ViewHolder.FlowerViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FlowerList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference flowersList;

    String categoryId ="";
    FirebaseRecyclerAdapter<Flower, FlowerViewHolder> adapter;
    //Search Function
    FirebaseRecyclerAdapter<Flower, FlowerViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower_list);

        //Firebase
        database = FirebaseDatabase.getInstance();
        flowersList = database.getReference("Flowers");

        recyclerView = findViewById(R.id.recycler_flower);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Get Intent
        if(getIntent()!= null){
            categoryId = getIntent().getStringExtra("CategoryId");
        }
        if(!categoryId.isEmpty() && categoryId!=null){
            loadFlowerList(categoryId);
        }
        //Search
        materialSearchBar = findViewById(R.id.searchBar);
        materialSearchBar.setHint("Search for flower...");


        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Change suggest list while typing
                List<String> suggest = new ArrayList<String>();
                for (String search:suggestList){
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //When search bar is close
                if(!enabled)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                    startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<Flower, FlowerViewHolder>(
                Flower.class,
                R.layout.flower_item,
                FlowerViewHolder.class,
                flowersList.orderByChild("Name").equalTo(text.toString())
        ) {
            @Override
            protected void populateViewHolder(FlowerViewHolder flowerViewHolder, Flower flower, int i) {

                flowerViewHolder.flower_name.setText(flower.getName());
                Picasso.with(getBaseContext()).load(flower.getImage())
                        .into(flowerViewHolder.flower_image);

                final Flower local = flower;
                flowerViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void OnClick(View view, int position, boolean isLongClick) {
                        //Open detail of flower item
                        Intent flowerDetail = new Intent(FlowerList.this, FlowerDetail.class);
                        flowerDetail.putExtra("FlowerId",searchAdapter.getRef(position).getKey());
                        startActivity(flowerDetail);
                    }
                });
            }
        };
        recyclerView.setAdapter(searchAdapter);
    }

    private void loadSuggest() {
        flowersList.orderByChild("MenuId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                            Flower item = postSnapshot.getValue(Flower.class);
                            suggestList.add(item.getName()); //Add name of flower to suggest list
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void loadFlowerList(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Flower, FlowerViewHolder>(Flower.class,
                R.layout.flower_item,
                FlowerViewHolder.class,
                flowersList.orderByChild("MenuId").equalTo(categoryId))//like Select * From Flowers where MenuId=categoryId
        {
            @Override
            protected void populateViewHolder(FlowerViewHolder flowerViewHolder, Flower flower, int i) {
                flowerViewHolder.flower_name.setText(flower.getName());
                Picasso.with(getBaseContext()).load(flower.getImage())
                        .into(flowerViewHolder.flower_image);

                final Flower local = flower;
                flowerViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void OnClick(View view, int position, boolean isLongClick) {
                       //Open detail of flower item
                        Intent flowerDetail = new Intent(FlowerList.this, FlowerDetail.class);
                        flowerDetail.putExtra("FlowerId",adapter.getRef(position).getKey());
                        startActivity(flowerDetail);
                    }
                });
            }
        };
        //Set adapter
        recyclerView.setAdapter(adapter);

    }
}
