package br.natanael.android.whatsapp.aplicacao.usuarios;

import br.natanael.android.whatsapp.aplicacao.usuarios.callbacks.OnSucessoAoBuscarUsuario;
import br.natanael.android.whatsapp.aplicacao.usuarios.callbacks.OnSucessoAoCadastrarUsuario;
import br.natanael.android.whatsapp.infra.firebase.auth.callbacks.onDataReceiveUsuarioCallback;
import br.natanael.android.whatsapp.dominio.Usuario;
import br.natanael.android.whatsapp.aplicacao.helper.Base64Custom;
import br.natanael.android.whatsapp.aplicacao.usuarios.callbacks.OnSucessoAoAtualizarUsuario;
import br.natanael.android.whatsapp.infra.firebase.repositorio.autenticacao.IRepositorioDeAutenticacao;
import br.natanael.android.whatsapp.infra.firebase.repositorio.autenticacao.RepositorioDeAutenticacao;
import br.natanael.android.whatsapp.infra.firebase.repositorio.autenticacao.callbacks.OnSucessoCadastrarComUsuarioESenha;
import br.natanael.android.whatsapp.infra.firebase.repositorio.usuario.IRepositorioDeUsuarios;
import br.natanael.android.whatsapp.infra.firebase.repositorio.usuario.callbacks.OnDataReceiveCallback;
import br.natanael.android.whatsapp.infra.firebase.repositorio.usuario.RepositorioDeUsuarios;
import br.natanael.android.whatsapp.aplicacao.model.usuarios.ModeloDeCadastroDeUsuario;
import br.natanael.android.whatsapp.infra.firebase.repositorio.usuario.callbacks.OnUsuarioAtualizadoCallback;

public class ServicoDeGestaoDeUsuarios implements IServicoDeGestaoDeUsuarios {

    private IRepositorioDeUsuarios _repositorioDeUsuarios;
    private IRepositorioDeAutenticacao _repositorioDeAutenticacao;


    public ServicoDeGestaoDeUsuarios() {
        this._repositorioDeUsuarios = new RepositorioDeUsuarios();
        this._repositorioDeAutenticacao = new RepositorioDeAutenticacao();
    }


    @Override
    public void cadastrar(ModeloDeCadastroDeUsuario modelo) {
        String id = Base64Custom.codificar(modelo.getEmail());
        modelo.setId(id);

        final Usuario usuario = new Usuario(modelo.getId(), modelo.getNome(), modelo.getEmail());

        _repositorioDeUsuarios.cadastrar(usuario);
    }

    @Override
    public void buscarUsuarioPorIdIdentificador(String id, final OnSucessoAoBuscarUsuario callback) {
        _repositorioDeUsuarios.recuperarUsuario(id, new OnDataReceiveCallback() {
            @Override
            public void onDataReceived(Usuario usuario) {
                callback.onDadosDeUsuarioRecebido(usuario);
            }
        });
    }

    @Override
    public void atualizarUsuario(String id, Usuario usuario, final OnSucessoAoAtualizarUsuario callback) {
        _repositorioDeUsuarios.atualizarUsuario(id, usuario, new OnUsuarioAtualizadoCallback() {
            @Override
            public boolean onSucesso(boolean retorno) {
                callback.onSucesso(retorno);
                return retorno;
            }
        });
    }
}

