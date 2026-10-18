package com.mycompany.sistembioskop;

public class FilmVIP extends Film implements DapatDipesan {
    private double biayaLayanan;
    private String jenisKursi;

    public FilmVIP(String judul, String genre, int durasiMenit,
                   double hargaDasar, double biayaLayanan, String jenisKursi) {
        super(judul, genre, durasiMenit, hargaDasar);
        this.setBiayaLayanan(biayaLayanan);
        this.setJenisKursi(jenisKursi);
    }

    public double getBiayaLayanan() { return biayaLayanan; }
    public String getJenisKursi() { return jenisKursi; }

    public void setBiayaLayanan(double biayaLayanan) {
        if (biayaLayanan < 0) {
            throw new IllegalArgumentException("Biaya layanan tidak boleh negatif.");
        }
        this.biayaLayanan = biayaLayanan;
    }

    public void setJenisKursi(String jenisKursi) {
        if (jenisKursi == null || jenisKursi.trim().isEmpty()) {
            throw new IllegalArgumentException("Jenis kursi tidak boleh kosong.");
        }
        this.jenisKursi = jenisKursi.trim();
    }

    @Override
    public String getJenis() {
        return "VIP";
    }

    @Override
    public String getFasilitas() {
        return "Kursi " + jenisKursi + ", gratis snack dan minuman";
    }

    @Override
    public void tampilkanAturanTayang() {
        System.out.println("-> [ATURAN TAYANG] Kursi " + jenisKursi + ", masuk lewat lounge VIP, snack dan minuman gratis.");
    }

    @Override
    public void pesanKursi(String nomorKursi) {
        System.out.println("-> [RESERVASI] Kursi VIP " + jenisKursi + " nomor " + nomorKursi + " berhasil dipesan, pelayan akan mengantar snack.");
    }

    @Override
    public double hitungHargaTiket() {
        return super.hitungHargaTiket() + biayaLayanan;
    }

    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo();
        System.out.printf("Kursi %s, layanan %s%n", jenisKursi, formatRupiah(biayaLayanan));
    }
}
