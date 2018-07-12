package io.github.eputra.equiz;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

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

public class UbahQuizActivity extends AppCompatActivity {

    public static final String URL = "http://192.168.122.1/web/equiz/";
    private ProgressDialog progress;
    private String gid_quiz;

    final Calendar c = Calendar.getInstance();
    int tahun = c.get(Calendar.YEAR);
    int bulan = c.get(Calendar.MONTH);
    int tanggal = c.get(Calendar.DAY_OF_MONTH);
    int jam = c.get(Calendar.HOUR_OF_DAY);
    int menit = c.get(Calendar.MINUTE);

    @BindView(R.id.etIDQuiz) EditText etIDQuiz;
    @BindView(R.id.etJudul) EditText etJudul;
    @BindView(R.id.etJumlah) EditText etJumlah;
    @BindView(R.id.etWaktuPengerjaan) EditText etWaktuPengerjaan;

    @BindView(R.id.etTanggalAkhirPengumpulan) EditText etTanggalAkhirPengumpulan;
    @BindView(R.id.etJamAkhirPengumpulan) EditText etJamAkhirPengumpulan;

    @OnClick(R.id.etTanggalAkhirPengumpulan) void setTanggal() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(UbahQuizActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int tahunPilih, int bulanPilih, int tanggalPilih) {
                tahun   = tahunPilih;
                bulan   = bulanPilih;
                tanggal = tanggalPilih;
                etTanggalAkhirPengumpulan.setText(tanggal + "-" + (bulan+1) + "-" + tahun);
            }
        }, tahun, bulan, tanggal);
        datePickerDialog.show();
    }

    @OnClick(R.id.etJamAkhirPengumpulan) void setJam() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(UbahQuizActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int jamPilih, int meenitPilih) {
                jam   = jamPilih;
                menit = meenitPilih;
                etJamAkhirPengumpulan.setText(jam + ":" + menit);
            }
        }, jam, menit, false);
        timePickerDialog.show();
    }

    @OnClick(R.id.btnUbahQuiz) void ubahQuiz() {
        //membuat progress dialog
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Loading ...");
        progress.show();

        //mengambil data dari edittext
        String id_quiz = etIDQuiz.getText().toString();
        String judul = etJudul.getText().toString();
        String jumlah_soal = etJumlah.getText().toString();
        String waktu_pengerjaan_soal = etWaktuPengerjaan.getText().toString();

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
        Call<Value> call = api.ubahQuiz(id_quiz, judul, jumlah_soal, waktu_pengerjaan_soal, tahun, bulan+1, tanggal,jam, menit);
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                progress.dismiss();
                if (value.equals("1")) {
                    Toast.makeText(UbahQuizActivity.this, message, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(UbahQuizActivity.this, DataQuizDosenActivity.class);
                    i.putExtra("id_quiz", gid_quiz);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(UbahQuizActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(UbahQuizActivity.this, "Jaringan Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hapus_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.hapus_quiz:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Peringatan");
                alertDialogBuilder
                        .setMessage("Apakah Anda yakin ingin mengapus data ini?")
                        .setCancelable(false)
                        .setPositiveButton("Hapus",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String id_quiz = etIDQuiz.getText().toString().toLowerCase();
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(URL)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();
                                RegisterAPI api = retrofit.create(RegisterAPI.class);
                                Call<Value> call = api.hapusQuiz(id_quiz);
                                call.enqueue(new Callback<Value>() {
                                    @Override
                                    public void onResponse(Call<Value> call, Response<Value> response) {
                                        String value = response.body().getValue();
                                        String message = response.body().getMessage();
                                        if (value.equals("1")) {
                                            Toast.makeText(UbahQuizActivity.this, message, Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(UbahQuizActivity.this, DosenActivity.class);
                                            startActivity(i);
                                            finish();
                                        } else {
                                            Toast.makeText(UbahQuizActivity.this, message, Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Value> call, Throwable t) {
                                        t.printStackTrace();
                                        Toast.makeText(UbahQuizActivity.this, "Jaringan Error!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Batal",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_quiz);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String sid_quiz = intent.getStringExtra("id_quiz");
        gid_quiz = sid_quiz;
        String sjudul = intent.getStringExtra("judul");
        String sjumlah_soal = intent.getStringExtra("jumlah_soal");
        String swaktu_pengerjaan_soal = intent.getStringExtra("waktu_pengerjaan_soal");
        String stahun = intent.getStringExtra("tahun");
        String sbulan = intent.getStringExtra("bulan");
        String stanggal = intent.getStringExtra("tanggal");
        String sjam = intent.getStringExtra("jam");
        String smenit = intent.getStringExtra("menit");

        tahun = Integer.parseInt(stahun);
        bulan = Integer.parseInt(sbulan)-1;
        tanggal = Integer.parseInt(stanggal);
        jam = Integer.parseInt(sjam);
        menit = Integer.parseInt(smenit);

        etIDQuiz.setText(sid_quiz);
        etJudul.setText(sjudul);
        etJumlah.setText(sjumlah_soal);
        etWaktuPengerjaan.setText(swaktu_pengerjaan_soal);
        etTanggalAkhirPengumpulan.setText(tanggal + "-" + (bulan+1) + "-" + tahun);
        etJamAkhirPengumpulan.setText(jam + ":" + menit);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(UbahQuizActivity.this, DataQuizDosenActivity.class);
        i.putExtra("id_quiz", gid_quiz);
        startActivity(i);
        finish();
    }
}
