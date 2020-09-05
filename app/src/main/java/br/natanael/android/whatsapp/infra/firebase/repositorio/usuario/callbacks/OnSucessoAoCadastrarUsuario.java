package br.natanael.android.whatsapp.infra.firebase.repositorio.usuario.callbacks;

public interface OnSucessoAoCadastrarUsuario {
    void onSucesso(boolean retorno);
    void onErro(String excecao);
}
