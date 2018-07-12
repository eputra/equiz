package io.github.eputra.equiz;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by eka on 07/01/18.
 */

public interface RegisterAPI {

    @FormUrlEncoded
    @POST("dosen/daftar.php")
    Call<Value> daftarDosen(@Field("nama_pengguna") String nama_pengguna,
                            @Field("nama") String nama,
                            @Field("kata_sandi") String kata_sandi);

    @FormUrlEncoded
    @POST("dosen/masuk.php")
    Call<Value> masukDosen(@Field("nama_pengguna") String nama_pengguna,
                           @Field("kata_sandi") String kata_sandi);

    @FormUrlEncoded
    @POST("dosen/buat_quiz.php")
    Call<Value> buatQuiz(@Field("id_quiz") String id_quiz,
                         @Field("nama_pengguna_dosen") String nama_pengguna_dosen,
                         @Field("judul") String judul,
                         @Field("jumlah_soal") String jumlah_soal,
                         @Field("waktu_pengerjaan_soal") String waktu_pengerjaan_soal,
                         @Field("tahun") int tahun,
                         @Field("bulan") int bulan,
                         @Field("tanggal") int tanggal,
                         @Field("jam") int jam,
                         @Field("menit") int menit);

    @FormUrlEncoded
    @POST("dosen/daftar_quiz.php")
    Call<Value> listQuiz(@Field("nama_pengguna_dosen") String nama_pengguna_dosen);

    @FormUrlEncoded
    @POST("dosen/ubah_quiz.php")
    Call<Value> ubahQuiz(@Field("id_quiz") String id_quiz,
                         @Field("judul") String judul,
                         @Field("jumlah_soal") String jumlah_soal,
                         @Field("waktu_pengerjaan_soal") String waktu_pengerjaan_soal,
                         @Field("tahun") int tahun,
                         @Field("bulan") int bulan,
                         @Field("tanggal") int tanggal,
                         @Field("jam") int jam,
                         @Field("menit") int menit);

    @FormUrlEncoded
    @POST("dosen/hapus_quiz.php")
    Call<Value> hapusQuiz(@Field("id_quiz") String id_quiz);

    @FormUrlEncoded
    @POST("dosen/buat_soal.php")
    Call<Value> buatSoal(@Field("id_quiz") String id_quiz,
                         @Field("soal") String soal,
                         @Field("pilihan_jawaban") String pilihan_jawaban,
                         @Field("kunci_jawaban") String kunci_jawaban,
                         @Field("nomor_soal") int nomor_soal);

    @FormUrlEncoded
    @POST("dosen/cari_soal.php")
    Call<Soal> cariSoal(@Field("id_quiz") String id_quiz,
                         @Field("nomor_soal") int nomor_soal);

    @FormUrlEncoded
    @POST("dosen/ubah_soal.php")
    Call<Value> ubahSoal(@Field("id_quiz") String id_quiz,
                         @Field("soal") String soal,
                         @Field("pilihan_jawaban") String pilihan_jawaban,
                         @Field("kunci_jawaban") String kunci_jawaban,
                         @Field("nomor_soal") int nomor_soal);

    @FormUrlEncoded
    @POST("dosen/nilai.php")
    Call<Value> nilaiQuiz(@Field("id_quiz") String id_quiz);

    @FormUrlEncoded
    @POST("dosen/cari_quiz.php")
    Call<Quiz2> dataQuizDosen(@Field("id_quiz") String id_quiz);

    @FormUrlEncoded
    @POST("dosen/terbit.php")
    Call<Value> terbitkanQuiz(@Field("id_quiz") String id_quiz);

    @FormUrlEncoded
    @POST("mahasiswa/daftar.php")
    Call<Value> daftarMahasiswa(@Field("nama_pengguna") String nama_pengguna,
                                @Field("nama") String nama,
                                @Field("kata_sandi") String kata_sandi);

    @FormUrlEncoded
    @POST("mahasiswa/masuk.php")
    Call<Value> masukMahasiswa(@Field("nama_pengguna") String nama_pengguna,
                               @Field("kata_sandi") String kata_sandi);

    @FormUrlEncoded
    @POST("mahasiswa/cari_quiz.php")
    Call<Quiz2> dataQuiz(@Field("id_quiz") String id_quiz,
                         @Field("nama_pengguna") String nama_pengguna);

    @FormUrlEncoded
    @POST("mahasiswa/jawab_soal.php")
    Call<Value> jawabSoal(@Field("id_soal") String id_soal,
                          @Field("nama_pengguna") String nama_pengguna,
                          @Field("jawaban") String jawaban,
                          @Field("nomor_soal") int nomor_soal);

    @FormUrlEncoded
    @POST("mahasiswa/simpan_nilai.php")
    Call<Value> simpanNilai(@Field("id_quiz") String id_quiz,
                            @Field("nilai") String nilai,
                            @Field("nama_pengguna") String nama_pengguna);

    @FormUrlEncoded
    @POST("mahasiswa/nilai.php")
    Call<Value> nilai(@Field("nama_pengguna") String nama_pengguna);

    @FormUrlEncoded
    @POST("mahasiswa/review_jawaban.php")
    Call<ReviewJawaban> reviewJawaban(@Field("nama_pengguna") String nama_pengguna,
                                      @Field("id_quiz") String id_quiz,
                                      @Field("nomor_soal") int nomor_soal);
}
