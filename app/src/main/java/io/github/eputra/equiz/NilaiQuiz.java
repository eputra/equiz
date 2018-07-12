package io.github.eputra.equiz;

/**
 * Created by eka on 24/01/18.
 */

public class NilaiQuiz {

    String id_nilai;
    String id_quiz;
    String nama_mahasiswa;
    String nama_pengguna;
    String nilai_mahasiswa;

    public String getId_nilai() {
        return id_nilai;
    }

    public void setId_nilai(String id_nilai) {
        this.id_nilai = id_nilai;
    }

    public String getId_quiz() {
        return id_quiz;
    }

    public void setId_quiz(String id_quiz) {
        this.id_quiz = id_quiz;
    }

    public String getNama_mahasiswa() {
        return nama_mahasiswa;
    }

    public void setNama_mahasiswa(String nama_mahasiswa) {
        this.nama_mahasiswa = nama_mahasiswa;
    }

    public String getNama_pengguna() {
        return nama_pengguna;
    }

    public void setNama_pengguna(String nama_pengguna) {
        this.nama_pengguna = nama_pengguna;
    }

    public String getNilai_mahasiswa() {
        return nilai_mahasiswa;
    }

    public void setNilai_mahasiswa(String nilai_mahasiswa) {
        this.nilai_mahasiswa = nilai_mahasiswa;
    }
}
