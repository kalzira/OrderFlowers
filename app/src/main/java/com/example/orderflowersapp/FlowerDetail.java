package com.example.orderflowersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.orderflowersapp.Database.Database;
import com.example.orderflowersapp.Model.Flower;
import com.example.orderflowersapp.Model.Order;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FlowerDetail extends AppCompatActivity {

    TextView flower_name, flower_price, flower_description;
    ImageView flower_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    FirebaseDatabase database;
    DatabaseReference flowers;
    String flowerId ="";

    Flower currentFlower;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower_detail);

        //Firebase
        database = FirebaseDatabase.getInstance();
        flowers = database.getReference("Flowers");

        //Init Views
        flower_description = findViewById(R.id.flower_description);
        flower_name = findViewById(R.id.flower_name);
        flower_image = findViewById(R.id.img_flower);
        flower_price = findViewById(R.id.flower_price);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

        btnCart = findViewById(R.id.btnCart);
        numberButton=findViewById(R.id.number_button);

        //Add Flower to Cart
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToCart(new Order(
                        flowerId,
                        currentFlower.getName(),
                        numberButton.getNumber(),
                        currentFlower.getPrice(),
                        currentFlower.getDiscount()
                ));

                Toast.makeText(FlowerDetail.this, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });

        //Get Flower Id from Intent
        //Get Intent
        if(getIntent()!= null){
            flowerId = getIntent().getStringExtra("FlowerId");
        }
        if(!flowerId.isEmpty() && flowerId!=null){
            getDetailFlower(flowerId);
        }
    }

    private void getDetailFlower(String flowerId) {
        flowers.child(flowerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentFlower = dataSnapshot.getValue(Flower.class);
                //Set Image
                Picasso.with(getBaseContext()).load(currentFlower.getImage())
                        .into(flower_image);

                collapsingToolbarLayout.setTitle(currentFlower.getName());

                flower_price.setText(currentFlower.getPrice());
                flower_name.setText(currentFlower.getName());
                flower_description.setText(currentFlower.getDescription());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
