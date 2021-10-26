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

public class ReceitasActivity extends AppCompatActivity {

    private DateCustom data;
    private EditText editDataReceita, editCategoriaReceita, editDescricaoReceita, editValorReceita;
    private FloatingActionButton fabSalvarReceita;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAuth();
    private Double receitaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        editValorReceita = findViewById(R.id.editValorReceita);
        editDataReceita = findViewById(R.id.editDataReceita);
        editCategoriaReceita = findViewById(R.id.editCategoriaReceita);
        editDescricaoReceita = findViewById(R.id.editDescricaoReceita);
        fabSalvarReceita = findViewById(R.id.fabSalvarReceita);
        recuperaReceitaTotal();

        data = new DateCustom(editDataReceita);

        editDataReceita.setOnClickListener(view -> data.getDatePickerDialog().show());

        fabSalvarReceita.setOnClickListener(view -> {

            if (validaCamposReceita()) {
                movimentacao = new Movimentacao();
                movimentacao.setValor(Double.parseDouble(editValorReceita.getText().toString()));
                movimentacao.setCategoria(editCategoriaReceita.getText().toString());
                movimentacao.setDescricao(editDescricaoReceita.getText().toString());
                movimentacao.setData(editDataReceita.getText().toString());
                movimentacao.setTipo("r");

                atualizaReceita(receitaTotal + Double.parseDouble(editValorReceita.getText().toString()));

                movimentacao.salvar();
            }
        });
    }

    public Boolean validaCamposReceita() {
        String valor = editValorReceita.getText().toString();
        String data = editDataReceita.getText().toString();
        String categoria = editCategoriaReceita.getText().toString();
        String descricao = editDescricaoReceita.getText().toString();

        if (valor.isEmpty() || data.isEmpty() || categoria.isEmpty() || descricao.isEmpty()) {
            Toast.makeText(ReceitasActivity.this,
                    "Preencha todos os campos!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void recuperaReceitaTotal() {
        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void atualizaReceita(Double receita) {
        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.child("receitaTotal").setValue(receita);
    }
}