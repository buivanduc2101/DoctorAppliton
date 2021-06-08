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
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable, Parcelable {

    private Long id;
    private String username;
    private String password;
    private String fullName;
    private String mobile;
    private String address;
    private String birthDay;
    private String gender;
    private Boolean isDoctor;

    protected User(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        username = in.readString();
        password = in.readString();
        fullName = in.readString();
        mobile = in.readString();
        birthDay = in.readString();
        address = in.readString();
        gender = in.readString();
        byte tmpIsDoctor = in.readByte();
        isDoctor = tmpIsDoctor == 0 ? null : tmpIsDoctor == 1;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
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
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(birthDay);
        dest.writeString(fullName);
        dest.writeString(mobile);
        dest.writeString(address);
        dest.writeString(gender);
        dest.writeByte((byte) (isDoctor == null ? 0 : isDoctor ? 1 : 2));
    }
}
