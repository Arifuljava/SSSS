package com.meass.cash_capital;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.owater.library.CircleTextView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Video10Activity extends AppCompatActivity {
    String videoid,ttttaka;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    TextView telegram,myusername,activeationresult,myiiid;
    CircleTextView cir11;
    LocalDate today = LocalDate.now(ZoneId.of("Asia/Dhaka"));
    UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video10);

        try {
            ttttaka=getIntent().getStringExtra("ttttaka");
            videoid=getIntent().getStringExtra("videoid");
        }catch (Exception e) {
            ttttaka=getIntent().getStringExtra("ttttaka");
            videoid=getIntent().getStringExtra("videoid");
        }

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("Last Task");
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
        YouTubePlayerView youTubePlayerView;
        youTubePlayerView = findViewById(R.id.activity_main_youtubePlayerView);
        getLifecycle().addObserver(youTubePlayerView);
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseAuth =FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        session=new UserSession(Video10Activity.this);
        getValues();
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = videoid;
                youTubePlayer.loadVideo(videoId, 0);

            }
        });
        CircleTextView cir=findViewById(R.id.cir);
        cir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new CountDownTimer(30000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        cir.setText("seconds remaining: \n" + millisUntilFinished / 1000);
                        // logic to set the EditText could go here
                    }

                    public void onFinish() {
                        cir.setText("done!\nTake Daily Bonus");
                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                AlertDialog.Builder builder=new AlertDialog.Builder(Video10Activity.this);
                                builder   .setTitle("Confirmation")
                                        .setMessage("Please take daily bonus.")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                final KProgressHUD progressDialog=  KProgressHUD.create(Video10Activity.this)
                                                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                                        .setLabel("Uploading Data.....")
                                                        .setCancellable(false)
                                                        .setAnimationSpeed(2)
                                                        .setDimAmount(0.5f)
                                                        .show();
                                                firebaseFirestore.collection("Packages_Checker")
                                                        .document(firebaseAuth.getCurrentUser().getEmail())
                                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            if (task.getResult().exists()) {
                                                                String price=task.getResult().getString("price");
                                                                firebaseFirestore.collection("Users")
                                                                        .document(firebaseAuth.getCurrentUser().getUid())
                                                                        .collection("Main_Balance")
                                                                        .document(firebaseAuth.getCurrentUser().getEmail())
                                                                        .get()
                                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    if (task.getResult().exists()) {
                                                                                        double self_income=Double.parseDouble(task.getResult().getString("main_balance"))+
                                                                                                Double.parseDouble(price);
                                                                                        firebaseFirestore.collection("Users")
                                                                                                .document(firebaseAuth.getCurrentUser().getUid())
                                                                                                .collection("Main_Balance")
                                                                                                .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                .update("main_balance",""+self_income)
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful())
                                                                                                        {
                                                                                                            Calendar calendar = Calendar.getInstance();
                                                                                                            String current11 = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                                                                                                            String current1 = DateFormat.getDateInstance().format(calendar.getTime());

                                                                                                            Long tsLong = System.currentTimeMillis()/1000;
                                                                                                            String ts = tsLong.toString();


                                                                                                            Income income=new Income(username,"Daily Income",firebaseAuth.getCurrentUser().getEmail(),""+price,current1,ts);

                                                                                                            firebaseFirestore.collection("DailyEarn")
                                                                                                                    .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                                    .collection(""+today)
                                                                                                                    .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                                    .set(income)
                                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                        @Override
                                                                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                                                                        }
                                                                                                                    });
                                                                                                            firebaseFirestore.collection("Income_History")
                                                                                                                    .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                                    .collection("List")
                                                                                                                    .document(ts)
                                                                                                                    .set(income)
                                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                        @Override
                                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                                            if (task.isSuccessful()) {
                                                                                                                                progressDialog.dismiss();
                                                                                                                                AlertDialog.Builder builder1=new AlertDialog.Builder(Video10Activity.this);
                                                                                                                                builder1 .setTitle("Congratulations")
                                                                                                                                        .setMessage("You are clime today's bonus.")
                                                                                                                                        .setPositiveButton("Go Home", new DialogInterface.OnClickListener() {
                                                                                                                                            @Override
                                                                                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                                                                                dialog.dismiss();
                                                                                                                                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                                                                                                                            }
                                                                                                                                        }).create();
                                                                                                                                builder1.show();

                                                                                                                            }
                                                                                                                        }
                                                                                                                    });
                                                                                                        }
                                                                                                    }
                                                                                                });

                                                                                    }
                                                                                }
                                                                            }
                                                                        });


                                                            }
                                                            else {


                                                            }
                                                        }
                                                    }
                                                });

                                            }
                                        }).create();
                                builder.show();
                            }
                        },2000);






                    }

                }.start();
            }
        });



    }
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(Video10Activity.this);
        builder.setTitle("Wrning")
                .setCancelable(false)
                .setMessage("You can not exit from while you are on task options>")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        builder.show();
    }

    @Override
    public boolean onNavigateUp() {
        AlertDialog.Builder builder=new AlertDialog.Builder(Video10Activity.this);
        builder.setTitle("Wrning")
                .setCancelable(false)
                .setMessage("You can not exit from while you are on task options>")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        builder.show();
        return true;
    }
    private void getValues() {
        //validating session


        try {
            //get User details if logged in
            session.isLoggedIn();
            user = session.getUserDetails();

            name = user.get(UserSession.KEY_NAME);
            email = user.get(UserSession.KEY_EMAIL);
            mobile = user.get(UserSession.KEY_MOBiLE);
            photo = user.get(UserSession.KEY_PHOTO);
            username=user.get(UserSession.Username);
        }catch (Exception e) {
            //get User details if logged in
            session.isLoggedIn();
            user = session.getUserDetails();

            name = user.get(UserSession.KEY_NAME);
            email = user.get(UserSession.KEY_EMAIL);
            mobile = user.get(UserSession.KEY_MOBiLE);
            photo = user.get(UserSession.KEY_PHOTO);
            username=user.get(UserSession.Username);
        }
        //Toast.makeText(this, ""+username, Toast.LENGTH_SHORT).show();
    }
    private UserSession ses2sion;
    private HashMap<String, String> user;
    private String name, email, photo, mobile,username;
    TextView todays;
    int flag=0;
}