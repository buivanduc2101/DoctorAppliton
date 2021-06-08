package com.sh.doctorapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitExamination implements Serializable, Parcelable {
    private Long id;
    private Patient patient;
    private Doctor doctor;
    private String sickName;
    private String sickStatus;
    private Date createdDate;
    private Date examinationTime;
    private Long priceExam;
    private String status;
    private String description;


    protected VisitExamination(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        patient = in.readParcelable(Patient.class.getClassLoader());
        doctor = in.readParcelable(Doctor.class.getClassLoader());
        sickName = in.readString();
        description = in.readString();
        sickStatus = in.readString();
        if (in.readByte() == 0) {
            priceExam = null;
        } else {
            priceExam = in.readLong();
        }
        status = in.readString();
    }

    public static final Creator<VisitExamination> CREATOR = new Creator<VisitExamination>() {
        @Override
        public VisitExamination createFromParcel(Parcel in) {
            return new VisitExamination(in);
        }

        @Override
        public VisitExamination[] newArray(int size) {
            return new VisitExamination[size];
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
        dest.writeParcelable(patient, flags);
        dest.writeParcelable(doctor, flags);
        dest.writeString(sickName);
        dest.writeString(description);
        dest.writeString(sickStatus);
        if (priceExam == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(priceExam);
        }
        dest.writeString(status);
    }
}
