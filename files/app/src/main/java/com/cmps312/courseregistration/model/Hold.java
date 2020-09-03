package com.cmps312.courseregistration.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Hold implements Parcelable {
    private String originator;
    private String reason;
    private Date receivedDate;

    public Hold() {}

    public Hold(String originator, String reason, Date receivedDate) {
        this.originator = originator;
        this.reason = reason;
        this.receivedDate = receivedDate;
    }

    protected Hold(Parcel in) {
        originator = in.readString();
        reason = in.readString();
        receivedDate = new Date(in.readLong());
    }

    public static final Creator<Hold> CREATOR = new Creator<Hold>() {
        @Override
        public Hold createFromParcel(Parcel in) {
            return new Hold(in);
        }

        @Override
        public Hold[] newArray(int size) {
            return new Hold[size];
        }
    };

    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originator);
        dest.writeString(reason);
        dest.writeLong(receivedDate.getTime());
    }
}
