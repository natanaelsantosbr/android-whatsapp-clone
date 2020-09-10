package br.natanael.android.whatsapp.activity;

import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import br.natanael.android.whatsapp.R;
import br.natanael.android.whatsapp.aplicacao.model.usuarios.ModeloDeCadastroDeUsuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private TextView textViewNome;
    private CircleImageView circleImageViewFoto;
    private ModeloDeCadastroDeUsuario usuarioDestinatario;


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
         }
    }

}
