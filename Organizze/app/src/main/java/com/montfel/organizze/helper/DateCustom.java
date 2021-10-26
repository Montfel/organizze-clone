package com.montfel.organizze.helper;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateCustom {
    private DatePickerDialog datePickerDialog;

    public DateCustom(EditText editText) {
        editText.setText(getTodaysDate());
        initDatePicker(editText);
    }

    public DatePickerDialog getDatePickerDialog() {
        return datePickerDialog;
    }

    private String getTodaysDate() {
        return new SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis());
    }

    private void initDatePicker(EditText editText) {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) ->
                editText.setText(makeDateString(dayOfMonth, ++month, year));
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        datePickerDialog = new DatePickerDialog(editText.getContext(), dateSetListener, year, month, day);
    }

    @SuppressLint("DefaultLocale")
    private String makeDateString(int dayOfMonth, int month, int year) {
        return String.format("%02d", dayOfMonth) + "/" + String.format("%02d", month) + "/" + year;
    }

    public static String mesAnoDataEscolhida(String data) {
        String[] dataQuebrada = data.split("/");
        return dataQuebrada[1] + dataQuebrada[2];
    }
}