package br.natanael.android.whatsapp.model.usuarios;

import android.widget.Toast;

public class ModeloDeCadastroDeUsuario {
    private String nome;
    private String email;
    private String senha;

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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
