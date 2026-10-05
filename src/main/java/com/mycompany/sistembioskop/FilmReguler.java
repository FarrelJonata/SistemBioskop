package com.mycompany.sistembioskop;

public class FilmReguler extends Film {
    private int nomorStudio;

    public FilmReguler(String judul, String genre, int durasiMenit,
                       double hargaDasar, int nomorStudio) {
        super(judul, genre, durasiMenit, hargaDasar);
        this.setNomorStudio(nomorStudio);
    }

    public int getNomorStudio() { return nomorStudio; }

    public void setNomorStudio(int nomorStudio) {
        if (nomorStudio < 1 || nomorStudio > 10) {
            throw new IllegalArgumentException("Nomor studio harus antara 1 - 10.");
        }
        this.nomorStudio = nomorStudio;
    }

    @Override
    public String getJenis() {
        return "Reguler";
    }

    @Override
    public String getFasilitas() {
        return "Layar standar di Studio " + nomorStudio;
    }

    @Override
    public double hitungHargaTiket() {
        return super.hitungHargaTiket();
    }

    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo();
        System.out.printf("Studio %d%n", nomorStudio);
    }
}
