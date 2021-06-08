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
public class UserDetail implements Serializable, Parcelable {
    private Doctor doctor;
    private Patient patient;


    protected UserDetail(Parcel in) {
        doctor = in.readParcelable(Doctor.class.getClassLoader());
    }

    public static final Creator<UserDetail> CREATOR = new Creator<UserDetail>() {
        @Override
        public UserDetail createFromParcel(Parcel in) {
            return new UserDetail(in);
        }

        @Override
        public UserDetail[] newArray(int size) {
            return new UserDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(doctor, flags);
    }
}
