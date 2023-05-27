package com.meass.cash_capital;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.owater.library.CircleTextView;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SecondWork extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    TextView telegram,myusername,activeationresult,myiiid;
    CircleTextView cir11;
    LocalDate today = LocalDate.now(ZoneId.of("Asia/Dhaka"));
    String ttttaka;
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_work);
        firebaseAuth =FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        session = new UserSession(SecondWork.this);
        try {
            ttttaka=getIntent().getStringExtra("ttttaka");
        }catch (Exception e) {
            ttttaka=getIntent().getStringExtra("ttttaka");
        }
        getValues();
        videoView =findViewById(R.id.videoView);
        String path="android.resource://"+ getApplicationContext().getPackageName()+"/"+R.raw.s_10;
        MediaController mc= new MediaController(SecondWork.this);
        videoView.setVideoURI(Uri.parse(path));
        videoView.setMediaController(mc);
        // videoView.start();


        ///////
        CircleTextView cir=findViewById(R.id.cir);
        cir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path="android.resource://"+ getApplicationContext().getPackageName()+"/"+R.raw.s_10;
                MediaController mc= new MediaController(SecondWork.this);
                videoView.setVideoURI(Uri.parse(path));
                videoView.setMediaController(mc);
                videoView.start();
                cir.setEnabled(false);
                flag=1;

                ///////
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new CountDownTimer(10000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                cir.setText("seconds remaining: " + millisUntilFinished / 1000);
                                // logic to set the EditText could go here
                            }

                            public void onFinish() {
                                cir.setText("done!");
                                videoView.pause();
                                AlertDialog.Builder builder=new AlertDialog.Builder(SecondWork.this);
                                builder   .setTitle("Confirmation")
                                        .setMessage("Please take daily bonus.")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                final KProgressHUD progressDialog=  KProgressHUD.create(SecondWork.this)
                                                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                                        .setLabel("Uploading Data.....")
                                                        .setCancellable(false)
                                                        .setAnimationSpeed(2)
                                                        .setDimAmount(0.5f)
                                                        .show();
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
                                                                                Double.parseDouble(ttttaka);
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
                                                                                            firebaseFirestore.collection("DailyHisab")
                                                                                                    .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                    .collection(current1)
                                                                                                    .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                    .get()
                                                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                            if (task.isSuccessful()) {
                                                                                                                if (task.getResult().exists()) {
                                                                                                                    String purches_balance=task.getResult().getString("self_income");
                                                                                                                    double total=Double.parseDouble(purches_balance)+Double.parseDouble("main_balance");


                                                                                                                    firebaseFirestore.collection("DailyHisab")
                                                                                                                            .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                                            .collection(current1)
                                                                                                                            .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                                            .update("self_income",""+total)
                                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                @Override
                                                                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                                                                }
                                                                                                                            });

                                                                                                                }
                                                                                                                else {
                                                                                                                    Map<String, String> userMap2 = new HashMap<>();

                                                                                                                    userMap2.put("main_balance","0");
                                                                                                                    userMap2.put("purches_balance","0");
                                                                                                                    userMap2.put("giving_balance","0");
                                                                                                                    userMap2.put("self_income",""+ttttaka);
                                                                                                                    userMap2.put("sponsor_income","0");
                                                                                                                    userMap2.put("first_level","0");
                                                                                                                    userMap2.put("second_level","0");
                                                                                                                    userMap2.put("third_level","0");
                                                                                                                    userMap2.put("forth_level","0");
                                                                                                                    userMap2.put("fifth_level","0");
                                                                                                                    userMap2.put("shoping_balance","0");
                                                                                                                    userMap2.put("cashwalet","0");
                                                                                                                    userMap2.put("monthly_income","0");
                                                                                                                    userMap2.put("leader_club","0");
                                                                                                                    userMap2.put("gen_bonus","0");
                                                                                                                    userMap2.put("ref_bonus","0");
                                                                                                                    userMap2.put("daily_income","0");
                                                                                                                    firebaseFirestore.collection("DailyHisab")
                                                                                                                            .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                                            .collection(current1)
                                                                                                                            .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                                            .set(userMap2)
                                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                @Override
                                                                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                                                                }
                                                                                                                            });

                                                                                                                }

                                                                                                            }

                                                                                                        }
                                                                                                    });
                                                                                            Long tsLong = System.currentTimeMillis()/1000;
                                                                                            String ts = tsLong.toString();


                                                                                            Income income=new Income(username,"Daily Income",firebaseAuth.getCurrentUser().getEmail(),""+ttttaka,current1,ts);
                                                                                            firebaseFirestore.collection("Monthly")
                                                                                                    .document("abc@gmail.com")
                                                                                                    .get()
                                                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                            if (task.isSuccessful()) {
                                                                                                                if (task.getResult().exists()) {
                                                                                                                    String totaldeposit=task.getResult().getString("totalincome");
                                                                                                                    double kkk=Double.parseDouble(totaldeposit)+Double.parseDouble(ttttaka.toString());
                                                                                                                    firebaseFirestore.collection("Monthly")
                                                                                                                            .document("abc@gmail.com")
                                                                                                                            .update("totalincome",""+kkk)
                                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                @Override
                                                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                    if (task.isSuccessful()) {
                                                                                                                                    }
                                                                                                                                }
                                                                                                                            });
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    });
                                                                                            int month=calendar.get(Calendar.MONTH)+1;
                                                                                            firebaseFirestore.collection("MonthIncome")
                                                                                                    .document(UUID.randomUUID().toString())
                                                                                                    .set(income)
                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                                                        }
                                                                                                    });
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
                                                                                                                AlertDialog.Builder builder1=new AlertDialog.Builder(SecondWork.this);
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
                                        }).create();
                                builder.show();



                            }

                        }.start();
                    }
                },10000);
            }
        });
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
    private UserSession session;
    private HashMap<String, String> user;
    private String name, email, photo, mobile,username;
    TextView todays;
    int flag=0;
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SecondWork.this);
        builder.setTitle("Warning")
                .setMessage("You can not  go back..do work")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        builder.show();
    }
}