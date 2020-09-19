package br.natanael.android.whatsapp.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.internal.InternalTokenProvider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.AdapterView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.natanael.android.whatsapp.R;
import br.natanael.android.whatsapp.adapter.ContatosAdapter;
import br.natanael.android.whatsapp.adapter.GrupoSelecionadoAdapter;
import br.natanael.android.whatsapp.aplicacao.config.ConfiguracaoFirebase;
import br.natanael.android.whatsapp.aplicacao.helper.RecyclerItemClickListener;
import br.natanael.android.whatsapp.aplicacao.helper.UsuarioFirebase;
import br.natanael.android.whatsapp.aplicacao.model.usuarios.ModeloDeCadastroDeUsuario;

public class GrupoActivity extends AppCompatActivity {

    private RecyclerView recyclerMembrosSelecionados, recyclerMembros;
    private ContatosAdapter contatosAdapter;
    private GrupoSelecionadoAdapter grupoSelecionadoAdapter;
    private List<ModeloDeCadastroDeUsuario> listaMembros = new ArrayList<>();
    private List<ModeloDeCadastroDeUsuario> listaMembrosSelecionados = new ArrayList<>();
    private ValueEventListener valueEventListenerMembros;
    private DatabaseReference usuariosRef;
    private FirebaseUser usuarioAtual;
    private  Toolbar toolbar;
    private FloatingActionButton fabAvancarCadastro;


    public void atualizarMembrosToolbar() {
        int totalSelecionados = listaMembrosSelecionados.size();
        int total  = listaMembros.size() + totalSelecionados;
        toolbar.setSubtitle(totalSelecionados + " de " + total + " selecionados");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo grupo");
        setSupportActionBar(toolbar);

        //Botao Voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerMembrosSelecionados = findViewById(R.id.recyclerMembrosSelecionados);
        recyclerMembros = findViewById(R.id.recyclerMembros);
        fabAvancarCadastro = findViewById(R.id.fabAvancarCadastro);

        //configurar o adapter
        contatosAdapter = new ContatosAdapter(listaMembros, getApplicationContext());

        usuariosRef = ConfiguracaoFirebase.getDatabaseReference().child("usuarios");
        usuarioAtual = UsuarioFirebase.getUsuarioAtual();

        //configurar recyclerView para contatos
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMembros.setLayoutManager(layoutManager);
        recyclerMembros.setHasFixedSize(true);
        recyclerMembros.setAdapter(contatosAdapter);




        recyclerMembros.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerMembros, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ModeloDeCadastroDeUsuario usuarioSelecionado = listaMembros.get(position);
                listaMembros.remove(usuarioSelecionado);
                contatosAdapter.notifyDataSetChanged();

                listaMembrosSelecionados.add(usuarioSelecionado);
                grupoSelecionadoAdapter.notifyDataSetChanged();
                atualizarMembrosToolbar();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

        //configurar o recyclerView para Membros selecionados
        grupoSelecionadoAdapter = new GrupoSelecionadoAdapter(listaMembrosSelecionados, getApplicationContext());

        RecyclerView.LayoutManager layoutManagerHorizontal = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false);
        recyclerMembrosSelecionados.setLayoutManager(layoutManagerHorizontal);
        recyclerMembrosSelecionados.setHasFixedSize(true);
        recyclerMembrosSelecionados.setAdapter(grupoSelecionadoAdapter);

        recyclerMembrosSelecionados.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerMembrosSelecionados, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ModeloDeCadastroDeUsuario usuario = listaMembrosSelecionados.get(position);
                listaMembrosSelecionados.remove(usuario);
                grupoSelecionadoAdapter.notifyDataSetChanged();

                listaMembros.add(usuario);
                contatosAdapter.notifyDataSetChanged();
                atualizarMembrosToolbar();


            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

        fabAvancarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GrupoActivity.this, CadastroGrupoActivity.class);
                i.putExtra("membros", (Serializable) listaMembrosSelecionados);

                startActivity(i);
            }
        });
    }

    public void onStart() {
        super.onStart();
        recuperarContatos();
    }

    @Override
    public void onStop() {
        super.onStop();
        usuariosRef.removeEventListener(valueEventListenerMembros);
    }

    public void recuperarContatos(){
        valueEventListenerMembros =  usuariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dados: dataSnapshot.getChildren())
                {
                    ModeloDeCadastroDeUsuario usuario = dados.getValue(ModeloDeCadastroDeUsuario.class);

                    if(!usuarioAtual.getEmail().equals(usuario.getEmail()))
                    {
                        listaMembros.add(usuario);
                    }
                }

                contatosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
