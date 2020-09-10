package br.natanael.android.whatsapp.aplicacao.usuarios;

import br.natanael.android.whatsapp.aplicacao.usuarios.callbacks.OnSucessoAoBuscarUsuario;
import br.natanael.android.whatsapp.dominio.Usuario;
import br.natanael.android.whatsapp.aplicacao.model.usuarios.ModeloDeCadastroDeUsuario;
import br.natanael.android.whatsapp.aplicacao.usuarios.callbacks.OnSucessoAoAtualizarUsuario;

public interface IServicoDeGestaoDeUsuarios {

    void cadastrar(ModeloDeCadastroDeUsuario usuario);

    void buscarUsuarioPorIdIdentificador(String id, OnSucessoAoBuscarUsuario callback);

    void atualizarUsuario(String id, Usuario usuario, OnSucessoAoAtualizarUsuario callback);
}
