package br.natanael.android.whatsapp.infra.firebase.repositorio.autenticacao.callbacks;

public interface OnSucessoCadastrarComUsuarioESenha {
    void onSucesso();
    void onErro(String erro);
}
