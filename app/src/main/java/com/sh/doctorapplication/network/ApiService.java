package com.sh.doctorapplication.network;

import com.sh.doctorapplication.model.Doctor;
import com.sh.doctorapplication.model.GroupDisease;
import com.sh.doctorapplication.model.Hospital;
import com.sh.doctorapplication.model.MedicalExam;
import com.sh.doctorapplication.model.Patient;
import com.sh.doctorapplication.model.Rate;
import com.sh.doctorapplication.model.StatisticMoneyAccount;
import com.sh.doctorapplication.model.UserDetail;
import com.sh.doctorapplication.model.VisitExamination;
import com.sh.doctorapplication.network.request.ChangePasswordRequest;
import com.sh.doctorapplication.network.request.DoctorUpdateRequest;
import com.sh.doctorapplication.network.request.LoginRequest;
import com.sh.doctorapplication.network.request.PatientRequest;
import com.sh.doctorapplication.network.request.RegisterRequest;
import com.sh.doctorapplication.network.request.VisitExamRegisterRequest;
import com.sh.doctorapplication.network.request.VisitExamUpdateRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("authentication/login")
    Call<UserDetail> login(@Body LoginRequest request);

    @POST("authentication/register")
    Call<UserDetail> register(@Body RegisterRequest registerRequest);

    @PUT("authentication/change-password")
    Call<UserDetail> changePassword(@Body ChangePasswordRequest request);

    @GET("hospitals")
    Call<List<Hospital>> getAllHospitals();

    @GET("hospitals/{hospitalId}")
    Call<Hospital> getAutoNumberHospital(@Path("hospitalId") Long hospitalId);

    @GET("group-diseases/")
    Call<List<GroupDisease>> getAllGroupDiseases();

    @GET("group-diseases/{hospitalId}/doctors")
    Call<List<Doctor>> getAllDoctors(@Path("hospitalId") Long hospitalId);

    @GET("group-diseases/{groupDiseaseId}/doctors")
    Call<List<Doctor>> getAllDoctorInGroupDisease(@Path("groupDiseaseId") Long groupDiseaseId);

    @GET("hospitals/{hospitalId}/medical-exams/{medicalExamId}/doctors")
    Call<List<Doctor>> getAllDoctorByMedicalExam(@Path("hospitalId") Long hospitalId,
                                                 @Path("medicalExamId") Long medicalExamId);

    @GET("doctors/list")
    Call<List<Doctor>> getAllDoctorBySearchValue(@Query("doctorName") String doctorName,
                                                 @Query("hospitalName") String hospitalName,
                                                 @Query("medicalExamName") String medicalExamName,
                                                 @Query("price") Long price,
                                                 @Query("sickName") String sickName);

    @GET("hospitals/{hospitalId}/medical-exams")
    Call<List<MedicalExam>> getAllMedicalExamInHospital(@Path("hospitalId") Long hospitalId);

    @GET("rates/")
    Call<List<Rate>> getAllRates();

    @GET("doctors/{doctorId}")
    Call<Doctor> getDoctor(@Path("doctorId") Long doctorId);

    @PUT("doctors/{doctorId}")
    Call<UserDetail> updateDoctor(@Path("doctorId") Long doctorId, @Body DoctorUpdateRequest request);

    @GET("patients/{patientId}")
    Call<Patient> getPatient(@Path("patientId") Long patientId);

    @PUT("patients/{patientId}")
    Call<UserDetail> updatePatient(@Path("patientId") Long patientId, @Body PatientRequest request);

    @GET("visit-exams/patients/{patientId}/list")
    Call<List<VisitExamination>> getAllVisitExamHistoryOfPatient(@Path("patientId") Long patientId,
                                                                 @Query("startDate") String startDate,
                                                                 @Query("endDate") String endDate,
                                                                 @Query("status") String status
    );

    @GET("visit-exams/doctors/{doctorId}/list/history")
    Call<List<VisitExamination>> getAllVisitExamHistoryOfDoctor(@Path("doctorId") Long doctorId,
                                                                @Query("startDate") String startDate,
                                                                @Query("endDate") String endDate,
                                                                @Query("status") String status
    );

    @GET("visit-exams/doctors/{doctorId}/list")
    Call<List<VisitExamination>> getAllVisitExamRegisterOfDoctor(@Path(value = "doctorId") Long doctorId);

    @POST("visit-exams/")
    Call<VisitExamination> createVisitExam(@Body VisitExamRegisterRequest request);

    @PUT("visit-exams/{visitExamId}")
    Call<VisitExamination> updateVisitExam(@Path(value = "visitExamId") Long visitExamId,
                                           @Body VisitExamUpdateRequest request);

    @GET("patients/{patientId}/accounts/statistic")
    Call<StatisticMoneyAccount> getStatisticMoneyPatient(@Path(value = "patientId") Long patientId);

    @GET("doctors/{doctorId}/accounts/statistic")
    Call<StatisticMoneyAccount> getStatisticMoneyDoctor(@Path(value = "doctorId") Long doctorId);


}
