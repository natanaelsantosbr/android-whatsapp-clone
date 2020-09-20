package br.natanael.android.whatsapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.natanael.android.whatsapp.R;
import br.natanael.android.whatsapp.adapter.MensagensAdapter;
import br.natanael.android.whatsapp.aplicacao.config.ConfiguracaoFirebase;
import br.natanael.android.whatsapp.aplicacao.config.ConfiguracaoRequestCode;
import br.natanael.android.whatsapp.aplicacao.helper.Base64Custom;
import br.natanael.android.whatsapp.aplicacao.helper.UsuarioFirebase;
import br.natanael.android.whatsapp.aplicacao.model.conversas.Conversa;
import br.natanael.android.whatsapp.aplicacao.model.grupos.Grupo;
import br.natanael.android.whatsapp.aplicacao.model.mensagens.Mensagem;
import br.natanael.android.whatsapp.aplicacao.model.usuarios.ModeloDeCadastroDeUsuario;
import br.natanael.android.whatsapp.dominio.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private TextView textViewNome;
    private CircleImageView circleImageViewFoto;
    private ModeloDeCadastroDeUsuario usuarioDestinatario;
    private EditText editMensagem;

    //identificar usuarios remetente e destinatario
    private String idUsuarioRemetente;
    private String idUsuarioDestinatario;

    private RecyclerView recyclerViewMensagens;

    private MensagensAdapter adapter;
    private List<Mensagem> mensagens = new ArrayList<>();

    private DatabaseReference database;
    private DatabaseReference mensagensRef;

    private ChildEventListener childEventListenerMensagens;
    private ImageView imageCamera;

    private StorageReference storage;

    private Grupo grupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Configurar toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Configuracoes iniciais
        textViewNome = findViewById(R.id.textViewNomeChat);
        circleImageViewFoto = findViewById(R.id.circleImageFotoChat);
        editMensagem = findViewById(R.id.editMensagem);
        idUsuarioRemetente = UsuarioFirebase.getIdentificadorUsuario();
        recyclerViewMensagens = findViewById(R.id.recyclerMensagens);
        imageCamera = findViewById(R.id.imageCamera);


        //Recuperar dados do usuario destinatario
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if(bundle.containsKey("chatGrupo"))
            {
                grupo = (Grupo)bundle.getSerializable("chatGrupo");
                idUsuarioDestinatario = grupo.getId();

                textViewNome.setText(grupo.getNome());

                String foto = grupo.getFoto();

                if (foto != null) {
                    Uri url = Uri.parse(foto);
                    Glide.with(ChatActivity.this).load(url).into(circleImageViewFoto);
                } else {
                    circleImageViewFoto.setImageResource(R.drawable.padrao);
                }

            }
            else
            {
                usuarioDestinatario = (ModeloDeCadastroDeUsuario) bundle.getSerializable("chatContato");
                textViewNome.setText(usuarioDestinatario.getNome());


                String foto = usuarioDestinatario.getFoto();

                if (foto != null) {
                    Uri url = Uri.parse(foto);

                    Glide.with(ChatActivity.this).load(url).into(circleImageViewFoto);
                } else {
                    circleImageViewFoto.setImageResource(R.drawable.padrao);
                }

                //recuperar dados usuario destinatario
                idUsuarioDestinatario = Base64Custom.codificar(usuarioDestinatario.getEmail());
            }


        }

        //Configuracao Adapter
        adapter = new MensagensAdapter(mensagens, getApplicationContext());

        //Configuracao recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewMensagens.setLayoutManager(layoutManager);
        recyclerViewMensagens.setHasFixedSize(true);
        recyclerViewMensagens.setAdapter(adapter);



        database = ConfiguracaoFirebase.getDatabaseReference();
        storage = ConfiguracaoFirebase.getFirebaseStorage();

        mensagensRef = database.child("mensagens")
                .child(idUsuarioRemetente)
                .child(idUsuarioDestinatario);

        //Evento de clique na camera
        imageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ir = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (ir.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(ir, ConfiguracaoRequestCode.SELECAO_CAMERA);

                }
            }
        });

    }

    public void enviarMensagem(View view) {
        String textoMensagem = editMensagem.getText().toString();

        if (!textoMensagem.isEmpty()) {

            if(usuarioDestinatario != null)
            {
                Mensagem mensagem = new Mensagem();
                mensagem.setIdUsuario(idUsuarioRemetente);
                mensagem.setMensagem(textoMensagem);

                //Salvar mensagem para o remetente
                salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);

                //Salvar mensagem para o destinatario
                salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, mensagem);



                //Salvar conversa remetente
                salvarConversa(idUsuarioRemetente, idUsuarioDestinatario, usuarioDestinatario,  mensagem, false) ;


                //Salvar conversa destinatario
                ModeloDeCadastroDeUsuario usuarioRemetente = UsuarioFirebase.getDadosUsuarioLogado();
                salvarConversa(idUsuarioDestinatario, idUsuarioRemetente, usuarioRemetente, mensagem, false );
            }
            else
            {
                for (ModeloDeCadastroDeUsuario membro : grupo.getMembros())
                {
                    String idRemetenteGrupo = Base64Custom.codificar(membro.getEmail());
                    String idUsuarioLogadoGrupo = UsuarioFirebase.getIdentificadorUsuario();

                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUsuarioLogadoGrupo);
                    mensagem.setMensagem(textoMensagem);
                    mensagem.setNome(UsuarioFirebase.getDadosUsuarioLogado().getNome());

                    salvarMensagem(idRemetenteGrupo, idUsuarioDestinatario,  mensagem);

                    salvarConversa(idRemetenteGrupo,idUsuarioDestinatario, usuarioDestinatario,  mensagem, true);
                }
            }

            editMensagem.setText("");

        } else {
            Toast.makeText(ChatActivity.this, "Digite uma mensagem para enviar!", Toast.LENGTH_SHORT).show();

        }

    }

    public void salvarMensagem(String idRemetente, String idDestinatario, Mensagem msg) {
        DatabaseReference database = ConfiguracaoFirebase.getDatabaseReference();
        DatabaseReference mensagemRef = database.child("mensagens");

        mensagemRef
                .child(idRemetente)
                .child(idDestinatario)
                .push()
                .setValue(msg);
    }

    private void salvarConversa(String idRemetente, String idDestinatario, ModeloDeCadastroDeUsuario usuarioExibicao, Mensagem mensagem, boolean isGroup)
    {
        Conversa conversaRemetente = new Conversa();
        conversaRemetente.setIdRemetente(idRemetente);
        conversaRemetente.setIdDestinatario(idDestinatario);
        conversaRemetente.setUltimaMensagem(mensagem.getMensagem());


        if(!isGroup)
        {
            conversaRemetente.setusuarioExibicao(usuarioExibicao);
            conversaRemetente.setIsGrupo("false");
        }
        else
        {
            conversaRemetente.setIsGrupo("true");
            conversaRemetente.setGrupo(grupo);
        }

        conversaRemetente.salvar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarMensagens();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mensagensRef.removeEventListener(childEventListenerMensagens);
    }

    private void recuperarMensagens() {
        childEventListenerMensagens = mensagensRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Mensagem mensagem = dataSnapshot.getValue(Mensagem.class);
                mensagens.add(mensagem);
                adapter.notifyDataSetChanged();

                recyclerViewMensagens.scrollToPosition(mensagens.size() -1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {
                switch (requestCode) {
                    case ConfiguracaoRequestCode.SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                }

                if (imagem != null) {

                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosDaImagem = baos.toByteArray();

                    //Criar nome da imagem
                    final String nomeImagem = UUID.randomUUID().toString();

                    final StorageReference imagemRef = storage
                            .child("imagens")
                            .child("fotos")
                            .child(idUsuarioRemetente)
                            .child(nomeImagem + ".jpeg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosDaImagem);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChatActivity.this, "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    Uri url = task.getResult();

                                    Mensagem mensagem = new Mensagem();
                                    mensagem.setIdUsuario(idUsuarioRemetente);
                                    mensagem.setMensagem("imagem.jpeg");
                                    mensagem.setImagem(url.toString());

                                    //Remetente
                                    salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);


                                    //Destinatario
                                    salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, mensagem);

                                    Toast.makeText(ChatActivity.this, "Sucesso ao enviar imagem", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

