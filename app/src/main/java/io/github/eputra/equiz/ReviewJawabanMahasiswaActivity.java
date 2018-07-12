package io.github.eputra.equiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
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

public class ReviewJawabanMahasiswaActivity extends AppCompatActivity {

    public static final String URL = "http://192.168.122.1/web/equiz/";
    private ProgressDialog progress;
    SessionManagement session;
    String nama_pengguna;
    String nama_mahasiswa;
    String id_quiz;
    String jumlah_soal;
    int nomor_soal = 1;

    @BindView(R.id.tvNomorSoal) TextView tvNomorSoal;
    @BindView(R.id.tvSoal) TextView tvSoal;
    @BindView(R.id.rbA) RadioButton rbA;
    @BindView(R.id.rbB) RadioButton rbB;
    @BindView(R.id.rbC) RadioButton rbC;
    @BindView(R.id.rbD) RadioButton rbD;
    @BindView(R.id.tvA) TextView tvA;
    @BindView(R.id.tvB) TextView tvB;
    @BindView(R.id.tvC) TextView tvC;
    @BindView(R.id.tvD) TextView tvD;
    @BindView(R.id.tvJawabanMahasiswa) TextView tvJawabanMahasiswa;
    @BindView(R.id.btnSelanjutnya) Button btnSelanjutnya;
    @BindView(R.id.svReviewJawabanMahasiswa) ScrollView svReviewJawabanMahasiswa;

    @OnClick(R.id.btnSelanjutnya) void selanjutnya() {
        nomor_soal += 1;
        loadReviewJawaban();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_jawaban_mahasiswa);
        ButterKnife.bind(this);
        Intent i = getIntent();
        id_quiz = i.getStringExtra("id_quiz");
        nama_pengguna = i.getStringExtra("nama_pengguna");
        nama_mahasiswa = i.getStringExtra("nama_mahasiswa");
        loadReviewJawaban();
    }

    @Override
    public void onBackPressed() {
        if (nomor_soal == 1) {
            Intent i = new Intent(ReviewJawabanMahasiswaActivity.this, NilaiQuizActivity.class);
            i.putExtra("id_quiz", id_quiz);
            startActivity(i);
            finish();
        } else {
            nomor_soal -= 1;
            loadReviewJawaban();
            svReviewJawabanMahasiswa.fullScroll(ScrollView.FOCUS_UP);
            btnSelanjutnya.setText("Selanjutnya");
        }
    }

    private void loadReviewJawaban() {
        //membuat progress dialog
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Loading ...");
        progress.show();

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
        Call<ReviewJawaban> call = api.reviewJawaban(nama_pengguna, id_quiz, nomor_soal);
        call.enqueue(new Callback<ReviewJawaban>() {
            @Override
            public void onResponse(Call<ReviewJawaban> call, Response<ReviewJawaban> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                String id_soal = response.body().getId_soal();
                //String id_quiz = response.body().getId_quiz();
                String soal = response.body().getSoal();
                String pilihan_jawaban = response.body().getPilihan_jawaban();
                String kunci_jawaban = response.body().getKunci_jawaban();
                String jumlah_soal = response.body().getJumlah_soal();
                String jawabahan_mahasiswa = response.body().getJawaban_mahasiswa();
                progress.dismiss();
                if (value.equals("1")) {
                    if (btnSelanjutnya.getText().toString().equalsIgnoreCase("Selesai")) {
                        Intent i = new Intent(ReviewJawabanMahasiswaActivity.this, NilaiQuizActivity.class);
                        i.putExtra("id_quiz", id_quiz);
                        startActivity(i);
                        finish();
                    } else {
                        tvNomorSoal.setText("Nomor " + nomor_soal + " dari " + jumlah_soal + " soal");
                        tvSoal.setText(soal);
                        Gson gson = new Gson();
                        PilihanJawaban pilihanJawaban = gson.fromJson(pilihan_jawaban, PilihanJawaban.class);
                        tvA.setText(pilihanJawaban.getPilihanJawabanA());
                        tvB.setText(pilihanJawaban.getPilihanJawabanB());
                        tvC.setText(pilihanJawaban.getPilihanJawabanC());
                        tvD.setText(pilihanJawaban.getPilihanJawabanD());
                        rbA.setChecked(false);
                        rbB.setChecked(false);
                        rbC.setChecked(false);
                        rbD.setChecked(false);
                        rbA.setEnabled(false);
                        rbB.setEnabled(false);
                        rbC.setEnabled(false);
                        rbD.setEnabled(false);
                        switch (kunci_jawaban) {
                            case "A":
                                rbA.setEnabled(true);
                                rbA.setChecked(true);
                                break;
                            case "B":
                                rbB.setEnabled(true);
                                rbB.setChecked(true);
                                break;
                            case "C":
                                rbC.setEnabled(true);
                                rbC.setChecked(true);
                                break;
                            case "D":
                                rbD.setEnabled(true);
                                rbD.setChecked(true);
                                break;
                        }
                        if (jawabahan_mahasiswa.equals("N")) {
                            tvJawabanMahasiswa.setText(nama_mahasiswa + " tidak mengerjakan soal ini.");
                        } else if (jawabahan_mahasiswa.equals(kunci_jawaban)) {
                            tvJawabanMahasiswa.setText("Benar : Jawaban " + nama_mahasiswa + " " + jawabahan_mahasiswa);
                        } else {
                            tvJawabanMahasiswa.setText("Salah : Jawaban " + nama_mahasiswa + " " + jawabahan_mahasiswa);
                        }
                        if (nomor_soal == Integer.parseInt(jumlah_soal)) {
                            btnSelanjutnya.setText("Selesai");
                        }
                        svReviewJawabanMahasiswa.fullScroll(ScrollView.FOCUS_UP);
                    }
                }
            }

            @Override
            public void onFailure(Call<ReviewJawaban> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(ReviewJawabanMahasiswaActivity.this, "Jaringan Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
