package com.example.learnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learnit.Model.Users;
import com.example.learnit.Prevalent.Prevalent;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {

    Button register, login;
    TextView logo_name, desc;
    ImageView logo_image;
    TextInputLayout phoneNo, password;
    private String parentDbName = "Users";
    private CheckBox chkBoxRememberMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        logo_name = findViewById(R.id.logo_name);
        desc = findViewById(R.id.desc);
        logo_image = findViewById(R.id.logo_image);
        phoneNo = findViewById(R.id.phoneNo);
        password = findViewById(R.id.password);
        chkBoxRememberMe = findViewById(R.id.remember);
        Paper.init(this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(register, "login_signup_tran");
                pairs[1] = new Pair<View, String>(login, "button_tran");
                pairs[2] = new Pair<View, String>(logo_name, "logo_text");
                pairs[3] = new Pair<View, String>(desc, "logo_desc");
                pairs[4] = new Pair<View, String>(logo_image, "logo_image");
                pairs[5] = new Pair<View, String>(phoneNo, "username_tran");
                pairs[6] = new Pair<View, String>(password, "password_tran");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });

    }

    private void LoginUser() {
        String phone = phoneNo.getEditText().getText().toString();
        String pword = password.getEditText().getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please Enter Your PhoneNumber", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(pword)) {
            Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
        }else {

            AllowAccessToAccount(phone, pword);

        }

    }

    private void AllowAccessToAccount(final String phone, final String pword) {

        if(chkBoxRememberMe.isChecked()){

            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, pword);
        }


        final DatabaseReference Rootref;
        Rootref = FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentDbName).child(phone).exists()){

                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);
                    if(usersData.getPhone().equals(phone)){
                        if(usersData.getPassword().equals(pword)){
                            Toast.makeText(Login.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Home.class);
                            startActivity(intent);

                        }else{
                            Toast.makeText(Login.this, "Password Incorrect", Toast.LENGTH_SHORT).show();
                        }

                    }


                }else{
                    Toast.makeText(Login.this, "Account With " + phone + " Does Not Exists...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
