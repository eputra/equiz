package io.github.eputra.equiz;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
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

public class DosenActivity extends AppCompatActivity {

    public static final String URL = "http://192.168.122.1/web/equiz/";
    SessionManagement session;

    private List<Result> results = new ArrayList<>();
    private ListQuizAdapter viewAdapter;

    @BindView(R.id.rvListView) RecyclerView recyclerView;
    @BindView(R.id.pbListQuiz) ProgressBar progressBar;

    @OnClick(R.id.fabTambahQuiz) void tambahQuiz() {
        Intent intent = new Intent(DosenActivity.this, BuatQuizActivity.class);
        startActivity(intent);
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
                                Intent i = new Intent(DosenActivity.this, MainActivity.class);
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
        setContentView(R.layout.activity_dosen);
        ButterKnife.bind(this);
        session = new SessionManagement(getApplicationContext());

        viewAdapter = new ListQuizAdapter(this, results);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(viewAdapter);

        loadDataQuiz();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataQuiz();
    }

    private void loadDataQuiz() {
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
        Call<Value> call = api.listQuiz(session.getNamaPengguna());
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                progressBar.setVisibility(View.GONE);
                if (value.equals("1")) {
                    results = response.body().getResult();
                    viewAdapter = new ListQuizAdapter(DosenActivity.this, results);
                    recyclerView.setAdapter(viewAdapter);
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {

            }
        });
    }
}
