package br.natanael.android.whatsapp.infra.firebase.auth;

import android.app.Activity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface IServicoExternoFirebaseAuth {
    Task<AuthResult> CadastrarUsuarioComEmailESenha(Activity activity, String email, String senha);
}
