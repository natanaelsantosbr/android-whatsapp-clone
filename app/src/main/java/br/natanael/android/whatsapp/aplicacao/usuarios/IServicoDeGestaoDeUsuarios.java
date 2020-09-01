package br.natanael.android.whatsapp.aplicacao.usuarios;

import br.natanael.android.whatsapp.model.usuarios.ModeloDeCadastroDeUsuario;

public interface IServicoDeGestaoDeUsuarios {
    void Cadastrar(ModeloDeCadastroDeUsuario usuario);
}
