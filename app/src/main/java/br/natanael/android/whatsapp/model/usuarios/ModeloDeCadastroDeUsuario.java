package br.natanael.android.whatsapp.model.usuarios;

import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import br.natanael.android.whatsapp.activity.ConfiguracoesActivity;
import br.natanael.android.whatsapp.config.ConfiguracaoFirebase;

public class ModeloDeCadastroDeUsuario {
    private String id;
    private String nome;
    private String email;
    private String senha;

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ModeloDeCadastroDeUsuario(String nome, String email, String senha){
        if (nome.isEmpty())
            return;

        if (email.isEmpty())
            return;

        if (senha.isEmpty())
            return;

        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void salvar() {
        DatabaseReference firebase = ConfiguracaoFirebase.getDatabaseReference();

        DatabaseReference usuario = firebase.child("usuarios").child(getId());

        usuario.setValue(this);
    }
}
