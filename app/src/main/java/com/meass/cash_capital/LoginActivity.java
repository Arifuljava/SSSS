package com.meass.cash_capital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flatdialoglibrary.dialog.FlatDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    private EditText edtemail,edtpass;
    private String email,pass,email_gating;
    private TextView appname,forgotpass,registernow;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser user;
    private String userID;
    UserSession session;
    FirebaseAuth firebaseAuth;
    Long tsLong = System.currentTimeMillis()/1000;
    String ts = tsLong.toString();
    //Other Variables
    private Animation topAnimation, bottomAnimation, startAnimation, endAnimation;
    private SharedPreferences onBoardingPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        topAnimation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.splash_top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.splash_bottom_animation);
        startAnimation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.splash_start_animation);
        endAnimation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.splash_end_animation);
        Log.e("Login CheckPoint","LoginActivity started");
        //check Internet Connection
        session= new UserSession(getApplicationContext());
        //new CheckInternetConnection(this).checkConnection();
        firebaseAuth=FirebaseAuth.getInstance();

        FirebaseApp.initializeApp(LoginActivity.this);
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);
        // appname = findViewById(R.id.appname);
        // appname.setTypeface(typeface);

        edtemail= findViewById(R.id.editTextEmail);
        edtpass= findViewById(R.id.editTextPassword);
        edtemail.setAnimation(startAnimation);
        edtpass.setAnimation(endAnimation);
        TextView sign_in_title=findViewById(R.id.sign_in_title);
        sign_in_title.setAnimation(topAnimation);
        TextView sign_in_subtitle=findViewById(R.id.sign_in_subtitle);
        sign_in_subtitle.setAnimation(bottomAnimation);
//////
        TextView sign_up_desc=findViewById(R.id.sign_up_desc);
        sign_up_desc.setAnimation(topAnimation);
        TextView sign_up_btn=findViewById(R.id.sign_up_btn);
        sign_up_btn.setAnimation(bottomAnimation);
        ///Imageview
        ImageView close_btn=findViewById(R.id.close_btn);
        close_btn.setAnimation(topAnimation);
        ImageView sign_in_illustration=findViewById(R.id.sign_in_illustration);
        sign_in_illustration.setAnimation(endAnimation);



        //if user wants to register
        TextView viewForgotPAssword= findViewById(R.id.forgot_password);
        viewForgotPAssword.setAnimation(endAnimation);
        viewForgotPAssword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FlatDialog flatDialog1 = new FlatDialog(LoginActivity.this);
                flatDialog1.setTitle("Foeget Password")
                        .setSubtitle("User forget his/her password.Now  you can change it.")
                        .setFirstTextFieldHint("Username")
                        .setSecondTextFieldHint("New Password")
                        .setFirstButtonText("Change")
                        .setSecondButtonText("Cancel")
                        .withFirstButtonListner(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                flatDialog1.dismiss();
                                final KProgressHUD progressDialog=  KProgressHUD.create(LoginActivity.this)
                                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                        .setLabel("Please wait")
                                        .setCancellable(false)
                                        .setAnimationSpeed(2)
                                        .setDimAmount(0.5f)
                                        .show();
                                firebaseFirestore.collection("Password")
                                        .document(flatDialog1.getFirstTextField().toLowerCase().toString()+"@gmail.com")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful())
                                                {
                                                    if (task.getResult().exists()) {
                                                        firebaseFirestore.collection("Password")
                                                                .document(flatDialog1.getFirstTextField().toLowerCase().toString()+"@gmail.com")
                                                                .update("uuid",flatDialog1.getSecondTextField().toLowerCase().toString())
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            progressDialog.dismiss();

                                                                            Toasty.success(getApplicationContext(),"Password Changed", Toast.LENGTH_SHORT,true).show();
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                    else {
                                                        progressDialog.dismiss();

                                                        Toasty.error(getApplicationContext(),"No user Found", Toast.LENGTH_SHORT,true).show();
                                                    }
                                                }
                                            }
                                        });

                            }
                        })
                        .withSecondButtonListner(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                flatDialog1.dismiss();
                            }
                        })
                        .show();
            }
        });
        ////
