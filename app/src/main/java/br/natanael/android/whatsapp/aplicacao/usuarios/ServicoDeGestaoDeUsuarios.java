package br.natanael.android.whatsapp.aplicacao.usuarios;

import br.natanael.android.whatsapp.dominio.Usuario;
import br.natanael.android.whatsapp.helper.Base64Custom;
import br.natanael.android.whatsapp.infra.firebase.repositorio.usuario.IRepositorioDeUsuarios;
import br.natanael.android.whatsapp.infra.firebase.repositorio.usuario.OnDataReceiveCallback;
import br.natanael.android.whatsapp.infra.firebase.repositorio.usuario.RepositorioDeUsuarios;
import br.natanael.android.whatsapp.infra.firebase.repositorio.usuario.callbacks.OnSucessoAoCadastrarUsuario;
import br.natanael.android.whatsapp.model.usuarios.ModeloDeCadastroDeUsuario;

public class ServicoDeGestaoDeUsuarios implements IServicoDeGestaoDeUsuarios {

    private IRepositorioDeUsuarios _repositorioDeUsuarios;


    public ServicoDeGestaoDeUsuarios() {
        this._repositorioDeUsuarios = new RepositorioDeUsuarios();
    }


    @Override
    public void cadastrar(ModeloDeCadastroDeUsuario modelo) {

        String id = Base64Custom.codificar(modelo.getEmail());
        modelo.setId(id);

        Usuario usuario = new Usuario(modelo.getId(), modelo.getNome(), modelo.getEmail());

        _repositorioDeUsuarios.cadastrar(usuario, new OnSucessoAoCadastrarUsuario() {
            @Override
            public void onSucesso(boolean retorno) {

            }

            @Override
            public void onErro(String excecao) {

            }
        });
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
