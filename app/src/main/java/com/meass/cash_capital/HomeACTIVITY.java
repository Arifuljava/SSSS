package com.meass.cash_capital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class HomeACTIVITY extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Toolbar mainToolbar;
    private String current_user_id;
    private BottomNavigationView mainBottomNav;
    private DrawerLayout mainDrawer;
    private ActionBarDrawerToggle mainToggle;
    private NavigationView mainNav;

    FrameLayout frameLayout;
    private TextView drawerName;
    private CircleImageView drawerImage;
    FirebaseAuth firebaseAuth;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseFirestoreSettings settings;
    private DatabaseReference mUserRef;

    private HashMap<String, String> user;
    private String name, email, photo, mobile,username;
    UserSession session;
    HomeFragment homeFragment;
    MessageFragment messageFragment;


    //Other Variables
    private Animation topAnimation, bottomAnimation, startAnimation, endAnimation;
    private SharedPreferences onBoardingPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_a_c_t_i_v_i_t_y);
        FirebaseApp.initializeApp(HomeACTIVITY.this);
        //check Internet Connection
        //new CheckInternetConnection(this).checkConnection();
        topAnimation = AnimationUtils.loadAnimation(HomeACTIVITY.this, R.anim.splash_top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(HomeACTIVITY.this, R.anim.splash_bottom_animation);
        startAnimation = AnimationUtils.loadAnimation(HomeACTIVITY.this, R.anim.splash_start_animation);
        endAnimation = AnimationUtils.loadAnimation(HomeACTIVITY.this, R.anim.splash_end_animation);


        session = new UserSession(getApplicationContext());
        //new CheckInternetConnection(this).checkConnection();
        Toolbar toolbar = findViewById(R.id.toolbar);
        mAuth=FirebaseAuth.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(10.0f);
        mainDrawer=findViewById(R.id.main_activity);
        mainNav = findViewById(R.id.main_nav);
        mainNav.setNavigationItemSelectedListener(this);
        frameLayout=findViewById(R.id.main_container);
        mainToggle = new ActionBarDrawerToggle(this,mainDrawer,toolbar,R.string.open,R.string.close);
        mainDrawer.addDrawerListener(mainToggle);
        mainToggle.setDrawerIndicatorEnabled(true);
        mainToggle.syncState();
        //mainNav.setAnimation(topAnimation);
        /////
        homeFragment=new HomeFragment();
        messageFragment=new MessageFragment();

        mainBottomNav = findViewById(R.id.mainBottomNav);
        //mainBottomNav.setAnimation(bottomAnimation);
        mainBottomNav.setOnNavigationItemSelectedListener(selectlistner);

        //
        /*
        int a=12;
        if (a>10&&a<200) {
            Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "false", Toast.LENGTH_SHORT).show();
        }
         */

        //
        initializeFragment();
        mainBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.bottom_home:
                        flag=1;
                        replaceFragment(homeFragment);
                        searchView.setVisibility(View.VISIBLE);
                        return true;

                    case R.id.navigation_chat:
                        flag=2;
                        replaceFragment(messageFragment);
                        searchView.setVisibility(View.GONE);
                        return true;
                   /*
                    case R.id.withlist:
                        flag=2;
                        replaceFragment(wishlistFragment);
                        searchView.setVisibility(View.GONE);
                        return true;
                    */





                    default:
                        return false;
                }
            }
        });
    }
    int flag=1;
    private BottomNavigationView.OnNavigationItemSelectedListener selectlistner =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.bottom_home:
                            HomeFragment fragment2 = new HomeFragment();
                            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.content, fragment2, "");
                            ft2.commit();
                            break;



                    }
                    return false;
                }
            };
    private void replaceFragment(Fragment fragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (fragment == homeFragment){
            fragmentTransaction.hide(messageFragment);

            // fragmentTransaction.hide(historyFragment);

        } else if (fragment == messageFragment){

            fragmentTransaction.hide(homeFragment);
            searchView.setVisibility(View.GONE);
            //   fragmentTransaction.hide(historyFragment);

        }

    }




    public void initializeFragment(){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container,homeFragment);
        fragmentTransaction.add(R.id.main_container,messageFragment);


        // fragmentTransaction.add(R.id.main_container,historyFragment);


        fragmentTransaction.hide(messageFragment);



        fragmentTransaction.commit();

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id) {
            case R.id.poffrf:
                firebaseFirestore.collection("Packages_Checker")
                        .document(firebaseAuth.getCurrentUser().getEmail())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                      String message = "Package name : "+task.getResult().getString("name")+"\n" +
                                              "Package Price : "+task.getResult().getString("time")+ " USD \n" +"" +
                                              "Daily Income : "+task.getResult().getString("income")+" USD \n" +
                                              "Amount Gained : "+task.getResult().getString("roomid")+" USD \n" +
                                              "Validity Date  : 30 Days ";
                                      AlertDialog.Builder builder=new AlertDialog.Builder(HomeACTIVITY.this);
                                      builder.setTitle("Package Details")
                                              .setMessage(message)
                                              .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                  @Override
                                                  public void onClick(DialogInterface dialog, int which) {
                                                      dialog.dismiss();
                                                  }
                                              });
                                      builder.create();
                                      builder.show();
                                    }
                                    else {
                                        Toast.makeText(HomeACTIVITY.this, "You are in inactive mode. You are not active any packages.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(HomeACTIVITY.this, "You are in inactive mode. You are not active any packages.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            case R.id.logout:
                AlertDialog.Builder warning = new AlertDialog.Builder(HomeACTIVITY.this)
                        .setTitle("Logout")
                        .setMessage("Are you want to logout?")
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();



                            }
                        }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // ToDO: delete all the notes created by the Anon user


                                firebaseAuth.signOut();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();


                            }
                        });

                warning.show();
                break;
            case R.id.addNote:
                Toasty.success(getApplicationContext(),"You are now in home",Toasty.LENGTH_SHORT,true).show();

                break;

            case R.id.porf:
                startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                break;

            case R.id.cartliost:
                startActivity(new Intent(getApplicationContext(),CartActivity.class));
                break;
            case R.id.history:
                startActivity(new Intent(getApplicationContext(),OrderHistoryActivity.class));
                break;
            case R.id.shareapp1:
                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                break;



        }

        return false;
    }
    SearchView searchView;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(HomeACTIVITY.this);
        builder.setTitle("Exit")
                .setCancelable(false)
                .setMessage("Are you want to exit")
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finishAffinity();
            }
        }).create();
        builder.show();



    }


}
