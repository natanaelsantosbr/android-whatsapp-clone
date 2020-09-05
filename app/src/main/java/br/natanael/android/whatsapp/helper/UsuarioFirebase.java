package br.natanael.android.whatsapp.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import br.natanael.android.whatsapp.config.ConfiguracaoFirebase;
import br.natanael.android.whatsapp.dominio.Usuario;

public class UsuarioFirebase {

    public static String getIdentificadorUsuario() {
        FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return Base64Custom.codificar(auth.getCurrentUser().getEmail());
    }

    public static FirebaseUser getUsuarioAtual()
    {
        FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return auth.getCurrentUser();
    }

    public static boolean atualizarFotoUsuario(Uri url)
    {
        try
        {
            FirebaseUser user = getUsuarioAtual();

            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(url)
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful())
                    {
                        Log.d("Perfil", "Erro ao atualizar foto de perfil");
                    }
                }
            });

            return  true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return  false;
        }
    }

    public static boolean atualizarNomeUsuario(String nome)
    {
        try
        {
            FirebaseUser user = getUsuarioAtual();

            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful())
                    {
                        Log.d("Perfil", "Erro ao atualizar nome do perfil");
                    }
                }
            });

            return  true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return  false;
        }
    }

    public static Usuario getDadosUsuarioLogado() {
        FirebaseUser auth = getUsuarioAtual();

        Usuario usuario = new Usuario();
        usuario.setEmail(auth.getEmail());
        usuario.setNome(auth.getDisplayName());

        if(auth.getPhotoUrl() == null)
            usuario.setFoto("");
        else
            usuario.setFoto(auth.getPhotoUrl().toString());

        return usuario;
    }

}
