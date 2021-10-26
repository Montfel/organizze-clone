package com.montfel.organizze.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.montfel.organizze.config.ConfiguracaoFirebase;
import com.montfel.organizze.helper.Base64Custom;
import com.montfel.organizze.helper.DateCustom;

public class Movimentacao {

    private String data, categoria, descricao, tipo;
    private double valor;

    public Movimentacao() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void salvar() {
        FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAuth();
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("movimentacao")
                .child(Base64Custom.codificarBase64(auth.getCurrentUser().getEmail()))
                .child(DateCustom.mesAnoDataEscolhida(this.data))
                .push()
                .setValue(this);
    }
}
