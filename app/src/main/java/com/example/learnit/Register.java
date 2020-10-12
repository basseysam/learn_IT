package com.example.learnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    Button login, register;
    TextView logo_name, desc;
    ImageView logo_image;
    TextInputLayout name, username, email, password, phoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        logo_image = findViewById(R.id.logo_image);
        logo_name = findViewById(R.id.logo_name);
        desc = findViewById(R.id.desc);
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phoneNo = findViewById(R.id.phoneNo);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(register, "login_signup_tran");
                pairs[1] = new Pair<View, String>(login, "button_tran");
                pairs[2] = new Pair<View, String>(logo_name, "logo_text");
                pairs[3] = new Pair<View, String>(desc, "logo_desc");
                pairs[4] = new Pair<View, String>(logo_image, "logo_image");
                pairs[5] = new Pair<View, String>(username, "username_tran");
                pairs[6] = new Pair<View, String>(password, "password_tran");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Register.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });

    }

    private void CreateAccount() {
        String fname = name.getEditText().getText().toString();
        String sname = username.getEditText().getText().toString();
        String mail = email.getEditText().getText().toString();
        String pword = password.getEditText().getText().toString();
        String phone = phoneNo.getEditText().getText().toString();

        if (TextUtils.isEmpty(fname)) {
            Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
        }

       else if (TextUtils.isEmpty(sname)) {
            Toast.makeText(this, "Please Enter Your Username", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(mail)) {
            Toast.makeText(this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
        }

       else if (TextUtils.isEmpty(pword)) {
            Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
        }

       else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show();
        }

        else{
            ValidatephoneNumber(fname, sname, mail, pword, phone);
        }

    }

    private void ValidatephoneNumber(final String fname, final String sname, final String mail, final String pword, final String phone) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

         RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if(!(dataSnapshot.child("Users").child(phone).exists())){

                     HashMap<String, Object> userdataMap = new HashMap<>();
                     userdataMap.put("phone", phone);
                     userdataMap.put("full_name", fname);
                     userdataMap.put("username", sname);
                     userdataMap.put("email", mail);
                     userdataMap.put("password", pword);

                     RootRef.child("Users").child(phone).updateChildren(userdataMap)
                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                     if(task.isSuccessful()){
                                         Toast.makeText(Register.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                         Intent intent = new Intent(getApplicationContext(), Login.class);
                                         startActivity(intent);
                                     }else {
                                         Toast.makeText(Register.this, "Network error... Please try again later", Toast.LENGTH_SHORT).show();
                                     }
                                 }
                             });

                 }else{
                     Toast.makeText(Register.this, "This " + phone + " Already Exists", Toast.LENGTH_SHORT).show();
                     Toast.makeText(Register.this, "Please Try Again USing Another Phone Number", Toast.LENGTH_SHORT).show();
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });
    }

}
