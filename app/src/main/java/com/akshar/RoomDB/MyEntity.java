package com.akshar.RoomDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "budget_table")
public class MyEntity {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "Amount")
    private int amount;

    @ColumnInfo(name = "AmountType")
    private String amount_type;

    @ColumnInfo(name = "ExpenseType")
    private String expense_type;

    @ColumnInfo(name = "WrittenNote")
    private String written_note;

    @ColumnInfo(name = "NoteDate")
    private String date_note;


    //------------Getter and setters-------------

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public String getAmount_type() {
        return amount_type;
    }

    public String getExpense_type() {
        return expense_type;
    }

    public String getWritten_note() {
        return written_note;
    }

    public String getDate_note() {
        return date_note;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setAmount_type(String amount_type) {
        this.amount_type = amount_type;
    }

    public void setExpense_type(String expense_type) {
        this.expense_type = expense_type;
    }

    public void setWritten_note(String written_note) {
        this.written_note = written_note;
    }

    public void setDate_note(String date_note) {
        this.date_note = date_note;
    }

}
