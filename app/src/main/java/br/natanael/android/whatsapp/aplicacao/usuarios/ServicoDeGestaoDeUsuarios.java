package br.natanael.android.whatsapp.aplicacao.usuarios;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import br.natanael.android.whatsapp.infraestrutura.firebase.auth.IServicoExternoFirebaseAuth;
import br.natanael.android.whatsapp.model.usuarios.ModeloDeCadastroDeUsuario;

public class ServicoDeGestaoDeUsuarios implements IServicoDeGestaoDeUsuarios {

    private IServicoExternoFirebaseAuth _servicoExternoFirebaseAuth;

    public ServicoDeGestaoDeUsuarios(IServicoExternoFirebaseAuth servicoExternoFirebaseAuth) {
        this._servicoExternoFirebaseAuth = servicoExternoFirebaseAuth;
    }

    @Override
    public void Cadastrar(ModeloDeCadastroDeUsuario usuario) {
        //_servicoExternoFirebaseAuth.CadastrarUsuarioComEmailESenha(activity, usuario.getEmail(), usuario.getSenha());
    }
}
