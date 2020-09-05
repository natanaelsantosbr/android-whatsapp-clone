package br.natanael.android.whatsapp.aplicacao.usuarios;

import br.natanael.android.whatsapp.dominio.Usuario;

public interface onDataReceiveUsuarioCallback {
    void onDataReceived(Usuario usuario);
    void onSucesso(boolean retorno);
}
