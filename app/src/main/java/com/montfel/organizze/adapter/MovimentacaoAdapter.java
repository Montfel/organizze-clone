package com.montfel.organizze.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.montfel.organizze.R;
import com.montfel.organizze.model.Movimentacao;

import java.util.List;

public class MovimentacaoAdapter extends RecyclerView.Adapter<MovimentacaoAdapter.MyViewHolder> {

    private final List<Movimentacao> listaMovimentacao;
    private Context context;

    public MovimentacaoAdapter(List<Movimentacao> listaAcademia, Context context) {
        this.listaMovimentacao = listaAcademia;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_movimentacao, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movimentacao movimentacao = listaMovimentacao.get(position);
        holder.tvTituloAdapter.setText(movimentacao.getDescricao());
        holder.tvValorAdpter.setText(String.valueOf(movimentacao.getValor()));
        holder.tvCategoriaAdapter.setText(movimentacao.getCategoria());
        holder.tvValorAdpter.setTextColor(context.getResources().getColor(R.color.colorAccentReceita));

        if (movimentacao.getTipo().equals("d")) {
            holder.tvValorAdpter.setTextColor(context.getResources().getColor(R.color.accent));
            holder.tvValorAdpter.setText("-" + movimentacao.getValor());
        }
    }

    @Override
    public int getItemCount() {
        return this.listaMovimentacao.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        final TextView tvTituloAdapter, tvValorAdpter, tvCategoriaAdapter;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvTituloAdapter = itemView.findViewById(R.id.tvTituloAdapter);
            tvValorAdpter = itemView.findViewById(R.id.tvValorAdapter);
            tvCategoriaAdapter = itemView.findViewById(R.id.tvCategoriaAdapter);

        }
    }
}