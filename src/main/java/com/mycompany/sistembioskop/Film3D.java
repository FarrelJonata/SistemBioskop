package com.mycompany.sistembioskop;

public class Film3D extends Film implements DapatDipesan, DapatDinilai {
    private double biayaKacamata;

    public Film3D(String judul, String genre, int durasiMenit,
                  double hargaDasar, double biayaKacamata) throws DataTidakValidException {
        super(judul, genre, durasiMenit, hargaDasar);
        this.setBiayaKacamata(biayaKacamata);
    }

    public double getBiayaKacamata() { return biayaKacamata; }

    public void setBiayaKacamata(double biayaKacamata) throws DataTidakValidException {
        if (biayaKacamata < 0) {
            throw new DataTidakValidException("Biaya kacamata tidak boleh negatif.");
        }
        this.biayaKacamata = biayaKacamata;
    }

    @Override
    public String getJenis() {
        return "3D";
    }

    @Override
    public String getFasilitas() {
        return "Layar 3D, kacamata 3D disediakan";
    }

    @Override
    public void tampilkanAturanTayang() {
        System.out.println("-> [ATURAN TAYANG] Wajib memakai kacamata 3D selama pemutaran, kacamata dikembalikan setelah film selesai.");
    }

    @Override
    public void pesanKursi(String nomorKursi) {
        System.out.println("-> [RESERVASI] Kursi " + nomorKursi + " untuk film 3D \"" + getJudul() + "\" berhasil dipesan, kacamata 3D disiapkan.");
    }

    @Override
    public void beriRating(int bintang) {
        System.out.println("-> [RATING] Film 3D \"" + getJudul() + "\" mendapat rating " + bintang + "/5 bintang.");
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
