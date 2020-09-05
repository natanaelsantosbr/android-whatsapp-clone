package br.natanael.android.whatsapp.infra.firebase.repositorio.usuario;

import br.natanael.android.whatsapp.dominio.Usuario;
import br.natanael.android.whatsapp.infra.firebase.repositorio.usuario.callbacks.OnSucessoAoCadastrarUsuario;

public interface IRepositorioDeUsuarios {
    void recuperarUsuario(String id, final OnDataReceiveCallback callback);

    void atualizarUsuario(String id, Usuario usuario, final OnDataReceiveCallback callback);

    void cadastrar(Usuario usuario);
}
