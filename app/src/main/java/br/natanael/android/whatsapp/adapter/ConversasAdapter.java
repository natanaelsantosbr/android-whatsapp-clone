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
import br.natanael.android.whatsapp.aplicacao.model.grupos.Grupo;
import br.natanael.android.whatsapp.aplicacao.model.conversas.Conversa;
import br.natanael.android.whatsapp.aplicacao.model.usuarios.ModeloDeCadastroDeUsuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class ConversasAdapter extends RecyclerView.Adapter<ConversasAdapter.MyViewHolder> {

    private List<Conversa> conversas;
    private Context context;

    public ConversasAdapter(List<Conversa> lista, Context c) {
        this.conversas = lista;
        this.context = c;
    }

    public List<Conversa> getConversas()
    {
        return this.conversas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contatos, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Conversa conversa = conversas.get(position);
        holder.ultimaMensagem.setText(conversa.getUltimaMensagem());

        if(conversa.getIsGrup().equals("false"))
        {
            ModeloDeCadastroDeUsuario usuario = conversa.getusuarioExibicao();

            if(usuario != null)
            {
                holder.nome.setText(usuario.getNome());

                if(usuario.getFoto() != null)
                {
                    Uri url = Uri.parse(usuario.getFoto());
                    Glide.with(context).load(url).into(holder.foto);
                }
                else
                {
                    holder.foto.setImageResource(R.drawable.padrao);
                }
            }


        }
        else
        {
            Grupo grupo = conversa.getGrupo();
            holder.nome.setText(grupo.getNome());

            if(grupo.getFoto() != null)
            {
                Uri url = Uri.parse(grupo.getFoto());
                Glide.with(context).load(url).into(holder.foto);
            }
            else
            {
                holder.foto.setImageResource(R.drawable.padrao);
            }
        }
    }

    @Override
    public int getItemCount() {
        return conversas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView foto;
        TextView nome, ultimaMensagem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.imageViewFotoContato);
            nome = itemView.findViewById(R.id.textNomeContato);
            ultimaMensagem = itemView.findViewById(R.id.textEmailContato);
        }
    }
}
