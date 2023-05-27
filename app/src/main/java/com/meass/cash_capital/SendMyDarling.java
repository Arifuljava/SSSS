package com.meass.cash_capital;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import es.dmoral.toasty.Toasty;

public class SendMyDarling extends AppCompatActivity {
    EditText spinner1,transcationpin,toid;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    TextView textammount,showing;
    Button cirLoginButton;
    KProgressHUD kProgressHUD;
    String check;
    TextView textammountss;
    String cus_name;
    private Animation topAnimation, bottomAnimation, startAnimation, endAnimation;
    private SharedPreferences onBoardingPreference;
    LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_my_darling);
        topAnimation = AnimationUtils.loadAnimation(SendMyDarling.this, R.anim.splash_top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(SendMyDarling.this, R.anim.splash_bottom_animation);
        startAnimation = AnimationUtils.loadAnimation(SendMyDarling.this, R.anim.splash_start_animation);
        endAnimation = AnimationUtils.loadAnimation(SendMyDarling.this, R.anim.splash_end_animation);
        linear=findViewById(R.id.linear);
        linear.setAnimation(endAnimation);
        try {
            cus_name=getIntent().getStringExtra("username");
        }catch (Exception e) {
            cus_name=getIntent().getStringExtra("username");
        }
        textammountss=findViewById(R.id.textammountss);
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("Send Balance");
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
        kProgressHUD=KProgressHUD.create(SendMyDarling.this);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        spinner1=findViewById(R.id.spinner1);
        textammount=findViewById(R.id.textammount);
        transcationpin=findViewById(R.id.transcationpin);
        cirLoginButton=findViewById(R.id.cirLoginButton);
        showing=findViewById(R.id.showing);
        toid=findViewById(R.id.toid);
        toid=findViewById(R.id.toid);
        toid.addTextChangedListener(nameWatcher);
        spinner1.addTextChangedListener(takawatcher);
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
                                try {
                                    String balance=task.getResult().getString("purches_balance");
                                    double main_=Double.parseDouble(balance);
                                    if (main_==0) {
                                        //cirLoginButton.setEnabled(false);

                                    }
                                    else {
                                        // cirLoginButton.setEnabled(true);
                                    }
                                    showing.setText("Available Balance : "+task.getResult().getString("purches_balance"));
                                }catch (Exception e) {
                                    String balance=task.getResult().getString("purches_balance");
                                    double main_=Double.parseDouble(balance);
                                    if (main_==0) {
                                        //cirLoginButton.setEnabled(false);

                                    }
                                    else {
                                        //cirLoginButton.setEnabled(true);
                                    }
                                    showing.setText("Available Balance : "+task.getResult().getString("purches_balance"));
                                }
                            }
                            else {

                                showing.setText("Available Balance : 0");
                            }
                        }
                        else {

                            showing.setText("Available Balance : 0");
                        }
                    }
                });

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Send_moneyChecker")
                .document("abc@gmail.com")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                String checker=task.getResult().getString("checker");
                                if (Integer.parseInt(checker)==0) {
                                    cirLoginButton.setEnabled(true);
                                }
                                else {
                                    cirLoginButton.setEnabled(false);
                                    Toasty.info(getApplicationContext(),"Balance transfer currently disable.",Toasty.LENGTH_SHORT,true).show();
                                    return;
                                }
                            }
                        }
                    }
                });

        cirLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ammount=spinner1.getText().toString().toLowerCase();
                String pin=transcationpin.getText().toString().toLowerCase();
                String toidi=toid.getText().toString().toLowerCase().toString();
                if (TextUtils.isEmpty(ammount)||TextUtils.isEmpty(pin)||TextUtils.isEmpty(toidi)) {
                    Toasty.error(getApplicationContext(), "Error give right information.", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                else {
                    firebaseFirestore.collection("Send_moneyChecker")
                            .document("abc@gmail.com")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().exists()) {
                                            String checker=task.getResult().getString("checker");
                                            if (Integer.parseInt(checker)==0) {
                                                if (firebaseAuth.getCurrentUser().getEmail().equals(toidi+"@gmail.com")) {
                                                    Toasty.info(getApplicationContext(),"You can not transfer balance yourself.",Toasty.LENGTH_SHORT,true).show();
                                                    return;
                                                }
                                                else {
                                                    if (Double.parseDouble(ammount)<10) {
                                                        Toasty.info(getApplicationContext(), "Minimum balance transfer  range is 10.", Toast.LENGTH_SHORT, true).show();
                                                        return;
                                                    }
                                                    else {
                                                        progress_check();
                                                        firebaseFirestore.collection("Password")
                                                                .document(firebaseAuth.getCurrentUser().getEmail())
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            if (task.getResult().exists()) {

                                                                                try {
                                                                                    String pass=task.getResult().getString("uuid");
                                                                                    if (pass.toLowerCase().toString().equals(pin)) {
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

                                                                                                                String purches_balance=task.getResult().getString("purches_balance");


                                                                                                                String ammount1=ammount;
                                                                                                                double subpurches_balance=Double.parseDouble(purches_balance)-Double.parseDouble(ammount1);

                                                                                                                if (Double.parseDouble(ammount1)>Double.parseDouble(purches_balance)) {
                                                                                                                    kProgressHUD.dismiss();
                                                                                                                    Toasty.error(getApplicationContext(), "You have not enough balance.", Toast.LENGTH_SHORT, true).show();
                                                                                                                }
                                                                                                                else {
                                                                                                                    firebaseFirestore.collection("Users")
                                                                                                                            .document(firebaseAuth.getCurrentUser().getUid())
                                                                                                                            .collection("Main_Balance")
                                                                                                                            .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                                            .update("purches_balance",
                                                                                                                                    ""+subpurches_balance)
                                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                @RequiresApi(api = Build.VERSION_CODES.N)
                                                                                                                                @Override
                                                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                    if (task.isSuccessful()) {
                                                                                                                                        firebaseFirestore.collection("All_UserID")
                                                                                                                                                .document(toidi+"@gmail.com")
                                                                                                                                                .get()
                                                                                                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                                                    @Override
                                                                                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                                                        if (task.isSuccessful()) {
                                                                                                                                                            if (task.getResult().exists()) {
                                                                                                                                                                String uuid=task.getResult().getString("uuid");
                                                                                                                                                                firebaseFirestore.collection("Users")
                                                                                                                                                                        .document(uuid)
                                                                                                                                                                        .collection("Main_Balance")
                                                                                                                                                                        .document(toidi+"@gmail.com")
                                                                                                                                                                        .get()
                                                                                                                                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                                                                            @Override
                                                                                                                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                                                                                if (task.isSuccessful()) {
                                                                                                                                                                                    if (task.getResult().exists()) {
                                                                                                                                                                                        String purches_balance=task.getResult().getString("purches_balance");

                                                                                                                                                                                        String ammount1=""+total1;
                                                                                                                                                                                        double subpurches_balance=Double.parseDouble(purches_balance)+Double.parseDouble(ammount1);
                                                                                                                                                                                        String monthly_income=task.getResult().getString("monthly_income");
                                                                                                                                                                                        double submonthly_income=Double.parseDouble(monthly_income)+Double.parseDouble(ammount1);
                                                                                                                                                                                        firebaseFirestore.collection("Users")
                                                                                                                                                                                                .document(uuid)
                                                                                                                                                                                                .collection("Main_Balance")
                                                                                                                                                                                                .document(toidi+"@gmail.com")
                                                                                                                                                                                                .update("purches_balance",
                                                                                                                                                                                                        ""+subpurches_balance)
                                                                                                                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                                                                    @Override
                                                                                                                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                                                                                        if (task.isSuccessful()) {
                                                                                                                                                                                                            Long tsLong = System.currentTimeMillis()/1000;
                                                                                                                                                                                                            String ts = tsLong.toString();
                                                                                                                                                                                                            Random r = new Random();
                                                                                                                                                                                                            int randomNumber = r.nextInt(1002563985);
                                                                                                                                                                                                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                                                                                                                                                                            Date date = new Date();
                                                                                                                                                                                                            String s=dateFormat.format(date);
                                                                                                                                                                                                            String randomid="GTR"+randomNumber;
                                                                                                                                                                                                            SendingModel sendingModel=new SendingModel("Transfer",ts,s,
                                                                                                                                                                                                                    randomid,"Receiver ID : ",spinner1.getText().toString(),""+fee1,ammount1,toidi.toLowerCase().toString());
                                                                                                                                                                                                            firebaseFirestore.collection("My_Sending")
                                                                                                                                                                                                                    .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                                                                                                                                    .collection("List")
                                                                                                                                                                                                                    .add(sendingModel)
                                                                                                                                                                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                                                                                                                                                                                                            if (task.isSuccessful()) {

                                                                                                                                                                                                                                Calendar calendar = Calendar.getInstance();
                                                                                                                                                                                                                                String current = java.text.DateFormat.getDateInstance(java.text.DateFormat.FULL).format(calendar.getTime());
                                                                                                                                                                                                                                String current1 = java.text.DateFormat.getDateInstance().format(calendar.getTime());
                                                                                                                                                                                                                                Random r = new Random();
                                                                                                                                                                                                                                int randomNumber = r.nextInt(1002563985);

                                                                                                                                                                                                                                //int ttt = Integer.parseInt(packageprice)*5/100;;
                                                                                                                                                                                                                                // int randomNumber = r.nextInt(1002563985);
                                                                                                                                                                                                                                String randomid="DLB"+randomNumber;
                                                                                                                                                                                                                                Income22 income=new Income22(cus_name,"Fund receive from user",firebaseAuth.getCurrentUser().getEmail(),
                                                                                                                                                                                                                                        String.valueOf(ammount1),current1,ts,randomid,""+fee1);
                                                                                                                                                                                                                                firebaseFirestore.collection("Fund_History") .document(toidi+"@gmail.com")
                                                                                                                                                                                                                                        .collection("List")
                                                                                                                                                                                                                                        .add(income)
                                                                                                                                                                                                                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                                                                                                                                                                                                            @Override
                                                                                                                                                                                                                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                                                                                                                                                                                                                if (task.isSuccessful()) {
                                                                                                                                                                                                                                                    kProgressHUD.dismiss();
                                                                                                                                                                                                                                                    AlertDialog.Builder builder=new AlertDialog.Builder(SendMyDarling.this);
                                                                                                                                                                                                                                                    builder.setTitle("Confirmation")
                                                                                                                                                                                                                                                            .setMessage("Balance Transfering is Successfully Done.")
                                                                                                                                                                                                                                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                                                                                                                                                                                                    dialog.dismiss();
                                                                                                                                                                                                                                                                    startActivity(new Intent(getApplicationContext(),HomeACTIVITY.class));

                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                            }).create();
                                                                                                                                                                                                                                                    builder.show();
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                        });

                                                                                                                                                                                                                                //Toasty.success(getApplicationContext(), "Balance transfer is done.", Toast.LENGTH_SHORT, true).show();
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
                                                                                                                                                        }
                                                                                                                                                    }
                                                                                                                                                });

                                                                                                                                    }
                                                                                                                                }
                                                                                                                            });
                                                                                                                }


                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                });

                                                                                    }
                                                                                    else {
                                                                                        kProgressHUD.dismiss();
                                                                                        Toasty.error(getApplicationContext(), "Password is incorrect.", Toast.LENGTH_SHORT, true).show();
                                                                                    }
                                                                                }catch (Exception e) {

                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                    }

                                                }
                                            }
                                            else {
                                                Toasty.info(getApplicationContext(),"balance transfer currently disable.",Toasty.LENGTH_SHORT,true).show();
                                                return;
                                            }
                                        }
                                    }
                                }
                            });


                }
            }
        });

    }
    private void progress_check() {
        kProgressHUD = KProgressHUD.create(SendMyDarling.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Checking Data")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

    }
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),HomeACTIVITY.class));
    }

    @Override
    public boolean onNavigateUp() {
        startActivity(new Intent(getApplicationContext(),HomeACTIVITY.class));
        return true;
    }
    TextWatcher nameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {

            check=s.toString();
            if (TextUtils.isEmpty(check)) {
            }
            else {
                check = s.toString();
                ;


                textammount.setVisibility(View.VISIBLE);
                firebaseFirestore.collection("User2")
                        .document(check+"@gmail.com")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        try {
                                            textammount.setText(task.getResult().getString("name"));
                                            cirLoginButton.setEnabled(true);
                                        }catch (Exception e ) {
                                            textammount.setText(task.getResult().getString("name"));
                                            cirLoginButton.setEnabled(true);
                                        }
                                    }
                                    else {
                                        textammount.setText("No Current User");
                                        cirLoginButton.setEnabled(false);
                                    }
                                }
                                else {
                                    textammount.setText("No Current User");
                                    cirLoginButton.setEnabled(false);
                                }
                            }
                        });

            }
        }

    };
    String check2;
    double total1,fee1;
    TextWatcher takawatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {
            check2=s.toString();
            if (TextUtils.isEmpty(check2)
            ) {

                textammountss.setVisibility(View.GONE);
            }
            else {

                try {
                    check2 = s.toString();
                    String ammount=check2;
                    double fee=Double.parseDouble(ammount)*0/100;
                    fee1=fee;
                    double total=Double.parseDouble(check2)-fee;
                    total1=total;

                    textammountss.setVisibility(View.VISIBLE);
                    textammountss.setText("Amount : "+check2+", Fee : "+fee+", Total : "+total);
                }catch (Exception e) {
                    check = s.toString();
                    String ammount=check2;
                    double fee=Double.parseDouble(ammount)*0/100;
                    double total=Double.parseDouble(check2)-fee;

                    fee1=fee;
                    total1=total;
                    textammountss.setVisibility(View.VISIBLE);
                    textammountss.setText("Amount : "+check2+", Fee : "+fee+", Total : "+total);
                }
            }
        }

    };

    public void invite(View view) {
        startActivity(new Intent(getApplicationContext(),ReceiveHistory.class));
    }

    public void tree(View view) {
        startActivity(new Intent(getApplicationContext(),TransferHistory.class));
    }
}