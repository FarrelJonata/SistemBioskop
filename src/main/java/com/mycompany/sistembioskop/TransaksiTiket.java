package com.mycompany.sistembioskop;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransaksiTiket {
    public static final String FILE_LOG = "log_transaksi.txt";

    private static int totalTiketTerjual = 0;
    private static double totalPendapatan = 0;

    public static int getTotalTiketTerjual() { return totalTiketTerjual; }
    public static double getTotalPendapatan() { return totalPendapatan; }

    public static void setStatistik(int tiket, double pendapatan) {
        totalTiketTerjual = tiket;
        totalPendapatan = pendapatan;
    }

    public static void tulisLog(String pesan) {
        try {
            FileWriter pipaTulis = new FileWriter(FILE_LOG, true);
            pipaTulis.write(pesan + System.lineSeparator());
            pipaTulis.close();
            System.out.println("[FILE] Transaksi dicatat ke " + FILE_LOG);
        } catch (IOException e) {
            System.out.println("[FILE] Gagal menulis log: " + e.getMessage());
        }
    }

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

        String waktu = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        tulisLog("[" + waktu + "] " + pembeli.getId() + " (" + pembeli.getNama() + ") membeli "
                + banyak + " tiket " + film.getJenis() + " \"" + film.getJudul()
                + "\" | Total " + Film.formatRupiah(total));
    }
}
