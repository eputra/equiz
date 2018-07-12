package io.github.eputra.equiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuatSoalActivity extends AppCompatActivity {

    public static final String URL = "http://192.168.122.1/web/equiz/";
    private ProgressDialog progress;
    SessionManagement session;

    String id_quiz;
    String kunciJawaban = null;
    int jumlah_soal;
    int nomor_soal = 1;

    @BindView(R.id.tvNomorSoal) TextView tvNomorSoal;
    @BindView(R.id.etSoal) EditText etSoal;
//    @BindView(R.id.rgJawaban) RadioGroup rgJawaban;
    @BindView(R.id.rbA) RadioButton rbA;
    @BindView(R.id.rbB) RadioButton rbB;
    @BindView(R.id.rbC) RadioButton rbC;
    @BindView(R.id.rbD) RadioButton rbD;
    @BindView(R.id.etA) EditText etA;
    @BindView(R.id.etB) EditText etB;
    @BindView(R.id.etC) EditText etC;
    @BindView(R.id.etD) EditText etD;
    @BindView(R.id.btnSelanjutnya) Button btnSelanjutnya;
    @BindView(R.id.svBuatSoal) ScrollView svBuatSoal;

    @OnClick(R.id.rbA) void rbA() {
        kunciJawaban = "A";
        rbB.setChecked(false);
        rbC.setChecked(false);
        rbD.setChecked(false);
    }

    @OnClick(R.id.rbB) void rbB() {
        kunciJawaban = "B";
        rbA.setChecked(false);
        rbC.setChecked(false);
        rbD.setChecked(false);
    }

    @OnClick(R.id.rbC) void rbC() {
        kunciJawaban = "C";
        rbA.setChecked(false);
        rbB.setChecked(false);
        rbD.setChecked(false);
    }

    @OnClick(R.id.rbD) void rbD() {
        kunciJawaban = "D";
        rbA.setChecked(false);
        rbB.setChecked(false);
        rbC.setChecked(false);
    }

    @OnClick(R.id.btnSelanjutnya) void selanjutnya() {
        if (etSoal.getText().toString().isEmpty()) {
            etSoal.setError("Harus diisi");
            return;
        }

        if (etA.getText().toString().isEmpty()) {
            etA.setError("Harus diisi");
            return;
        }

        if (etB.getText().toString().isEmpty()) {
            etB.setError("Harus diisi");
            return;
        }

        if (etC.getText().toString().isEmpty()) {
            etC.setError("Harus diisi");
            return;
        }

        if (etD.getText().toString().isEmpty()) {
            etD.setError("Harus diisi");
            return;
        }

        if (kunciJawaban == null) {
            Toast.makeText(this, "Kunci jawaban harus ditentukan", Toast.LENGTH_SHORT).show();
            return;
        }

        //membuat progress dialog
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Loading ...");
        progress.show();

        //mengambil data
        String soal = etSoal.getText().toString();
        PilihanJawaban pilihanJawaban = new PilihanJawaban();
        pilihanJawaban.setPilihanJawabanA(etA.getText().toString());
        pilihanJawaban.setPilihanJawabanB(etB.getText().toString());
        pilihanJawaban.setPilihanJawabanC(etC.getText().toString());
        pilihanJawaban.setPilihanJawabanD(etD.getText().toString());
        Gson gson = new Gson();
        String jsonPilihanJawaban = gson.toJson(pilihanJawaban);
        System.out.println(jsonPilihanJawaban);
//        String kunciJawaban = null;
//        int rgKunciJawaban = rgJawaban.getCheckedRadioButtonId();
//        switch (rgKunciJawaban) {
//            case R.id.rbA:
//                kunciJawaban = "A";
//                break;
//            case R.id.rbB:
//                kunciJawaban = "B";
//                break;
//            case R.id.rbC:
//                kunciJawaban = "C";
//                break;
//            case R.id.rbD:
//                kunciJawaban = "D";
//                break;
//        }

        //logging request and response
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        RegisterAPI api = retrofit.create(RegisterAPI.class);

        Call<Value> call = api.buatSoal(id_quiz, soal, jsonPilihanJawaban, kunciJawaban, nomor_soal);
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, retrofit2.Response<Value> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                progress.dismiss();
                if (value.equals("1")) {
                    if (btnSelanjutnya.getText().toString().equalsIgnoreCase("Selesai")) {
                        Intent i = new Intent(BuatSoalActivity.this, DosenActivity.class);
                        startActivity(i);
                        Toast.makeText(BuatSoalActivity.this, "Sukses membuat quiz.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(BuatSoalActivity.this, message, Toast.LENGTH_SHORT).show();
                        nomor_soal += 1;
                        tvNomorSoal.setText("Nomor " + nomor_soal + " dari " + jumlah_soal + " soal");
                        kunciJawaban = null;
                        etSoal.setText("");
                        etA.setText("");
                        etB.setText("");
                        etC.setText("");
                        etD.setText("");
                        rbA.setChecked(false);
                        rbB.setChecked(false);
                        rbC.setChecked(false);
                        rbD.setChecked(false);
                        if (nomor_soal == jumlah_soal) {
                            btnSelanjutnya.setText("Selesai");
                        }
                        svBuatSoal.fullScroll(ScrollView.FOCUS_UP);
                    }
                } else {
                    Toast.makeText(BuatSoalActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(BuatSoalActivity.this, "Jaringan Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_soal);
        ButterKnife.bind(this);
        session = new SessionManagement(getApplicationContext());

        Intent i = getIntent();
        id_quiz = i.getStringExtra("id_quiz");
        jumlah_soal = Integer.parseInt(i.getStringExtra("jumlah_soal"));

        tvNomorSoal.setText("Nomor " + nomor_soal + " dari " + jumlah_soal + " soal");
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(BuatSoalActivity.this, "Soal harus diisi semua.", Toast.LENGTH_SHORT).show();
    }
}
