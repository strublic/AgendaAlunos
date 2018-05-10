package br.com.gledsonweb.agenda;

import android.widget.EditText;
import android.widget.RatingBar;

import br.com.gledsonweb.agenda.modelo.Aluno;

public class FormularioHelper {

    private Aluno aluno;

    private final EditText nome;
    private final EditText endereco;
    private final EditText telefone;
    private final EditText site;
    private final RatingBar nota;

    public FormularioHelper(FormularioActivity activity){
        nome = (EditText) activity.findViewById(R.id.etNome);
        endereco = (EditText) activity.findViewById(R.id.etEndereco);
        telefone = (EditText) activity.findViewById(R.id.etTelefone);
        site = (EditText) activity.findViewById(R.id.etSite);
        nota = (RatingBar) activity.findViewById(R.id.rbNota);
        aluno = new Aluno();
    }

    public Aluno getAluno() {
        aluno.setNome(nome.getText().toString());
        aluno.setEndereco(endereco.getText().toString());
        aluno.setTelefone(telefone.getText().toString());
        aluno.setSite(site.getText().toString());
        aluno.setNota(Double.valueOf(nota.getProgress()));

        return aluno;
    }

    public void preencheFormulario(Aluno aluno) {
        nome.setText(aluno.getNome());
        endereco.setText(aluno.getEndereco());
        telefone.setText(aluno.getTelefone());
        site.setText(aluno.getSite());
        nota.setProgress(aluno.getNota().intValue());
        this.aluno = aluno;
    }
}
