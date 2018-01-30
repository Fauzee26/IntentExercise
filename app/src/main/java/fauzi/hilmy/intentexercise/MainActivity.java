package fauzi.hilmy.intentexercise;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Spinner spinChoose;
    EditText etLink, etPhone, etEmail, etSubject, etMessage;
    Button btnSend;

    String itemIntent;
    String[] dataIntent = new String[]{
            "Email", "SMS", "Phone", "Web"
    };
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinChoose = (Spinner)findViewById(R.id.spinIntent);
        etLink = (EditText)findViewById(R.id.etUrl);
        etPhone = (EditText)findViewById(R.id.etPhone);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etSubject = (EditText)findViewById(R.id.etSubject);
        etMessage = (EditText)findViewById(R.id.etMessage);
        btnSend = (Button)findViewById(R.id.btnSend);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataIntent);
        spinChoose.setAdapter(adapter);

        spinChoose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //mengambil nilai dari posisi dan dijadikan string
                itemIntent = parent.getItemAtPosition(position).toString();
                if (itemIntent.equalsIgnoreCase("Email")) {
                    etLink.setVisibility(View.GONE);
                    etPhone.setVisibility(View.GONE);
                    etEmail.setVisibility(View.VISIBLE);
                    etSubject.setVisibility(View.VISIBLE);
                    etMessage.setVisibility(View.VISIBLE);
                } else if (itemIntent.equalsIgnoreCase("SMS")) {
                    etLink.setVisibility(View.GONE);
                    etPhone.setVisibility(View.VISIBLE);
                    etEmail.setVisibility(View.GONE);
                    etSubject.setVisibility(View.GONE);
                    etMessage.setVisibility(View.VISIBLE);
                } else if (itemIntent.equalsIgnoreCase("Phone")) {
                    etLink.setVisibility(View.GONE);
                    etPhone.setVisibility(View.VISIBLE);
                    etEmail.setVisibility(View.GONE);
                    etSubject.setVisibility(View.GONE);
                    etMessage.setVisibility(View.GONE);
                } else if (itemIntent.equalsIgnoreCase("Web")) {
                    etLink.setVisibility(View.VISIBLE);
                    etPhone.setVisibility(View.GONE);
                    etEmail.setVisibility(View.GONE);
                    etSubject.setVisibility(View.GONE);
                    etMessage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = etLink.getText().toString();
                String phone = etPhone.getText().toString();
                String email = etEmail.getText().toString();
                String message = etMessage.getText().toString();
                String subjek = etSubject.getText().toString();
                if (itemIntent.equalsIgnoreCase("Email")) {
                    //intent Email
                    Intent nEmail = new Intent(Intent.ACTION_SEND);
                    nEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                    nEmail.putExtra(Intent.EXTRA_SUBJECT, subjek);
                    nEmail.putExtra(Intent.EXTRA_TEXT, message);

                    //format kode untuk pengiriman Email
                    nEmail.setType("message/rfc822");
                    startActivity(Intent.createChooser(nEmail, "Pilih Email Client"));
                } else if (itemIntent.equalsIgnoreCase("SMS")) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                            Log.d("permission","permission denied to SEND_SMS - requesting it");
                            String[] permissions = {Manifest.permission.SEND_SMS};
                            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
                        }
                    }
                    Intent intent3 = new Intent(getApplicationContext(), MainActivity.class);
                    //tentang pendingIntent
                    //https://developer.android.com/reference/android/app/PendingIntent.html
                    PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent3, 0);

                    //memanggil library SMSManager dan memanggil string dataIsiSMS
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(phone, null, message, pi, null);
                    Toast.makeText(getApplicationContext(), "SMS Berhasil dikirim", Toast.LENGTH_SHORT).show();
                } else if (itemIntent.equalsIgnoreCase("Phone")) {
                    @SuppressLint("MissingPermission")
                    Intent intent2 = new Intent(Intent.ACTION_DIAL);
                    intent2.setData(Uri.parse("tel:" + phone));
                    if (intent2.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent2);
                    }
                }else if (itemIntent.equalsIgnoreCase("Web")) {
                    Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    startActivity(intent1);
                }
            }
        });

    }
}
