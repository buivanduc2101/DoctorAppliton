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
public class GroupDisease implements Serializable, Parcelable {

    private Long id;
    private String name;
    private String linkImage;
    private String description;

    protected GroupDisease(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        linkImage = in.readString();
        description = in.readString();
    }

    public static final Creator<GroupDisease> CREATOR = new Creator<GroupDisease>() {
        @Override
        public GroupDisease createFromParcel(Parcel in) {
            return new GroupDisease(in);
        }

        @Override
        public GroupDisease[] newArray(int size) {
            return new GroupDisease[size];
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
        dest.writeString(description);
    }
}
