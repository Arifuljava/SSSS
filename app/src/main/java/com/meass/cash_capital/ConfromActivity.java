package com.meass.cash_capital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class ConfromActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinnerTextSize, spinnerTextSize1, spinnerTextSize2;
    EditText Email_Log;
    String valueFromSpinner;
    String valueFromSpinner1;
    String valueFromSpinner2;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    TextView no_of_items, total_amount, spinner4;
    String package_name, package_price, packing;
    EditText spinner1, spinner2;
    Button upgrade;
    KProgressHUD kProgressHUD;
    private ClipboardManager myClipboard;
    private ClipData myClip;

    EditText myaccount;
    EditText amountdd;
    private Animation topAnimation, bottomAnimation, startAnimation, endAnimation;
    private SharedPreferences onBoardingPreference;
    LinearLayout linear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confrom);
        upgrade = findViewById(R.id.upgrade);
        amountdd=findViewById(R.id.amountdd);
        topAnimation = AnimationUtils.loadAnimation(ConfromActivity.this, R.anim.splash_top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(ConfromActivity.this, R.anim.splash_bottom_animation);
        startAnimation = AnimationUtils.loadAnimation(ConfromActivity.this, R.anim.splash_start_animation);
        endAnimation = AnimationUtils.loadAnimation(ConfromActivity.this, R.anim.splash_end_animation);
        linear=findViewById(R.id.linear);
        linear.setAnimation(endAnimation);

        kProgressHUD = KProgressHUD.create(ConfromActivity.this);

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("Depsoit");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_myarrow);
        getSupportActionBar().setElevation(10.0f);
        getSupportActionBar().setElevation(10.0f);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_myarrow);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_myarrow);
        getSupportActionBar().setElevation(10.0f);
        getSupportActionBar().setElevation(10.0f);
        getSupportActionBar().setElevation(10.0f);
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
        no_of_items = findViewById(R.id.no_of_items);
        spinner4 = findViewById(R.id.spinner4);
        total_amount = findViewById(R.id.total_amount);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        try {
            packing = getIntent().getStringExtra("packing");
            package_name = getIntent().getStringExtra("package");
            package_price = getIntent().getStringExtra("price");
            no_of_items.setText(package_name);
            total_amount.setText(package_price);
        } catch (Exception e) {
            packing = getIntent().getStringExtra("packing");
            package_name = getIntent().getStringExtra("package");
            package_price = getIntent().getStringExtra("price");
            no_of_items.setText(package_name);
            total_amount.setText(package_price);
        }
        spinnerTextSize = findViewById(R.id.spinner3);
        spinnerTextSize.setOnItemSelectedListener(this);

        String[] textSizes = getResources().getStringArray(R.array.deposite);
        ArrayAdapter adapter = new ArrayAdapter(this,
                R.layout.spinner_row, textSizes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTextSize.setAdapter(adapter);
        upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(amountdd.getText().toString())||TextUtils.isEmpty(valueFromSpinner) || TextUtils.isEmpty(spinner1.getText().toString()) ||
                        TextUtils.isEmpty(spinner2.getText().toString()) ) {
                    Toasty.error(getApplicationContext(), "Error", Toast.LENGTH_SHORT, true).show();
                    return;

                } else {
                    if (valueFromSpinner.contains("Select Payment Method")) {
                        Toasty.info(getApplicationContext(), "Select Your Payment Method", Toast.LENGTH_SHORT, true).show();
                        return;
                    } else {
                        progress_check();
                        Calendar calendar = Calendar.getInstance();
                        String current = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                        String current1 = DateFormat.getDateInstance().format(calendar.getTime());
                        firebaseFirestore.collection("Users")
                                .document(firebaseAuth.getCurrentUser().getUid())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult().exists()) {
                                                String username = task.getResult().getString("number");
                                                String uuid = UUID.randomUUID().toString();


                                                Package packag1 = new Package(firebaseAuth.getCurrentUser().getEmail(),
                                                        "Deposit Request", amountdd.getText().toString(), valueFromSpinner, spinner1.getText().toString(), spinner2.getText().toString()
                                                        , uuid, firebaseAuth.getCurrentUser().getUid(), "Pending", username, current1);
                                                int month=calendar.get(Calendar.MONTH)+1;
                                                firebaseFirestore.collection("MonthDeposit")
                                                        .document(uuid)
                                                        .set(packag1)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                            }
                                                        });
                                                firebaseFirestore.collection("Admin_PackageRequest__1")
                                                        .document(uuid)
                                                        .set(packag1)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                    firebaseFirestore.collection("Subadmin")
                                                                            .document("Packages")
                                                                            .collection("101")
                                                                            .document(uuid)
                                                                            .set(packag1)
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {



                                                                                        firebaseFirestore.collection("MyPackage")
                                                                                                .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                .collection("List")
                                                                                                .document(uuid)
                                                                                                .set(packag1)
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()) {
                                                                                                            kProgressHUD.dismiss();
                                                                                                            Toasty.success(getApplicationContext(), "Deposit Request  Successfully Done.\nAdmin Will Check Your Request.", Toast.LENGTH_SHORT, true).show();
                                                                                                            startActivity(new Intent(getApplicationContext(), HomeACTIVITY.class));
                                                                                                            finish();
                                                                                                        }
                                                                                                    }
                                                                                                });
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


        //

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinner3) {
            valueFromSpinner = parent.getItemAtPosition(position).toString();
            if (valueFromSpinner.equals("Binance")) {
                firebaseFirestore.collection("Payment")
                        .document("abc@gmail.com")
                        .collection("1")
                        .document("abc@gmail.com")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        try {
                                            spinner4.setText(task.getResult().getString("number"));
                                            String text = task.getResult().getString("number");
                                            myClip = ClipData.newPlainText("text", text);
                                            myClipboard.setPrimaryClip(myClip);
                                            Toast.makeText(getApplicationContext(), "Address Copied",
                                                    Toast.LENGTH_SHORT).show();
                                        }catch (Exception e) {
                                            spinner4.setText(task.getResult().getString("number"));
                                            String text = task.getResult().getString("number");
                                            myClip = ClipData.newPlainText("text", text);
                                            myClipboard.setPrimaryClip(myClip);
                                            Toast.makeText(getApplicationContext(), "Address Copied",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {
                                        spinner4.setText("No Number Found");
                                    }
                                }
                                else {
                                    spinner4.setText("No Number Found");
                                }
                            }
                        });
                // spinner4.setText("Call Admin");
            }  else if (valueFromSpinner.equals("Bkash")) {
                firebaseFirestore.collection("Payment")
                        .document("abc@gmail.com")
                        .collection("3")
                        .document("abc@gmail.com")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {

                                        try {
                                            spinner4.setText(task.getResult().getString("number"));
                                            String text = task.getResult().getString("number");
                                            myClip = ClipData.newPlainText("text", text);
                                            myClipboard.setPrimaryClip(myClip);
                                            Toast.makeText(getApplicationContext(), "Address Copied",
                                                    Toast.LENGTH_SHORT).show();
                                        }catch (Exception e) {
                                            spinner4.setText(task.getResult().getString("number"));
                                            String text = task.getResult().getString("number");
                                            myClip = ClipData.newPlainText("text", text);
                                            myClipboard.setPrimaryClip(myClip);
                                            Toast.makeText(getApplicationContext(), "Address Copied",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {
                                        spinner4.setText("No Number Found");
                                    }
                                }
                                else {
                                    spinner4.setText("No Number Found");
                                }
                            }
                        });
            }
            else if (valueFromSpinner.equals("Nagad")) {
                firebaseFirestore.collection("Payment")
                        .document("abc@gmail.com")
                        .collection("2")
                        .document("abc@gmail.com")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {

                                        try {
                                            spinner4.setText(task.getResult().getString("number"));
                                            String text = task.getResult().getString("number");
                                            myClip = ClipData.newPlainText("text", text);
                                            myClipboard.setPrimaryClip(myClip);
                                            Toast.makeText(getApplicationContext(), "Address Copied",
                                                    Toast.LENGTH_SHORT).show();
                                        }catch (Exception e) {
                                            spinner4.setText(task.getResult().getString("number"));
                                            String text = task.getResult().getString("number");
                                            myClip = ClipData.newPlainText("text", text);
                                            myClipboard.setPrimaryClip(myClip);
                                            Toast.makeText(getApplicationContext(), "Address Copied",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {
                                        spinner4.setText("No Number Found");
                                    }
                                }
                                else {
                                    spinner4.setText("No Number Found");
                                }
                            }
                        });
            }
            //4
            else if (valueFromSpinner.equals("Paypal")) {
                firebaseFirestore.collection("Payment")
                        .document("abc@gmail.com")
                        .collection("4")
                        .document("abc@gmail.com")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {

                                        try {
                                            spinner4.setText(task.getResult().getString("number"));
                                            String text = task.getResult().getString("number");
                                            myClip = ClipData.newPlainText("text", text);
                                            myClipboard.setPrimaryClip(myClip);
                                            Toast.makeText(getApplicationContext(), "Address Copied",
                                                    Toast.LENGTH_SHORT).show();
                                        }catch (Exception e) {
                                            spinner4.setText(task.getResult().getString("number"));
                                            String text = task.getResult().getString("number");
                                            myClip = ClipData.newPlainText("text", text);
                                            myClipboard.setPrimaryClip(myClip);
                                            Toast.makeText(getApplicationContext(), "Address Copied",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {
                                        spinner4.setText("No Number Found");
                                    }
                                }
                                else {
                                    spinner4.setText("No Number Found");
                                }
                            }
                        });
            }//5
            else if (valueFromSpinner.equals("USDT-TRON-TRC20")) {
                firebaseFirestore.collection("Payment")
                        .document("abc@gmail.com")
                        .collection("5")
                        .document("abc@gmail.com")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {

                                        try {
                                            spinner4.setText(task.getResult().getString("number"));
                                            String text = task.getResult().getString("number");
                                            myClip = ClipData.newPlainText("text", text);
                                            myClipboard.setPrimaryClip(myClip);
                                            Toast.makeText(getApplicationContext(), "Address Copied",
                                                    Toast.LENGTH_SHORT).show();
                                        }catch (Exception e) {
                                            spinner4.setText(task.getResult().getString("number"));
                                            String text = task.getResult().getString("number");
                                            myClip = ClipData.newPlainText("text", text);
                                            myClipboard.setPrimaryClip(myClip);
                                            Toast.makeText(getApplicationContext(), "Address Copied",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {
                                        spinner4.setText("No Number Found");
                                    }
                                }
                                else {
                                    spinner4.setText("No Number Found");
                                }
                            }
                        });
            }




        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void progress_check() {
        kProgressHUD = KProgressHUD.create(ConfromActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(getApplicationContext(), HomeACTIVITY.class));

        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomeACTIVITY.class));
    }
}