//Validating login details
        ConstraintLayout login_button=findViewById(R.id.sign_in_btn);
        login_button.setAnimation(startAnimation);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                String text1=edtemail.getText().toString();;
                String tet2=edtpass.getText().toString();
                if (TextUtils.isEmpty(text1)||TextUtils.isEmpty(tet2)) {
                    Toasty.error(getApplicationContext(),"Give all information", Toast.LENGTH_SHORT,true).show();
                }
                else {

                    email_gating=edtemail.getText().toString();
                    email=email_gating+"@gmail.com";
                    // Toast.makeText(LoginActivity.this, ""+email, Toast.LENGTH_SHORT).show();

                    pass=edtpass.getText().toString();
                    final KProgressHUD progressDialog=  KProgressHUD.create(LoginActivity.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setLabel("Checking Data.....")
                            .setCancellable(false)
                            .setAnimationSpeed(2)
                            .setDimAmount(0.5f)
                            .show();
                    firebaseFirestore.collection("BlockList")
                            .document(text1.toLowerCase() +"@gmail.com")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().exists()) {
                                            progressDialog.dismiss();
                                            Toasty.error(getApplicationContext(),"You  are in block list.", Toast.LENGTH_SHORT,true).show();
                                        }
                                        else {
                                            firebaseFirestore.collection("User2").document(text1.toLowerCase() +"@gmail.com")
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                if (task.getResult().exists()) {
                                                                    firebaseAuth.signInWithEmailAndPassword(text1.toLowerCase() +"@gmail.com","123456").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                                                            if(task.isSuccessful()){
                                                                                firebaseFirestore.collection("Password")
                                                                                        .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                        .get()
                                                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                if (task.isSuccessful()) {
                                                                                                    if (task.getResult().exists()) {
                                                                                                        String pass=task.getResult().getString("uuid");
                                                                                                        if (pass.contains(tet2.toLowerCase())) {
                                                                                                            userID = firebaseAuth.getCurrentUser().getUid();
                                                                                                            firebaseFirestore.collection("Users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                                                                    if (task.isSuccessful()) {

                                                                                                                        if (task.getResult().exists()) {


                                                                                                                            String sessionname = task.getResult().getString("name");
                                                                                                                            String sessionmobile = task.getResult().getString("number");
                                                                                                                            String sessionphoto = task.getResult().getString("image");
                                                                                                                            String sessionemail = task.getResult().getString("email");
                                                                                                                            String sessionusername = task.getResult().getString("username");


                                                                                                                            session.createLoginSession(sessionname,sessionemail,sessionmobile,sessionphoto,sessionusername);


                                                                                                                            Toasty.success(getApplicationContext(), "Login Successfully .", Toasty.LENGTH_SHORT, true).show();

                                                                                                                            Intent loginSuccess = new Intent(LoginActivity.this, HomeACTIVITY.class);

                                                                                                                            startActivity(loginSuccess);
                                                                                                                            finish();

                                                                                                                        }
                                                                                                                    } else {
                                                                                                                        firebaseAuth.signOut();
                                                                                                                        progressDialog.dismiss();
                                                                                                                        Toast.makeText(LoginActivity.this, "Login Error. Please try again.", Toast.LENGTH_SHORT).show();
                                                                                                                    }

                                                                                                                }
                                                                                                            });
                                                                                                        }
                                                                                                        else {
                                                                                                            firebaseAuth.signOut();
                                                                                                            progressDialog.dismiss();
                                                                                                            Toasty.error(getApplicationContext(),"Password not match", Toast.LENGTH_SHORT,true).show();
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        });





                                                                            } else {
                                                                                firebaseAuth.signOut();
                                                                                progressDialog.dismiss();
                                                                                Toasty.error(LoginActivity.this,"Couldn't Log In. Please check your Email/Password",2000).show();
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                                else {
                                                                    ////
                                                                    firebaseFirestore.collection("Users3").document(text1.toLowerCase())
                                                                            .get()
                                                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        if (task.getResult().exists()) {
                                                                                            String number=task.getResult().getString("number");
                                                                                            firebaseAuth.signInWithEmailAndPassword(number +"@gmail.com","123456").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<AuthResult> task) {

                                                                                                    if(task.isSuccessful()){
                                                                                                        firebaseFirestore.collection("Password")
                                                                                                                .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                                .get()
                                                                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                    @Override
                                                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                        if (task.isSuccessful()) {
                                                                                                                            if (task.getResult().exists()) {
                                                                                                                                String pass=task.getResult().getString("uuid");
                                                                                                                                if (pass.contains(tet2.toLowerCase())) {
                                                                                                                                    userID = firebaseAuth.getCurrentUser().getUid();
                                                                                                                                    firebaseFirestore.collection("Users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                                        @Override
                                                                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                                                                                            if (task.isSuccessful()) {

                                                                                                                                                if (task.getResult().exists()) {


                                                                                                                                                    String sessionname = task.getResult().getString("name");
                                                                                                                                                    String sessionmobile = task.getResult().getString("number");
                                                                                                                                                    String sessionphoto = task.getResult().getString("image");
                                                                                                                                                    String sessionemail = task.getResult().getString("email");
                                                                                                                                                    String sessionusername = task.getResult().getString("username");


                                                                                                                                                    session.createLoginSession(sessionname,sessionemail,sessionmobile,sessionphoto,sessionusername);


                                                                                                                                                    Toasty.success(getApplicationContext(), "Login Successfully .", Toasty.LENGTH_SHORT, true).show();

                                                                                                                                                    Intent loginSuccess = new Intent(LoginActivity.this, HomeACTIVITY.class);

                                                                                                                                                    startActivity(loginSuccess);
                                                                                                                                                    finish();

                                                                                                                                                }
                                                                                                                                            } else {
                                                                                                                                                firebaseAuth.signOut();
                                                                                                                                                progressDialog.dismiss();
                                                                                                                                                Toast.makeText(LoginActivity.this, "Login Error. Please try again.", Toast.LENGTH_SHORT).show();
                                                                                                                                            }

                                                                                                                                        }
                                                                                                                                    });
                                                                                                                                }
                                                                                                                                else {
                                                                                                                                    firebaseAuth.signOut();
                                                                                                                                    progressDialog.dismiss();
                                                                                                                                    Toasty.error(getApplicationContext(),"Password not match", Toast.LENGTH_SHORT,true).show();
                                                                                                                                }
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }
                                                                                                                });





                                                                                                    } else {
                                                                                                        firebaseAuth.signOut();
                                                                                                        progressDialog.dismiss();
                                                                                                        Toasty.error(LoginActivity.this,"Couldn't Log In. Please check your Email/Password",2000).show();
                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                        }
                                                                                        else {
                                                                                            progressDialog.dismiss();
                                                                                            Toasty.error(LoginActivity.this,"You are not a valid user . Please register first.",2000).show();
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
                                }
                            });

                    /*
                    final KProgressHUD progressDialog=  KProgressHUD.create(LoginActivity.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setLabel("Checking Data.....")
                            .setCancellable(false)
                            .setAnimationSpeed(2)
                            .setDimAmount(0.5f)
                            .show();
                    firebaseFirestore.collection("BlockList")
                            .document(text1.toLowerCase() +"@gmail.com")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().exists()) {
                                            progressDialog.dismiss();
                                            Toasty.error(getApplicationContext(),"You  are in block list.", Toast.LENGTH_SHORT,true).show();
                                        }
                                        else {
                                            firebaseFirestore.collection("User2").document(text1.toLowerCase() +"@gmail.com")
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                if (task.getResult().exists()) {
                                                                    firebaseAuth.signInWithEmailAndPassword(text1.toLowerCase() +"@gmail.com","123456").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                                                            if(task.isSuccessful()){
                                                                                firebaseFirestore.collection("Password")
                                                                                        .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                        .get()
                                                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                if (task.isSuccessful()) {
                                                                                                    if (task.getResult().exists()) {
                                                                                                        String pass=task.getResult().getString("uuid");
                                                                                                        if (pass.contains(tet2.toLowerCase())) {
                                                                                                            userID = firebaseAuth.getCurrentUser().getUid();
                                                                                                            firebaseFirestore.collection("Users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                                                                    if (task.isSuccessful()) {

                                                                                                                        if (task.getResult().exists()) {


                                                                                                                            String sessionname = task.getResult().getString("name");
                                                                                                                            String sessionmobile = task.getResult().getString("number");
                                                                                                                            String sessionphoto = task.getResult().getString("image");
                                                                                                                            String sessionemail = task.getResult().getString("email");
                                                                                                                            String sessionusername = task.getResult().getString("username");


                                                                                                                            session.createLoginSession(sessionname,sessionemail,sessionmobile,sessionphoto,sessionusername);


                                                                                                                            Toasty.success(getApplicationContext(), "Login Successfully .", Toasty.LENGTH_SHORT, true).show();

                                                                                                                            Intent loginSuccess = new Intent(LoginActivity.this, HomeACTIVITY.class);

                                                                                                                            startActivity(loginSuccess);
                                                                                                                            finish();

                                                                                                                        }
                                                                                                                    } else {
                                                                                                                        firebaseAuth.signOut();
                                                                                                                        progressDialog.dismiss();
                                                                                                                        Toast.makeText(LoginActivity.this, "Login Error. Please try again.", Toast.LENGTH_SHORT).show();
                                                                                                                    }

                                                                                                                }
                                                                                                            });
                                                                                                        }
                                                                                                        else {
                                                                                                            firebaseAuth.signOut();
                                                                                                            progressDialog.dismiss();
                                                                                                            Toasty.error(getApplicationContext(),"Password not match", Toast.LENGTH_SHORT,true).show();
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        });





                                                                            } else {
                                                                                firebaseAuth.signOut();
                                                                                progressDialog.dismiss();
                                                                                Toasty.error(LoginActivity.this,"Couldn't Log In. Please check your Email/Password",2000).show();
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                                else {
                                                                    ////
                                                                    firebaseFirestore.collection("Users3").document(text1.toLowerCase())
                                                                            .get()
                                                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        if (task.getResult().exists()) {
                                                                                            String number=task.getResult().getString("number");
                                                                                            firebaseAuth.signInWithEmailAndPassword(number +"@gmail.com","123456").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<AuthResult> task) {

                                                                                                    if(task.isSuccessful()){
                                                                                                        firebaseFirestore.collection("Password")
                                                                                                                .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                                .get()
                                                                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                    @Override
                                                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                        if (task.isSuccessful()) {
                                                                                                                            if (task.getResult().exists()) {
                                                                                                                                String pass=task.getResult().getString("uuid");
                                                                                                                                if (pass.contains(tet2.toLowerCase())) {
                                                                                                                                    userID = firebaseAuth.getCurrentUser().getUid();
                                                                                                                                    firebaseFirestore.collection("Users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                                        @Override
                                                                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                                                                                            if (task.isSuccessful()) {

                                                                                                                                                if (task.getResult().exists()) {


                                                                                                                                                    String sessionname = task.getResult().getString("name");
                                                                                                                                                    String sessionmobile = task.getResult().getString("number");
                                                                                                                                                    String sessionphoto = task.getResult().getString("image");
                                                                                                                                                    String sessionemail = task.getResult().getString("email");
                                                                                                                                                    String sessionusername = task.getResult().getString("username");


                                                                                                                                                    session.createLoginSession(sessionname,sessionemail,sessionmobile,sessionphoto,sessionusername);


                                                                                                                                                    Toasty.success(getApplicationContext(), "Login Successfully .", Toasty.LENGTH_SHORT, true).show();

                                                                                                                                                    Intent loginSuccess = new Intent(LoginActivity.this, HomeACTIVITY.class);

                                                                                                                                                    startActivity(loginSuccess);
                                                                                                                                                    finish();

                                                                                                                                                }
                                                                                                                                            } else {
                                                                                                                                                firebaseAuth.signOut();
                                                                                                                                                progressDialog.dismiss();
                                                                                                                                                Toast.makeText(LoginActivity.this, "Login Error. Please try again.", Toast.LENGTH_SHORT).show();
                                                                                                                                            }

                                                                                                                                        }
                                                                                                                                    });
                                                                                                                                }
                                                                                                                                else {
                                                                                                                                    firebaseAuth.signOut();
                                                                                                                                    progressDialog.dismiss();
                                                                                                                                    Toasty.error(getApplicationContext(),"Password not match", Toast.LENGTH_SHORT,true).show();
                                                                                                                                }
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }
                                                                                                                });





                                                                                                    } else {
                                                                                                        firebaseAuth.signOut();
                                                                                                        progressDialog.dismiss();
                                                                                                        Toasty.error(LoginActivity.this,"Couldn't Log In. Please check your Email/Password",2000).show();
                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                        }
                                                                                        else {
                                                                                            progressDialog.dismiss();
                                                                                            Toasty.error(LoginActivity.this,"You are not a valid user . Please register first.",2000).show();
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
                                }
                            });
                     */
                }


