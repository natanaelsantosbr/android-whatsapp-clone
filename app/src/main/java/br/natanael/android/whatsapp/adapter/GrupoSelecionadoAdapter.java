package br.natanael.android.whatsapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import br.natanael.android.whatsapp.R;
import br.natanael.android.whatsapp.aplicacao.model.usuarios.ModeloDeCadastroDeUsuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class GrupoSelecionadoAdapter extends RecyclerView.Adapter<GrupoSelecionadoAdapter.MyViewHolder> {


    private List<ModeloDeCadastroDeUsuario> contatosSelecionados;
    private Context context;


    public GrupoSelecionadoAdapter(List<ModeloDeCadastroDeUsuario> listaContatos, Context c) {
        this.contatosSelecionados  = listaContatos;
        this.context = c;
    }

    //Puxa o layout xml
    @NonNull
    @Override
    public GrupoSelecionadoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_grupo_selecionado, parent, false);
        return  new GrupoSelecionadoAdapter.MyViewHolder(itemLista);
    }

    //Preenche os dados
    @Override
    public void onBindViewHolder(@NonNull GrupoSelecionadoAdapter.MyViewHolder holder, int position) {
        ModeloDeCadastroDeUsuario usuario = contatosSelecionados.get(position);

        holder.nome.setText(usuario.getNome());

        if(usuario.getFoto() != null)
        {
            Uri uri = Uri.parse(usuario.getFoto());
            Glide.with(context).load(uri).into(holder.foto);
        }
        else
        {
            holder.foto.setImageResource(R.drawable.padrao);
        }
    }

    @Override
    public int getItemCount() {
        return contatosSelecionados.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView foto;
        TextView nome;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.imageViewFotoMembroSelecionado);
            nome = itemView.findViewById(R.id.textNomeMembroSelecionado);
        }
    }
}
