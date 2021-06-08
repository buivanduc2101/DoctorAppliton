package com.sh.doctorapplication.network.request;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import lombok.Data;

@Data
public class SearchDoctorRequest implements Serializable, Parcelable {

    private String doctorName;
    private String hospitalName;
    private String medicalExamName;
    private Long price;
    private String sickName;

    public SearchDoctorRequest() {

    }

    protected SearchDoctorRequest(Parcel in) {
        doctorName = in.readString();
        hospitalName = in.readString();
        medicalExamName = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readLong();
        }
        sickName = in.readString();
    }

    public static final Creator<SearchDoctorRequest> CREATOR = new Creator<SearchDoctorRequest>() {
        @Override
        public SearchDoctorRequest createFromParcel(Parcel in) {
            return new SearchDoctorRequest(in);
        }

        @Override
        public SearchDoctorRequest[] newArray(int size) {
            return new SearchDoctorRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(doctorName);
        dest.writeString(hospitalName);
        dest.writeString(medicalExamName);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(price);
        }
        dest.writeString(sickName);
    }
}
