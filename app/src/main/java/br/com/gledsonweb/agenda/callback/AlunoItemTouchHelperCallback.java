package br.com.gledsonweb.agenda.callback;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import br.com.gledsonweb.agenda.adapter.ListaAlunosAdapter;
import br.com.gledsonweb.agenda.dao.AlunoDAO;

public class AlunoItemTouchHelperCallback extends ItemTouchHelper.Callback{
    private final ListaAlunosAdapter adapter;

    public AlunoItemTouchHelperCallback(ListaAlunosAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int marcacoesDeDeslize = ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        int marcacoesDeArrastar = ItemTouchHelper.DOWN | ItemTouchHelper.UP
                | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        return makeMovementFlags(marcacoesDeArrastar, marcacoesDeDeslize);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int posicaoInicial = viewHolder.getAdapterPosition();
        int posicaoFinal = target.getAdapterPosition();
        trocaAlunos(posicaoInicial, posicaoFinal);
        return true;
    }

    private void trocaAlunos(int posicaoInicial, int posicaoFinal) {
//        new AlunoDAO().troca(posicaoInicial, posicaoFinal);
        adapter.troca(posicaoInicial, posicaoFinal);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int posicaoDoAlunoDeslizada = viewHolder.getAdapterPosition();
        removeAluno(posicaoDoAlunoDeslizada);
    }

    private void removeAluno(int posicao) {
//        new AlunoDAO().deleta(posicao);
        adapter.remove(posicao);
    }
}
