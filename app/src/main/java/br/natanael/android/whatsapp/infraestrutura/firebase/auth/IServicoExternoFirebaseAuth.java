package br.natanael.android.whatsapp.infraestrutura.firebase.auth;

import android.app.Activity;

public interface IServicoExternoFirebaseAuth {
    void CadastrarUsuarioComEmailESenha(Activity activity, String email, String senha);
}
