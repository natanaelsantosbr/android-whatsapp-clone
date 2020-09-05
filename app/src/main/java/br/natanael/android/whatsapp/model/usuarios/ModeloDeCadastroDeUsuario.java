package br.natanael.android.whatsapp.model.usuarios;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import java.util.HashMap;
import java.util.Map;
import br.natanael.android.whatsapp.config.ConfiguracaoFirebase;
import br.natanael.android.whatsapp.helper.UsuarioFirebase;

public class ModeloDeCadastroDeUsuario {
    private String id;
    private String nome;
    private String email;
    private String senha;
    private String foto;

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ModeloDeCadastroDeUsuario(String nome, String email, String senha){
        if (nome.isEmpty())
            return;

        if (email.isEmpty())
            return;

        if (senha.isEmpty())
            return;

        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void salvar() {
        DatabaseReference firebase = ConfiguracaoFirebase.getDatabaseReference();

        DatabaseReference usuario = firebase.child("usuarios").child(getId());

        usuario.setValue(this);
    }

    public void atualizar() {
        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();

        DatabaseReference database = ConfiguracaoFirebase.getDatabaseReference();

        DatabaseReference usuarioRef = database.child("usuarios")
                .child(identificadorUsuario);

        Map<String, Object> valoresUsuario = converterParaMap();
        usuarioRef.updateChildren(valoresUsuario);
    }

    @Exclude
    public Map<String, Object> converterParaMap() {

        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("email", getEmail());
        usuarioMap.put("nome", getNome());
        usuarioMap.put("foto", getFoto());

        return  usuarioMap;
    }
}
