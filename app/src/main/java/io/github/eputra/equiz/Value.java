package io.github.eputra.equiz;

import java.util.List;

/**
 * Created by eka on 07/01/18.
 */

public class Value {

    String value;
    String message;
    String nama;
    String nama_pengguna;
    String level;
    List<Result> result;
    List<Nilai> nilai;
    List<NilaiQuiz> nilai_quiz;

    public List<NilaiQuiz> getNilai_quiz() {
        return nilai_quiz;
    }

    public void setNilai_quiz(List<NilaiQuiz> nilai_quiz) {
        this.nilai_quiz = nilai_quiz;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public List<Nilai> getNilai() {
        return nilai;
    }

    public void setNilai(List<Nilai> nilai) {
        this.nilai = nilai;
    }

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama_pengguna() {
        return nama_pengguna;
    }

    public void setNama_pengguna(String nama_pengguna) {
        this.nama_pengguna = nama_pengguna;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<Result> getResult() {
        return result;
    }
}