//////////////////////////


            }
        });

    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

    public void viewRegisterClicked(View view) {
        startActivity(new Intent(getApplicationContext(),Register2.class));
    }
    public void foegdet(View view) {
        final FlatDialog flatDialog1 = new FlatDialog(LoginActivity.this);
        flatDialog1.setTitle("Foeget Password")
                .setSubtitle("User forget his/her password.Now  you can change it.")
                .setFirstTextFieldHint("Mobile Number")
                .setSecondTextFieldHint("Password")
                .setFirstButtonText("Change")
                .setSecondButtonText("Cancel")
                .withFirstButtonListner(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        flatDialog1.dismiss();
                        final KProgressHUD progressDialog=  KProgressHUD.create(LoginActivity.this)
                                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                .setLabel("Please wait")
                                .setCancellable(false)
                                .setAnimationSpeed(2)
                                .setDimAmount(0.5f)
                                .show();
                        firebaseFirestore.collection("Password")
                                .document(flatDialog1.getFirstTextField().toLowerCase().toString()+"@gmail.com")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful())
                                        {
                                            if (task.getResult().exists()) {
                                                firebaseFirestore.collection("Password")
                                                        .document(flatDialog1.getFirstTextField().toLowerCase().toString()+"@gmail.com")
                                                        .update("uuid",flatDialog1.getSecondTextField().toLowerCase().toString())
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    progressDialog.dismiss();

                                                                    Toasty.success(getApplicationContext(),"Password Changed", Toast.LENGTH_SHORT,true).show();
                                                                }
                                                            }
                                                        });
                                            }
                                            else {
                                                progressDialog.dismiss();

                                                Toasty.error(getApplicationContext(),"No user Found", Toast.LENGTH_SHORT,true).show();
                                            }
                                        }
                                    }
                                });

                    }
                })
                .withSecondButtonListner(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        flatDialog1.dismiss();
                    }
                })
                .show();
    }

    public void close(View view) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}
