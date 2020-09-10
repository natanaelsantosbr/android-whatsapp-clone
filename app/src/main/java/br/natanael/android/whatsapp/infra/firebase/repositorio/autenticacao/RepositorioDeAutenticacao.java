package br.natanael.android.whatsapp.infra.firebase.repositorio.autenticacao;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import java.util.concurrent.Executor;
import br.natanael.android.whatsapp.infra.firebase.repositorio.autenticacao.callbacks.OnSucessoCadastrarComUsuarioESenha;

public class RepositorioDeAutenticacao implements IRepositorioDeAutenticacao {

    private FirebaseAuth _autenticacao;

    public RepositorioDeAutenticacao() {
        _autenticacao = FirebaseAuth.getInstance();
    }

    @Override
    public void CadastrarComUsuarioESenha(final String email, String senha, final OnSucessoCadastrarComUsuarioESenha callback) {
        _autenticacao.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            callback.onSucesso();
                        }
                        else
                        {
                            String excecao = "";
                            excecao = "Erro";
                            callback.onErro(excecao);
                        }
                    }
                });
    }
}
