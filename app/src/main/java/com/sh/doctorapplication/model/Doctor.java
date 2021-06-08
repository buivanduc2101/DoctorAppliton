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
public class Doctor implements Serializable, Parcelable {
    private Long id;
    private User user;
    private String linkImage;
    private Long price;
    private String status;
    private String frequency;
    private Long quantityStar;
    private String specialize;
    private Long quantityPersonWait;
    private String degree;
    private String yearOfBirth;
    private Long experience;
    private String learningProcess;
    private String workingProcess;
    private String sickCanDo;
    private String nghienCuuKhoaHoc;
    private String giangDayHuongDan;
    private GroupDisease groupDisease;
    private MedicalExam medicalExam;


    protected Doctor(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        user = in.readParcelable(User.class.getClassLoader());
        linkImage = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readLong();
        }
        status = in.readString();
        frequency = in.readString();
        if (in.readByte() == 0) {
            quantityStar = null;
        } else {
            quantityStar = in.readLong();
        }
        specialize = in.readString();
        if (in.readByte() == 0) {
            quantityPersonWait = null;
        } else {
            quantityPersonWait = in.readLong();
        }
        degree = in.readString();
        yearOfBirth = in.readString();
        if (in.readByte() == 0) {
            experience = null;
        } else {
            experience = in.readLong();
        }
        learningProcess = in.readString();
        workingProcess = in.readString();
        sickCanDo = in.readString();
        nghienCuuKhoaHoc = in.readString();
        giangDayHuongDan = in.readString();
        groupDisease = in.readParcelable(GroupDisease.class.getClassLoader());
        medicalExam = in.readParcelable(MedicalExam.class.getClassLoader());
    }

    public static final Creator<Doctor> CREATOR = new Creator<Doctor>() {
        @Override
        public Doctor createFromParcel(Parcel in) {
            return new Doctor(in);
        }

        @Override
        public Doctor[] newArray(int size) {
            return new Doctor[size];
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
        dest.writeParcelable(user, flags);
        dest.writeString(linkImage);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(price);
        }
        dest.writeString(status);
        dest.writeString(frequency);
        if (quantityStar == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(quantityStar);
        }
        dest.writeString(specialize);
        if (quantityPersonWait == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(quantityPersonWait);
        }
        dest.writeString(degree);
        dest.writeString(yearOfBirth);
        if (experience == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(experience);
        }
        dest.writeString(learningProcess);
        dest.writeString(workingProcess);
        dest.writeString(sickCanDo);
        dest.writeString(nghienCuuKhoaHoc);
        dest.writeString(giangDayHuongDan);
        dest.writeParcelable(groupDisease, flags);
        dest.writeParcelable(medicalExam, flags);
    }
}
