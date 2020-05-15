package com.akshar.homebudget;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.akshar.RoomDB.MyDatabase;

import java.util.Calendar;


public class UtilityActivity extends Fragment {

    TextView budget_estimation_title, total_expense, total_income, avg_income, avg_expense, total_saving;
    String budget_estimation_title_str, total_expense_str, total_income_str, avg_income_str, avg_expense_str, total_saving_str;
    MyDatabase myDatabase;
    Button monthly_btn, yearly_btn;
    Spinner month_chooser_spinner;
    EditText year_monthly_et, year_yearly_et;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.frag_utility, container, false);
        view_init(view);
        str_init();
        add_item_to_spinner();
        room_db_init();
        getBudget_All();


        return view;

    }

    private void view_init(View view) {
        budget_estimation_title = view.findViewById(R.id.title_budget_estimation);
        total_expense = view.findViewById(R.id.total_expenditure);
        total_income = view.findViewById(R.id.total_income);
        avg_income = view.findViewById(R.id.avg_income);
        avg_expense = view.findViewById(R.id.avg_expense);
        total_saving = view.findViewById(R.id.total_saving);
        monthly_btn = view.findViewById(R.id.monthly_estimation);
        yearly_btn = view.findViewById(R.id.yearly_estimation);
        year_monthly_et = view.findViewById(R.id.year_monthly_et);
        year_yearly_et = view.findViewById(R.id.year_yearly_et);
        month_chooser_spinner = view.findViewById(R.id.month_chooser_spinner);
        monthlyOnClick();
        yearlyOnClick();
    }

    private void str_init() {
        budget_estimation_title_str = budget_estimation_title.getText().toString();
        total_expense_str = total_expense.getText().toString();
        total_income_str = total_income.getText().toString();
        avg_income_str = avg_income.getText().toString();
        avg_expense_str = avg_expense.getText().toString();
        total_saving_str = total_saving.getText().toString();
    }

    private void room_db_init() {
        myDatabase = Room.databaseBuilder(getContext(), MyDatabase.class, "budget_db")
                .allowMainThreadQueries()
                .build();
    }


    private void monthlyOnClick() {
        monthly_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(month_chooser_spinner.getSelectedItem().toString().equals("Month")) {
                    Toast.makeText(getContext(), "Choose month please", Toast.LENGTH_SHORT).show();
                }
                else if (year_monthly_et.getText().toString().isEmpty()){
                    year_monthly_et.requestFocus();
                    year_monthly_et.setError("Enter Year");
                }
                else {
                    String month = month_chooser_spinner.getSelectedItem().toString();
                    String year = year_monthly_et.getText().toString();

                    //get the rows
                    getBudgetEstimation(month + "/" + year);
                }
            }
        });
    }

    private void yearlyOnClick() {
        yearly_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String year = year_yearly_et.getText().toString();
                if (year.isEmpty()) {
                    year_yearly_et.requestFocus();
                    year_yearly_et.setError("Enter Year");
                }
                else {

                    // get the rows
                    getBudgetEstimation(year);
                }
            }
        });
    }

    private void getBudgetEstimation(String date) {

        int row = myDatabase.myDao().getIsExist("%" + date + "%");
        if (row > 0) {
            //title date set
            budget_estimation_title.setText(budget_estimation_title_str + date);
            //sum of total expense
            int sum_total_expense = myDatabase.myDao().getSumExpenditure("%" + date + "%");
            total_expense.setText(total_expense_str + ": " + sum_total_expense);
            //sum of total income
            int sum_total_income = myDatabase.myDao().getSumIncome("%" + date + "%");
            total_income.setText(total_income_str + ": " + sum_total_income);
            //total saving
            int sum_total_saving = sum_total_income - sum_total_expense;
            total_saving.setText(total_saving_str + ": " + Math.abs(sum_total_saving));

            //avg of total expense
            int avg_total_expense = myDatabase.myDao().getAvgExpenditure("%" + date + "%");
            avg_expense.setText(avg_expense_str + ": " + avg_total_expense);
            //avg of total income
            int avg_total_income = myDatabase.myDao().getAvgIncome("%" + date + "%");
            avg_income.setText(avg_income_str + ": " + avg_total_income);
        } else {
            Toast.makeText(getContext(), "No Record Found!", Toast.LENGTH_SHORT).show();
        }
    }


    private void add_item_to_spinner() {
        Object arr[] = {"Month",1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        ArrayAdapter<Object> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, arr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month_chooser_spinner.setAdapter(dataAdapter);
    }


    public void getBudget_All() {

        //sum of total expense
        int sum_total_expense = myDatabase.myDao().getExpenseSum_all();
        total_expense.setText(total_expense_str + ": " + sum_total_expense);
        //sum of total income
        int sum_total_income = myDatabase.myDao().getIncomeSum_all();
        total_income.setText(total_income_str + ": " + sum_total_income);
        //total saving
        int sum_total_saving = sum_total_income - sum_total_expense;
        total_saving.setText(total_saving_str + ": " + Math.abs(sum_total_saving));

        //avg of total expense
        int avg_total_expense = myDatabase.myDao().getExpenseAvg_all();
        avg_expense.setText(avg_expense_str + ": " + avg_total_expense);
        //avg of total income
        int avg_total_income = myDatabase.myDao().getIncomeAvg_all();
        avg_income.setText(avg_income_str + ": " + avg_total_income);


    }

}
