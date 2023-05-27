package com.meass.cash_capital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.owater.library.CircleTextView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class Video3Activity extends AppCompatActivity {
    String videoid,ttttaka;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video3);
        try {
            ttttaka=getIntent().getStringExtra("ttttaka");
            videoid=getIntent().getStringExtra("videoid");
        }catch (Exception e) {
            ttttaka=getIntent().getStringExtra("ttttaka");
            videoid=getIntent().getStringExtra("videoid");
        }

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("Task 3");
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
                        cir.setText("done!\nGo to next task.");
                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                firebaseFirestore.collection("Videos")
                                        .document("four")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    if (task.getResult().exists()) {
                                                        Intent intent=new Intent(getApplicationContext(),Video4Activity.class);
                                                        intent.putExtra("ttttaka",""+ttttaka);
                                                        intent.putExtra("videoid",""+task.getResult().getString("videoid"));
                                                        startActivity(intent);
                                                    }
                                                }
                                            }
                                        });

                            }
                        },2000);






                    }

                }.start();
            }
        });
        CircleTextView cir2=findViewById(R.id.cir2);


    }
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(Video3Activity.this);
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
        AlertDialog.Builder builder=new AlertDialog.Builder(Video3Activity.this);
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
}