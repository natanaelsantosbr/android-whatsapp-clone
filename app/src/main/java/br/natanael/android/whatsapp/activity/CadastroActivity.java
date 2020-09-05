package br.natanael.android.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.natanael.android.whatsapp.R;
import br.natanael.android.whatsapp.aplicacao.usuarios.IServicoDeGestaoDeUsuarios;
import br.natanael.android.whatsapp.aplicacao.usuarios.ServicoDeGestaoDeUsuarios;
import br.natanael.android.whatsapp.config.ConfiguracaoFirebase;
import br.natanael.android.whatsapp.dominio.Usuario;
import br.natanael.android.whatsapp.helper.Base64Custom;
import br.natanael.android.whatsapp.helper.UsuarioFirebase;
import br.natanael.android.whatsapp.model.usuarios.ModeloDeCadastroDeUsuario;

public class CadastroActivity extends AppCompatActivity  {

    private TextInputEditText campoNome;
    private TextInputEditText campoEmail;
    private TextInputEditText campoSenha;

    private FirebaseAuth autenticacao;

    private IServicoDeGestaoDeUsuarios _servicoDeGestaoDeUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        configurarFindViewById();

        inicializarVariaveis();
    }

    private void inicializarVariaveis() {
        _servicoDeGestaoDeUsuarios = new ServicoDeGestaoDeUsuarios();
    }

    private void configurarFindViewById() {
        campoNome = findViewById(R.id.editNome);
        campoEmail = findViewById(R.id.editLoginEmail);
        campoSenha = findViewById(R.id.editLoginSenha);
    }

    public void cadastrarUsuario(View view)
    {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        String textoNome = campoNome.getText().toString();
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();

        final ModeloDeCadastroDeUsuario modelo = new ModeloDeCadastroDeUsuario(textoNome, textoEmail, textoSenha);

        if(modelo == null)
            Toast.makeText(CadastroActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
        else
        {
            autenticacao.createUserWithEmailAndPassword(modelo.getEmail(), modelo.getSenha())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                try {

                                    _servicoDeGestaoDeUsuarios.cadastrar(modelo);

                                    UsuarioFirebase.atualizarNomeUsuario(modelo.getNome());

                                    Toast.makeText(CadastroActivity.this, "Sucesso ao cadastrar usuário! 111111111111", Toast.LENGTH_SHORT).show();

                                    finish();
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }


                            }
                            else
                            {
                                String excecao = "";

                                excecao = getString(task);

                                Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();

                            }
                        }

                        private String getString(@NonNull Task<AuthResult> task) {
                            String excecao;
                            try{
                                throw task.getException();
                            }
                            catch (FirebaseAuthWeakPasswordException e){
                                excecao = "Digite uma senha mais forte!";
                            }
                            catch (FirebaseAuthInvalidCredentialsException e)
                            {
                                excecao = "Por favor, digite um e-mail válido";
                            }
                            catch (FirebaseAuthUserCollisionException e)

                            {
                                excecao = "Esta conta já foi cadastrada";
                            }
                            catch (Exception e)
                            {
                                excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                                e.printStackTrace();
                            }
                            return excecao;
                        }
                    });


        }
    }
}
