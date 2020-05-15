package com.akshar.homebudget;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.akshar.RoomDB.MyDatabase;
import com.akshar.RoomDB.MyEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UpdateNote extends AppCompatActivity {
    DatePickerDialog picker;
    EditText amount, note;
    TextView date;
    RadioGroup amount_type;
    RadioButton income_rb, expense_rb;
    Spinner category_spinner;
    ArrayAdapter<String> dataAdapter;
    int myRowId;
    //Room db
    static MyDatabase myDatabase;
    String amount_str, note_str, amount_type_str, category_spinner_str, date_str;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);
        init_view();
        room_db_init(); // initialize room database
        add_item_to_spinner();
        myRowId = getIntentData();
    }


    //---------------------------------------------------------------------------------------------
    public void init_view() {
        amount = findViewById(R.id.amount_updatenote);
        category_spinner = findViewById(R.id.category_updatenote);
        date = findViewById(R.id.display_date_updatenote);
        amount_type = findViewById(R.id.amount_type_updatenote);
        note = findViewById(R.id.note_updatenote);
        income_rb = findViewById(R.id.income_rb_updatenote);
        expense_rb = findViewById(R.id.expense_rb_updatenote);
    }

    private void room_db_init() {
        myDatabase = Room.databaseBuilder(this, MyDatabase.class, "budget_db")
                .allowMainThreadQueries()
                .build();
    }

    public int getIntentData() {
        Intent intent = getIntent();
        int row_id = intent.getIntExtra("id", 0);
        setFieldValues(row_id);
        return row_id;
    }

    public void setFieldValues(int id) {
        MyEntity row = myDatabase.myDao().getBudgetRow(id);
        amount.setText(String.valueOf(row.getAmount()));
        date.setText(row.getDate_note());
        note.setText(row.getWritten_note());

        if (row.getAmount_type().equals("Income"))
            income_rb.setChecked(true);
        else if (row.getAmount_type().equals("Expense"))
            expense_rb.setChecked(true);

        category_spinner.setSelection(dataAdapter.getPosition(row.getExpense_type()));
    }

    //-------------------db task---------------------------------------------------

    public void getRadioFields() {
        // radio group
        int id = amount_type.getCheckedRadioButtonId();
        RadioButton mAmountType = amount_type.findViewById(id);
        amount_type_str = mAmountType.getText().toString();
    }

    public boolean field_validation() {

        amount_str = amount.getText().toString();
        note_str = note.getText().toString();
        date_str = date.getText().toString();
        // spinner
        category_spinner_str = category_spinner.getSelectedItem().toString();

        if (amount_str.isEmpty()) {
            amount.setError("Enter Amount");
            amount.requestFocus();
            return false;
        } else if (note_str.isEmpty()) {
            note.setError("Write Some Notes");
            note.requestFocus();
            return false;
        } else if (amount_type.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select amount Type", Toast.LENGTH_SHORT).show();
            return false;
        } else if (category_spinner_str.equals("Choose")) {
            Toast.makeText(this, "Please choose expense type", Toast.LENGTH_SHORT).show();
            return false;
        } else return true;
    }

    public void update_note_submit(View view) {
        boolean isValidated = field_validation();
        if (isValidated) {
            getRadioFields();
            MyEntity row = myDatabase.myDao().getBudgetRow(myRowId);
            if (row != null) {
                row.setAmount(Integer.parseInt(amount_str));
                row.setAmount_type(amount_type_str);
                row.setExpense_type(category_spinner_str);
                row.setDate_note(date_str);
                row.setWritten_note(note_str);
                long t = myDatabase.myDao().updateBudget(row);
                if (t > 0)
                    Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "No records", Toast.LENGTH_SHORT).show();
        }

    }


    //--------------------------------------------------------------------------------------
    public void add_item_to_spinner() {

        String arr[]= {"Choose","Loan","Bill","Home","Salary","Rent","Entertainment",
                "Bus/Taxi","Grocery","Medical/ Insurance","Education Fee","Kitchen","Furniture"};
        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(dataAdapter);

    }
    //---------------------------------------------------------------------------------------------


    public void date_picker(View view) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        picker.show();
    }

}
