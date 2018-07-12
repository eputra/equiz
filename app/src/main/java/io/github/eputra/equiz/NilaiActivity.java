package io.github.eputra.equiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
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

public class NilaiActivity extends AppCompatActivity {

    public static final String URL = "http://192.168.122.1/web/equiz/";
    private ProgressDialog progress;
    SessionManagement session;
    Call<Value> call;

    String nama_pengguna;
    String id_quiz;
    String nilai;
    String jumlah_soal;

    @BindView(R.id.tvNilai) TextView tvNilai;
    @BindView(R.id.pbNilai) ProgressBar pbNilai;

    @OnClick(R.id.btnOk) void ok() {
        Intent i = new Intent(NilaiActivity.this, MahasiswaActivity.class);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.btnReviewJawaban) void reviewJawaban() {
        Intent i = new Intent(NilaiActivity.this, ReviewJawabanActivity.class);
        i.putExtra("id_quiz", id_quiz);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nilai);
        ButterKnife.bind(this);
        session = new SessionManagement(getApplicationContext());
        nama_pengguna = session.getNamaPengguna();
        Intent i = getIntent();
        nilai = i.getStringExtra("nilai");
        id_quiz = i.getStringExtra("id_quiz");
        jumlah_soal = i.getStringExtra("jumlah_soal");
        tvNilai.setText(i.getStringExtra("nilai"));
        System.out.println("test");
        simpanNilai();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        call.cancel();
    }

    private void simpanNilai() {
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

        call = api.simpanNilai(id_quiz, nilai, nama_pengguna);
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                pbNilai.setVisibility(View.GONE);
//                if (value.equals("1")) {
//                    //System.out.println(message);
//                } else {
//                    Toast.makeText(NilaiActivity.this, message, Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                pbNilai.setVisibility(View.GONE);
                Toast.makeText(NilaiActivity.this, "Jaringan Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
