package io.github.eputra.equiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eka on 08/01/18.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Result> results;

    public RecyclerViewAdapter(Context context, List<Result> results) {
        this.context = context;
        this.results = results;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_quiz, parent, false);
        ViewHolder holder = new ViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Result result = results.get(position);
        holder.textViewIDQuiz.setText(result.getId_quiz());
        holder.textViewJudul.setText(result.getJudul());
        holder.textViewJumlahSoal.setText(result.getJumlah_soal());
        holder.textViewWaktuPengerjaanSoal.setText(result.getWaktu_pengerjaan_soal() + " menit");
        holder.textViewWaktuAkhirPengerjaan.setText(result.getTanggal() + "-" +result.getBulan() + "-" + result.getTahun() + " " + result.getJam() + ":" + result.getMenit());
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.textIDQuiz) TextView textViewIDQuiz;
        @BindView(R.id.textJudul) TextView textViewJudul;
        @BindView(R.id.textJumlahSoal) TextView textViewJumlahSoal;
        @BindView(R.id.textWaktuPengerjaanSoal) TextView textViewWaktuPengerjaanSoal;
        @BindView(R.id.textWaktuAkhirPengerjaan) TextView textViewWaktuAkhirPengerjaan;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String id_quiz = textViewIDQuiz.getText().toString().toUpperCase();
            String judul = textViewJudul.getText().toString();
            String jumlah_soal = textViewJumlahSoal.getText().toString();
            String waktu_pengerjaan_soal = textViewWaktuPengerjaanSoal.getText().toString();
            String waktu_akhir_pengerjaan = textViewWaktuAkhirPengerjaan.getText().toString();

//            String[] wps = waktu_pengerjaan_soal.split(" ");
//
//            String[] datetime = waktu_akhir_pengerjaan.split(" ");
//            String[] date = datetime[0].split("-");
//            String[] time = datetime[1].split(":");
//            String tahun = date[2];
//            String bulan = date[1];
//            String tanggal = date[0];
//            String jam = time[0];
//            String menit = time[1];
//
//            Intent i = new Intent(context, UbahQuizActivity.class);
//            i.putExtra("id_quiz", id_quiz);
//            i.putExtra("judul", judul);
//            i.putExtra("jumlah_soal", jumlah_soal);
//            i.putExtra("waktu_pengerjaan_soal", wps[0]);
//            i.putExtra("tahun", tahun);
//            i.putExtra("bulan", bulan);
//            i.putExtra("tanggal", tanggal);
//            i.putExtra("jam", jam);
//            i.putExtra("menit", menit);
            Intent i = new Intent(context, DataQuizDosenActivity.class);
            i.putExtra("id_quiz", id_quiz);
            i.putExtra("judul", judul);
            i.putExtra("jumlah_soal", jumlah_soal);
            i.putExtra("waktu_pengerjaan_soal", waktu_pengerjaan_soal);
            i.putExtra("waktu_akhir_pengerjaan", waktu_akhir_pengerjaan);
            context.startActivity(i);
        }
    }
}
