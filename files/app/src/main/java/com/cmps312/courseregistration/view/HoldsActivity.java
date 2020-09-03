package com.cmps312.courseregistration.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cmps312.courseregistration.R;
import com.cmps312.courseregistration.controller.HoldsAdapter;
import com.cmps312.courseregistration.controller.Interactions;
import com.cmps312.courseregistration.db.DatabaseSystem;
import com.cmps312.courseregistration.model.Hold;
import com.cmps312.courseregistration.model.Student;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HoldsActivity extends AppCompatActivity {

    private RecyclerView rvHolds;
    private HoldsAdapter adapter;
    private ArrayList<Hold> holds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holds);

        setTitle("My Holds");

        rvHolds = findViewById(R.id.rv_holds);

        holds = getIntent().getParcelableArrayListExtra(Interactions.HOLDSKEY);
        long startTimer = getIntent().getLongExtra(Interactions.TESTVALKEY, 0);


        adapter = new HoldsAdapter(this, holds, new HoldsAdapter.HoldsInteraction() {
            @Override
            public void viewHoldInfo(View view, int position) {
                showHoldInfo(holds.get(position));
            }
        });

        rvHolds.setLayoutManager(new LinearLayoutManager(this));

        rvHolds.setAdapter(adapter);

        long diff = (new Date()).getTime() - startTimer;
        Log.i("DIFF", "TIMER DIFFERENCE HOLDS ACTIVITY=  " + diff);

    }

    private void showHoldInfo(Hold hold) {

        //Create dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.hold_info_dialog);
        dialog.setTitle("Hold Info");

        //Populate dialog Text views with information
        TextView tvReason = dialog.findViewById(R.id.hd_tv_reason);
        TextView tvOrigin = dialog.findViewById(R.id.hd_tv_originator);
        TextView tvDate = dialog.findViewById(R.id.hd_tv_date);
        Button btnClose = dialog.findViewById(R.id.hd_btn_dialog_close);

        tvReason.setText(hold.getReason());
        tvOrigin.setText(hold.getOriginator());
        tvDate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.US)
                .format(hold.getReceivedDate()));
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

}
