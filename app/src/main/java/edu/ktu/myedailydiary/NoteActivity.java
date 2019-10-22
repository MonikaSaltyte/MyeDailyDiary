package edu.ktu.myedailydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

import edu.ktu.myedailydiary.User.Notedata;

public class NoteActivity extends AppCompatActivity {
    EditText title,desc, date;
    String idsend,titlesend,descsend,datasend ;
    Button addnote;
    Notedata notedata;
    String iid, idate,ititle,idesc;

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Calendar calendar = Calendar.getInstance();
        String currentDay= DateFormat.getDateInstance().format(calendar.getTime());
        title=findViewById(R.id.title);
        desc=findViewById(R.id.desc);
        date=findViewById(R.id.date);
        date.setText(currentDay);
        date.setEnabled(false);
        addnote=findViewById(R.id.addnotes);
        final Notedata notedate = new Notedata(iid, idate,ititle,idesc);



        mDatabase = FirebaseDatabase.getInstance().getReference().child("Notes");


    }

    public void AddNotess(android.view.View view) {
        titlesend=title.getText().toString();
        descsend=desc.getText().toString();
        datasend=date.getText().toString();
        if(TextUtils.isEmpty(titlesend) || TextUtils.isEmpty(descsend) || TextUtils.isEmpty(datasend)){
            return;
        }
        AddNotes( titlesend,descsend, datasend);
    }

    private void AddNotes( String titlesend, String descsend, String datasend)
    {
        String uniq_messageID=mDatabase.push().getKey();
        String id=FirebaseAuth.getInstance().getCurrentUser().getUid();
        Notedata listdata = new Notedata(uniq_messageID, datasend, titlesend, descsend);
        mDatabase.child(id).child(uniq_messageID).setValue(listdata).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(NoteActivity.this, "Notes Added", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),SecondActivity.class));
                        finish();
                    }
                });

    }
}
