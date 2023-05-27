package com.meass.cash_capital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import es.dmoral.toasty.Toasty;

public class PaymentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinnerTextSize, spinnerTextSize1, spinnerTextSize2;
    EditText Email_Log;
    String valueFromSpinner;
    String valueFromSpinner1;
    String valueFromSpinner2;
    TextView coinsT1v,tana;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    EditText spinner1, spinner2;
    Button upgrade;
    KProgressHUD kProgressHUD;
    LottieAnimationView empty_cart;


    DocumentReference documentReference;
    RecyclerView recyclerView;
    Payment_Adapter getDataAdapter1;
    List<Payment_request> getList;
    String url;

    FirebaseUser firebaseUser;
    KProgressHUD progressHUD;
    String cus_name;
    String main_account;
    Long tsLong = System.currentTimeMillis() / 1000;
    String ts = tsLong.toString();
    String detector;
    String package_inde;
    EditText myaccount;
    TextView account_his;
    EditText passworddd;
    String taka,  sponsor_income;
    private Animation topAnimation, bottomAnimation, startAnimation, endAnimation;
    private SharedPreferences onBoardingPreference;
    LinearLayout linear;
    String roomid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        topAnimation = AnimationUtils.loadAnimation(PaymentActivity.this, R.anim.splash_top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(PaymentActivity.this, R.anim.splash_bottom_animation);
        startAnimation = AnimationUtils.loadAnimation(PaymentActivity.this, R.anim.splash_start_animation);
        endAnimation = AnimationUtils.loadAnimation(PaymentActivity.this, R.anim.splash_end_animation);
        linear=findViewById(R.id.linear);
        linear.setAnimation(topAnimation);

        account_his=findViewById(R.id.account_his);
        passworddd=findViewById(R.id.passworddd);
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("Withdraw");
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
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        upgrade = findViewById(R.id.upgrade);
        tana=findViewById(R.id.tana);
        try {
            cus_name = getIntent().getStringExtra("username");
            roomid=getIntent().getStringExtra("roomid");
        } catch (Exception e) {
            cus_name = getIntent().getStringExtra("username");
            roomid=getIntent().getStringExtra("roomid");
        }
        // Toast.makeText(this, ""+cus_name, Toast.LENGTH_SHORT).show();
        kProgressHUD = KProgressHUD.create(PaymentActivity.this);



        spinnerTextSize = findViewById(R.id.spinner3);
        spinnerTextSize.setOnItemSelectedListener(this);

        String[] textSizes = getResources().getStringArray(R.array.payment);
        ArrayAdapter adapter = new ArrayAdapter(this,
                R.layout.spinner_row, textSizes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTextSize.setAdapter(adapter);
        coinsT1v = findViewById(R.id.coinsT1v);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        myaccount=findViewById(R.id.myaccount);

        firebaseFirestore.collection("CardDetails")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().exists()) {
                                try {
                                    myaccount.setText("My Card : "+task.getResult().getString("transcationpin"));
                                }catch (Exception e) {
                                    myaccount.setText("My Card : "+task.getResult().getString("transcationpin"));
                                }
                            }
                        }
                    }
                });

        //check
        firebaseFirestore = FirebaseFirestore.getInstance();



        //
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
                                    tana.setText(task.getResult().getString("cashwalet"));
                                    taka = task.getResult().getString("main_balance");
                                    sponsor_income=task.getResult().getString("self_income");
                                    double tt=Double.parseDouble(taka);
                                    coinsT1v.setText(""+tt);
                                } catch (Exception e) {

                                }
                            } else {
                                try {
                                    coinsT1v.setText("0");
                                } catch (Exception e) {
                                    coinsT1v.setText("0");
                                }
                            }
                        }
                    }
                });
        spinner2.addTextChangedListener(textaaa);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH)+1;
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(passworddd.getText().toString()) ||TextUtils.isEmpty(spinner1.getText().toString()) || TextUtils.isEmpty(spinner2.getText().toString()) ||
                        TextUtils.isEmpty(valueFromSpinner)) {
                    Toasty.error(getApplicationContext(), "error", Toast.LENGTH_SHORT, true).show();
                    return;
                } else {
                    // progress_check();
                    String with[]={"Main Balance ( "+taka+" )","Refer Balance ( "+sponsor_income+" )"};
                    AlertDialog.Builder builder=new AlertDialog.Builder(PaymentActivity.this);
                    builder.setTitle("Withdraw Options")
                            .setItems(with, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which==0) {
                                        firebaseFirestore.collection("Package_takingChecker")
                                                .document(firebaseAuth.getCurrentUser().getEmail())
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            if (task.getResult().exists()) {
                                                                Double a = Double.parseDouble(coinsT1v.getText().toString());
                                                                Double b = Double.parseDouble(spinner2.getText().toString());
                                                                if (valueFromSpinner.equals("Select Payment Method")) {
                                                                    Toasty.error(getApplicationContext(), "Select a payment methode.", Toasty.LENGTH_SHORT).show();
                                                                    return;
                                                                } else if (b < Double.parseDouble(roomid)||b>100000000) {
                                                                    kProgressHUD.dismiss();
                                                                    Toasty.info(getApplicationContext(), "Minimum Payment is "+roomid, Toasty.LENGTH_SHORT, true).show();
                                                                } else {
                                                                    double dooo= Double.parseDouble(spinner2.getText().toString());
                                                                    String double_valuye=String.format("%.0f",dooo);
                                                                    if(a<Double.parseDouble(double_valuye)) {
                                                                        // Toast.makeText(PaymentActivity.this, double_valuye+"", Toast.LENGTH_SHORT).show();
                                                                        Toasty.error(getApplicationContext(),"Not enough money.",Toasty.LENGTH_SHORT,true).show();
                                                                        return;
                                                                    }
                                                                    else if (a >= Double.parseDouble(double_valuye)) {
                                                                        final EditText input = new EditText(PaymentActivity.this);
                                                                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                                                LinearLayout.LayoutParams.MATCH_PARENT);
                                                                        input.setLayoutParams(lp);
                                                                        input.setHint("Enter Password");
                                                                        input.setText(passworddd.getText().toString().toLowerCase());


                                                                        ///
                                                                        firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid())
                                                                                .get()
                                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            if (task.getResult().exists()) {
                                                                                                String pas = task.getResult().getString("pass");
                                                                                                String getingUser = task.getResult().getString("number");
                                                                                                if (pas.contains(input.getText().toString().toLowerCase())) {
                                                                                                    final String uuid = UUID.randomUUID().toString();
                                                                                                    Calendar calendar = Calendar.getInstance();
                                                                                                    String current = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                                                                                                    String current1 = DateFormat.getDateInstance().format(calendar.getTime());
                                                                                                    counterDays(firebaseAuth.getCurrentUser().getEmail());
                                                                                                    String ammount=check;
                                                                                                    double fee=Double.parseDouble(ammount)*5/100;
                                                                                                    double total=Double.parseDouble(spinner2.getText().toString())-fee;

                                                                                                    String double_valuye=String.format("%.0f",total);

                                                                                                    final Payment_request payment_request = new Payment_request(firebaseAuth.getCurrentUser().getEmail(),
                                                                                                            valueFromSpinner, spinner1.getText().toString(), double_valuye, current1, "Pending", uuid, getingUser, ts,"Main Balance");

                                                                                                    int month=calendar.get(Calendar.MONTH)+1;
                                                                                                    firebaseFirestore.collection("MonthWithdraw")
                                                                                                            .document(UUID.randomUUID().toString())
                                                                                                            .set(payment_request)
                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                                                }
                                                                                                            });

                                                                                                    firebaseFirestore.collection("Payment_Request")
                                                                                                            .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                            .collection("List")
                                                                                                            .document(uuid)
                                                                                                            .set(payment_request)
                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                                    if (task.isSuccessful()) {
                                                                                                                        firebaseFirestore.collection("Admin_paymentRequest")
                                                                                                                                .document(uuid)
                                                                                                                                .set(payment_request)
                                                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                    @Override
                                                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                        if (task.isSuccessful()) {
                                                                                                                                            double dooo= Double.parseDouble(spinner2.getText().toString());
                                                                                                                                            double main = Double.parseDouble(coinsT1v.getText().toString()) - dooo;
                                                                                                                                            double main2 = Double.parseDouble(tana.getText().toString()) + dooo;
                                                                                                                                            String double_valuye=String.format("%.0f",main);
                                                                                                                                            String double_valuye1=String.format("%.0f",main2);
                                                                                                                                            firebaseFirestore.collection("Users")
                                                                                                                                                    .document(firebaseAuth.getCurrentUser().getUid())
                                                                                                                                                    .collection("Main_Balance")
                                                                                                                                                    .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                                                                    .update("main_balance", String.valueOf(double_valuye))
                                                                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                        @Override
                                                                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                                            if (task.isSuccessful()) {
                                                                                                                                                                kProgressHUD.dismiss();
                                                                                                                                                                kProgressHUD.dismiss();
                                                                                                                                                                kProgressHUD.dismiss();
                                                                                                                                                                Toasty.success(getApplicationContext(), " Withdraw Request Successfully Done", Toast.LENGTH_SHORT, true).show();
                                                                                                                                                                startActivity(new Intent(getApplicationContext(),HomeACTIVITY.class));
                                                                                                                                                            }
                                                                                                                                                        }
                                                                                                                                                    });
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                });
                                                                                                                    }
                                                                                                                }
                                                                                                            });
                                                                                                } else {
                                                                                                    kProgressHUD.dismiss();
                                                                                                    Toasty.error(getApplicationContext(), "Password not match", Toast.LENGTH_SHORT, true).show();
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                });
                                                                        ////


                                                                    } else {
                                                                        kProgressHUD.dismiss();
                                                                        Toasty.error(getApplicationContext(), "User hav't enough money", Toast.LENGTH_SHORT, true).show();
                                                                    }

                                                                }

                                                                //Toasty.error(getApplicationContext(), "Already Active This  Package", Toasty.LENGTH_SHORT, true).show();

                                                            } else {
                                                                Toasty.error(getApplicationContext(), "Active a package please", Toasty.LENGTH_SHORT, true).show();
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                    else if(which==1) {
                                        Double a = Double.parseDouble(sponsor_income);
                                        Double b = Double.parseDouble(spinner2.getText().toString());
                                        if (valueFromSpinner.equals("Select Payment Method")) {
                                            Toasty.error(getApplicationContext(), "Select a payment methode.", Toasty.LENGTH_SHORT).show();
                                            return;
                                        }  else {
                                            double dooo= Double.parseDouble(spinner2.getText().toString());
                                            String double_valuye=String.format("%.0f",dooo);
                                            if(a<Double.parseDouble(double_valuye)) {
                                                // Toast.makeText(PaymentActivity.this, double_valuye+"", Toast.LENGTH_SHORT).show();
                                                Toasty.error(getApplicationContext(),"Not enough money.",Toasty.LENGTH_SHORT,true).show();
                                                return;
                                            }
                                            else if (a >= Double.parseDouble(double_valuye)) {
                                                final EditText input = new EditText(PaymentActivity.this);
                                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                                        LinearLayout.LayoutParams.MATCH_PARENT);
                                                input.setLayoutParams(lp);
                                                input.setHint("Enter Password");
                                                input.setText(passworddd.getText().toString().toLowerCase());


                                                ///
                                                firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid())
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    if (task.getResult().exists()) {
                                                                        String pas = task.getResult().getString("pass");
                                                                        String getingUser = task.getResult().getString("number");
                                                                        if (pas.contains(input.getText().toString().toLowerCase())) {
                                                                            final String uuid = UUID.randomUUID().toString();
                                                                            Calendar calendar = Calendar.getInstance();
                                                                            String current = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                                                                            String current1 = DateFormat.getDateInstance().format(calendar.getTime());
                                                                            counterDays(firebaseAuth.getCurrentUser().getEmail());
                                                                            String ammount=check;
                                                                            double fee=Double.parseDouble(ammount)*5/100;
                                                                            double total=Double.parseDouble(spinner2.getText().toString())-fee;

                                                                            String double_valuye=String.format("%.0f",total);

                                                                            final Payment_request payment_request = new Payment_request(firebaseAuth.getCurrentUser().getEmail(),
                                                                                    valueFromSpinner, spinner1.getText().toString(), double_valuye, current1, "Pending", uuid, getingUser, ts,"Refer Balance");

                                                                            int month=calendar.get(Calendar.MONTH)+1;
                                                                            firebaseFirestore.collection("MonthWithdraw")
                                                                                    .document(UUID.randomUUID().toString())
                                                                                    .set(payment_request)
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                                        }
                                                                                    });

                                                                            firebaseFirestore.collection("Payment_Request")
                                                                                    .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                    .collection("List")
                                                                                    .document(uuid)
                                                                                    .set(payment_request)
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                firebaseFirestore.collection("Admin_paymentRequest")
                                                                                                        .document(uuid)
                                                                                                        .set(payment_request)
                                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                if (task.isSuccessful()) {
                                                                                                                    double dooo= Double.parseDouble(spinner2.getText().toString());
                                                                                                                    double main = Double.parseDouble(sponsor_income) - dooo;
                                                                                                                    double main2 = Double.parseDouble(tana.getText().toString()) + dooo;
                                                                                                                    String double_valuye=String.format("%.0f",main);
                                                                                                                    String double_valuye1=String.format("%.0f",main2);
                                                                                                                    firebaseFirestore.collection("Users")
                                                                                                                            .document(firebaseAuth.getCurrentUser().getUid())
                                                                                                                            .collection("Main_Balance")
                                                                                                                            .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                                            .update("self_income", String.valueOf(double_valuye))
                                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                @Override
                                                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                    if (task.isSuccessful()) {
                                                                                                                                        kProgressHUD.dismiss();
                                                                                                                                        kProgressHUD.dismiss();
                                                                                                                                        kProgressHUD.dismiss();
                                                                                                                                        Toasty.success(getApplicationContext(), " Withdraw Request Successfully Done", Toast.LENGTH_SHORT, true).show();
                                                                                                                                        startActivity(new Intent(getApplicationContext(),HomeACTIVITY.class));
                                                                                                                                    }
                                                                                                                                }
                                                                                                                            });
                                                                                                                }
                                                                                                            }
                                                                                                        });
                                                                                            }
                                                                                        }
                                                                                    });
                                                                        } else {
                                                                            kProgressHUD.dismiss();
                                                                            Toasty.error(getApplicationContext(), "Password not match", Toast.LENGTH_SHORT, true).show();
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        });
                                                ////


                                            } else {
                                                kProgressHUD.dismiss();
                                                Toasty.error(getApplicationContext(), "User hav't enough money", Toast.LENGTH_SHORT, true).show();
                                            }

                                        }
                                    }
                                }
                            }).create();
                    builder.show();




                }//faith
            }

        });

    }
    String check ;
    TextWatcher textaaa=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            check=s.toString();
            if (TextUtils.isEmpty(check)
            ) {


            }
            else {
                try {
                    check = s.toString();
                    String ammount=check;
                    double fee=Double.parseDouble(ammount)*5/100;
                    double total=Double.parseDouble(check)-fee;


                    account_his.setVisibility(View.VISIBLE);
                    account_his.setText("Amount : "+check+", Fee : "+fee+", Total : "+total);
                }catch (Exception e) {
                    check = s.toString();
                    String ammount=check;
                    double fee=Double.parseDouble(ammount)*5/100;
                    double total=Double.parseDouble(check)-fee;


                    account_his.setVisibility(View.VISIBLE);
                    account_his.setText("Amount : "+check+", Fee : "+fee+", Total : "+total);
                }
            }
        }
    };
    private void counterDays(String email) {
        Map<String, String> userMap1 = new HashMap<>();

        userMap1.put("counter", firebaseAuth.getCurrentUser().getEmail());
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH)+1;
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        firebaseFirestore.collection("Payment_date_Detector_EachDay")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .collection(""+month)
                .document(""+day)
                .set(userMap1)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinner3) {
            valueFromSpinner = parent.getItemAtPosition(position).toString();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void progress_check() {
        kProgressHUD = KProgressHUD.create(PaymentActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Uploading Data..")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(getApplicationContext(), HomeACTIVITY.class);

        startActivity(intent);

        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), HomeACTIVITY.class);

        startActivity(intent);
    }
}