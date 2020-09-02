package br.natanael.android.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import br.natanael.android.whatsapp.R;
import br.natanael.android.whatsapp.config.ConfiguracaoFirebase;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText campoEmail;
    private TextInputEditText campoSenha;

    private FirebaseAuth autenticacao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        campoEmail = findViewById(R.id.editLoginEmail);
        campoSenha = findViewById(R.id.editLoginSenha);

    }

    public void logarUsuario(View view) {
        String email = campoEmail.getText().toString();
        String senha = campoSenha.getText().toString();

        if(!email.isEmpty())
        {
            if(!senha.isEmpty())
            {
                autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

                autenticacao.signInWithEmailAndPassword(email, senha).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            String usuario = autenticacao.getCurrentUser().getEmail();
                            Toast.makeText(LoginActivity.this,usuario, Toast.LENGTH_SHORT).show();
                            abrirTelaPrincipal();
                        }
                        else
                        {
                            String excecao = "";

                            try {
                                throw  task.getException();
                            }
                            catch (FirebaseAuthInvalidUserException e)
                            {
                                excecao = "Usuario nao encontrado";
                            }
                            catch (FirebaseAuthInvalidCredentialsException e )
                            {
                                excecao = "Email e senha nao correspondem a um usuario cadastrado";

                            }
                            catch (Exception e)
                            {
                                excecao = e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(LoginActivity.this, excecao, Toast.LENGTH_SHORT).show();

                        }

                    }
                });


            }
            else
            {
                Toast.makeText(LoginActivity.this, "Digite a senha", Toast.LENGTH_SHORT).show();

            }
        }
        else
        {
            Toast.makeText(LoginActivity.this, "Digite o e-mail", Toast.LENGTH_SHORT).show();

        }
    }

    public void abrirTelaDeCadastro(View view){
        startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
    }
    private void abrirTelaPrincipal() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        FirebaseUser usuarioAtual = autenticacao.getCurrentUser();

        if(usuarioAtual != null)
            abrirTelaPrincipal();


    }
}
