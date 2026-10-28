package com.mycompany.sistembioskop;

public abstract class Film {

    private String judul;
    private String genre;
    private int durasiMenit;
    private double hargaDasar;

    private static int totalFilm = 0;

    public Film(String judul, String genre, int durasiMenit, double hargaDasar) throws DataTidakValidException {
        this.setJudul(judul);
        this.setGenre(genre);
        this.setDurasiMenit(durasiMenit);
        this.setHargaDasar(hargaDasar);
        totalFilm++;
    }

    public Film(String judul) throws DataTidakValidException {
        this(judul, "Umum", 90, 35000);
    }

    public String getJudul() { return judul; }
    public String getGenre() { return genre; }
    public int getDurasiMenit() { return durasiMenit; }
    public double getHargaDasar() { return hargaDasar; }
    public static int getTotalFilm() { return totalFilm; }

    public void setJudul(String judul) throws DataTidakValidException {
        if (judul == null || judul.trim().isEmpty()) {
            throw new DataTidakValidException("Judul film tidak boleh kosong.");
        }
        this.judul = judul.trim();
    }

    public void setGenre(String genre) throws DataTidakValidException {
        if (genre == null || genre.trim().isEmpty()) {
            throw new DataTidakValidException("Genre tidak boleh kosong.");
        }
        this.genre = genre.trim();
    }

    public void setDurasiMenit(int durasiMenit) throws DataTidakValidException {
        if (durasiMenit < 1 || durasiMenit > 600) {
            throw new DataTidakValidException("Durasi harus antara 1 - 600 menit.");
        }
        this.durasiMenit = durasiMenit;
    }

    public void setHargaDasar(double hargaDasar) throws DataTidakValidException {
        if (hargaDasar < 0) {
            throw new DataTidakValidException("Harga dasar tidak boleh negatif.");
        }
        this.hargaDasar = hargaDasar;
    }

    public static String formatRupiah(double nilai) {
        return "Rp " + String.format("%,.0f", nilai).replace(',', '.');
    }

    public String getJenis() {
        return "Film";
    }

    public String getFasilitas() {
        return "Layar standar";
    }

    public abstract void tampilkanAturanTayang();

    public double hitungHargaTiket() {
        return hargaDasar;
    }

    public double hitungHargaTiket(int jumlah) {
        return this.hitungHargaTiket() * jumlah;
    }

    public void tampilkanInfo() {
        System.out.printf("%-10s | %-26s | %-14s | %4d mnt | %-11s | ",
                getJenis(), judul, genre, durasiMenit,
                formatRupiah(hitungHargaTiket()));
    }
}
