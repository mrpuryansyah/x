package com.example.x.Utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imamulakhyar on 24/05/18.
 */

public class PtnKeyValue implements Parcelable {

    private String key, value;

    public PtnKeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    protected PtnKeyValue(Parcel in) {
        key = in.readString();
        value = in.readString();
    }

    public static final Creator<PtnKeyValue> CREATOR = new Creator<PtnKeyValue>() {
        @Override
        public PtnKeyValue createFromParcel(Parcel in) {
            return new PtnKeyValue(in);
        }

        @Override
        public PtnKeyValue[] newArray(int size) {
            return new PtnKeyValue[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.key);
        parcel.writeString(this.value);
    }
}
