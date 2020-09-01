package br.natanael.android.whatsapp.infraestrutura.firebase.auth;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class ServicoExternoFirebaseAuth implements IServicoExternoFirebaseAuth {

    private static FirebaseAuth auth;

    public ServicoExternoFirebaseAuth() {
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void CadastrarUsuarioComEmailESenha(final Activity activity, String email, String senha)
    {
        auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(activity, "Sucesso ao cadastrar usuário!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String excecao = "";

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
                        }
                    }
                });
    }
}
