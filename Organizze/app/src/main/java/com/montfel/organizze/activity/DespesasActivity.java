package com.montfel.organizze.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.montfel.organizze.R;
import com.montfel.organizze.helper.Data;

public class DespesasActivity extends AppCompatActivity {

    private Data data;
    private TextInputEditText editDataDespesa, editCategoriaDespesa, editDescricaoDespesa;
    private EditText editValorDespesa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        editValorDespesa = findViewById(R.id.editValorDespesa);
        editDataDespesa = findViewById(R.id.editDataDespesa);
        editCategoriaDespesa = findViewById(R.id.editCategoriaDespesa);
        editDescricaoDespesa = findViewById(R.id.editDescricaoDespesa);

        data = new Data(editDataDespesa);

        editDataDespesa.setOnClickListener(view -> data.getDatePickerDialog().show());

    }
}