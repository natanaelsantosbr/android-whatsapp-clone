package br.natanael.android.whatsapp.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    public static boolean validar(String[] permissoes, Activity activity, int requestCode) {

        if(Build.VERSION.SDK_INT >= 23){

            List<String> listaDePermissoes = new ArrayList<>();

            for (String permissao: permissoes)
            {
                boolean temPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;

                if(!temPermissao)
                {
                    listaDePermissoes.add(permissao);
                }
            }

            if(listaDePermissoes.isEmpty())
                return  true;

            String[] novasPermissoes = new String[listaDePermissoes.size()];
            listaDePermissoes.toArray(novasPermissoes);



            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);

        }

        return true;
    }
}
