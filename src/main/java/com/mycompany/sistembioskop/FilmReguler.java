package com.mycompany.sistembioskop;

public class FilmReguler extends Film implements DapatDinilai {
    private int nomorStudio;

    public FilmReguler(String judul, String genre, int durasiMenit,
                       double hargaDasar, int nomorStudio) throws DataTidakValidException {
        super(judul, genre, durasiMenit, hargaDasar);
        this.setNomorStudio(nomorStudio);
    }

    public int getNomorStudio() { return nomorStudio; }

    public void setNomorStudio(int nomorStudio) throws DataTidakValidException {
        if (nomorStudio < 1 || nomorStudio > 10) {
            throw new DataTidakValidException("Nomor studio harus antara 1 - 10.");
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
    public void tampilkanAturanTayang() {
        System.out.println("-> [ATURAN TAYANG] Tiket berlaku untuk Studio " + nomorStudio + ", kursi bebas (siapa cepat dia dapat).");
    }

    @Override
    public void beriRating(int bintang) {
        System.out.println("-> [RATING] Film reguler \"" + getJudul() + "\" mendapat rating " + bintang + "/5 bintang.");
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
