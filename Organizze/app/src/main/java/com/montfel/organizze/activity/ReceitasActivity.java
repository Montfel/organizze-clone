package com.montfel.organizze.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.montfel.organizze.R;
import com.montfel.organizze.helper.Data;

public class ReceitasActivity extends AppCompatActivity {

    private Data data;
    private TextInputEditText editDataReceita, editCategoriaReceita, editDescricaoReceita;
    private EditText editValorReceita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        editValorReceita = findViewById(R.id.editValorReceita);
        editDataReceita = findViewById(R.id.editDataReceita);
        editCategoriaReceita = findViewById(R.id.editCategoriaReceita);
        editDescricaoReceita = findViewById(R.id.editDescricaoReceita);

        data = new Data(editDataReceita);

        editDataReceita.setOnClickListener(view -> data.getDatePickerDialog().show());

    }
}