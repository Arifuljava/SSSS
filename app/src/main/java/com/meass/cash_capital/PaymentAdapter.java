package com.meass.cash_capital;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.myview> {
    public List<BloodBankModel> data;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String usernmae;

    public PaymentAdapter(List<BloodBankModel> data, String usernmae) {
        this.data = data;
        this.usernmae=usernmae;
    }

    @NonNull
    @Override
    public PaymentAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.healthcard, parent, false);
        return new PaymentAdapter.myview(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.myview holder, final int position) {
        holder.packagename.setText("Packagee Name : "+data.get(position).getName());
        holder.packagetime.setText("Package Price :  "+data.get(position).getFeeentry()+" USD\n" +
                "Daily Income : "+data.get(position).getTime()+" USD\n" +
                "Validity Time : 30 Days\n" +"" +
                        "Amount Gain : "+data.get(position).getRoomid()+" USD \n"+
                "Refer Commission : "+data.get(position).getFeeprice()+" USD");
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();
        holder.makerules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///alertdialouge
                firebaseFirestore.collection("Packages_Checker")
                        .document(firebaseAuth.getCurrentUser().getEmail())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        String package1=task.getResult().getString("package");
                                        if (package1.equals("a")) {
                                            AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                                            builder.setTitle("Confirmation")
                                                    .setMessage("Do you want to active this package right now?\n")
                                                    .setPositiveButton("Not Now", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();

                                                        }
                                                    }).setNegativeButton("Active Now", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();

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
                                                                            String pass = task.getResult().getString("purches_balance");
                                                                            if (Double.parseDouble(pass) < Double.parseDouble(data.get(position).getFeeentry())) {
                                                                                Toasty.error(v.getContext(), "User have not enough money", Toasty.LENGTH_SHORT, true).show();

                                                                            } else {
                                                                                final KProgressHUD progressDialog=  KProgressHUD.create(v.getContext())
                                                                                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                                                                        .setLabel("Checking Data.....")
                                                                                        .setCancellable(false)
                                                                                        .setAnimationSpeed(2)
                                                                                        .setDimAmount(0.5f)
                                                                                        .show();
                                                                                double mainBaki = Double.parseDouble(pass) - Double.parseDouble(data.get(position).getFeeentry());
                                                                                firebaseFirestore.collection("Users")
                                                                                        .document(firebaseAuth.getCurrentUser().getUid())
                                                                                        .collection("Main_Balance")
                                                                                        .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                        .update("purches_balance", "" + mainBaki)
                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if (task.isSuccessful()) {
                                                                                                    Map<String, String> userMap1 = new HashMap<>();

                                                                                                    userMap1.put("counter", "1");
                                                                                                    userMap1.put("package", data.get(position).getName());
                                                                                                    userMap1.put("time", data.get(position).getFeeentry());
                                                                                                    userMap1.put("refer", data.get(position).getFeeprice());
                                                                                                    userMap1.put("price", data.get(position).getTime());
                                                                                                    userMap1.put("name", data.get(position).getName());
                                                                                                    userMap1.put("income", data.get(position).getTime());
                                                                                                    userMap1.put("ads", data.get(position).getTime());
                                                                                                    userMap1.put("roomid", data.get(position).getRoomid());
                                                                                                    Map<String, String> userMap11 = new HashMap<>();

                                                                                                    userMap1.put("counter", "1");
                                                                                                    firebaseFirestore.collection("Package_takingChecker")
                                                                                                            .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                            .set(userMap11)
                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                                    if (task.isSuccessful()) {
                                                                                                                    }
                                                                                                                }
                                                                                                            });


                                                                                                    firebaseFirestore.collection("Packages_Checker")
                                                                                                            .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                            .set(userMap1)
                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                                    if (task.isSuccessful()) {
                                                                                                                        firebaseFirestore.collection("ALL_GET")
                                                                                                                                .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                                                .get()
                                                                                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                                    @Override
                                                                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                                        if (task.isSuccessful()) {
                                                                                                                                            if (task.getResult().exists()) {
                                                                                                                                                String mainuuid = UUID.randomUUID().toString();
                                                                                                                                                // new CheckInternetConnection(this).checkConnection();
                                                                                                                                                Long tsLong = System.currentTimeMillis() / 1000;
                                                                                                                                                String ts = tsLong.toString();
                                                                                                                                                final String refername = task.getResult().getString("refername");
                                                                                                                                                String referemail = task.getResult().getString("refername_email");
                                                                                                                                                //todays_freePeruser(referemail);
                                                                                                                                                // Toast.makeText(v.getContext(), ""+referemail, Toast.LENGTH_SHORT).show();

                                                                                                                                                firebaseFirestore.collection("All_UserID")
                                                                                                                                                        .document(referemail)
                                                                                                                                                        .get()
                                                                                                                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                                                            @Override
                                                                                                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                                                                if (task.isSuccessful()) {
                                                                                                                                                                    if (task.getResult().exists()) {
                                                                                                                                                                        final String uuid = task.getResult().getString("uuid");
                                                                                                                                                                        firebaseFirestore.collection("Users")
                                                                                                                                                                                .document(uuid)
                                                                                                                                                                                .collection("Main_Balance")
                                                                                                                                                                                .document(referemail)
                                                                                                                                                                                .get()
                                                                                                                                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                                                                                    @Override
                                                                                                                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                                                                                        if (task.isSuccessful()) {
                                                                                                                                                                                            if (task.getResult().exists()) {

                                                                                                                                                                                                final double a = (Double.parseDouble(data.get(position).getFeeprice()));
                                                                                                                                                                                                String main_balance = task.getResult().getString("self_income");
                                                                                                                                                                                                double Submain = Double.parseDouble(main_balance) + a;

                                                                                                                                                                                                Calendar calendar = Calendar.getInstance();
                                                                                                                                                                                                String current11 = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                                                                                                                                                                                                String current1 = DateFormat.getDateInstance().format(calendar.getTime());

                                                                                                                                                                                                Income income = new Income(refername, "Refer from " + usernmae, firebaseAuth.getCurrentUser().getEmail(),
                                                                                                                                                                                                        String.valueOf(a), current1, ts);
                                                                                                                                                                                                firebaseFirestore.collection("Refer_History").document(referemail)
                                                                                                                                                                                                        .collection("List")
                                                                                                                                                                                                        .document(UUID.randomUUID().toString())
                                                                                                                                                                                                        .set(income)
                                                                                                                                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                                                                            @Override
                                                                                                                                                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                                                                                                                                            }
                                                                                                                                                                                                        });



                                                                                                                                                                                                firebaseFirestore.collection("Users")
                                                                                                                                                                                                        .document(uuid)
                                                                                                                                                                                                        .collection("Main_Balance")
                                                                                                                                                                                                        .document(referemail)
                                                                                                                                                                                                        .update("self_income", String.valueOf(Submain)
                                                                                                                                                                                                               )
                                                                                                                                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                                                                            @Override
                                                                                                                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                                                                                                if (task.isSuccessful()) {
                                                                                                                                                                                                                    progressDialog.dismiss();
                                                                                                                                                                                                                    Toasty.success(v.getContext(), "Account Activation Successfully Done.", Toasty.LENGTH_SHORT, true).show();
                                                                                                                                                                                                                    v.getContext().startActivity(new Intent(v.getContext(),MainActivity.class));

                                                                                                                                                                                                                }
                                                                                                                                                                                                            }
                                                                                                                                                                                                        });

                                                                                                                                                                                            }
                                                                                                                                                                                        }
                                                                                                                                                                                    }
                                                                                                                                                                                });
                                                                                                                                                                    }
                                                                                                                                                                    else {
                                                                                                                                                                        progressDialog.dismiss();
                                                                                                                                                                        Toasty.success(v.getContext(), "Account Activation Successfully Done.", Toasty.LENGTH_SHORT, true).show();
                                                                                                                                                                        v.getContext().startActivity(new Intent(v.getContext(),MainActivity.class));
                                                                                                                                                                    }
                                                                                                                                                                }
                                                                                                                                                            }
                                                                                                                                                        });




                                                                                                                                            }
                                                                                                                                            else {
                                                                                                                                                progressDialog.dismiss();
                                                                                                                                                Toasty.success(v.getContext(), "Account Activation Successfully Done.", Toasty.LENGTH_SHORT, true).show();
                                                                                                                                                v.getContext().startActivity(new Intent(v.getContext(),MainActivity.class));
                                                                                                                                            }
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
                                                                }
                                                            });
                                                }
                                            }).create();
                                            builder.show();
                                        }
                                        else {
                                            AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                                            builder.setTitle("Confirmation")
                                                    .setMessage("Do you want to active this package right now?\n")
                                                    .setPositiveButton("Not Now", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();

                                                        }
                                                    }).setNegativeButton("Active Now", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();

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
                                                                            String pass = task.getResult().getString("purches_balance");
                                                                            if (Double.parseDouble(pass) < Double.parseDouble(data.get(position).getFeeentry())) {
                                                                                Toasty.error(v.getContext(), "User have not enough money", Toasty.LENGTH_SHORT, true).show();

                                                                            } else {
                                                                                final KProgressHUD progressDialog=  KProgressHUD.create(v.getContext())
                                                                                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                                                                        .setLabel("Checking Data.....")
                                                                                        .setCancellable(false)
                                                                                        .setAnimationSpeed(2)
                                                                                        .setDimAmount(0.5f)
                                                                                        .show();
                                                                                double mainBaki = Double.parseDouble(pass) - Double.parseDouble(data.get(position).getFeeentry());
                                                                                firebaseFirestore.collection("Users")
                                                                                        .document(firebaseAuth.getCurrentUser().getUid())
                                                                                        .collection("Main_Balance")
                                                                                        .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                        .update("purches_balance", "" + mainBaki)
                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if (task.isSuccessful()) {
                                                                                                    Map<String, String> userMap1 = new HashMap<>();

                                                                                                    userMap1.put("counter", "1");
                                                                                                    userMap1.put("package", data.get(position).getName());
                                                                                                    userMap1.put("time", data.get(position).getFeeentry());
                                                                                                    userMap1.put("refer", data.get(position).getFeeprice());
                                                                                                    userMap1.put("price", data.get(position).getTime());
                                                                                                    userMap1.put("name", data.get(position).getName());
                                                                                                    userMap1.put("income", data.get(position).getTime());
                                                                                                    userMap1.put("ads", data.get(position).getTime());
                                                                                                    userMap1.put("roomid", data.get(position).getRoomid());
                                                                                                    Map<String, String> userMap11 = new HashMap<>();

                                                                                                    userMap1.put("counter", "1");
                                                                                                    firebaseFirestore.collection("Package_takingChecker")
                                                                                                            .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                            .set(userMap11)
                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                                    if (task.isSuccessful()) {
                                                                                                                    }
                                                                                                                }
                                                                                                            });


                                                                                                    firebaseFirestore.collection("Packages_Checker")
                                                                                                            .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                            .set(userMap1)
                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                                    if (task.isSuccessful()) {
                                                                                                                        firebaseFirestore.collection("ALL_GET")
                                                                                                                                .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                                                .get()
                                                                                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                                    @Override
                                                                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                                        if (task.isSuccessful()) {
                                                                                                                                            if (task.getResult().exists()) {
                                                                                                                                                String mainuuid = UUID.randomUUID().toString();
                                                                                                                                                // new CheckInternetConnection(this).checkConnection();
                                                                                                                                                Long tsLong = System.currentTimeMillis() / 1000;
                                                                                                                                                String ts = tsLong.toString();
                                                                                                                                                final String refername = task.getResult().getString("refername");
                                                                                                                                                String referemail = task.getResult().getString("refername_email");
                                                                                                                                                //todays_freePeruser(referemail);
                                                                                                                                                // Toast.makeText(v.getContext(), ""+referemail, Toast.LENGTH_SHORT).show();

                                                                                                                                                firebaseFirestore.collection("All_UserID")
                                                                                                                                                        .document(referemail)
                                                                                                                                                        .get()
                                                                                                                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                                                            @Override
                                                                                                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                                                                if (task.isSuccessful()) {
                                                                                                                                                                    if (task.getResult().exists()) {
                                                                                                                                                                        final String uuid = task.getResult().getString("uuid");
                                                                                                                                                                        firebaseFirestore.collection("Users")
                                                                                                                                                                                .document(uuid)
                                                                                                                                                                                .collection("Main_Balance")
                                                                                                                                                                                .document(referemail)
                                                                                                                                                                                .get()
                                                                                                                                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                                                                                    @Override
                                                                                                                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                                                                                        if (task.isSuccessful()) {
                                                                                                                                                                                            if (task.getResult().exists()) {

                                                                                                                                                                                                final double a = (Double.parseDouble(data.get(position).getFeeprice()));
                                                                                                                                                                                                String main_balance = task.getResult().getString("self_income");
                                                                                                                                                                                                double Submain = Double.parseDouble(main_balance) + a;

                                                                                                                                                                                                Calendar calendar = Calendar.getInstance();
                                                                                                                                                                                                String current11 = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                                                                                                                                                                                                String current1 = DateFormat.getDateInstance().format(calendar.getTime());

                                                                                                                                                                                                Income income = new Income(refername, "Refer from " + usernmae, firebaseAuth.getCurrentUser().getEmail(),
                                                                                                                                                                                                        String.valueOf(a), current1, ts);
                                                                                                                                                                                                firebaseFirestore.collection("Refer_History").document(referemail)
                                                                                                                                                                                                        .collection("List")
                                                                                                                                                                                                        .document(UUID.randomUUID().toString())
                                                                                                                                                                                                        .set(income)
                                                                                                                                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                                                                            @Override
                                                                                                                                                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                                                                                                                                            }
                                                                                                                                                                                                        });



                                                                                                                                                                                                firebaseFirestore.collection("Users")
                                                                                                                                                                                                        .document(uuid)
                                                                                                                                                                                                        .collection("Main_Balance")
                                                                                                                                                                                                        .document(referemail)
                                                                                                                                                                                                        .update("self_income", String.valueOf(Submain)
                                                                                                                                                                                                        )
                                                                                                                                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                                                                            @Override
                                                                                                                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                                                                                                if (task.isSuccessful()) {
                                                                                                                                                                                                                    progressDialog.dismiss();
                                                                                                                                                                                                                    Toasty.success(v.getContext(), "Account Activation Successfully Done.", Toasty.LENGTH_SHORT, true).show();
                                                                                                                                                                                                                    v.getContext().startActivity(new Intent(v.getContext(),MainActivity.class));

                                                                                                                                                                                                                }
                                                                                                                                                                                                            }
                                                                                                                                                                                                        });

                                                                                                                                                                                            }
                                                                                                                                                                                        }
                                                                                                                                                                                    }
                                                                                                                                                                                });
                                                                                                                                                                    }
                                                                                                                                                                    else {
                                                                                                                                                                        progressDialog.dismiss();
                                                                                                                                                                        Toasty.success(v.getContext(), "Account Activation Successfully Done.", Toasty.LENGTH_SHORT, true).show();
                                                                                                                                                                        v.getContext().startActivity(new Intent(v.getContext(),MainActivity.class));
                                                                                                                                                                    }
                                                                                                                                                                }
                                                                                                                                                            }
                                                                                                                                                        });




                                                                                                                                            }
                                                                                                                                            else {
                                                                                                                                                progressDialog.dismiss();
                                                                                                                                                Toasty.success(v.getContext(), "Account Activation Successfully Done.", Toasty.LENGTH_SHORT, true).show();
                                                                                                                                                v.getContext().startActivity(new Intent(v.getContext(),MainActivity.class));
                                                                                                                                            }
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
                                                                }
                                                            });
                                                }
                                            }).create();
                                            builder.show();
                                        }
                                    }
                                    else {
                                        AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                                        builder.setTitle("Confirmation")
                                                .setMessage("Do you want to active this package right now?\n")
                                                .setPositiveButton("Not Now", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();

                                                    }
                                                }).setNegativeButton("Active Now", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();

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
                                                                        String pass = task.getResult().getString("purches_balance");
                                                                        if (Double.parseDouble(pass) < Double.parseDouble(data.get(position).getFeeentry())) {
                                                                            Toasty.error(v.getContext(), "User have not enough money", Toasty.LENGTH_SHORT, true).show();

                                                                        } else {
                                                                            final KProgressHUD progressDialog=  KProgressHUD.create(v.getContext())
                                                                                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                                                                    .setLabel("Checking Data.....")
                                                                                    .setCancellable(false)
                                                                                    .setAnimationSpeed(2)
                                                                                    .setDimAmount(0.5f)
                                                                                    .show();
                                                                            double mainBaki = Double.parseDouble(pass) - Double.parseDouble(data.get(position).getFeeentry());
                                                                            firebaseFirestore.collection("Users")
                                                                                    .document(firebaseAuth.getCurrentUser().getUid())
                                                                                    .collection("Main_Balance")
                                                                                    .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                    .update("purches_balance", "" + mainBaki)
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                Map<String, String> userMap1 = new HashMap<>();

                                                                                                userMap1.put("counter", "1");
                                                                                                userMap1.put("package", data.get(position).getName());
                                                                                                userMap1.put("time", data.get(position).getFeeentry());
                                                                                                userMap1.put("refer", data.get(position).getFeeprice());
                                                                                                userMap1.put("price", data.get(position).getTime());
                                                                                                userMap1.put("name", data.get(position).getName());
                                                                                                userMap1.put("income", data.get(position).getTime());
                                                                                                userMap1.put("ads", data.get(position).getTime());
                                                                                                userMap1.put("roomid", data.get(position).getRoomid());
                                                                                                Map<String, String> userMap11 = new HashMap<>();

                                                                                                userMap1.put("counter", "1");
                                                                                                firebaseFirestore.collection("Package_takingChecker")
                                                                                                        .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                        .set(userMap11)
                                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                if (task.isSuccessful()) {
                                                                                                                }
                                                                                                            }
                                                                                                        });


                                                                                                firebaseFirestore.collection("Packages_Checker")
                                                                                                        .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                        .set(userMap1)
                                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                if (task.isSuccessful()) {
                                                                                                                    firebaseFirestore.collection("ALL_GET")
                                                                                                                            .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                                            .get()
                                                                                                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                                @Override
                                                                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                                    if (task.isSuccessful()) {
                                                                                                                                        if (task.getResult().exists()) {
                                                                                                                                            String mainuuid = UUID.randomUUID().toString();
                                                                                                                                            // new CheckInternetConnection(this).checkConnection();
                                                                                                                                            Long tsLong = System.currentTimeMillis() / 1000;
                                                                                                                                            String ts = tsLong.toString();
                                                                                                                                            final String refername = task.getResult().getString("refername");
                                                                                                                                            String referemail = task.getResult().getString("refername_email");
                                                                                                                                            //todays_freePeruser(referemail);
                                                                                                                                            // Toast.makeText(v.getContext(), ""+referemail, Toast.LENGTH_SHORT).show();

                                                                                                                                            firebaseFirestore.collection("All_UserID")
                                                                                                                                                    .document(referemail)
                                                                                                                                                    .get()
                                                                                                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                                                        @Override
                                                                                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                                                            if (task.isSuccessful()) {
                                                                                                                                                                if (task.getResult().exists()) {
                                                                                                                                                                    final String uuid = task.getResult().getString("uuid");
                                                                                                                                                                    firebaseFirestore.collection("Users")
                                                                                                                                                                            .document(uuid)
                                                                                                                                                                            .collection("Main_Balance")
                                                                                                                                                                            .document(referemail)
                                                                                                                                                                            .get()
                                                                                                                                                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                                                                                @Override
                                                                                                                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                                                                                    if (task.isSuccessful()) {
                                                                                                                                                                                        if (task.getResult().exists()) {

                                                                                                                                                                                            final double a = (Double.parseDouble(data.get(position).getFeeprice()));
                                                                                                                                                                                            String main_balance = task.getResult().getString("self_income");
                                                                                                                                                                                            double Submain = Double.parseDouble(main_balance) + a;

                                                                                                                                                                                            Calendar calendar = Calendar.getInstance();
                                                                                                                                                                                            String current11 = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                                                                                                                                                                                            String current1 = DateFormat.getDateInstance().format(calendar.getTime());

                                                                                                                                                                                            Income income = new Income(refername, "Refer from " + usernmae, firebaseAuth.getCurrentUser().getEmail(),
                                                                                                                                                                                                    String.valueOf(a), current1, ts);
                                                                                                                                                                                            firebaseFirestore.collection("Refer_History").document(referemail)
                                                                                                                                                                                                    .collection("List")
                                                                                                                                                                                                    .document(UUID.randomUUID().toString())
                                                                                                                                                                                                    .set(income)
                                                                                                                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                                                                        @Override
                                                                                                                                                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                                                                                                                                                        }
                                                                                                                                                                                                    });



                                                                                                                                                                                            firebaseFirestore.collection("Users")
                                                                                                                                                                                                    .document(uuid)
                                                                                                                                                                                                    .collection("Main_Balance")
                                                                                                                                                                                                    .document(referemail)
                                                                                                                                                                                                    .update("self_income", String.valueOf(Submain)
                                                                                                                                                                                                    )
                                                                                                                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                                                                        @Override
                                                                                                                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                                                                                            if (task.isSuccessful()) {
                                                                                                                                                                                                                progressDialog.dismiss();
                                                                                                                                                                                                                Toasty.success(v.getContext(), "Account Activation Successfully Done.", Toasty.LENGTH_SHORT, true).show();
                                                                                                                                                                                                                v.getContext().startActivity(new Intent(v.getContext(),MainActivity.class));

                                                                                                                                                                                                            }
                                                                                                                                                                                                        }
                                                                                                                                                                                                    });

                                                                                                                                                                                        }
                                                                                                                                                                                    }
                                                                                                                                                                                }
                                                                                                                                                                            });
                                                                                                                                                                }
                                                                                                                                                                else {
                                                                                                                                                                    progressDialog.dismiss();
                                                                                                                                                                    Toasty.success(v.getContext(), "Account Activation Successfully Done.", Toasty.LENGTH_SHORT, true).show();
                                                                                                                                                                    v.getContext().startActivity(new Intent(v.getContext(),MainActivity.class));
                                                                                                                                                                }
                                                                                                                                                            }
                                                                                                                                                        }
                                                                                                                                                    });




                                                                                                                                        }
                                                                                                                                        else {
                                                                                                                                            progressDialog.dismiss();
                                                                                                                                            Toasty.success(v.getContext(), "Account Activation Successfully Done.", Toasty.LENGTH_SHORT, true).show();
                                                                                                                                            v.getContext().startActivity(new Intent(v.getContext(),MainActivity.class));
                                                                                                                                        }
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
                                                            }
                                                        });
                                            }
                                        }).create();
                                        builder.show();
                                    }
                                }
                            }
                        });

            }
        });


    }

    private void generation_bonus1(String bronze, String email, String uid, String refername, String teamB, String teamB1, String refer, String price, String price1) {
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myview extends RecyclerView.ViewHolder {
      TextView packagename,packagetime;
      Button makerules;

        public myview(@NonNull View itemView) {
            super(itemView);
            packagename=itemView.findViewById(R.id.packagename);
            packagetime=itemView.findViewById(R.id.packagetime);
            makerules=itemView.findViewById(R.id.makerules);

        }
    }
}
