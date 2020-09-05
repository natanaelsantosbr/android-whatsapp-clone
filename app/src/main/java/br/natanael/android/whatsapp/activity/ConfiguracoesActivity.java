package br.natanael.android.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import br.natanael.android.whatsapp.R;
import br.natanael.android.whatsapp.config.ConfiguracaoFirebase;
import br.natanael.android.whatsapp.config.ConfiguracaoRequestCode;
import br.natanael.android.whatsapp.helper.Base64Custom;
import br.natanael.android.whatsapp.helper.Permissao;
import br.natanael.android.whatsapp.helper.UsuarioFirebase;
import de.hdodenhof.circleimageview.CircleImageView;

public class ConfiguracoesActivity extends AppCompatActivity {

    private ImageButton imageButtonCamera;
    private ImageButton imageButtonGaleria;
    private CircleImageView circleImageViewPerfil;

    private StorageReference storageReference;
    private String identificadorUsuario;

    private FirebaseUser usuarioLogado;

    private EditText editTextNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        prepararProcessamentoDaActivity();
    }

    private void prepararProcessamentoDaActivity() {
        configurarFindViewById();

        inicializarVariaveis();
        criarPermissoes();
        criarToolbar();
        abrirEventosDeCliques();
    }

    private void inicializarVariaveis() {
        storageReference =  ConfiguracaoFirebase.getFirebaseStorage();
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        usuarioLogado = UsuarioFirebase.getUsuarioAtual();

        Uri url = usuarioLogado.getPhotoUrl();
        if(url != null)
        {
            Glide.with(getApplicationContext())
                    .load(url)
                    .into(circleImageViewPerfil);
        }
        else
        {
            circleImageViewPerfil.setImageResource(R.drawable.padrao);
        }

        String nomeDoUsuario = usuarioLogado.getDisplayName();
        editTextNome.setText(nomeDoUsuario);

    }

    private void configurarFindViewById() {
        imageButtonCamera = findViewById(R.id.imageButtonCamera);
        imageButtonGaleria = findViewById(R.id.imageButtonGaleria);
        circleImageViewPerfil = findViewById(R.id.circleImageViewFotoPerfil);
        editTextNome = findViewById(R.id.editTextNome);
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

    private void abrirEventosDeCliques() {
        abrirCamera();
        abrirGaleria();
    }

    private void abrirCamera() {
        imageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ir = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(ir, ConfiguracaoRequestCode.SELECAO_CAMERA);

                if(ir.resolveActivity(getPackageManager()) != null)
                {

                }
            }
        });
    }

    private void abrirGaleria() {
        imageButtonGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ir = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                if(ir.resolveActivity(getPackageManager()) != null)
                {
                    startActivityForResult(ir, ConfiguracaoRequestCode.SELECAO_GALERIA);
                }
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            Bitmap imagem = null;

            try {
                switch (requestCode) {
                    case ConfiguracaoRequestCode.SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case ConfiguracaoRequestCode.SELECAO_GALERIA:
                        Uri localDaImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localDaImagemSelecionada);

                        break;
                }

                if (imagem != null) {
                    circleImageViewPerfil.setImageBitmap(imagem);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70,baos);
                    byte[] dadosDaImagem = baos.toByteArray();

                    final StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("perfil")
                            .child(identificadorUsuario + "perfil.jpeg");


                    UploadTask uploadTask = imagemRef.putBytes(dadosDaImagem);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ConfiguracoesActivity.this, "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();

                                    atualizaFotoUsuario(url);

                                    Toast.makeText(ConfiguracoesActivity.this, "Sucesso ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void atualizaFotoUsuario(Uri url) {
        UsuarioFirebase.atualizarFotoUsuario(url);
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
