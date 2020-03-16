package com.maha.uds;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maha.uds.Model.DailyReportModel;

public class DailyReport extends AppCompatActivity {

    Button update , back;
    EditText arrive,leave,nap, mealReport,notes;
    TextView arriveView,leaveView,napView, foodReportView,notesView;
    DatabaseReference mReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_report);

        mReference = FirebaseDatabase.getInstance().getReference("orders")
                .child(MotherHome.mOrderKey);
        setUI();
        displayInfo();




    }

    public void setUI(){
        update = findViewById(R.id.updateBtn);
        back = findViewById(R.id.backBtn);
        arrive = findViewById(R.id.arriveTimeText);
        leave = findViewById(R.id.LeaveTimeText);
        nap = findViewById(R.id.napTimeText);
        mealReport = findViewById(R.id.mealText);
        notes = findViewById(R.id.notesText);
        arriveView = findViewById(R.id.arriveTimeView);
        leaveView = findViewById(R.id.LeaveTimeView);
        napView = findViewById(R.id.napTimeView);
        foodReportView = findViewById(R.id.mealView);
        notesView = findViewById(R.id.notesView);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String type = "babysitter";

        FirebaseDatabase.getInstance().getReference("accounts").child(userId).child("accountType")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().equals(type)){
                    arriveView.setVisibility(View.GONE);
                    leaveView.setVisibility(View.GONE);
                    napView.setVisibility(View.GONE);
                    foodReportView.setVisibility(View.GONE);
                    notesView.setVisibility(View.GONE);
                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateReportInfo();
                        }
                    });
                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(DailyReport.this,BabysitterHome.class));
                        }
                    });
                }else {
                    arrive.setVisibility(View.GONE);
                    leave.setVisibility(View.GONE);
                    nap.setVisibility(View.GONE);
                    mealReport.setVisibility(View.GONE);
                    notes.setVisibility(View.GONE);
                    update.setVisibility(View.GONE);

                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(DailyReport.this,MotherHome.class));
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void updateReportInfo(){
        String arriveTime = arrive.getText().toString();
        String leaveTime = leave.getText().toString();
        String napTime = nap.getText().toString();
        String foodReport = mealReport.getText().toString();
        String unusualNotes = notes.getText().toString();


        DailyReportModel report = new DailyReportModel(arriveTime,leaveTime,napTime,foodReport,unusualNotes);
        mReference.child("dailyReport").setValue(report);
    }


    public void displayInfo(){

        mReference.child("dailyReport").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    if(dataSnapshot.exists()) {
                        DailyReportModel model = dataSnapshot.getValue(DailyReportModel.class);
                        Log.d("DailyReportModel", model.toString());
                        arriveView.setText(model.getArriveTime());
                        leaveView.setText(model.getLeavingTime());
                        napView.setText(model.getNapTime());
                        foodReportView.setText(model.getMealReport());
                        notesView.setText(model.getUnusualNotes());
                    }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}