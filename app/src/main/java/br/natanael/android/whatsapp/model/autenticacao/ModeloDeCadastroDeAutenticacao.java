package br.natanael.android.whatsapp.model.autenticacao;

public class ModeloDeCadastroDeAutenticacao {
    private String email;
    private String senha;

    public ModeloDeCadastroDeAutenticacao(String email, String senha) {
        if(email.isEmpty())
            return;

        if(senha.isEmpty())
            return;

        this.email = email;
        this.senha = senha;
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
