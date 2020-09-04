package br.natanael.android.whatsapp.helper;

import com.google.firebase.auth.FirebaseAuth;

import br.natanael.android.whatsapp.config.ConfiguracaoFirebase;

public class UsuarioFirebase {

    public static String getIdentificadorUsuario() {
        FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return Base64Custom.codificar(auth.getCurrentUser().getEmail());
    }


}
