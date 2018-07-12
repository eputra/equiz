package io.github.eputra.equiz;

/**
 * Created by eka on 08/01/18.
 */

public class Result {

    String id_quiz;
    String nama_pengguna_dosen;
    String judul;
    String jumlah_soal;
    String waktu_pengerjaan_soal;
    String tahun;
    String bulan;
    String tanggal;
    String jam;
    String menit;

    public String getJumlah_soal() {
        return jumlah_soal;
    }

    public String getId_quiz() {
        return id_quiz;
    }

    public String getNama_pengguna_dosen() {
        return nama_pengguna_dosen;
    }

    public String getJudul() {
        return judul;
    }

    public String getWaktu_pengerjaan_soal() {
        return waktu_pengerjaan_soal;
    }

    public String getTahun() {
        return tahun;
    }

    public String getBulan() {
        return bulan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getJam() {
        return jam;
    }

    public String getMenit() {
        return menit;
    }
}
