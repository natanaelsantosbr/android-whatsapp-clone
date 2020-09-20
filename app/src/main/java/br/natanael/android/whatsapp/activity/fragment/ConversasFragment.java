package br.natanael.android.whatsapp.activity.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.FontRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import br.natanael.android.whatsapp.R;
import br.natanael.android.whatsapp.activity.ChatActivity;
import br.natanael.android.whatsapp.adapter.ContatosAdapter;
import br.natanael.android.whatsapp.adapter.ConversasAdapter;
import br.natanael.android.whatsapp.aplicacao.config.ConfiguracaoFirebase;
import br.natanael.android.whatsapp.aplicacao.helper.RecyclerItemClickListener;
import br.natanael.android.whatsapp.aplicacao.helper.UsuarioFirebase;
import br.natanael.android.whatsapp.aplicacao.model.conversas.Conversa;
import br.natanael.android.whatsapp.aplicacao.model.usuarios.ModeloDeCadastroDeUsuario;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {

    private RecyclerView recyclerViewConversas;
    private List<Conversa> listaConversas = new ArrayList<>();
    private ConversasAdapter adapter;
    private DatabaseReference database;
    private DatabaseReference conversasRef;
    private ChildEventListener childEventListenerConversas;


    public List<Conversa> getConversas()
    {
        return this.listaConversas;
    }
    public ConversasFragment() {    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_conversas, container, false);

        recyclerViewConversas = view.findViewById(R.id.recyclerListaConversas);

        //Configurar o adapter
        adapter = new ConversasAdapter(listaConversas, getActivity());

        //Configurar recyclerView
        RecyclerView.LayoutManager layoutManager  = new LinearLayoutManager(getActivity());
        recyclerViewConversas.setLayoutManager(layoutManager);
        recyclerViewConversas.setHasFixedSize(true);
        recyclerViewConversas.setAdapter(adapter);

        //configurar evento de clique
        recyclerViewConversas.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerViewConversas, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<Conversa> listaConversaAtualizadas = adapter.getConversas();
                Conversa conversaSelecionada = listaConversaAtualizadas.get(position);

                Intent i = new Intent(getActivity(), ChatActivity.class);

                if(conversaSelecionada.getIsGrup().equals("false"))
                {
                    i.putExtra("chatContato", conversaSelecionada.getusuarioExibicao());
                }
                else
                {
                    i.putExtra("chatGrupo", conversaSelecionada.getGrupo());
                }

                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }

        }));

        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();

        database = ConfiguracaoFirebase.getDatabaseReference();
        conversasRef =  database.child("conversas").child(identificadorUsuario);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarConversas();
    }

    @Override
    public void onStop() {
        super.onStop();
        conversasRef.removeEventListener(childEventListenerConversas);
    }

    public void pesquisarConversas(String texto){

        texto   = texto.toLowerCase();

        List<Conversa> listaConversasBusca = new ArrayList<>();

        for (Conversa conversa : listaConversas)
        {
            if(conversa.getusuarioExibicao() != null)
            {
                String nome = conversa.getusuarioExibicao().getNome().toLowerCase();
                String ultimaMensagem = conversa.getUltimaMensagem().toLowerCase();

                if(nome.contains(texto) || ultimaMensagem.contains(texto))
                {
                    listaConversasBusca.add(conversa);
                }
            }
            else
            {
                String nome = conversa.getGrupo().getNome().toLowerCase();
                String ultimaMensagem = conversa.getUltimaMensagem().toLowerCase();

                if(nome.contains(texto) || ultimaMensagem.contains(texto))
                {
                    listaConversasBusca.add(conversa);
                }
            }
        }

        adapter = new ConversasAdapter(listaConversasBusca, getActivity());
        recyclerViewConversas.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void recarregarConversas() {
        adapter = new ConversasAdapter(listaConversas, getActivity());
        recyclerViewConversas.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void recuperarConversas() {
        listaConversas.clear();

        listaConversas = new ArrayList<>();
        adapter = new ConversasAdapter(listaConversas, getActivity());
        recyclerViewConversas.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        childEventListenerConversas = conversasRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Conversa conversa = dataSnapshot.getValue(Conversa.class);
                listaConversas.add(conversa);

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
