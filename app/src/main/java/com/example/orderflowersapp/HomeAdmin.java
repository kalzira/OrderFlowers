package com.example.orderflowersapp;

import android.content.Intent;
import android.os.Bundle;

import com.example.orderflowersapp.Interface.ItemClickListener;
import com.example.orderflowersapp.Model.Category;
import com.example.orderflowersapp.ViewHolder.MenuAdminViewHolder;
import com.example.orderflowersapp.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;


import static com.example.orderflowersapp.Common.Common.currentUser;

public class HomeAdmin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    FirebaseDatabase database;
    DatabaseReference category;
    TextView txtFullName;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category, MenuAdminViewHolder> adapter;

    private AppBarConfiguration mAppBarConfiguration;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        Toolbar toolbar = findViewById(R.id.toolbarAdmin);
        toolbar.setTitle("Catalog Management");
        setSupportActionBar(toolbar);


        //Init Firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        FloatingActionButton fab = findViewById(R.id.fabAdmin);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeAdmin.this, "fab clicked", Toast.LENGTH_SHORT).show();

            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_admin_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_admin);
        navigationView.setNavigationItemSelectedListener(this);

        //Set name
        View headerView = navigationView.getHeaderView(0);
        txtFullName = headerView.findViewById(R.id.txtFullNameAdmin);
        txtFullName.setText("Admin");

        //Load menu
        recycler_menu = findViewById(R.id.recycler_menuAdmin);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadMenu();





    }

    private void loadMenu() {
        adapter = new FirebaseRecyclerAdapter<Category, MenuAdminViewHolder>(Category.class,
                R.layout.menu_admin_item,
                MenuAdminViewHolder.class,
                category) {
            @Override
            protected void populateViewHolder(MenuAdminViewHolder menuAdminViewHolder, Category category, int i) {
                menuAdminViewHolder.txtMenuName.setText(category.getName());
                Picasso.with(getBaseContext()).load(category.getImage())
                        .into(menuAdminViewHolder.imageView);
            }
        };
        adapter.notifyDataSetChanged(); //Refresh data when changed
        recycler_menu.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_admin_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_catalog) {
            // Handle the camera action
        } else if (id == R.id.nav_cart) {


        } else if (id == R.id.nav_log_out) {


        } else if (id == R.id.nav_orders) {


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_admin_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
