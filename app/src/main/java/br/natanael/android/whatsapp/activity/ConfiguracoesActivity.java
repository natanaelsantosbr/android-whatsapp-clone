package br.natanael.android.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;

import java.io.BufferedInputStream;

import br.natanael.android.whatsapp.R;
import br.natanael.android.whatsapp.config.ConfiguracaoFirebase;
import br.natanael.android.whatsapp.config.ConfiguracaoRequestCode;
import br.natanael.android.whatsapp.helper.Permissao;

public class ConfiguracoesActivity extends AppCompatActivity {

    private ImageButton imageButtonCamera;
    private ImageButton imageButtonGaleria;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        prepararProcessamentoDaActivity();
    }

    private void prepararProcessamentoDaActivity() {

        configurarFindViewById();

        criarPermissoes();

        criarToolbar();

        abrirCamera();
    }

    private void abrirCamera() {
        imageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ir = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if(ir.resolveActivity(getPackageManager()) != null)
                {
                    startActivityForResult(ir, ConfiguracaoRequestCode.SELECAO_CAMERA);
                }
            }
        });
    }

    private void configurarFindViewById() {
        imageButtonCamera = findViewById(R.id.imageButtonCamera);
        imageButtonGaleria = findViewById(R.id.imageButtonGaleria);
    }

    private void criarPermissoes() {
        String[] permissoesNecessarias = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };

        Permissao.validar(permissoesNecessarias, this, 1);
    }

    private void criarToolbar() {


        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults)
        {
            if(permissaoResultado == PackageManager.PERMISSION_DENIED)
            {
                alertaValidacaoPermissao();

            }
        }
    }

    private void alertaValidacaoPermissao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitas as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
