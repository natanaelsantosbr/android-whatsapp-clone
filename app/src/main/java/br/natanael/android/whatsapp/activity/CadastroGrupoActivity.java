package br.natanael.android.whatsapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import br.natanael.android.whatsapp.R;
import br.natanael.android.whatsapp.adapter.GrupoSelecionadoAdapter;
import br.natanael.android.whatsapp.aplicacao.config.ConfiguracaoFirebase;
import br.natanael.android.whatsapp.aplicacao.config.ConfiguracaoRequestCode;
import br.natanael.android.whatsapp.aplicacao.helper.UsuarioFirebase;
import br.natanael.android.whatsapp.aplicacao.model.grupos.Grupo;
import br.natanael.android.whatsapp.aplicacao.model.usuarios.ModeloDeCadastroDeUsuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class CadastroGrupoActivity extends AppCompatActivity {

    private List<ModeloDeCadastroDeUsuario> listaMembrosSelecionados = new ArrayList<>();
    private TextView textTotalParticipantes;
    private EditText editNomeGrupo;
    private GrupoSelecionadoAdapter grupoSelecionadoAdapter;
    private RecyclerView recyclerMembrosGrupo;
    private CircleImageView imageGrupo;
    private StorageReference storageReference;
    private Grupo grupo;
    private  FloatingActionButton fabSalvarGrupo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cadastro_grupo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo grupo");
        toolbar.setSubtitle("Defina o nome");
        setSupportActionBar(toolbar);

        textTotalParticipantes = findViewById(R.id.textTotalParticipantes);
        editNomeGrupo = findViewById(R.id.editNomeGrupo);
        recyclerMembrosGrupo = findViewById(R.id.recyclerMembrosGrupo);
        imageGrupo  = findViewById(R.id.imageGrupo);
        fabSalvarGrupo = findViewById(R.id.fabSalvarGrupo);

        grupo = new Grupo();

        storageReference =  ConfiguracaoFirebase.getFirebaseStorage();

        //recuperar lista de membros passada
        if(getIntent().getExtras() != null)
        {
            List<ModeloDeCadastroDeUsuario> membros = (List<ModeloDeCadastroDeUsuario>) getIntent().getExtras().getSerializable("membros");
            listaMembrosSelecionados.addAll(membros);

            textTotalParticipantes.setText("Participantes: " + membros.size());
        }

        //configurar recyclerView
        grupoSelecionadoAdapter = new GrupoSelecionadoAdapter(listaMembrosSelecionados, getApplicationContext());

        RecyclerView.LayoutManager layoutManagerHorizontal = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false);
        recyclerMembrosGrupo.setLayoutManager(layoutManagerHorizontal);
        recyclerMembrosGrupo.setHasFixedSize(true);
        recyclerMembrosGrupo.setAdapter(grupoSelecionadoAdapter);

        imageGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ir = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                if(ir.resolveActivity(getPackageManager()) != null)
                {
                    startActivityForResult(ir, ConfiguracaoRequestCode.SELECAO_GALERIA);
                }
            }
        });

        fabSalvarGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeDoGrupo = editNomeGrupo.getText().toString();

                //adiciona o usuario que esta logado
                listaMembrosSelecionados.add(UsuarioFirebase.getDadosUsuarioLogado());

                grupo.setMembros(listaMembrosSelecionados);

                grupo.setNome(nomeDoGrupo);
                grupo.salvar();

                Intent i = new Intent(CadastroGrupoActivity.this, ChatActivity.class);
                i.putExtra("chatGrupo", grupo);
                startActivity(i);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            Bitmap imagem = null;

            try {
                Uri localDaImagemSelecionada = data.getData();
                imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localDaImagemSelecionada);


                if (imagem != null) {
                    imageGrupo.setImageBitmap(imagem);

                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70,baos);
                    byte[] dadosDaImagem = baos.toByteArray();


                    final StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("grupos")
                            .child(grupo.getId() + ".jpeg");


                    UploadTask uploadTask = imagemRef.putBytes(dadosDaImagem);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CadastroGrupoActivity.this, "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();

                                    grupo.setFoto(url.toString());

                                    //atualizaFotoUsuario(url);

                                    Toast.makeText(CadastroGrupoActivity.this, "Sucesso ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
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

}
