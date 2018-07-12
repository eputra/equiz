package io.github.eputra.equiz;

/**
 * Created by eka on 24/01/18.
 */

public class Nilai {

    String id_nilai;
    String id_quiz;
    String judul_quiz;
    String nama_dosen;
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

    public String getJudul_quiz() {
        return judul_quiz;
    }

    public void setJudul_quiz(String judul_quiz) {
        this.judul_quiz = judul_quiz;
    }

    public String getNama_dosen() {
        return nama_dosen;
    }

    public void setNama_dosen(String nama_dosen) {
        this.nama_dosen = nama_dosen;
    }

    public String getNilai_mahasiswa() {
        return nilai_mahasiswa;
    }

    public void setNilai_mahasiswa(String nilai_mahasiswa) {
        this.nilai_mahasiswa = nilai_mahasiswa;
    }
}
