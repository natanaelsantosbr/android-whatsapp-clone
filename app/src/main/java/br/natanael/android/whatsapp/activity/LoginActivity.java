package br.natanael.android.whatsapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import br.natanael.android.whatsapp.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void abrirTelaDeCadastro(View view){
        startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
    }
}
