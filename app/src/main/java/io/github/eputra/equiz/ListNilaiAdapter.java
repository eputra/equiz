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

public class ListNilaiAdapter extends RecyclerView.Adapter<ListNilaiAdapter.ViewHolder> {

    private Context context;
    private List<Nilai> nilais;
    private String id_quiz;

    public ListNilaiAdapter(Context context, List<Nilai> nilais) {
        this.context = context;
        this.nilais = nilais;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_nilai, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Nilai nilai = nilais.get(position);
        holder.textIDQuiz.setText(nilai.getId_quiz());
        holder.textJudulQuiz.setText(nilai.getJudul_quiz());
        holder.textNamaDosen.setText(nilai.getNama_dosen());
        holder.textNilai.setText(nilai.getNilai_mahasiswa());
        id_quiz = nilai.getId_quiz();
    }

    @Override
    public int getItemCount() {
        return nilais.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.textIDQuiz) TextView textIDQuiz;
        @BindView(R.id.textJudulQuiz) TextView textJudulQuiz;
        @BindView(R.id.textNamaDosen) TextView textNamaDosen;
        @BindView(R.id.textNilai) TextView textNilai;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(context, ReviewJawabanActivity.class);
            i.putExtra("id_quiz", id_quiz);
            context.startActivity(i);
            ((Activity)context).finish();
        }
    }
}
