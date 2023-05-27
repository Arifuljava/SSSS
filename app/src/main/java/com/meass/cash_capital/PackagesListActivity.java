package com.meass.cash_capital;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class PackagesListActivity extends AppCompatActivity {
    LottieAnimationView empty_cart;
    DocumentReference documentReference;
    RecyclerView recyclerView;
    PaymentAdapter getDataAdapter1;
    List<BloodBankModel> getList;
    String url;

    FirebaseUser firebaseUser;
    KProgressHUD progressHUD;
    String cus_name;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages_list);
        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        try {
            cus_name=getIntent().getStringExtra("cus_name");
        }catch (Exception e) {
            cus_name=getIntent().getStringExtra("cus_name");
        }
        toolbar.setTitle("Packages");
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_myarrow);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_myarrow);
        getSupportActionBar().setElevation(10.0f);
        getSupportActionBar().setElevation(10.0f);
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();

        getList = new ArrayList<>();
        getDataAdapter1 = new PaymentAdapter(getList,cus_name);
        firebaseFirestore = FirebaseFirestore.getInstance();
        documentReference =   firebaseFirestore.collection("Packages")
                .document();
        recyclerView = findViewById(R.id.rreeeed);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(PackagesListActivity.this));
        recyclerView.setAdapter(getDataAdapter1);


        name=findViewById(R.id.name);
        reciveData();
        ////
        name=findViewById(R.id.name);

        name.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //fullsearch(query);

                //phoneSerach(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchAllUser(newText);

                //phoneSerach1(newText);
                return false;
            }
        });
        topAnimation = AnimationUtils.loadAnimation(PackagesListActivity.this, R.anim.splash_top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(PackagesListActivity.this, R.anim.splash_bottom_animation);
        startAnimation = AnimationUtils.loadAnimation(PackagesListActivity.this, R.anim.splash_start_animation);
        endAnimation = AnimationUtils.loadAnimation(PackagesListActivity.this, R.anim.splash_end_animation);

        name.setAnimation(topAnimation);
        recyclerView.setAnimation(endAnimation);
    }
    private Animation topAnimation, bottomAnimation, startAnimation, endAnimation;
    private SharedPreferences onBoardingPreference;
    SearchView name;
    private void searchAllUser(String newText) {
        firebaseFirestore.collection("Packages")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        getList.clear();
                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                            String dta = documentSnapshot.getString("name");
                            if (dta.toLowerCase().toString().contains(newText.toLowerCase().toString())) {
                                BloodBankModel add_customer=new BloodBankModel(
                                        documentSnapshot.getString("name"),
                                        documentSnapshot.getString("time"),
                                        documentSnapshot.getString("feeentry"),

                                        documentSnapshot.getString("feeprice"),
                                        documentSnapshot.getString("roomid"),
                                        documentSnapshot.getString("timeuuid")

                                        ,
                                        documentSnapshot.getString("uuid"),
                                        documentSnapshot.getString("day"),
                                        documentSnapshot.getString("month")

                                        ,
                                        documentSnapshot.getString("year"),
                                        documentSnapshot.getString("hours"),
                                        documentSnapshot.getString("min")



                                );
                                getList.add(add_customer);

                            }
                            getDataAdapter1 = new PaymentAdapter(getList,cus_name);
                            recyclerView.setAdapter(getDataAdapter1);


                        }
                    }
                });
    }
    private void reciveData() {

        firebaseFirestore.collection("Packages")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange ds : queryDocumentSnapshots.getDocumentChanges()) {
                    if (ds.getType() == DocumentChange.Type.ADDED) {

                 /*String first;
                 first = ds.getDocument().getString("name");
                 Toast.makeText(MainActivity2.this, "" + first, Toast.LENGTH_SHORT).show();*/
                        BloodBankModel get = ds.getDocument().toObject(BloodBankModel.class);
                        getList.add(get);
                        getDataAdapter1.notifyDataSetChanged();
                    }

                }
            }
        });

    }
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),HomeACTIVITY.class));
    }

    @Override
    public boolean onNavigateUp() {
        startActivity(new Intent(getApplicationContext(),HomeACTIVITY.class));
        return true;
    }
}