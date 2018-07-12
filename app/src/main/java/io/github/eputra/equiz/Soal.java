package io.github.eputra.equiz;

/**
 * Created by eka on 20/01/18.
 */

public class Soal {

    String value;
    String message;
    String id_soal;
    String id_quiz;
    String soal;
    String pilihan_jawaban;
    String kunci_jawaban;
    String nomor_soal;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId_soal() {
        return id_soal;
    }

    public void setId_soal(String id_soal) {
        this.id_soal = id_soal;
    }

    public String getId_quiz() {
        return id_quiz;
    }

    public void setId_quiz(String id_quiz) {
        this.id_quiz = id_quiz;
    }

    public String getSoal() {
        return soal;
    }

    public void setSoal(String soal) {
        this.soal = soal;
    }

    public String getPilihan_jawaban() {
        return pilihan_jawaban;
    }

    public void setPilihan_jawaban(String pilihan_jawaban) {
        this.pilihan_jawaban = pilihan_jawaban;
    }

    public String getKunci_jawaban() {
        return kunci_jawaban;
    }

    public void setKunci_jawaban(String kunci_jawaban) {
        this.kunci_jawaban = kunci_jawaban;
    }

    public String getNomor_soal() {
        return nomor_soal;
    }

    public void setNomor_soal(String nomor_soal) {
        this.nomor_soal = nomor_soal;
    }
}
