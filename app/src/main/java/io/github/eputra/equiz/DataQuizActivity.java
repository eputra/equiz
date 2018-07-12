package io.github.eputra.equiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DataQuizActivity extends AppCompatActivity {

    @BindView(R.id.textIDQuiz) TextView tvIDQuiz;
    @BindView(R.id.textDosen) TextView tvDosen;
    @BindView(R.id.textJudul) TextView tvJudul;
    @BindView(R.id.textJumlahSoal) TextView tvJumlahSoal;
    @BindView(R.id.textWaktuPengerjaanSoal) TextView tvWaktuPengerjaanSoal;
    @BindView(R.id.textWaktuAkhirPengerjaan) TextView tvWaktuAkhirPengerjaan;

    @OnClick(R.id.btnIkutiQuiz) void ikutiQuiz() {
        Intent i = new Intent(DataQuizActivity.this, MengerjakanQuizActivity.class);
        i.putExtra("id_quiz", getIntent().getExtras().getString("id_quiz"));
        i.putExtra("jumlah_soal", getIntent().getExtras().getString("jumlah_soal"));
        i.putExtra("waktu_pengerjaan_soal", getIntent().getExtras().getString("waktu_pengerjaan_soal"));
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_quiz);
        ButterKnife.bind(this);

        String id_quiz = getIntent().getExtras().getString("id_quiz");
        String nama_dosen = getIntent().getExtras().getString("nama_dosen");
        String judul = getIntent().getExtras().getString("judul");
        String jumlah_soal = getIntent().getExtras().getString("jumlah_soal");
        String waktu_pengerjaan_soal = getIntent().getExtras().getString("waktu_pengerjaan_soal");
        String tahun = getIntent().getExtras().getString("tahun");
        String bulan = getIntent().getExtras().getString("bulan");
        String tanggal = getIntent().getExtras().getString("tanggal");
        String jam = getIntent().getExtras().getString("jam");
        String menit = getIntent().getExtras().getString("menit");

        tvIDQuiz.setText(id_quiz);
        tvDosen.setText(nama_dosen);
        tvJudul.setText(judul);
        tvJumlahSoal.setText(jumlah_soal);
        tvWaktuPengerjaanSoal.setText(waktu_pengerjaan_soal + " menit");
        tvWaktuAkhirPengerjaan.setText(tanggal + "-" + bulan + "-" + tahun + " " + jam + ":" + menit);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MahasiswaActivity.class);
        startActivity(i);
        finish();
    }
}
