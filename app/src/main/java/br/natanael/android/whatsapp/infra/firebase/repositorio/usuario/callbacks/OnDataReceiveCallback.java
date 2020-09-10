package br.natanael.android.whatsapp.infra.firebase.repositorio.usuario.callbacks;

import br.natanael.android.whatsapp.dominio.Usuario;

public interface OnDataReceiveCallback {
    void onDataReceived(Usuario usuario);
}
