package com.mycompany.sistembioskop;

public class Film3D extends Film {
    private double biayaKacamata;

    public Film3D(String judul, String genre, int durasiMenit,
                  double hargaDasar, double biayaKacamata) {
        super(judul, genre, durasiMenit, hargaDasar);
        this.setBiayaKacamata(biayaKacamata);
    }

    public double getBiayaKacamata() { return biayaKacamata; }

    public void setBiayaKacamata(double biayaKacamata) {
        if (biayaKacamata < 0) {
            throw new IllegalArgumentException("Biaya kacamata tidak boleh negatif.");
        }
        this.biayaKacamata = biayaKacamata;
    }

    @Override
    public String getJenis() {
        return "3D";
    }

    @Override
    public double hitungHargaTiket() {
        return super.hitungHargaTiket() + biayaKacamata;
    }

    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo();
        System.out.printf("Kacamata %s%n", formatRupiah(biayaKacamata));
    }
}
