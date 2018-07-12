package io.github.eputra.equiz;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class DataQuizDosenActivity extends AppCompatActivity {

    public static final String URL = "http://192.168.122.1/web/equiz/";
    private ProgressDialog progress;
    String id_quiz = null;
    String terbit;

    @BindView(R.id.textIDQuiz) TextView textIDQuiz;
    @BindView(R.id.textJudul) TextView textJudul;
    @BindView(R.id.textJumlahSoal) TextView textJumlahSoal;
    @BindView(R.id.textWaktuPengerjaanSoal) TextView textWaktuPengerjaanSoal;
    @BindView(R.id.textWaktuAkhirPengerjaanSoal) TextView textWaktuAkhirPengerjaanSoal;
    @BindView(R.id.btnUbahQuiz) Button btnUbahQuiz;
    @BindView(R.id.btnLihatNilai) Button btnLihatNilai;
    @BindView(R.id.btnTerbitkanQuiz) Button btnTerbitkanQuiz;

    @OnClick(R.id.btnTerbitkanQuiz) void terbitkanQuiz() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Peringatan");
        alertDialogBuilder
                .setMessage("Quiz yang sudah diterbitkan tidak bisa diubah lagi datanya. Apakah Anda yakin?")
                .setCancelable(false)
                .setPositiveButton("Terbitkan",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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
                        Call<Value> call = api.terbitkanQuiz(id_quiz);
                        call.enqueue(new Callback<Value>() {
                            @Override
                            public void onResponse(Call<Value> call, Response<Value> response) {
                                String value = response.body().getValue();
                                String message = response.body().getMessage();
                                if (value.equals("1")) {
                                    terbit = "1";
                                    btnUbahQuiz.setVisibility(View.GONE);
                                    btnTerbitkanQuiz.setVisibility(View.GONE);
                                    btnLihatNilai.setVisibility(View.VISIBLE);
                                    Toast.makeText(DataQuizDosenActivity.this, message, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(DataQuizDosenActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Value> call, Throwable t) {
                                Toast.makeText(DataQuizDosenActivity.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @OnClick(R.id.btnUbahQuiz) void ubahQuiz() {
        id_quiz = textIDQuiz.getText().toString().toUpperCase();
        String judul = textJudul.getText().toString();
        String jumlah_soal = textJumlahSoal.getText().toString();
        String waktu_pengerjaan_soal = textWaktuPengerjaanSoal.getText().toString();
        String waktu_akhir_pengerjaan = textWaktuAkhirPengerjaanSoal.getText().toString();

        String[] wps = waktu_pengerjaan_soal.split(" ");

        String[] datetime = waktu_akhir_pengerjaan.split(" ");
        String[] date = datetime[0].split("-");
        String[] time = datetime[1].split(":");
        String tahun = date[2];
        String bulan = date[1];
        String tanggal = date[0];
        String jam = time[0];
        String menit = time[1];

        Intent i = new Intent(this, UbahQuizActivity.class);
        i.putExtra("terbit", terbit);
        i.putExtra("id_quiz", id_quiz);
        i.putExtra("judul", judul);
        i.putExtra("jumlah_soal", jumlah_soal);
        i.putExtra("waktu_pengerjaan_soal", wps[0]);
        i.putExtra("tahun", tahun);
        i.putExtra("bulan", bulan);
        i.putExtra("tanggal", tanggal);
        i.putExtra("jam", jam);
        i.putExtra("menit", menit);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.btnLihatSoal) void lihatSoal() {
        if (terbit.equals("1")) {
            Intent i = new Intent(this, NoUbahSoalActivity.class);
            i.putExtra("id_quiz", id_quiz);
            i.putExtra("jumlah_soal", textJumlahSoal.getText().toString());
            startActivity(i);
            finish();
        } else {
            Intent i = new Intent(this, UbahSoalActivity.class);
            i.putExtra("id_quiz", id_quiz);
            i.putExtra("jumlah_soal", textJumlahSoal.getText().toString());
            startActivity(i);
            finish();
        }
    }

    @OnClick(R.id.btnLihatNilai) void lihatNilai() {
        Intent i = new Intent(this, NilaiQuizActivity.class);
        i.putExtra("id_quiz", id_quiz);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_quiz_dosen);
        ButterKnife.bind(this);

        Intent i = getIntent();
        id_quiz = i.getStringExtra("id_quiz");
        loadDataQuiz();
//        textIDQuiz.setText(i.getStringExtra("id_quiz"));
//        textJudul.setText(i.getStringExtra("judul"));
//        textJumlahSoal.setText(i.getStringExtra("jumlah_soal"));
//        textWaktuPengerjaanSoal.setText(i.getStringExtra("waktu_pengerjaan_soal"));
//        textWaktuAkhirPengerjaanSoal.setText(i.getStringExtra("waktu_akhir_pengerjaan"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataQuiz();
    }

    private void loadDataQuiz(){
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
        Call<Quiz2> call = api.dataQuizDosen(id_quiz);
        call.enqueue(new Callback<Quiz2>() {
            @Override
            public void onResponse(Call<Quiz2> call, Response<Quiz2> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                String id_quiz = response.body().getId_quiz();
                String judul = response.body().getJudul();
                String jumlah_soal = response.body().getJumlah_soal();
                String waktu_pengerjaan_soal = response.body().getWaktu_pengerjaan_soal();
                String tahun = response.body().getTahun();
                String bulan = response.body().getBulan();
                String tanggal = response.body().getTanggal();
                String jam = response.body().getJam();
                String menit = response.body().getMenit();
                String nama_dosen = response.body().getNama_dosen();
                terbit = response.body().getTerbit();
                if (value.equals("1")) {
                    textIDQuiz.setText(id_quiz);
                    textJudul.setText(judul);
                    textJumlahSoal.setText(jumlah_soal);
                    textWaktuPengerjaanSoal.setText(waktu_pengerjaan_soal + " menit");
                    textWaktuAkhirPengerjaanSoal.setText(tanggal + "-" + bulan + "-" + tahun + " " + jam + ":" + menit);
                    if (terbit.equals("1")) {
                        btnUbahQuiz.setVisibility(View.GONE);
                        btnTerbitkanQuiz.setVisibility(View.GONE);
                    } else {
                        btnLihatNilai.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(DataQuizDosenActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Quiz2> call, Throwable t) {
                Toast.makeText(DataQuizDosenActivity.this, "Jaringan Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
