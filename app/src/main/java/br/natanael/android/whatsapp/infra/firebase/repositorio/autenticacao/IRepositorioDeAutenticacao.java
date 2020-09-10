package br.natanael.android.whatsapp.infra.firebase.repositorio.autenticacao;

import br.natanael.android.whatsapp.infra.firebase.repositorio.autenticacao.callbacks.OnSucessoCadastrarComUsuarioESenha;

public interface IRepositorioDeAutenticacao {
    void CadastrarComUsuarioESenha(String email, String senha, OnSucessoCadastrarComUsuarioESenha callback);
}
