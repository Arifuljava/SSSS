package com.meass.cash_capital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class BalanceDetailsActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    TextView currentdollarrate;
    String dollar;
    private Animation topAnimation, bottomAnimation, startAnimation, endAnimation;
    private SharedPreferences onBoardingPreference;
    LinearLayout linear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_details);
        topAnimation = AnimationUtils.loadAnimation(BalanceDetailsActivity.this, R.anim.splash_top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(BalanceDetailsActivity.this, R.anim.splash_bottom_animation);
        startAnimation = AnimationUtils.loadAnimation(BalanceDetailsActivity.this, R.anim.splash_start_animation);
        endAnimation = AnimationUtils.loadAnimation(BalanceDetailsActivity.this, R.anim.splash_end_animation);
        linear=findViewById(R.id.linear);
        linear.setAnimation(startAnimation);
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("Balance Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(10.0f);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_myarrow);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_myarrow);
        getSupportActionBar().setElevation(10.0f);
        getSupportActionBar().setElevation(10.0f);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        currentdollarrate=findViewById(R.id.currentdollarrate);
        loginid=findViewById(R.id.loginid);
        name____1=findViewById(R.id.name____1);
        sponsar=findViewById(R.id.sponsar);
        current_state1=findViewById(R.id.current_state1);
        current_state2=findViewById(R.id.current_state2);
        current_state=findViewById(R.id.current_state);
        final int min = 100;
        final int max = 110;
        final int random = new Random().nextInt((max - min) + 1) + min;
        dollar=""+random;
        currentdollarrate.setText("Current Dollar Rate : "+dollar+" BDT");
        firebaseFirestore.collection("Users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("Main_Balance")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                try {
                                    String  purches_balance=task.getResult().getString("purches_balance");
                                    String  main_balance=task.getResult().getString("main_balance");
                                    String  self_income=task.getResult().getString("self_income");
                                    double purches_balancebdt= Double.parseDouble(purches_balance)*(Double.parseDouble(dollar));
                                    double main_balancebdt= Double.parseDouble(main_balance)*(Double.parseDouble(dollar));
                                    double self_incomebdt= Double.parseDouble(self_income)*(Double.parseDouble(dollar));
                                    loginid.setText(purches_balance);
                                    name____1.setText(""+purches_balancebdt);
                                    sponsar.setText(main_balance);
                                    current_state1.setText(""+main_balancebdt);
                                    current_state2.setText(self_income);
                                    current_state.setText(""+self_incomebdt);

                                }catch (Exception e) {
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BalanceDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }
    TextView loginid,name____1,sponsar,current_state1,current_state2,current_state;
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),HomeACTIVITY.class));
    }

    @Override
    public boolean onNavigateUp() {
        startActivity(new Intent(getApplicationContext(),HomeACTIVITY.class));
        return true;
    }
}