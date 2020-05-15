package com.akshar.RoomDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface MyDao {

    // Here we will define methods to perform various db operations
    @Insert
    long addBudget(MyEntity budget);

    @Delete
    int deleteBudget(MyEntity budget);

    @Update
    int updateBudget(MyEntity budget);

    @Query("SELECT id,Amount,AmountType,ExpenseType,NoteDate,WrittenNote FROM budget_table")
    List<MyEntity> getAllBudgetRows();

    @Query("SELECT id,Amount,AmountType,ExpenseType,WrittenNote,NoteDate FROM budget_table WHERE id IN (:id)")
    MyEntity getBudgetRow(int id);

    //------------Specific month/year's data-------------
    @Query("SELECT SUM(Amount) FROM budget_table WHERE AmountType ='Expense' AND NoteDate LIKE(:date) ")
    int getSumExpenditure(String date);

    @Query("SELECT SUM(Amount) FROM budget_table WHERE AmountType ='Income' AND NoteDate LIKE(:date)")
    int getSumIncome(String date);

    @Query("SELECT AVG(Amount) FROM budget_table WHERE AmountType ='Expense'AND NoteDate LIKE(:date) ")
    int getAvgExpenditure(String date);

    @Query("SELECT AVG(Amount) FROM budget_table WHERE AmountType ='Income'AND NoteDate LIKE(:date)")
    int getAvgIncome(String date);

    //------------All years data---------------
    @Query("SELECT SUM(Amount) FROM budget_table WHERE AmountType ='Income'")
    int getIncomeSum_all();

    @Query("SELECT AVG(Amount) FROM budget_table WHERE AmountType ='Income'")
    int getIncomeAvg_all();

    @Query("SELECT SUM(Amount) FROM budget_table WHERE AmountType ='Expense'")
    int getExpenseSum_all();

    @Query("SELECT AVG(Amount) FROM budget_table WHERE AmountType ='Expense'")
    int getExpenseAvg_all();

    //-------ISExist---
    @Query("SELECT id FROM budget_table WHERE (AmountType IN('Income','Expense')) AND NoteDate LIKE(:date)")
    int getIsExist(String date);
}



