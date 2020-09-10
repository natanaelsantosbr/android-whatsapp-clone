package br.natanael.android.whatsapp.aplicacao.model.conversas;

import android.provider.ContactsContract;

import com.google.firebase.database.DatabaseReference;

import br.natanael.android.whatsapp.aplicacao.config.ConfiguracaoFirebase;
import br.natanael.android.whatsapp.aplicacao.model.usuarios.ModeloDeCadastroDeUsuario;

public class Conversa {

    private String idRemetente;
    private String idDestinatario;
    private String ultimaMensagem;
    private ModeloDeCadastroDeUsuario usuarioExibicao;

    public Conversa() {

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
