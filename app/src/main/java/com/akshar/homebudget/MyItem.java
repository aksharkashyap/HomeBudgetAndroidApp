package com.akshar.homebudget;

public class MyItem {
    private int id;
    private String mText1,mText2,mText3,mText4,mText5;

    public MyItem(int id, String text1, String text2, String text3, String text4, String text5) {

        this.id = id;
        mText1 = text1;
        mText2 = text2;
        mText3 = text3;
        mText4 = text4;
        mText5 = text5;
    }

    public String getText1() {
        return mText1;
    }

    public String getText2() {
        return mText2;
    }
    public String getText3() {
        return mText3;
    }
    public String getText4() {
        return mText4;
    }
    public String getText5() {
        return mText5;
    }
    public int getTextID() {
        return id;
    }
}