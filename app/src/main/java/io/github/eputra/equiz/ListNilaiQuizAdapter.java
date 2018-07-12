package io.github.eputra.equiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eka on 24/01/18.
 */

public class ListNilaiQuizAdapter extends RecyclerView.Adapter<ListNilaiQuizAdapter.ViewHolder> {

    private Context context;
    private List<NilaiQuiz> nilaiQuizs;
    private String id_quiz;

    public ListNilaiQuizAdapter(Context context, List<NilaiQuiz> nilaiQuizs) {
        this.context = context;
        this.nilaiQuizs = nilaiQuizs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_nilai_quiz, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NilaiQuiz nilaiQuiz = nilaiQuizs.get(position);
        holder.tvNama.setText(nilaiQuiz.getNama_mahasiswa());
        holder.tvNilai.setText(nilaiQuiz.getNilai_mahasiswa());
        holder.tvId.setText(nilaiQuiz.getNama_pengguna());
        id_quiz = nilaiQuiz.getId_quiz();
    }

    @Override
    public int getItemCount() {
        return nilaiQuizs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tvNama) TextView tvNama;
        @BindView(R.id.tvNilai) TextView tvNilai;
        @BindView(R.id.tvId) TextView tvId;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String nama_pengguna = tvId.getText().toString();
            Intent i = new Intent(context, ReviewJawabanMahasiswaActivity.class);
            i.putExtra("nama_pengguna", nama_pengguna);
            i.putExtra("id_quiz", id_quiz);
            i.putExtra("nama_mahasiswa", tvNama.getText().toString());
            context.startActivity(i);
            ((Activity)context).finish();
        }
    }

}
