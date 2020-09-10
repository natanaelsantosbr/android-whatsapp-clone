package br.natanael.android.whatsapp.activity;

import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.natanael.android.whatsapp.R;
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


}
