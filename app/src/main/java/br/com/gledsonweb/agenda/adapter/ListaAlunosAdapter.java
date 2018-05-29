package br.com.gledsonweb.agenda.adapter;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import br.com.gledsonweb.agenda.ListaAlunosActivity;
import br.com.gledsonweb.agenda.R;
import br.com.gledsonweb.agenda.adapter.listener.OnItemClickListener;
import br.com.gledsonweb.agenda.dao.AlunoDAO;
import br.com.gledsonweb.agenda.modelo.Aluno;

public class ListaAlunosAdapter extends RecyclerView.Adapter<ListaAlunosAdapter.AlunoViewHolder> {

    private List<Aluno> alunos;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private int position;

    public ListaAlunosAdapter(Context context, List<Aluno> alunos){
        this.context = context;
        this.alunos = alunos;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ListaAlunosAdapter.AlunoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new AlunoViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(ListaAlunosAdapter.AlunoViewHolder holder, int position) {
        final int posicao = position;
        Aluno aluno = alunos.get(position);
        holder.vincula(aluno);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(posicao);
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return alunos.size();
    }

    public void adiciona(Aluno aluno){
        alunos.add(aluno);
        notifyDataSetChanged();
    }

    public void altera(int posicao, Aluno aluno) {
        alunos.set(posicao, aluno);
        notifyItemChanged(posicao);
    }

    public void remove(int posicao) {
        alunos.remove(posicao);
        notifyItemRemoved(posicao);
    }

    private void removerItem(int position) {
        alunos.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, alunos.size());
    }

    public void troca(int posicaoInicial, int posicaoFinal) {
        Collections.swap(alunos, posicaoInicial, posicaoFinal);
        notifyItemMoved(posicaoInicial, posicaoFinal);
    }


    public void setPosition(int position) {
        this.position = position;
    }

    class AlunoViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private final TextView nome;
        private final TextView telefone;
        private final ImageView campoFoto;
        private Aluno aluno;

        public AlunoViewHolder(View itemView){
            super(itemView);
            nome = itemView.findViewById(R.id.item_nome);
            telefone = itemView.findViewById(R.id.item_telefone);
            campoFoto = itemView.findViewById(R.id.item_foto);

            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(aluno, getAdapterPosition());
                }
            });
        }

        public void vincula(Aluno aluno){
            this.aluno = aluno;
            nome.setText(aluno.getNome());
            telefone.setText(aluno.getTelefone());

            String foto = aluno.getCaminhoFoto();
            if (foto != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(foto);
                Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
                campoFoto.setImageBitmap(bitmapReduzido);
                campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo info) {
//            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
//            final Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(info.position);


            MenuItem itemLigar = menu.add("Ligar");
            itemLigar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions((ListaAlunosActivity)context,
                            new String[]{Manifest.permission.CALL_PHONE}, 123);

                    }else {
                        Intent intentTelefone = new Intent(Intent.ACTION_CALL);
                        intentTelefone.setData(Uri.parse("tel:" + aluno.getTelefone()));
                        context.startActivity(intentTelefone);
                    }
                    return false;
                }
            });


            MenuItem itemWhats = menu.add("Whatsapp");
            itemWhats.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
                    sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(aluno.getTelefone())+"@s.whatsapp.net");
                    context.startActivity(sendIntent);



//                    String whatsAppMessage = "Olá meu jogo preferido é ${jogo.titulo}";
//                    Intent sendIntent = new Intent();
//                    sendIntent.action = Intent.ACTION_SEND;
//                    sendIntent.putExtra(Intent.EXTRA_TEXT, whatsAppMessage);
//                    sendIntent.type = "text/plain";
//
//                    // Do not forget to add this to open whatsApp App specifically
//                    sendIntent.`package` = "com.whatsapp";
//                    startActivity(sendIntent);

//                    String text = "Olá meu jogo preferido é " + aluno.getNome();// Replace with your message.
//                    String toNumber = aluno.getTelefone();
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.putExtra("jid", Uri.parse("http://api.whatsapp.com/send?phone=$toNumber&text=$text"));
//                    context.startActivity(intent);

                    return true;

                }
            });

            MenuItem itemSMS = menu.add("Enviar SMS");
            Intent intentSMS = new Intent(Intent.ACTION_VIEW);
            intentSMS.setData(Uri.parse("sms:" + aluno.getTelefone()));
            itemSMS.setIntent(intentSMS);

            MenuItem itemMapa = menu.add("Visualizar Mapa");
            Intent intentMapa = new Intent(Intent.ACTION_VIEW);
            intentMapa.setData(Uri.parse("geo:0,0?q=" + aluno.getEndereco()));
            itemMapa.setIntent(intentMapa);

            MenuItem itemSite = menu.add("Visitar site");
            Intent intentSite = new Intent(Intent.ACTION_VIEW);

            String site = aluno.getSite();
            if (!site.startsWith("http://") && !site.startsWith("https://")){
                site = "http://" + site;
            }
            intentSite.setData(Uri.parse(site));
            itemSite.setIntent(intentSite);

            MenuItem deletar = menu.add("Deletar");
            deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    AlunoDAO dao = new AlunoDAO(context);
                    dao.deleta(aluno);
                    dao.close();
                    removerItem(getAdapterPosition());
                    return false;
                }
            });
        }
    }
}
