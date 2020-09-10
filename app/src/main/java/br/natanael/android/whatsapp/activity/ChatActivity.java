package br.natanael.android.whatsapp.activity;

import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.natanael.android.whatsapp.R;
import br.natanael.android.whatsapp.adapter.MensagensAdapter;
import br.natanael.android.whatsapp.aplicacao.config.ConfiguracaoFirebase;
import br.natanael.android.whatsapp.aplicacao.helper.Base64Custom;
import br.natanael.android.whatsapp.aplicacao.helper.UsuarioFirebase;
import br.natanael.android.whatsapp.aplicacao.model.mensagens.Mensagem;
import br.natanael.android.whatsapp.aplicacao.model.usuarios.ModeloDeCadastroDeUsuario;
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


        //Recuperar dados do usuario destinatario
         Bundle bundle = getIntent().getExtras();

         if(bundle != null)
         {
             usuarioDestinatario = (ModeloDeCadastroDeUsuario) bundle.getSerializable("chatContato");
             textViewNome.setText(usuarioDestinatario.getNome());


             String foto = usuarioDestinatario.getFoto();

             if(foto != null)
             {
                 Uri url = Uri.parse(foto);

                 Glide.with(ChatActivity.this).load(url).into(circleImageViewFoto);
             }
             else
             {
                 circleImageViewFoto.setImageResource(R.drawable.padrao);
             }

             //recuperar dados usuario destinatario
             idUsuarioDestinatario = Base64Custom.codificar(usuarioDestinatario.getEmail());
         }

         //Configuracao Adapter
        adapter = new MensagensAdapter(mensagens, getApplicationContext());

         //Configuracao recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
         recyclerViewMensagens.setLayoutManager(layoutManager);
         recyclerViewMensagens.setHasFixedSize(true);
         recyclerViewMensagens.setAdapter(adapter);


        database = ConfiguracaoFirebase.getDatabaseReference();

        mensagensRef = database.child("mensagens")
                .child(idUsuarioRemetente)
                .child(idUsuarioDestinatario);

    }

    public void enviarMensagem(View view)
    {
        String textoMensagem= editMensagem.getText().toString();

        if(!textoMensagem.isEmpty())
        {
            Mensagem mensagem = new Mensagem();
            mensagem.setIdUsuario(idUsuarioRemetente);
            mensagem.setMensagem(textoMensagem);

            salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);

            editMensagem.setText("");
        }
        else
        {
            Toast.makeText(ChatActivity.this, "Digite uma mensagem para enviar!", Toast.LENGTH_SHORT).show();

        }

    }

    public void salvarMensagem(String idRemetente, String idDestinatario, Mensagem msg)
    {
        DatabaseReference database = ConfiguracaoFirebase.getDatabaseReference();
        DatabaseReference mensagemRef = database.child("mensagens");

        mensagemRef
                .child(idRemetente)
                .child(idDestinatario)
                .push()
                .setValue(msg);
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

    private void recuperarMensagens () {
        childEventListenerMensagens = mensagensRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Mensagem mensagem = dataSnapshot.getValue(Mensagem.class);
                mensagens.add(mensagem);
                adapter.notifyDataSetChanged();
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


}
