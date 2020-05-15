package com.akshar.homebudget;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

public class AddNote extends AppCompatActivity {
    DatePickerDialog picker;
    EditText amount, note;
    TextView date;
    RadioGroup amount_type;
    Spinner category_spinner;
    ImageView reset_btn;
    //Room db
    static MyDatabase myDatabase;
    String amount_str, note_str, amount_type_str, category_spinner_str, date_str;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        init_view();
        room_db_init(); // initialize room database
        add_item_to_spinner();

    }

    //---------------------------------------------------------------------------------------------
    public void init_view() {
        amount = findViewById(R.id.amount_addnote);
        category_spinner = findViewById(R.id.category_addnote);
        date = findViewById(R.id.display_date_addnote);
        amount_type = findViewById(R.id.amount_type_addnote);
        note = findViewById(R.id.note_addnote);
        reset_btn = findViewById(R.id.reset_btn);
    }

    private void room_db_init() {
        myDatabase = Room.databaseBuilder(this, MyDatabase.class, "budget_db")
                .allowMainThreadQueries()
                .build();
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
            Toast.makeText(this, "Please choose category", Toast.LENGTH_SHORT).show();
            return false;
        } else if (date_str.equals("Date")) {
            Toast.makeText(this, "Please choose Date", Toast.LENGTH_SHORT).show();
            return false;
        } else return true;
    }


    public void add_note_submit(View view) {
        boolean isValidated = field_validation();
        if (isValidated) {
            getRadioFields();
            MyEntity insert_row = new MyEntity();
            insert_row.setAmount(Integer.parseInt(amount_str));
            insert_row.setAmount_type(amount_type_str);
            insert_row.setWritten_note(note_str);
            insert_row.setExpense_type(category_spinner_str);
            insert_row.setDate_note(date_str);

            long isInserted = myDatabase.myDao().addBudget(insert_row);

            if (isInserted > 0) {
                Toast.makeText(this, "Note Added !!", Toast.LENGTH_SHORT).show();
                resetFields();
            } else
                Toast.makeText(this, "add operation Failed", Toast.LENGTH_SHORT).show();
        }

    }

    //--------------------------------------------------------------------------------------
    public void add_item_to_spinner() {

        String arr[]= {"Choose","Loan","Bill","Home","Salary","Rent","Entertainment",
                "Bus/Taxi","Grocery","Medical/ Insurance","Education Fee","Kitchen","Furniture"};

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
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

    public void resetOnClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               resetFields();
               Toast.makeText(AddNote.this, "Reset Success!!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setTitle("Reset")
                .setMessage("Are you sure to Reset Fields?")
                .setCancelable(true)
                .create()
                .show();
    }


    private void resetFields(){
        amount_type.clearCheck();
        amount.setText("");
        note.setText("");
        category_spinner.setSelection(0);
        date.setText("Date");
    }
}
