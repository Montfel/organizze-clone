package com.montfel.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.montfel.organizze.R;
import com.montfel.organizze.config.ConfiguracaoFirebase;
import com.montfel.organizze.helper.Base64Custom;
import com.montfel.organizze.helper.DateCustom;
import com.montfel.organizze.model.Movimentacao;
import com.montfel.organizze.model.Usuario;

public class DespesasActivity extends AppCompatActivity {

    private DateCustom data;
    private EditText editDataDespesa, editCategoriaDespesa, editDescricaoDespesa, editValorDespesa;
    private FloatingActionButton fabSalvarDespesa;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAuth();
    private Double despesaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        editValorDespesa = findViewById(R.id.editValorDespesa);
        editDataDespesa = findViewById(R.id.editDataDespesa);
        editCategoriaDespesa = findViewById(R.id.editCategoriaDespesa);
        editDescricaoDespesa = findViewById(R.id.editDescricaoDespesa);
        fabSalvarDespesa = findViewById(R.id.fabSalvarDespesa);
        recuperaDespesaTotal();

        data = new DateCustom(editDataDespesa);

        editDataDespesa.setOnClickListener(view -> data.getDatePickerDialog().show());

        fabSalvarDespesa.setOnClickListener(view -> {

            if (validaCamposDespesa()) {
                movimentacao = new Movimentacao();
                movimentacao.setValor(Double.parseDouble(editValorDespesa.getText().toString()));
                movimentacao.setCategoria(editCategoriaDespesa.getText().toString());
                movimentacao.setDescricao(editDescricaoDespesa.getText().toString());
                movimentacao.setData(editDataDespesa.getText().toString());
                movimentacao.setTipo("d");

                atualizaDespesa(despesaTotal + Double.parseDouble(editValorDespesa.getText().toString()));

                movimentacao.salvar();
            }
        });
    }

    public Boolean validaCamposDespesa() {
        String valor = editValorDespesa.getText().toString();
        String data = editDataDespesa.getText().toString();
        String categoria = editCategoriaDespesa.getText().toString();
        String descricao = editDescricaoDespesa.getText().toString();

        if (valor.isEmpty() || data.isEmpty() || categoria.isEmpty() || descricao.isEmpty()) {
            Toast.makeText(DespesasActivity.this,
                    "Preencha todos os campos!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void recuperaDespesaTotal() {
        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void atualizaDespesa(Double despesa) {
        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.child("despesaTotal").setValue(despesa);
    }
}