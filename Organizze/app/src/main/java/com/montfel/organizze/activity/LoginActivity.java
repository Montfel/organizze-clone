package com.montfel.organizze.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.montfel.organizze.R;
import com.montfel.organizze.config.ConfiguracaoFirebase;
import com.montfel.organizze.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail, editSenha;
    private Button btnEntrar;
    private FirebaseAuth auth;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        btnEntrar = findViewById(R.id.btnEntrar);

        btnEntrar.setOnClickListener(view -> {
            String email = editEmail.getText().toString();
            String senha = editSenha.getText().toString();

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(LoginActivity.this,
                        "Preencha todos os campos!",
                        Toast.LENGTH_SHORT).show();
            } else {
                usuario = new Usuario();
                usuario.setEmail(email);
                usuario.setSenha(senha);
                verificarUsuario();
            }
        });
    }

    public void verificarUsuario() {
        auth = ConfiguracaoFirebase.getFirebaseAuth();
        auth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        abrirTelaPrincipal();
                    } else {
                        String excecao = "";
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            excecao = "E-mail não corresponde a um usuário existente";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            excecao = "Senha incorreta";
                        } catch (Exception e) {
                            excecao = "Erro ao logar usuário: " + e.getMessage();
                            e.printStackTrace();
                        }

                        Toast.makeText(LoginActivity.this, excecao, Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    public void abrirTelaPrincipal() {
        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
    }
}