package io.github.eputra.equiz;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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

public class MahasiswaActivity extends AppCompatActivity {

    public static final String URL = "http://192.168.122.1/web/equiz/";
    private ProgressDialog progress;
    SessionManagement session;

    private List<Nilai> nilais = new ArrayList<>();
    private ListNilaiAdapter viewAdapter;

    @BindView(R.id.rvListView) RecyclerView recyclerView;
    @BindView(R.id.pbListQuiz) ProgressBar progressBar;

    @OnClick(R.id.fabCariQuiz) void cariQuiz() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MahasiswaActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_cari_quiz, null);
        final EditText mIdQuiz = (EditText) mView.findViewById(R.id.etIdQuiz);
        Button mCariQuiz = (Button) mView.findViewById(R.id.btnCariQuiz);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        progress = new ProgressDialog(this);

        mCariQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //membuat progress dialog
                progress.setCancelable(false);
                progress.setMessage("Loading ...");
                progress.show();

                //mengambil data dari edittext
                String id_quiz = mIdQuiz.getText().toString().toLowerCase();

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
                Call<Quiz2> call = api.dataQuiz(id_quiz, session.getNamaPengguna());
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
                        progress.dismiss();
                        if (value.equals("1")) {
                            dialog.dismiss();
                            Intent i = new Intent(MahasiswaActivity.this, DataQuizActivity.class);
                            i.putExtra("id_quiz", id_quiz);
                            i.putExtra("judul", judul);
                            i.putExtra("jumlah_soal", jumlah_soal);
                            i.putExtra("waktu_pengerjaan_soal", waktu_pengerjaan_soal);
                            i.putExtra("tahun", tahun);
                            i.putExtra("bulan", bulan);
                            i.putExtra("tanggal", tanggal);
                            i.putExtra("jam", jam);
                            i.putExtra("menit", menit);
                            i.putExtra("nama_dosen", nama_dosen);
                            startActivity(i);
                            finish();
                            Toast.makeText(MahasiswaActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MahasiswaActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Quiz2> call, Throwable t) {
                        progress.dismiss();
                        Toast.makeText(MahasiswaActivity.this, "Jaringan Error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_dosen, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.keluar:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Peringatan");
                alertDialogBuilder
                        .setMessage("Apakah Anda yakin ingin keluar?")
                        .setCancelable(false)
                        .setPositiveButton("Keluar",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                session.logoutUser();
                                Intent i = new Intent(MahasiswaActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
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
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mahasiswa);
        session = new SessionManagement(getApplicationContext());
        ButterKnife.bind(this);

        viewAdapter = new ListNilaiAdapter(this, nilais);
        RecyclerView.LayoutManager mLayaoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayaoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(viewAdapter);

        loadNilai();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNilai();
    }

    private void loadNilai() {
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
        Call<Value> call = api.nilai(session.getNamaPengguna());
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                progressBar.setVisibility(View.GONE);
                if (value.equals("1")) {
                    nilais = response.body().getNilai();
                    viewAdapter = new ListNilaiAdapter(MahasiswaActivity.this, nilais);
                    recyclerView.setAdapter(viewAdapter);
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {

            }
        });
    }

}
