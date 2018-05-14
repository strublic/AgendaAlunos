package br.com.gledsonweb.agenda;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;

import br.com.gledsonweb.agenda.dao.AlunoDAO;
import br.com.gledsonweb.agenda.modelo.Aluno;

import static br.com.gledsonweb.agenda.constante.AlunoConstantes.CHAVE_ALUNO;
import static br.com.gledsonweb.agenda.constante.AlunoConstantes.CHAVE_POSICAO;
import static br.com.gledsonweb.agenda.constante.AlunoConstantes.CODIGO_CAMERA;
import static br.com.gledsonweb.agenda.constante.AlunoConstantes.CODIGO_REQ_ALTERA_ALUNO;

public class FormularioActivity extends AppCompatActivity {

    private FormularioHelper helper;
    private String caminhoFoto;
    private int posicaoRecebida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        helper = new FormularioHelper(this);

        Intent dadosRecebidos = getIntent();
        if(dadosRecebidos.hasExtra(CHAVE_ALUNO)) {
            Aluno alunoRecebido = (Aluno) dadosRecebidos.getSerializableExtra(CHAVE_ALUNO);
            posicaoRecebida = dadosRecebidos.getIntExtra(CHAVE_POSICAO, -1);
            if (alunoRecebido != null) {
                helper.preencheFormulario(alunoRecebido);
            }
        }

        Button botaoFoto = findViewById(R.id.btFoto);
        botaoFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                caminhoFoto = getExternalFilesDir(null ) +  "/" + System.currentTimeMillis() + ".jpg";
                File arquivoFoto = new File(caminhoFoto);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(arquivoFoto));
                startActivityForResult(intentCamera, CODIGO_CAMERA);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == CODIGO_CAMERA) {
                helper.carregaImagem(caminhoFoto);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_formulario, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_formulario_ok:
                Aluno aluno = helper.getAluno();

                AlunoDAO dao = new AlunoDAO(this);
                if (aluno.getId() != null){
                    dao.altera(aluno);
                }else{
                    dao.insere(aluno);
                }

                dao.close();

                Toast.makeText(FormularioActivity.this, "Aluno " + aluno.getNome() + " salvo!", Toast.LENGTH_SHORT).show();
                retornaAluno(aluno);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void retornaAluno(Aluno aluno) {
        Intent resultadoInsercao = new Intent();
        resultadoInsercao.putExtra(CHAVE_ALUNO, aluno);
        resultadoInsercao.putExtra(CHAVE_POSICAO, posicaoRecebida);
        setResult(CODIGO_REQ_ALTERA_ALUNO, resultadoInsercao);
    }
}
