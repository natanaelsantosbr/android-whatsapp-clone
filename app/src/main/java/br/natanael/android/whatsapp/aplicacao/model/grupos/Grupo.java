package br.natanael.android.whatsapp.aplicacao.model.grupos;

import android.provider.ContactsContract;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

import br.natanael.android.whatsapp.aplicacao.config.ConfiguracaoFirebase;
import br.natanael.android.whatsapp.aplicacao.helper.Base64Custom;
import br.natanael.android.whatsapp.aplicacao.model.conversas.Conversa;
import br.natanael.android.whatsapp.aplicacao.model.usuarios.ModeloDeCadastroDeUsuario;

public class Grupo implements Serializable {
    private String id;
    private String nome;
    private String foto;
    private List<ModeloDeCadastroDeUsuario> membros;

    public Grupo() {
        DatabaseReference database = ConfiguracaoFirebase.getDatabaseReference();
        DatabaseReference grupoRef = database.child("grupos");

        String idGrupoFirebase = grupoRef.push().getKey();
        this.setId(idGrupoFirebase);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<ModeloDeCadastroDeUsuario> getMembros() {
        return membros;
    }

    public void setMembros(List<ModeloDeCadastroDeUsuario> membros) {
        this.membros = membros;
    }

    public void salvar(){
        DatabaseReference database = ConfiguracaoFirebase.getDatabaseReference();
        DatabaseReference grupoRef = database.child("grupos");

        grupoRef.child(getId()).setValue(this);

        //Salvar conversa para membros do grupo
        for (ModeloDeCadastroDeUsuario membro : getMembros())
        {
            String idRemetente = Base64Custom.codificar(membro.getEmail());
            String idDestinatario = getId();

            Conversa conversa = new Conversa();
            conversa.setIdRemetente(idRemetente);
            conversa.setIdDestinatario(idDestinatario);
            conversa.setUltimaMensagem("");
            conversa.setIsGrupo("true");
            conversa.setGrupo(this);

            conversa.salvar();

        }

    }
}
