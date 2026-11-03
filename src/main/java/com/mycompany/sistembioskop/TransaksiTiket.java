package com.mycompany.sistembioskop;

public class TransaksiTiket {
    private static int totalTiketTerjual = 0;
    private static double totalPendapatan = 0;

    public static int getTotalTiketTerjual() { return totalTiketTerjual; }
    public static double getTotalPendapatan() { return totalPendapatan; }

    public static void prosesBeli(Member pembeli, Film film, int banyak) {
        cetakStruk(pembeli, film, banyak, 0);
    }

    public static void prosesBeli(Member pembeli, Film film, int banyak, String kodePromo) {
        double diskon = 0;
        if (kodePromo.trim().equalsIgnoreCase("HEMAT10")) {
            diskon = film.hitungHargaTiket(banyak) * 0.10;
            System.out.println("\nKode promo HEMAT10 diterapkan (diskon 10%).");
        } else {
            System.out.println("\nKode promo tidak valid, tidak ada diskon.");
        }
        cetakStruk(pembeli, film, banyak, diskon);
    }

    private static void cetakStruk(Member pembeli, Film film, int banyak, double diskon) {
        double subtotal = film.hitungHargaTiket(banyak);
        double total = subtotal - diskon;
        int poinDidapat = banyak * 10;

        totalTiketTerjual += banyak;
        totalPendapatan += total;
        pembeli.tambahPoin(poinDidapat);

        System.out.println("\n========== STRUK PEMBELIAN ==========");
        System.out.printf("Pembeli      : %s (%s)%n", pembeli.getNama(), pembeli.getId());
        System.out.printf("Film         : %s%n", film.getJudul());
        System.out.printf("Jenis        : %s%n", film.getJenis());
        System.out.printf("Fasilitas    : %s%n", film.getFasilitas());
        System.out.printf("Harga/tiket  : %s%n", Film.formatRupiah(film.hitungHargaTiket()));
        System.out.printf("Jumlah tiket : %d%n", banyak);
        System.out.printf("Subtotal     : %s%n", Film.formatRupiah(subtotal));
        System.out.printf("Diskon       : %s%n", Film.formatRupiah(diskon));
        System.out.printf("TOTAL BAYAR  : %s%n", Film.formatRupiah(total));
        System.out.printf("Poin didapat : +%d (total poin %d)%n", poinDidapat, pembeli.getPoin());
        System.out.println("=====================================");
        System.out.println("[STATUS] Transaksi berhasil diproses.");
    }
}
