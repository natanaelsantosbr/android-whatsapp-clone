package br.natanael.android.whatsapp.aplicacao.model.conversas;

import android.provider.ContactsContract;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import br.natanael.android.whatsapp.aplicacao.config.ConfiguracaoFirebase;
import br.natanael.android.whatsapp.aplicacao.model.Grupo;
import br.natanael.android.whatsapp.aplicacao.model.usuarios.ModeloDeCadastroDeUsuario;

public class Conversa {

    private String idRemetente;
    private String idDestinatario;
    private String ultimaMensagem;
    private ModeloDeCadastroDeUsuario usuarioExibicao;
    private String isGrup;
    private Grupo grupo;

    public Conversa() {
        this.setIsGrupo("false");
    }

    public String getIsGrup() {
        return isGrup;
    }

    public void setIsGrupo(String isGrupo) {
        this.isGrup = isGrupo;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }



    public String getIdRemetente() {
        return idRemetente;
    }

    public void setIdRemetente(String idRemetente) {
        this.idRemetente = idRemetente;
    }

    public String getIdDestinatario() {
        return idDestinatario;
    }

    public void setIdDestinatario(String idDestinatario) {
        this.idDestinatario = idDestinatario;
    }

    public String getUltimaMensagem() {
        return ultimaMensagem;
    }

    public void setUltimaMensagem(String ultimaMensagem) {
        this.ultimaMensagem = ultimaMensagem;
    }

    public ModeloDeCadastroDeUsuario getusuarioExibicao() {
        return usuarioExibicao;
    }

    public void setusuarioExibicao(ModeloDeCadastroDeUsuario usuario) {
        this.usuarioExibicao = usuario;
    }

    public void salvar() {
        DatabaseReference database = ConfiguracaoFirebase.getDatabaseReference();
        DatabaseReference conversarRef = database.child("conversas");

        conversarRef.child(this.getIdRemetente())
                .child(this.getIdDestinatario())
                .setValue(this);
    }
}
