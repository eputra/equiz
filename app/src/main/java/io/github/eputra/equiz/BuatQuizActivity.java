package io.github.eputra.equiz;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.UUID;

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

public class BuatQuizActivity extends AppCompatActivity {

    public static final String URL = "http://192.168.122.1/web/equiz/";
    private ProgressDialog progress;
    SessionManagement session;

    final Calendar c = Calendar.getInstance();
    int thnSekarang = c.get(Calendar.YEAR);
    int blnSekarang = c.get(Calendar.MONTH);
    int tglSekarang = c.get(Calendar.DAY_OF_MONTH);
    int jamSekarang = c.get(Calendar.HOUR_OF_DAY);
    int menitSekarang = c.get(Calendar.MINUTE);
    int tahun = c.get(Calendar.YEAR);
    int bulan = c.get(Calendar.MONTH);
    int tanggal = c.get(Calendar.DAY_OF_MONTH);
    int jam = c.get(Calendar.HOUR_OF_DAY);
    int menit = c.get(Calendar.MINUTE);

    @BindView(R.id.etJudul) EditText etJudul;
    @BindView(R.id.etJumlah) EditText etJumlah;
    @BindView(R.id.etWaktuPengerjaan) EditText etWaktuPengerjaan;

    @BindView(R.id.etTanggalAkhirPengumpulan) EditText etTanggalAkhirPengumpulan;
    @BindView(R.id.etJamAkhirPengumpulan) EditText etJamAkhirPengumpulan;

    @OnClick(R.id.etTanggalAkhirPengumpulan) void setTanggal() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(BuatQuizActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int tahunPilih, int bulanPilih, int tanggalPilih) {
                if (tahunPilih < thnSekarang) {
                    Toast.makeText(BuatQuizActivity.this, "Tahun tidak boleh lebih kecil dari tahun sekarang.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ((tahunPilih == thnSekarang) && (bulanPilih < blnSekarang)) {
                    Toast.makeText(BuatQuizActivity.this, "Bulan tidak boleh lebih kecil dari bulan sekarang.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ((tahunPilih == thnSekarang) && (bulanPilih == blnSekarang) && (tanggalPilih < tglSekarang)) {
                    Toast.makeText(BuatQuizActivity.this, "Tanggal tidak boleh lebih kecil dari tanggal sekarang.", Toast.LENGTH_SHORT).show();
                    return;
                }

                tahun   = tahunPilih;
                bulan   = bulanPilih;
                tanggal = tanggalPilih;
                etTanggalAkhirPengumpulan.setText(tanggal + "-" + (bulan+1) + "-" + tahun);
            }
        }, tahun, bulan, tanggal);
        datePickerDialog.show();
    }

    @OnClick(R.id.etJamAkhirPengumpulan) void setJam() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(BuatQuizActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int jamPilih, int meenitPilih) {
                if ((tahun == thnSekarang) && (bulan == blnSekarang) && (tanggal == tglSekarang)) {
                    if (jamPilih < jamSekarang) {
                        Toast.makeText(BuatQuizActivity.this, "Jam tidak boleh lebih kecil dari jam sekarang.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if ((jamPilih == jamSekarang) && (meenitPilih < menitSekarang)) {
                        Toast.makeText(BuatQuizActivity.this, "Menit tidak boleh lebih kecil dari menit sekarang.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                jam   = jamPilih;
                menit = meenitPilih;
                etJamAkhirPengumpulan.setText(jam + ":" + menit);
            }
        }, jam, menit, false);
        timePickerDialog.show();
    }

    @OnClick(R.id.btnBuatQuiz) void buatQuiz() {
        if (etJudul.getText().toString().isEmpty()) {
            etJudul.setError("Harus diisi");
            return;
        }

        if (etJumlah.getText().toString().isEmpty()) {
            etJumlah.setError("Harus diisi");
            return;
        }

        if (etJumlah.getText().toString().equals("0")) {
            etJumlah.setError("Harus lebih dari 0");
            return;
        }

        if (etWaktuPengerjaan.getText().toString().isEmpty()) {
            etWaktuPengerjaan.setError("Harus diisi");
            return;
        }

        if (etWaktuPengerjaan.getText().toString().equals("0")) {
            etWaktuPengerjaan.setError("Harus lebih dari 0");
            return;
        }

        if (etTanggalAkhirPengumpulan.getText().toString().isEmpty()) {
            Toast.makeText(this, "Tanggal akhir pengumpulan harus diisi.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (etJamAkhirPengumpulan.getText().toString().isEmpty()) {
            Toast.makeText(this, "Jam akhir pengumpulan harus diisi.", Toast.LENGTH_SHORT).show();
            return;
        }

        //membuat progress dialog
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Loading ...");
        progress.show();

        //generate id_quiz
        String uuid = UUID.randomUUID().toString();
        final String id_quiz = uuid.substring(28);

        //mengambil data dari edittext
        String judul = etJudul.getText().toString();
        final String jumlah_soal = etJumlah.getText().toString();
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
                .client(httpClient.build())
                .build();
        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<Value> call = api.buatQuiz(id_quiz, session.getNamaPengguna(), judul, jumlah_soal,
                waktu_pengerjaan_soal, tahun, bulan+1, tanggal,jam, menit);
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                progress.dismiss();
                if (value.equals("1")) {
                    Intent i = new Intent(BuatQuizActivity.this, BuatSoalActivity.class);
                    i.putExtra("id_quiz", id_quiz);
                    i.putExtra("jumlah_soal", jumlah_soal);
                    startActivity(i);
                    //Toast.makeText(BuatQuizActivity.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(BuatQuizActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(BuatQuizActivity.this, "Jaringan Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_quiz);
        ButterKnife.bind(this);
        session = new SessionManagement(getApplicationContext());
    }

}
