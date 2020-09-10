package br.natanael.android.whatsapp.aplicacao.autenticacao;

import br.natanael.android.whatsapp.aplicacao.model.autenticacao.ModeloDeCadastroDeAutenticacao;

public interface IServicoDeGestaoDeAutenticacao {
    void Cadastrar(ModeloDeCadastroDeAutenticacao modelo);
}
