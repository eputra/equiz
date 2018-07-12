package io.github.eputra.equiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
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

import java.text.DecimalFormat;

public class MengerjakanQuizActivity extends AppCompatActivity {

    public static final String URL = "http://192.168.122.1/web/equiz/";
    private ProgressDialog progress;
    SessionManagement session;
    String nama_pengguna;
    String id_quiz;
    String jumlah_soal;
    int nomor_soal = 1;
    String id_soal;

    String kunci_jawaban;
    String jawaban = null;
    double nilai = 0;
    double nilaiAkhir = 0;
    String sNilaiAkhir = "0";
    long waktu;

    @BindView(R.id.tvNomorSoal) TextView tvNomorSoal;
    @BindView(R.id.tvSoal) TextView tvSoal;
//    @BindView(R.id.rgJawaban) RadioGroup rgJawaban;
    @BindView(R.id.rbA) RadioButton rbA;
    @BindView(R.id.rbB) RadioButton rbB;
    @BindView(R.id.rbC) RadioButton rbC;
    @BindView(R.id.rbD) RadioButton rbD;
    @BindView(R.id.tvA) TextView tvA;
    @BindView(R.id.tvB) TextView tvB;
    @BindView(R.id.tvC) TextView tvC;
    @BindView(R.id.tvD) TextView tvD;
    @BindView(R.id.btnSelanjutnya) Button btnSelanjutnya;
    @BindView(R.id.svBuatSoal) ScrollView svBuatSoal;
    @BindView(R.id.tvWaktu) TextView tvWaktu;

    @OnClick(R.id.rbA) void rbA() {
        jawaban = "A";
        rbB.setChecked(false);
        rbC.setChecked(false);
        rbD.setChecked(false);
    }

    @OnClick(R.id.rbB) void rbB() {
        jawaban = "B";
        rbA.setChecked(false);
        rbC.setChecked(false);
        rbD.setChecked(false);
    }

    @OnClick(R.id.rbC) void rbC() {
        jawaban = "C";
        rbA.setChecked(false);
        rbB.setChecked(false);
        rbD.setChecked(false);
    }

    @OnClick(R.id.rbD) void rbD() {
        jawaban = "D";
        rbA.setChecked(false);
        rbB.setChecked(false);
        rbC.setChecked(false);
    }

    @OnClick(R.id.btnSelanjutnya) void selajutnya() {
        if (jawaban == null) {
            Toast.makeText(this, "Pilih salahsatu jawaban", Toast.LENGTH_SHORT).show();
            return;
        }

        //membuat progress dialog
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Loading ...");
        progress.show();

        //mengambil data
//        int rgPilihJawaban = rgJawaban.getCheckedRadioButtonId();
//        switch (rgPilihJawaban) {
//            case R.id.rbA:
//                jawaban = "A";
//                break;
//            case R.id.rbB:
//                jawaban = "B";
//                break;
//            case R.id.rbC:
//                jawaban = "C";
//                break;
//            case R.id.rbD:
//                jawaban = "D";
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

        Call<Value> call = api.jawabSoal(id_soal, nama_pengguna, jawaban, nomor_soal);
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, retrofit2.Response<Value> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                progress.dismiss();
                if (value.equals("1")) {
                    if (jawaban.equalsIgnoreCase(kunci_jawaban)) {
                        nilai += 1;
                    }
                    nilaiAkhir = (nilai / Double.parseDouble(jumlah_soal) * 100);
                    DecimalFormat df = new DecimalFormat("#.#");
                    sNilaiAkhir = df.format(nilaiAkhir);
//                    System.out.println(jawaban);
//                    System.out.println(kunci_jawaban);
//                    System.out.println(nilai);
                    if (btnSelanjutnya.getText().toString().equalsIgnoreCase("Selesai")) {
                        Intent i = new Intent(MengerjakanQuizActivity.this, NilaiActivity.class);
                        i.putExtra("nilai", sNilaiAkhir);
                        i.putExtra("id_quiz", id_quiz);
                        i.putExtra("jumlah_soal", jumlah_soal);
                        startActivity(i);
                        //Toast.makeText(MengerjakanQuizActivity.this, "Sukses mengerjakan quiz.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(MengerjakanQuizActivity.this, message, Toast.LENGTH_SHORT).show();
                        nomor_soal += 1;
                        loadSoal();
                        jawaban = null;
                        rbA.setChecked(false);
                        rbB.setChecked(false);
                        rbC.setChecked(false);
                        rbD.setChecked(false);
                        if (nomor_soal == Integer.parseInt(jumlah_soal)) {
                            btnSelanjutnya.setText("Selesai");
                        }
                        svBuatSoal.fullScroll(ScrollView.FOCUS_UP);
                    }
                } else {
                    Toast.makeText(MengerjakanQuizActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(MengerjakanQuizActivity.this, "Jaringan Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mengerjakan_quiz);
        ButterKnife.bind(this);
        session = new SessionManagement(getApplicationContext());
        nama_pengguna = session.getNamaPengguna();
        Intent i = getIntent();
        id_quiz = i.getStringExtra("id_quiz");
        jumlah_soal = i.getStringExtra("jumlah_soal");
        waktu = Long.parseLong(i.getStringExtra("waktu_pengerjaan_soal"));
        loadSoal();
        new CountDownTimer(waktu*60000, 1000) {
            public void onTick(long millisUntilFinished) {
                tvWaktu.setText("Waktu " + millisUntilFinished / 1000 + " detik");
            }
            public void onFinish() {
                Intent i = new Intent(MengerjakanQuizActivity.this, NilaiActivity.class);
                i.putExtra("nilai", sNilaiAkhir);
                i.putExtra("id_quiz", id_quiz);
                i.putExtra("jumlah_soal", jumlah_soal);
                startActivity(i);
                //Toast.makeText(MengerjakanQuizActivity.this, "Sukses mengerjakan quiz.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Soal harus dikerjakan semua.", Toast.LENGTH_SHORT).show();
    }

    private void loadSoal() {
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
                id_soal = response.body().getId_soal();
                String id_quiz = response.body().getId_quiz();
                String soal = response.body().getSoal();
                String pilihan_jawaban = response.body().getPilihan_jawaban();
                kunci_jawaban = response.body().getKunci_jawaban();
                String nomor_soal = response.body().getNomor_soal();
                if (value.equals("1")) {
                    tvNomorSoal.setText("Nomor " + nomor_soal + " dari " + jumlah_soal + " soal");
                    tvSoal.setText(soal);
                    Gson gson = new Gson();
                    PilihanJawaban pilihanJawaban = gson.fromJson(pilihan_jawaban, PilihanJawaban.class);
                    tvA.setText(pilihanJawaban.getPilihanJawabanA());
                    tvB.setText(pilihanJawaban.getPilihanJawabanB());
                    tvC.setText(pilihanJawaban.getPilihanJawabanC());
                    tvD.setText(pilihanJawaban.getPilihanJawabanD());
                } else {
                    Toast.makeText(MengerjakanQuizActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Soal> call, Throwable t) {
                Toast.makeText(MengerjakanQuizActivity.this, "Jaringan Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
