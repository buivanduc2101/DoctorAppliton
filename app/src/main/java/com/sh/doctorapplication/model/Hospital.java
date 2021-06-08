package com.sh.doctorapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hospital implements Serializable, Parcelable {

    private Long id;
    private String name;
    private String linkImage;
    private String address;
    private String phoneNumber;
    private Long autoNumber;


    protected Hospital(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        linkImage = in.readString();
        address = in.readString();
        phoneNumber = in.readString();
        if (in.readByte() == 0) {
            autoNumber = null;
        } else {
            autoNumber = in.readLong();
        }
    }

    public static final Creator<Hospital> CREATOR = new Creator<Hospital>() {
        @Override
        public Hospital createFromParcel(Parcel in) {
            return new Hospital(in);
        }

        @Override
        public Hospital[] newArray(int size) {
            return new Hospital[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(name);
        dest.writeString(linkImage);
        dest.writeString(address);
        dest.writeString(phoneNumber);
        if (autoNumber == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(autoNumber);
        }
    }
}
