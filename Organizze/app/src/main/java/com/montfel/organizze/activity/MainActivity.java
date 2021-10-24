package com.montfel.organizze.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import com.montfel.organizze.R;
import com.montfel.organizze.activity.CadastroActivity;
import com.montfel.organizze.activity.LoginActivity;

public class MainActivity extends IntroActivity {

//    private Button btnCadastrar;
//    private TextView tvTemConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

//        btnCadastrar = findViewById(R.id.btnCadastrar);
//        tvTemConta = findViewById(R.id.tvTemConta);



        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_1)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_2)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_3)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_4)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_cadastro)
                .canGoForward(false)
                .build()

        );
//        btnCadastrar.setOnClickListener(view -> {
//            startActivity(new Intent(this, CadastroActivity.class));
//        });
//
//        tvTemConta.setOnClickListener(view -> {
//            startActivity(new Intent(this, LoginActivity.class));
//        });
    }

    public void entrar (View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void cadastrar (View view) {
        startActivity(new Intent(this, CadastroActivity.class));
    }
}