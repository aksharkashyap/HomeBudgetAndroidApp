package com.akshar.homebudget;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.akshar.RoomDB.MyDatabase;
import com.akshar.RoomDB.MyEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ProgressBar pb;
    FloatingActionButton btn_floating;
    private ArrayList<MyItem> myItemList;
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    TextView txtmain;
    //Room db
    MyDatabase myDatabase;
    private RelativeLayout mlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtmain = findViewById(R.id.txt_main);
        mlayout = findViewById(R.id.my_progress_layout);
        btn_floating = findViewById(R.id.add_note_floating);
        myDatabase = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "budget_db")
                .allowMainThreadQueries()
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setRecyclerList();
    }

    public void removeItem(int position) {
        //remove from db
        MyEntity row = myDatabase.myDao().getBudgetRow(myItemList.get(position).getTextID());
        myDatabase.myDao().deleteBudget(row);

        //remove from list
        myItemList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    public void updateItem(int position) {
        int row_id = myItemList.get(position).getTextID();
        Intent intent = new Intent(MainActivity.this, UpdateNote.class);
        intent.putExtra("id", row_id);
        startActivity(intent);

    }

    /*
    public void changeItem(int position, String text) {

        Toast.makeText(this, "" + myItemList.get(position).getTextID(), Toast.LENGTH_SHORT).show();
        mAdapter.notifyItemChanged(position);
    }*/


    public void setRecyclerList() {
        //fetch data from db
        List<MyEntity> rows = myDatabase.myDao().getAllBudgetRows();
        if (rows.size() > 0) {
            txtmain.setVisibility(View.GONE);
            myItemList = new ArrayList<>();
            for (int i = rows.size() - 1; i >= 0; i--) {
                myItemList.add(new MyItem(rows.get(i).getId(), rows.get(i).getAmount_type(),
                        "Category: " + rows.get(i).getExpense_type(),
                        "Amount: " + rows.get(i).getAmount() + " Rs.",
                        "Date: " + rows.get(i).getDate_note(),
                        "Note: " + rows.get(i).getWritten_note()));
            }

            buildRecyclerView();
        } else {
            txtmain.setVisibility(View.VISIBLE);
            txtmain.setText("Click + \nto add your budget notes");
            txtmain.setTextSize(30);
        }
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MyRecyclerAdapter(myItemList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MyRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // changeItem(position, "Clicked");
            }

            @Override
            public void onUpdateClick(int position) {
                updateItem(position);
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });
    }


    public void add_note_float(View view) {

        btn_floating.setEnabled(false);
        pb = new ProgressBar(getApplicationContext());
        pb.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        pb.setIndeterminate(true);
        pb.setVisibility(View.VISIBLE);
        mlayout.setVisibility(View.VISIBLE);
        mlayout.addView(pb);
        Intent intent = new Intent(MainActivity.this, AddNote.class);
        startActivity(intent);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mlayout.setVisibility(View.GONE);
        btn_floating.setEnabled(true);
    }
}