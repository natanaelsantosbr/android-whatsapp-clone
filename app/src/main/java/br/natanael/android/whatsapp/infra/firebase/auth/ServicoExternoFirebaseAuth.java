package br.natanael.android.whatsapp.infra.firebase.auth;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ServicoExternoFirebaseAuth implements IServicoExternoFirebaseAuth {

    private static FirebaseAuth auth;

    public ServicoExternoFirebaseAuth() {
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public Task<AuthResult> CadastrarUsuarioComEmailESenha(final Activity activity, String email, String senha) {

        Task<AuthResult> retorno = auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.i("TAG", "Sucesso");
            }
        });

        return retorno;
    }
}
