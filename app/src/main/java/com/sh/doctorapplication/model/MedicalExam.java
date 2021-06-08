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
public class MedicalExam implements Serializable, Parcelable {

    private Long id;
    private String name;
    private String startTime;
    private String description;
    private Hospital hospital;

    protected MedicalExam(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        startTime = in.readString();
        description = in.readString();
        hospital = in.readParcelable(Hospital.class.getClassLoader());
    }

    public static final Creator<MedicalExam> CREATOR = new Creator<MedicalExam>() {
        @Override
        public MedicalExam createFromParcel(Parcel in) {
            return new MedicalExam(in);
        }

        @Override
        public MedicalExam[] newArray(int size) {
            return new MedicalExam[size];
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
        dest.writeString(startTime);
        dest.writeString(description);
        dest.writeParcelable(hospital, flags);
    }



}
