package br.com.gledsonweb.agenda;

import android.Manifest;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import br.com.gledsonweb.agenda.adapter.AlunosAdapter;
import br.com.gledsonweb.agenda.adapter.ListaAlunosAdapter;
import br.com.gledsonweb.agenda.adapter.listener.OnItemClickListener;
import br.com.gledsonweb.agenda.dao.AlunoDAO;
import br.com.gledsonweb.agenda.modelo.Aluno;

import static br.com.gledsonweb.agenda.constante.AlunoConstantes.CHAVE_ALUNO;
import static br.com.gledsonweb.agenda.constante.AlunoConstantes.CHAVE_POSICAO;
import static br.com.gledsonweb.agenda.constante.AlunoConstantes.CODIGO_REQ_ALTERA_ALUNO;
import static br.com.gledsonweb.agenda.constante.AlunoConstantes.CODIGO_REQ_INSERE_ALUNO;

public class ListaAlunosFragment extends Fragment {
    private RecyclerView listaAlunos;
    private ListaAlunosAdapter adapter;
    private View view;

    public static ListaAlunosFragment newInstance() {

        return new ListaAlunosFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lista_alunos, container, false);
        listaAlunos = view.findViewById(R.id.rvListaAluno);
        carregaLista();
        configuraBotaoInsereAluno();
        registerForContextMenu(listaAlunos);
        return view;

    }

    private void configuraBotaoInsereAluno(){
        Button novoAluno = view.findViewById(R.id.btnNovoAluno);
        novoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentVaiProFormulario = new Intent(getContext(), FormularioActivity.class);
                startActivityForResult(intentVaiProFormulario, 1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CODIGO_REQ_INSERE_ALUNO && data.hasExtra(CHAVE_ALUNO)){
            Aluno alunoRecebido = (Aluno) data.getSerializableExtra(CHAVE_ALUNO);
            adapter.adiciona(alunoRecebido);
        }

        if(requestCode == CODIGO_REQ_ALTERA_ALUNO && data.hasExtra(CHAVE_POSICAO)){
            Aluno alunoRecebido = (Aluno) data.getSerializableExtra(CHAVE_ALUNO);
            int posicaoRecebida = (int) data.getSerializableExtra(CHAVE_POSICAO);
            adapter.altera(posicaoRecebida, alunoRecebido);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        int position = -1;
//        try {
//            position = ((ListaAlunosAdapter)getAdapter()).getPosition();
//        } catch (Exception e) {
////            Log.d(TAG, e.getLocalizedMessage(), e);
//            return super.onContextItemSelected(item);
//        }
//        switch (item.getItemId()) {
//            case R.id.:
//                // do your stuff
//                break;
//        }
//        return super.onContextItemSelected(item);
//    }

    //    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
//        final Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(info.position);
//
//
//        MenuItem itemLigar = menu.add("Ligar");
//        itemLigar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
//                        != PackageManager.PERMISSION_GRANTED){
//
////                    ActivityCompat.requestPermissions(getContext(),
////                            new String[]{Manifest.permission.CALL_PHONE}, 123);
//
//                }else {
//                    Intent intentTelefone = new Intent(Intent.ACTION_CALL);
//                    intentTelefone.setData(Uri.parse("tel:" + aluno.getTelefone()));
//                    startActivity(intentTelefone);
//                }
//                return false;
//            }
//        });
//
//        MenuItem itemSMS = menu.add("Enviar SMS");
//        Intent intentSMS = new Intent(Intent.ACTION_VIEW);
//        intentSMS.setData(Uri.parse("sms:" + aluno.getTelefone()));
//        itemSMS.setIntent(intentSMS);
//
//        MenuItem itemMapa = menu.add("Visualizar Mapa");
//        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
//        intentMapa.setData(Uri.parse("geo:0,0?q=" + aluno.getEndereco()));
//        itemMapa.setIntent(intentMapa);
//
//        MenuItem itemSite = menu.add("Visitar site");
//        Intent intentSite = new Intent(Intent.ACTION_VIEW);
//
//        String site = aluno.getSite();
//        if (!site.startsWith("http://") && !site.startsWith("https://")){
//            site = "http://" + site;
//        }
//        intentSite.setData(Uri.parse(site));
//        itemSite.setIntent(intentSite);
//
//        MenuItem deletar = menu.add("Deletar");
//        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//
//                AlunoDAO dao = new AlunoDAO(getContext());
//                dao.deleta(aluno);
//                dao.close();
//
//                carregaLista();
//                return false;
//            }
//        });
//    }

    private void carregaLista() {
        AlunoDAO dao = new AlunoDAO(getContext());
        List<Aluno> alunos = dao.buscaAlunos();
        dao.close();

        configuraAdapter(alunos);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        listaAlunos.setLayoutManager(layoutManager);
    }

    private void configuraAdapter(List<Aluno> alunos) {
        adapter = new ListaAlunosAdapter(getContext(), alunos);
        listaAlunos.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Aluno aluno, int posicao) {
                vaiParaFormularioActivityAltera(aluno, posicao);
            }
        });
    }

    private void vaiParaFormularioActivityAltera(Aluno aluno, int posicao) {
        Intent intentVaiProFormulario = new Intent(getContext(), FormularioActivity.class);
        intentVaiProFormulario.putExtra(CHAVE_ALUNO, aluno);
        intentVaiProFormulario.putExtra(CHAVE_POSICAO, posicao);
        startActivityForResult(intentVaiProFormulario, CODIGO_REQ_ALTERA_ALUNO);
    }
}
