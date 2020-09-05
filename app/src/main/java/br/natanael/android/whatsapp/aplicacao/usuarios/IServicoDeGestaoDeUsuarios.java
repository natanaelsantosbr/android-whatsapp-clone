package br.natanael.android.whatsapp.aplicacao.usuarios;

import br.natanael.android.whatsapp.dominio.Usuario;

public interface IServicoDeGestaoDeUsuarios {
    void buscarUsuarioPorIdIdentificador(String id, onDataReceiveUsuarioCallback callback);

    void atualizarUsuario(String id, Usuario usuario, onDataReceiveUsuarioCallback callback);
}
