package com.mycompany.sistembioskop;

public class FilmIMAX extends Film implements DapatDipesan, DapatDinilai {
    private double biayaPremium;
    private String ukuranLayar;

    public FilmIMAX(String judul, String genre, int durasiMenit,
                    double hargaDasar, double biayaPremium, String ukuranLayar) throws DataTidakValidException {
        super(judul, genre, durasiMenit, hargaDasar);
        this.setBiayaPremium(biayaPremium);
        this.setUkuranLayar(ukuranLayar);
    }

    public double getBiayaPremium() { return biayaPremium; }
    public String getUkuranLayar() { return ukuranLayar; }

    public void setBiayaPremium(double biayaPremium) throws DataTidakValidException {
        if (biayaPremium < 0) {
            throw new DataTidakValidException("Biaya premium tidak boleh negatif.");
        }
        this.biayaPremium = biayaPremium;
    }

    public void setUkuranLayar(String ukuranLayar) throws DataTidakValidException {
        if (ukuranLayar == null || ukuranLayar.trim().isEmpty()) {
            throw new DataTidakValidException("Ukuran layar tidak boleh kosong.");
        }
        this.ukuranLayar = ukuranLayar.trim();
    }

    @Override
    public String getJenis() {
        return "IMAX";
    }

    @Override
    public String getFasilitas() {
        return "Layar IMAX " + ukuranLayar + ", audio premium";
    }

    @Override
    public void tampilkanAturanTayang() {
        System.out.println("-> [ATURAN TAYANG] Tiket IMAX tidak dapat ditukar, penonton hadir 15 menit sebelum film mulai.");
    }

    @Override
    public void pesanKursi(String nomorKursi) {
        System.out.println("-> [RESERVASI] Kursi " + nomorKursi + " di layar IMAX " + ukuranLayar + " berhasil dipesan.");
    }

    @Override
    public void beriRating(int bintang) {
        System.out.println("-> [RATING] Film IMAX \"" + getJudul() + "\" mendapat rating " + bintang + "/5 bintang.");
    }

    @Override
    public double hitungHargaTiket() {
        return super.hitungHargaTiket() + biayaPremium;
    }

    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo();
        System.out.printf("Layar %s, premium %s%n", ukuranLayar, formatRupiah(biayaPremium));
    }
}
