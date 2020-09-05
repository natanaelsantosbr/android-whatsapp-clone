package br.natanael.android.whatsapp.aplicacao.usuarios;

import br.natanael.android.whatsapp.dominio.Usuario;
import br.natanael.android.whatsapp.model.usuarios.ModeloDeCadastroDeUsuario;

public interface IServicoDeGestaoDeUsuarios {

    void cadastrar(ModeloDeCadastroDeUsuario usuario);

    void buscarUsuarioPorIdIdentificador(String id, onDataReceiveUsuarioCallback callback);

    void atualizarUsuario(String id, Usuario usuario, onDataReceiveUsuarioCallback callback);
}
