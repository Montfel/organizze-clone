package com.montfel.organizze.activity;

import android.content.Intent;
import android.os.Bundle;

import com.github.clans.fab.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.montfel.organizze.R;
import com.montfel.organizze.adapter.MovimentacaoAdapter;
import com.montfel.organizze.config.ConfiguracaoFirebase;
import com.montfel.organizze.databinding.ActivityPrincipalBinding;
import com.montfel.organizze.helper.Base64Custom;
import com.montfel.organizze.model.Movimentacao;
import com.montfel.organizze.model.Usuario;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private ActivityPrincipalBinding binding;
    private FloatingActionButton menuDespesa, menuReceita;
    private MaterialCalendarView calendarView;
    private TextView tvSaudacao, tvSaldo;
    private FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAuth();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private Double resumoUsuario = 0.0, receitaTotal = 0.0, despesaTotal = 0.0;
    private DatabaseReference usuarioRef, movimentacaoRef;
    private ValueEventListener valueEventListenerUsuario, valueEventListenerMovimentacao;
    private RecyclerView rvMovimentos;
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private String mesAnoSelecionado;
    private MovimentacaoAdapter movimentacaoAdapter;
    private Movimentacao movimentacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Organizze");
        setSupportActionBar(binding.toolbar);

        menuDespesa = findViewById(R.id.menu_despesa);
        menuReceita = findViewById(R.id.menu_receita);
        calendarView = findViewById(R.id.calendarView);
        tvSaldo = findViewById(R.id.tvSaldo);
        tvSaudacao = findViewById(R.id.tvSaudacao);
        rvMovimentos = findViewById(R.id.rvMovimentos);

        configuraCalendarView();
        configuraRecyclerView();
        swipe();

        menuDespesa.setOnClickListener(view -> startActivity(new Intent(this, DespesasActivity.class)));

        menuReceita.setOnClickListener(view -> startActivity(new Intent(this, ReceitasActivity.class)));
    }

    public void configuraCalendarView() {
        CharSequence[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho",
                "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

        calendarView.setTitleMonths(meses);

        CalendarDay dataAtual = calendarView.getCurrentDate();
        mesAnoSelecionado = String.format("%02d", dataAtual.getMonth()) + "" + dataAtual.getYear();

        calendarView.setOnMonthChangedListener((widget, date) -> {
            mesAnoSelecionado = String.format("%02d", date.getMonth()) + "" + date.getYear();
            movimentacaoRef.removeEventListener(valueEventListenerMovimentacao);
            recuperaMovimentacao();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sair :
                auth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void recuperaResumo() {
        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
                despesaTotal = usuario.getDespesaTotal();
                resumoUsuario = receitaTotal - despesaTotal;

                DecimalFormat decimalFormat = new DecimalFormat("0.##");

                tvSaudacao.setText("Olá, " + usuario.getNome());
                tvSaldo.setText("R$ " + decimalFormat.format(resumoUsuario));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperaResumo();
        recuperaMovimentacao();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioRef.removeEventListener(valueEventListenerUsuario);
        movimentacaoRef.removeEventListener(valueEventListenerMovimentacao);
    }

    public void configuraRecyclerView() {

        movimentacaoAdapter = new MovimentacaoAdapter(movimentacoes, this);
        rvMovimentos.setLayoutManager(new LinearLayoutManager(this));
        rvMovimentos.setHasFixedSize(true);
        rvMovimentos.setAdapter(movimentacaoAdapter);
    }

    public void recuperaMovimentacao() {
        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        movimentacaoRef = firebaseRef
                .child("movimentacao")
                .child(idUsuario)
                .child(mesAnoSelecionado);

        valueEventListenerMovimentacao = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                movimentacoes.clear();
                for (DataSnapshot dados: snapshot.getChildren()) {
                    Movimentacao movimentacao = dados.getValue(Movimentacao.class);
                    movimentacao.setKey(dados.getKey());
                    movimentacoes.add(movimentacao);
                }
                movimentacaoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void swipe() {
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirMovimentacao(viewHolder);
            }
        };
        new ItemTouchHelper(itemTouch).attachToRecyclerView(rvMovimentos);
    }

    public void excluirMovimentacao(RecyclerView.ViewHolder viewHolder) {
        new AlertDialog
                .Builder(this)
                .setTitle("Excluir Movimentação da conta")
                .setMessage("Você tem certeza que deseja realmente excluir essa movimentação da sua conta?")
                .setCancelable(false)
                .setPositiveButton("Confirmar", (dialogInterface, i) -> {
                    int position = viewHolder.getAdapterPosition();
                    movimentacao = movimentacoes.get(position);

                    String emailUsuario = auth.getCurrentUser().getEmail();
                    String idUsuario = Base64Custom.codificarBase64(emailUsuario);
                    movimentacaoRef = firebaseRef
                            .child("movimentacao")
                            .child(idUsuario)
                            .child(mesAnoSelecionado);

                    movimentacaoRef.child(movimentacao.getKey()).removeValue();
                    movimentacaoAdapter.notifyItemRemoved(position);
                    atualizarSaldo();
                })
                .setNegativeButton("Cancelar", (dialogInterface, i) -> {
                    Toast.makeText(PrincipalActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                    movimentacaoAdapter.notifyDataSetChanged();
                })
                .create()
                .show();
    }

    public void atualizarSaldo() {
        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        if (movimentacao.getTipo().equals("r")) {
            receitaTotal -= movimentacao.getValor();
            usuarioRef.child("receitaTotal").setValue(receitaTotal);
        }

        if (movimentacao.getTipo().equals("d")) {
            despesaTotal -= movimentacao.getValor();
            usuarioRef.child("despesaTotal").setValue(despesaTotal);
        }
    }
}