package com.mycompany.sistembioskop;

public class FilmIMAX extends Film {
    private double biayaPremium;
    private String ukuranLayar;

    public FilmIMAX(String judul, String genre, int durasiMenit,
                    double hargaDasar, double biayaPremium, String ukuranLayar) {
        super(judul, genre, durasiMenit, hargaDasar);
        this.setBiayaPremium(biayaPremium);
        this.setUkuranLayar(ukuranLayar);
    }

    public double getBiayaPremium() { return biayaPremium; }
    public String getUkuranLayar() { return ukuranLayar; }

    public void setBiayaPremium(double biayaPremium) {
        if (biayaPremium < 0) {
            throw new IllegalArgumentException("Biaya premium tidak boleh negatif.");
        }
        this.biayaPremium = biayaPremium;
    }

    public void setUkuranLayar(String ukuranLayar) {
        if (ukuranLayar == null || ukuranLayar.trim().isEmpty()) {
            throw new IllegalArgumentException("Ukuran layar tidak boleh kosong.");
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
    public double hitungHargaTiket() {
        return super.hitungHargaTiket() + biayaPremium;
    }

    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo();
        System.out.printf("Layar %s, premium %s%n", ukuranLayar, formatRupiah(biayaPremium));
    }
}
