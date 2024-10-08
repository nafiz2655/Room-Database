package com.example.hiltroom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hiltroom.DataBase.MyDataBase;
import com.example.hiltroom.Helper.RoomHelper;
import com.example.hiltroom.Model.Student;
import com.example.hiltroom.VIew.ViewData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import de.hdodenhof.circleimageview.CircleImageView;

@AndroidEntryPoint
public class InsertData extends AppCompatActivity {

    EditText tv_name,tv_roll,tv_reg,tv_subject,tv_phone,tv_address;

    TextView tv_submit;
    CircleImageView profile_image;
    Uri mainUri;
    


    Bitmap bitmap;
    String encodedImageString;



    //Fild injection....................
    @Inject
    RoomHelper roomHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tv_name = findViewById(R.id.tv_name);
        tv_roll = findViewById(R.id.tv_roll);
        tv_reg = findViewById(R.id.tv_reg);
        tv_subject = findViewById(R.id.tv_subject);
        tv_phone = findViewById(R.id.tv_phone);
        tv_address = findViewById(R.id.tv_address);
        tv_submit = findViewById(R.id.tv_submit);
        profile_image = findViewById(R.id.profile_image);




        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (encodedImageString==null){
                    Toast.makeText(InsertData.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }else {




                try {

                    String name = tv_name.getText().toString();
                    String roll = tv_roll.getText().toString();
                    String reg = tv_reg.getText().toString();
                    String subject = tv_subject.getText().toString();
                    String phone = tv_phone.getText().toString();
                    String address = tv_address.getText().toString();

                    //Image Convart Bitmap




                    roomHelper.insertData(new Student(name,roll,reg,subject,phone,address,encodedImageString));
                    Toast.makeText(InsertData.this, "Data Insert Succeessfull", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(InsertData.this, ViewData.class));
                }catch (Exception e){
                    Toast.makeText(InsertData.this, "Data can not Add", Toast.LENGTH_SHORT).show();
                }
                }
            }
        });

        //Single Data gate
        /*

        tv_submit.setOnClickListener( view -> {

            String name = tv_name.getText().toString();
            String roll = tv_roll.getText().toString();
            String reg = tv_reg.getText().toString();
            String subject = tv_subject.getText().toString();
            String phone = tv_phone.getText().toString();
            String address = tv_address.getText().toString();

            try {
                Student student = roomHelper.readdata(new Student(1).getId());
                String sName = student.getName();
                String sRoll = student.getRoll();
                String sReg = student.getReg();
                String sSubject = student.getSubject();
                String sPhone = student.getPhone();
                String sAddress = student.getAddress();

                tv_name.setText(sName);
                tv_roll.setText(sRoll);
                tv_reg.setText(sReg);
                tv_subject.setText(sSubject);
                tv_phone.setText(sPhone);
                tv_address.setText(sAddress);
                Toast.makeText(this, "Success"+sAddress, Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
            }


        });

         */




    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    //profile_image.setImageURI(uri);
                    mainUri = uri;

                    try {

                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        profile_image.setImageBitmap(bitmap);
                        encodeImagetobitmap(bitmap);

                    }catch (Exception e){
                        Toast.makeText(InsertData.this, "error", Toast.LENGTH_SHORT).show();
                    }
                }
            });


    private void encodeImagetobitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        encodedImageString = Base64.encodeToString(bytes, Base64.DEFAULT);


    }




}