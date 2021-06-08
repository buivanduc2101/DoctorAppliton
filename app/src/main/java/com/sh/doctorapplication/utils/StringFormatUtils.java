package com.sh.doctorapplication.utils;

import android.annotation.SuppressLint;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StringFormatUtils {
    public static String convertToStringMoneyVND(long money) {
        NumberFormat formatter = new DecimalFormat("#,###");
        return formatter.format(money) + " đ";
    }

    public static Date convertToDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static String convertDateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

    public static String getCurrentDateStr() {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
        return date;
    }

    @SuppressLint("SimpleDateFormat")
    public static Date convertStringToDate(String dateStr) {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") Date date = null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String generateStatusVisitExam(String status) {
        String result = "Đã đăng ký";
        if (Const.VisitExamStatus.CANCELED.equals(status)) {
            result = "Hủy bỏ";
        } else if (Const.VisitExamStatus.DONE.equals(status)) {
            result = "Hoàn thành";
        }
        return result;
    }

}
