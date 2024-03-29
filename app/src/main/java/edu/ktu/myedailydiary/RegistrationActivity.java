package edu.ktu.myedailydiary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import edu.ktu.myedailydiary.User.User;

public class RegistrationActivity extends AppCompatActivity {

    String name,password,email,age;
    private EditText userName, userPassword, userEmail, userAge;
    private Button registerButton;
    private TextView userLogin;
    private FirebaseAuth firebaseauth;
    private ImageView userProfilePic;
    private FirebaseStorage firebaseStorage;
    private static int PICK_IMAGE = 1;
    Uri imagePath;
    private StorageReference storageReference;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null){
            imagePath = data.getData();

            Picasso.get().load(imagePath).into(userProfilePic);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        setupUIViews();

        firebaseauth=FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference();

        userProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("images/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    String user_email=userEmail.getText().toString().trim();
                    String user_password=userPassword.getText().toString().trim();

                    firebaseauth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //Toast.makeText(RegistrationActivity.this,"Registration Successful", Toast.LENGTH_SHORT).show();
                               // startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                                //sendEmailVerification();
                                //sendUserData();

                                User user = new User(name, email, age);
                                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);


                                sendUserData();
                                Toast.makeText(RegistrationActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                finish();

                            }else{
                                Toast.makeText(RegistrationActivity.this,"Registration is Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            }
        });

    }

    private void setupUIViews(){
        userName=(EditText)findViewById(R.id.etUserName);
        userPassword=(EditText)findViewById(R.id.etUserPassword);
        userEmail=(EditText)findViewById(R.id.etUserEmail);
        registerButton=(Button)findViewById(R.id.btnRegister);
        userLogin=(TextView)findViewById(R.id.tvUserLogin);
        userAge=(EditText)findViewById((R.id.etAge));
        userProfilePic = (ImageView)findViewById(R.id.ivProfile);
    }

    private Boolean validate(){
        Boolean result=false;

         name=userName.getText().toString();
         password=userPassword.getText().toString();
         email=userEmail.getText().toString();
         age=userAge.getText().toString();

        if(name.isEmpty() || password.isEmpty() || email.isEmpty() || age.isEmpty() || imagePath==null) {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        }else{
            result=true;
        }
        return result;

    }

    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseauth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        sendUserData();
                        Toast.makeText(RegistrationActivity.this, "Successfully Registered, Verification mail sent!", Toast.LENGTH_SHORT).show();
                        firebaseauth.signOut();
                        finish();
                        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                    }else{
                        Toast.makeText(RegistrationActivity.this, "Verification mail has'nt been sent!", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
    private void sendUserData(){
         FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
      //  DatabaseReference myRef = firebaseDatabase.getReference().child("Users");
        // DatabaseReference myRef = firebaseDatabase.getReference("Users");
         //myRef = firebaseDatabase.getReference(firebaseauth.getUid());
       // myRef.setValue(firebaseauth.getUid());

        StorageReference imageReference = storageReference.child(firebaseauth.getUid()).child("Images").child("Profile Pic");  //User id/Images/Profile Pic.jpg
        UploadTask uploadTask = imageReference.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistrationActivity.this, "Upload failed!", Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Toast.makeText(RegistrationActivity.this, "Upload successful!", Toast.LENGTH_SHORT).show();
            }
        });
        User userProfile = new User(age, email, name);
        //myRef.setValue(userProfile);
    }
}
