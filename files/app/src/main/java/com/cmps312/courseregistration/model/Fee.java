package com.cmps312.courseregistration.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Fee implements Parcelable {
    private boolean paid;
    private Date paidDate;
    private double amount;

    public Fee() {

    }

    public Fee(boolean paid, Date paidDate, double amount) {
        this.paid = paid;
        this.paidDate = paidDate;
        this.amount = amount;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Date getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void pay() {
        paid = true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.paid ? (byte) 1 : (byte) 0);
        dest.writeLong(this.paidDate != null ? this.paidDate.getTime() : -1);
        dest.writeDouble(this.amount);
    }

    protected Fee(Parcel in) {
        this.paid = in.readByte() != 0;
        long tmpPaidDate = in.readLong();
        this.paidDate = tmpPaidDate == -1 ? null : new Date(tmpPaidDate);
        this.amount = in.readDouble();
    }

    public static final Creator<Fee> CREATOR = new Creator<Fee>() {
        @Override
        public Fee createFromParcel(Parcel source) {
            return new Fee(source);
        }

        @Override
        public Fee[] newArray(int size) {
            return new Fee[size];
        }
    };
}
