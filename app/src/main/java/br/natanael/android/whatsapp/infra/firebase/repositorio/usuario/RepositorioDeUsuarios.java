package br.natanael.android.whatsapp.infra.firebase.repositorio.usuario;

import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import br.natanael.android.whatsapp.config.ConfiguracaoFirebase;
import br.natanael.android.whatsapp.dominio.Usuario;
import br.natanael.android.whatsapp.infra.firebase.repositorio.usuario.callbacks.OnSucessoAoCadastrarUsuario;

public class RepositorioDeUsuarios implements  IRepositorioDeUsuarios {

    FirebaseDatabase _database;

    public RepositorioDeUsuarios() {
        _database = FirebaseDatabase.getInstance();
    }


    @Override
    public void cadastrar(Usuario usuario) {
        DatabaseReference usuarioRef = _database.getReference("usuarios").child(usuario.getId());
        usuarioRef.setValue(usuario);
    }

    @Override
    public void recuperarUsuario(String id, final OnDataReceiveCallback callback) {

        DatabaseReference reference = _database.getReference("usuarios").child(id);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                callback.onDataReceived(usuario);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void atualizarUsuario(String id, Usuario usuario, final OnDataReceiveCallback callback) {
        DatabaseReference reference = _database.getReference("usuarios").child(id);

        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("email", usuario.getEmail());
        usuarioMap.put("nome", usuario.getNome());
        usuarioMap.put("foto", usuario.getFoto());

        DatabaseReference usuarioRef = _database.getReference("usuarios/" + id);

        usuarioRef.updateChildren(usuarioMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.onSucesso(true);
            }
        });
    }
}
