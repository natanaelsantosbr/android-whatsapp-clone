package br.natanael.android.whatsapp.aplicacao.usuarios.callbacks;

import br.natanael.android.whatsapp.dominio.Usuario;

public interface OnSucessoAoBuscarUsuario {
    void onDadosDeUsuarioRecebido(Usuario usuario);
}
