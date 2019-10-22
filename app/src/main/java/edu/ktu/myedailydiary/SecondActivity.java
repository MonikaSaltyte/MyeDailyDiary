package edu.ktu.myedailydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import edu.ktu.myedailydiary.User.Notedata;
import edu.ktu.myedailydiary.User.NotesAdapter;

public class SecondActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    NotesAdapter notesAdapter;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference dr;
    List<Notedata> list = new ArrayList<>();
    Context context;

    LinearLayoutManager linearLayoutManager;
    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        sharedPreferences=getSharedPreferences("SortSettings", MODE_PRIVATE);
        String mSorting = sharedPreferences.getString("Sort", "newest");

        if(mSorting.equals("newest")){
            linearLayoutManager =new LinearLayoutManager(this);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
        }
        else if (mSorting.equals("oldest")){
            linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setReverseLayout(false);
            linearLayoutManager.setStackFromEnd(false);
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notes/" + id );
        // dr=FirebaseDatabase.getInstance().getReference().child("Notes/" + id + "/" + "-LrnJJLg_TijWD2IwTku/" + "-LrnJNW_Nnq-cGmWABBh" );
        list=  new ArrayList<Notedata>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);

        notesAdapter = new NotesAdapter(list, this);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
//                TextView tv = (TextView) findViewById(R.id.textView2);
//                tv.setText("");
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    Notedata listdata = dataSnapshot1.getValue(Notedata.class);
                    list.add(listdata);
                }
//                for(int i=0; i<messageList.size(); i++) {
//                    tv.append(messageList.get(i).getFullString() + " \n\n");
//                }
                notesAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView.setAdapter(notesAdapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FloatingActionButton floatingActionButton= findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SecondActivity.this, "Add new note", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), NoteActivity.class));
            }
        });





    }



    private void delete() {

        databaseReference.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {



                        finish();

                    }
                });
    }

    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(SecondActivity.this, MainActivity.class));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.searchMenu);
        SearchView searchView = (SearchView)searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //notesAdapter.getFilter().filter(newText);
                search(newText);

                return true;
            }
        });
        return true;
    }

    private void search(String str) {
        ArrayList<Notedata>myList = new ArrayList<>();
        for(Notedata object : list)
        {
            if(object.getTitle().toLowerCase().contains(str.toLowerCase()))
            {
                myList.add(object);
            }
        }
        NotesAdapter notesAdapter=new NotesAdapter(myList, this);
        recyclerView.setAdapter(notesAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logoutMenu:{
                Logout();
                break;
            }
            case R.id.sortMenu:{
                showSortDialog();
                break;
            }
            case R.id.profileMenu:{
                startActivity(new Intent(SecondActivity.this, ProfileActivity.class));
                break;
            }


        }
        return super.onOptionsItemSelected(item);
    }

    private void showSortDialog() {
        String[] sortOption = {"Newest", "Oldest"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort by")
                .setIcon(R.drawable.ic_action_name)
                .setItems(sortOption, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which ==0){

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Sort", "newest");
                            editor.apply();
                            recreate();

                        }else if(which==1){
                            {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("Sort", "oldest");
                                editor.apply();
                                recreate();

                            }
                        }
                    }
                });
        builder.show();
    }
}
