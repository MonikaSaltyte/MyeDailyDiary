package edu.ktu.myedailydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.ktu.myedailydiary.User.Notedata;

public class EditActivity extends AppCompatActivity {

    EditText title,desc, date;
    String titlesend,descsend, datesend;
    private DatabaseReference mDatabase;
    private Notedata listdata;
    Button update,delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        date=findViewById(R.id.date);
        date.setEnabled(false);
        title=findViewById(R.id.title);
        desc=findViewById(R.id.desc);
        update=findViewById(R.id.updatesbutton);
        delete=findViewById(R.id.deletedbutton);
        final Intent i=getIntent();


        final String nid=i.getStringExtra("nId");
        String getdate=i.getStringExtra("date");
        String gettitle=i.getStringExtra("title");
        String getdesc=i.getStringExtra("desc");

        //final String id=i.getStringExtra("id");

        final String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Notes/" + id + "/" + nid);



        date.setText(getdate);
        title.setText(gettitle);
        desc.setText(getdesc);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(),id,Toast.LENGTH_SHORT).show();
                UpdateNotes(id, nid);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote();
            }
        });
    }

    private void deleteNote() {
        mDatabase.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditActivity.this,"Note Deleted",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),SecondActivity.class));
                        finish();

                    }
                });
    }
    private void UpdateNotes(String id, String nid)
    {

        datesend=date.getText().toString();
        titlesend=title.getText().toString();
        descsend=desc.getText().toString();
        Notedata listdata = new Notedata(nid, datesend, titlesend,  descsend);
        mDatabase.setValue(listdata).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(EditActivity.this, "Note Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),SecondActivity.class));
                        finish();
                    }
                });

    }
}
