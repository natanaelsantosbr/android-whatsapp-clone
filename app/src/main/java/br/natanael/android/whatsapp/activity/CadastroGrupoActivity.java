package br.natanael.android.whatsapp.activity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.natanael.android.whatsapp.R;
import br.natanael.android.whatsapp.aplicacao.model.usuarios.ModeloDeCadastroDeUsuario;

public class CadastroGrupoActivity extends AppCompatActivity {

    private List<ModeloDeCadastroDeUsuario> listaMembrosSelecionados = new ArrayList<>();
    private TextView textTotalMembros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_grupo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textTotalMembros = findViewById(R.id.textTotalMembros);

        FloatingActionButton fab = findViewById(R.id.fabAvancarCadastro);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //recuperar lista de membros passada
        if(getIntent().getExtras() != null)
        {
            List<ModeloDeCadastroDeUsuario> membros = (List<ModeloDeCadastroDeUsuario>) getIntent().getExtras().getSerializable("membros");
            listaMembrosSelecionados.addAll(membros);

            textTotalMembros.setText("total:" + listaMembrosSelecionados.size());


        }
    }

}
