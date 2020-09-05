package br.natanael.android.whatsapp.aplicacao.usuarios;

import android.util.Base64;
import android.util.Log;

import br.natanael.android.whatsapp.config.ConfiguracaoFirebase;
import br.natanael.android.whatsapp.dominio.Usuario;
import br.natanael.android.whatsapp.helper.Base64Custom;
import br.natanael.android.whatsapp.infra.firebase.auth.IServicoExternoFirebaseAuth;
import br.natanael.android.whatsapp.infra.firebase.repositorio.usuario.IRepositorioDeUsuarios;
import br.natanael.android.whatsapp.infra.firebase.repositorio.usuario.OnDataReceiveCallback;
import br.natanael.android.whatsapp.infra.firebase.repositorio.usuario.RepositorioDeUsuarios;
public class ServicoDeGestaoDeUsuarios implements IServicoDeGestaoDeUsuarios {

    private IRepositorioDeUsuarios _repositorioDeUsuarios;


    public ServicoDeGestaoDeUsuarios() {
        this._repositorioDeUsuarios = new RepositorioDeUsuarios();
    }


    @Override
    public void buscarUsuarioPorIdIdentificador(String id,  final onDataReceiveUsuarioCallback callback) {
        _repositorioDeUsuarios.recuperarUsuario(id, new OnDataReceiveCallback() {
            @Override
            public void onDataReceived(Usuario usuario) {
                callback.onDataReceived(usuario);
            }

            @Override
            public void onSucesso(boolean retorno) {

            }
        });
    }

    @Override
    public void atualizarUsuario(String id, Usuario usuario, final onDataReceiveUsuarioCallback callback) {
        _repositorioDeUsuarios.atualizarUsuario(id, usuario, new OnDataReceiveCallback() {
            @Override
            public void onDataReceived(Usuario usuario) {

            }

            @Override
            public void onSucesso(boolean retorno) {
                callback.onSucesso(retorno);
            }
        });
    }
}
