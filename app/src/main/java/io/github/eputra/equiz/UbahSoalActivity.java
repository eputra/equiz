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
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UbahSoalActivity extends AppCompatActivity {

    public static final String URL = "http://192.168.122.1/web/equiz/";
    private ProgressDialog progress;

    String id_quiz;
    String kunciJawaban;
    String jumlah_soal;
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

    @OnClick(R.id.btnUbahSoal) void ubahSoal() {
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

        Call<Value> call = api.ubahSoal(id_quiz, soal, jsonPilihanJawaban, kunciJawaban, nomor_soal);
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, retrofit2.Response<Value> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                progress.dismiss();
                if (value.equals("1")) {
                    Toast.makeText(UbahSoalActivity.this, message, Toast.LENGTH_SHORT).show();
                    loadSoal();
                    svBuatSoal.fullScroll(ScrollView.FOCUS_UP);
                } else {
                    Toast.makeText(UbahSoalActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(UbahSoalActivity.this, "Jaringan Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.btnSelanjutnya) void selanjutnya() {
        if (btnSelanjutnya.getText().toString().equalsIgnoreCase("Selesai")) {
            Intent i = new Intent(UbahSoalActivity.this, DataQuizDosenActivity.class);
            i.putExtra("id_quiz", id_quiz);
            startActivity(i);
            finish();
        } else {
            nomor_soal += 1;
            if (nomor_soal == Integer.parseInt(jumlah_soal)) {
                btnSelanjutnya.setText("Selesai");
            }
            loadSoal();
            svBuatSoal.fullScroll(ScrollView.FOCUS_UP);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_soal);
        ButterKnife.bind(this);

        Intent i = getIntent();
        id_quiz = i.getStringExtra("id_quiz");
        jumlah_soal = i.getStringExtra("jumlah_soal");
        loadSoal();
    }

    @Override
    public void onBackPressed() {
        if (nomor_soal == 1) {
            Intent i = new Intent(UbahSoalActivity.this, DataQuizDosenActivity.class);
            i.putExtra("id_quiz", id_quiz);
            startActivity(i);
            finish();
        } else {
            nomor_soal -= 1;
            loadSoal();
            svBuatSoal.fullScroll(ScrollView.FOCUS_UP);
            btnSelanjutnya.setText("Selanjutnya");
        }
    }

    public void loadSoal() {
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
        Call<Soal> call = api.cariSoal(id_quiz, nomor_soal);
        call.enqueue(new Callback<Soal>() {
            @Override
            public void onResponse(Call<Soal> call, Response<Soal> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                String id_soal = response.body().getId_soal();
                String id_quiz = response.body().getId_quiz();
                String soal = response.body().getSoal();
                String pilihan_jawaban = response.body().getPilihan_jawaban();
                String kunci_jawaban = response.body().getKunci_jawaban();
                String nomor_soal = response.body().getNomor_soal();
                if (value.equals("1")) {
                    tvNomorSoal.setText("Nomor " + nomor_soal + " dari " + jumlah_soal + " soal");
                    etSoal.setText(soal);
                    Gson gson = new Gson();
                    PilihanJawaban pilihanJawaban = gson.fromJson(pilihan_jawaban, PilihanJawaban.class);
                    etA.setText(pilihanJawaban.getPilihanJawabanA());
                    etB.setText(pilihanJawaban.getPilihanJawabanB());
                    etC.setText(pilihanJawaban.getPilihanJawabanC());
                    etD.setText(pilihanJawaban.getPilihanJawabanD());
                    rbA.setChecked(false);
                    rbB.setChecked(false);
                    rbC.setChecked(false);
                    rbD.setChecked(false);
                    switch (kunci_jawaban) {
                        case "A":
                            rbA.setChecked(true);
                            kunciJawaban = "A";
                            break;
                        case "B":
                            rbB.setChecked(true);
                            kunciJawaban = "B";
                            break;
                        case "C":
                            rbC.setChecked(true);
                            kunciJawaban = "C";
                            break;
                        case "D":
                            rbD.setChecked(true);
                            kunciJawaban = "D";
                            break;
                    }
                } else {
                    Toast.makeText(UbahSoalActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Soal> call, Throwable t) {
                Toast.makeText(UbahSoalActivity.this, "Jaringan Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
