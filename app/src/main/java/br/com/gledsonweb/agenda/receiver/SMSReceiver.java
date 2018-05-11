package br.com.gledsonweb.agenda.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.telephony.SmsMessage;
import android.widget.Toast;

import br.com.gledsonweb.agenda.R;
import br.com.gledsonweb.agenda.dao.AlunoDAO;

public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            Object[] pdus = (Object[]) intent.getSerializableExtra("pdus");
            byte[] pdu = (byte[]) pdus[0];
            String formato = (String) intent.getSerializableExtra("format");

            SmsMessage sms = SmsMessage.createFromPdu(pdu, formato);
            String telefone = sms.getDisplayOriginatingAddress();

            AlunoDAO dao = new AlunoDAO(context);
            if(dao.isAluno(telefone)) {
                Toast.makeText(context, "Chegou um SMS de Aluno!", Toast.LENGTH_SHORT).show();
                MediaPlayer m = MediaPlayer.create(context, R.raw.msg);
            }
        }
    }
}
