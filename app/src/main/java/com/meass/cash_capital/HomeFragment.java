package com.meass.cash_capital;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogAnimation;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;
import com.thecode.aestheticdialogs.OnDialogClickListener;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;


public class HomeFragment extends Fragment {
View view;
    private HashMap<String, String> user;
    private String name, email, photo, mobile,username;
    UserSession session;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_home, container, false);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();
        session = new UserSession(view.getContext());
        getValues();

        CardView dailyCheckCard=view.findViewById(R.id.dailyCheckCard);
        dailyCheckCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.success(view.getContext(),"You are now in home",Toasty.LENGTH_SHORT,true).show();
            }
        });
        CardView luckySpinCard=view.findViewById(R.id.luckySpinCard);
        luckySpinCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),BalanceDetailsActivity.class));
            }
        });
        CardView taskCard=view.findViewById(R.id.taskCard);
        taskCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ConfromActivity.class));
            }
        });

        //4-6
        CardView referCardq=view.findViewById(R.id.referCardq);
        referCardq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),PackagesListActivity.class);
                intent.putExtra("cus_name",""+mobile);
                v.getContext().startActivity(intent);
            }
        });
        CardView watccchCard=view.findViewById(R.id.watccchCard);
        watccchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Packages_Checker")
                        .document(firebaseAuth.getCurrentUser().getEmail())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {

                                        firebaseFirestore.collection("Daily_BBonus")
                                                .document("abc@gmail.com")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            if (task.getResult().exists()) {
                                                                String checker=task.getResult().getString("checker");
                                                                if (Integer.parseInt(checker)==1) {
                                                                    new AestheticDialog.Builder((Activity) view.getContext(), DialogStyle.FLASH, DialogType.INFO)
                                                                            .setTitle("Warning")
                                                                            .setMessage("Daily work is currently off.")
                                                                            .setAnimation(DialogAnimation.SPLIT)
                                                                            .setOnClickListener(new OnDialogClickListener() {
                                                                                @Override
                                                                                public void onClick(AestheticDialog.Builder builder) {
                                                                                    builder.dismiss();

                                                                                }
                                                                            }).show();
                                                                }
                                                                else {
                                                                    AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                                                                    builder.setTitle("Confirmation")
                                                                            .setMessage("Do you want to take daily bonus?")
                                                                            .setPositiveButton("Not Now", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                    dialog.dismiss();

                                                                                }
                                                                            }).setNegativeButton("Right Now", new DialogInterface.OnClickListener() {
                                                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                            dialog.dismiss();
                                                                            Calendar calendar=Calendar.getInstance();
                                                                            final  int year=calendar.get(Calendar.YEAR);
                                                                            final int month=calendar.get(Calendar.MONTH);
                                                                            final int day=calendar.get(Calendar.DAY_OF_MONTH);
                                                                            LocalDate today = LocalDate.now(ZoneId.of("Asia/Dhaka"));
                                                                            firebaseFirestore.collection("DailyEarn")
                                                                                    .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                    .collection(""+today)
                                                                                    .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                    .get()
                                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                if (task.getResult().exists()) {
                                                                                                    new AestheticDialog.Builder((Activity) view.getContext(), DialogStyle.FLASH, DialogType.WARNING)
                                                                                                            .setTitle("Warning")
                                                                                                            .setMessage("You are alreday clime  task for today.\nWait for next day's task.")
                                                                                                            .setAnimation(DialogAnimation.SPLIT)
                                                                                                            .setOnClickListener(new OnDialogClickListener() {
                                                                                                                @Override
                                                                                                                public void onClick(AestheticDialog.Builder builder) {
                                                                                                                    builder.dismiss();

                                                                                                                }
                                                                                                            }).show();
                                                                                                }
                                                                                                else {
                                                                                                    firebaseFirestore.collection("Packages_Checker")
                                                                                                            .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                            if (task.isSuccessful()) {
                                                                                                                if (task.getResult().exists()) {
                                                                                                                    firebaseFirestore.collection("Videos")
                                                                                                                            .document("one")
                                                                                                                            .get()
                                                                                                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                                @Override
                                                                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                                    if (task.isSuccessful()) {
                                                                                                                                        if (task.getResult().exists()) {
                                                                                                                                            String price=task.getResult().getString("price");
                                                                                                                                            Intent intent=new Intent(getContext(),Video1Activity.class);
                                                                                                                                            intent.putExtra("ttttaka",""+price);
                                                                                                                                            intent.putExtra("videoid",""+task.getResult().getString("videoid"));
                                                                                                                                            getContext().startActivity(intent);
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
                                                                                            }
                                                                                            else {

                                                                                            }
                                                                                        }
                                                                                    });

                                                                        }
                                                                    }).create().show();
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                    else {
                                        Toasty.info(view.getContext(),"Please firstly active this package then click on daily bonus.",Toasty.LENGTH_SHORT,true).show();
                                        return;
                                    }
                                }
                                else {
                                    Toasty.info(view.getContext(),"Please firstly active this package then click on daily bonus.",Toasty.LENGTH_SHORT,true).show();
                                    return;
                                }
                            }
                        });
            }
        });
        CardView recccdeemCard=view.findViewById(R.id.recccdeemCard);
        recccdeemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Packages_Checker")
                        .document(firebaseAuth.getCurrentUser().getEmail())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        Intent intent=new Intent(v.getContext(),PaymentActivity.class);
                                        intent.putExtra("username",""+mobile);
                                        intent.putExtra("roomid",""+task.getResult().getString("roomid"));
                                        v.getContext().startActivity(intent);
                                    }
                                    else {
                                        Intent intent=new Intent(v.getContext(),PaymentActivity.class);
                                        intent.putExtra("username",""+mobile);
                                        intent.putExtra("roomid","90");
                                        v.getContext().startActivity(intent);
                                    }
                                }
                                else {
                                    Intent intent=new Intent(v.getContext(),PaymentActivity.class);
                                    intent.putExtra("username",""+mobile);
                                    intent.putExtra("roomid","90");
                                    v.getContext().startActivity(intent);
                                }
                            }
                        });



            }
        });
        ///7-9
        CardView referqqqCard=view.findViewById(R.id.referqqqCard);
        referqqqCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),SendMyDarling.class);
                intent.putExtra("username",""+mobile);
                v.getContext().startActivity(intent);

            }
        });
        CardView watchCard=view.findViewById(R.id.watchCard);
        watchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(getContext(),NotificationActivity.class));
            }
        });
        CardView redeemCard=view.findViewById(R.id.redeemCard);
        redeemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(getContext(),ProfileActivity.class));

            }
        });


        return view;
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
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
}