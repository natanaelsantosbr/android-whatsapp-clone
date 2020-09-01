package br.natanael.android.whatsapp.aplicacao.usuarios;

import br.natanael.android.whatsapp.infra.firebase.auth.IServicoExternoFirebaseAuth;
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
