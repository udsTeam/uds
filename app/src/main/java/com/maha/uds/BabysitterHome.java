package com.maha.uds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maha.uds.Chat.ChatActivity;
import com.maha.uds.Chat.ChatKeys;
import com.maha.uds.Chat.FirebaseManager;
import com.maha.uds.Chat.MessageModel;
import com.maha.uds.Chat.MyNotificationManager;
import com.maha.uds.Chat.SharedPrefsKeys;
import com.maha.uds.Chat.SharedPrefsManager;
import com.maha.uds.Model.AccountModel;
import com.maha.uds.Model.OrderModel;

import java.util.List;

public class BabysitterHome extends AppCompatActivity{

    TextView nameView;
    String displayName;
    Intent mIntent;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseAuth mAuth;
    private Button viewOrdersBtn;
    private Button profile;
    private Button logout;
    private Button scheduleBtn;
    private Button reportBtn;
    private Button chatBtn;
    static  OrderModel mOrderModel;
    static String mOrderKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.babysitter_home);
        setUIview();
        setupFirebaseListener();
        //setupDisplayName();
        readChatNotification();


        mIntent = new Intent(BabysitterHome.this,BabysitterProfile.class);
        mAuth = FirebaseAuth.getInstance();


        getMyOrder();

        FirebaseDatabase.getInstance().getReference("accounts").child(mAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        AccountModel accountModel = dataSnapshot.getValue(AccountModel.class);
                        displayName = accountModel.getName();
                        nameView.setText(displayName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(mIntent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
            }
        });




/*

        FirebaseDatabase.getInstance().getReference("accounts").child(mAuth.getCurrentUser().getUid())
                .child("status")
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String status = dataSnapshot.getValue(String.class);

                    if (status.equals("busy")){
                        reportBtn.setVisibility(View.VISIBLE);
                        chatBtn.setVisibility(View.VISIBLE);
                        scheduleBtn.setVisibility(View.VISIBLE);
                        viewOrdersBtn.setVisibility(View.GONE);

                        scheduleBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(BabysitterHome.this,WorkSchedule.class));
                            }
                        });
                        reportBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(BabysitterHome.this,DailyReport.class));
                            }
                        });
                        chatBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(BabysitterHome.this, ChatActivity.class));
                            }
                        });
                    }else {
                        reportBtn.setVisibility(View.GONE);
                        chatBtn.setVisibility(View.GONE);
                        scheduleBtn.setVisibility(View.GONE);
                        viewOrdersBtn.setVisibility(View.VISIBLE);

                        viewOrdersBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(BabysitterHome.this,OrdersList.class));
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/

    }
    public void setUIview(){
        nameView = findViewById(R.id.name_view);
        viewOrdersBtn = findViewById(R.id.viewOrder_btn);
        scheduleBtn = findViewById(R.id.payment_btn);
        reportBtn = findViewById(R.id.dailyReports_btn);
        chatBtn = findViewById(R.id.chat_btn);
        profile = findViewById(R.id.profile_btn);
        logout = findViewById(R.id.logOut_btn);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
        }
    }

    private void setupFirebaseListener() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    user.getUid();
                } else {
                    Intent intent = new Intent(BabysitterHome.this, SignIn.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }

//    private void setupDisplayName() {
//        SharedPreferences prefs = getSharedPreferences(BabysitterRegister.CHAT_PREFS, MODE_PRIVATE);
//        displayName = prefs.getString(BabysitterRegister.DISPLAY_NAME, null);
//        if (displayName == null) {
//            displayName = "Anonymous";
//        }
//    }

    private void readChatNotification() {
        String orderId="Test233";
        FirebaseManager.readChat(orderId, new FirebaseManager.OnMessagesRetrieved() {
            @Override
            public void DataIsLoaded(List<MessageModel> messageModels, List<String> keys) {
                String FirebaeChatID = keys.get(keys.size()-1);
                MessageModel mMessageModel = messageModels.get(messageModels.size()-1);
                String LocalChatID = SharedPrefsManager.getInstance().getString(SharedPrefsKeys.CHAT_ID,"Empty");
                if (!mMessageModel.getSenderID().equals(ChatKeys.USER_ID)){
                    if (!LocalChatID.equals(FirebaeChatID)){
                        if (mMessageModel.getMessageType().equals(ChatKeys.TEXT)){
                            MyNotificationManager.sendNotification("Person send you a message",mMessageModel.getMessage(),BabysitterHome.this);
                        }else {
                            MyNotificationManager.sendNotification("Person send you a Image","Image",BabysitterHome.this);
                        }
                        SharedPrefsManager.getInstance().setString(SharedPrefsKeys.CHAT_ID,FirebaeChatID);
                    }
                }

            }
        });
    }





    private void getMyOrder(){
        //show progress dialog
        mOrderModel = new OrderModel();
        mOrderKey = "";
        FirebaseDatabase.getInstance().getReference("orders")
                .orderByChild("babysitterID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //progress dialog dissmis

                        //Case 1 no orders
                        if(!dataSnapshot.exists()){
                            updadeUIForCase1();


                        }else{
                            for(DataSnapshot mSnapshot : dataSnapshot.getChildren()){
                                if(mSnapshot.getValue(OrderModel.class).getOrderStatus().equals("pending")){
                                    mOrderModel = mSnapshot.getValue(OrderModel.class);

                                    //status = mOrderModel.getOrderStatus();

                                    //Case 2 Pending
                                    updadeUIForcase2();
                                }else {
                                    //Case 3 Active
                                    updadeUIForcase3();
                                    mOrderKey = mSnapshot.getKey();
                                }

                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {


                    }
                });
    }

    private void updadeUIForCase1(){
        reportBtn.setVisibility(View.GONE);
        chatBtn.setVisibility(View.GONE);
        scheduleBtn.setVisibility(View.GONE);
        viewOrdersBtn.setVisibility(View.GONE);

        Toast.makeText(BabysitterHome.this, "You don't have any order", Toast.LENGTH_SHORT).show();
        //we will fill the dashboard with a text shows that you don't have any order
        //status = "no order";

    }
    private void updadeUIForcase2(){
        reportBtn.setVisibility(View.GONE);
        chatBtn.setVisibility(View.GONE);
        scheduleBtn.setVisibility(View.GONE);
        viewOrdersBtn.setVisibility(View.VISIBLE);

        viewOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BabysitterHome.this,OrdersList.class));
            }
        });

    }
    private void updadeUIForcase3(){
        reportBtn.setVisibility(View.VISIBLE);
        chatBtn.setVisibility(View.VISIBLE);
        scheduleBtn.setVisibility(View.VISIBLE);
        viewOrdersBtn.setVisibility(View.GONE);

        scheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BabysitterHome.this,WorkSchedule.class));
            }
        });
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BabysitterHome.this,DailyReport.class));
            }
        });
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BabysitterHome.this, ChatActivity.class));
            }
        });

    }

}
