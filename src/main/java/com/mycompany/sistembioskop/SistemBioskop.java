package com.mycompany.sistembioskop;

import java.util.Scanner;

public class SistemBioskop {
    private static Scanner input = new Scanner(System.in);
    private static final int KAPASITAS = 30;
    private static Film[] daftar = new Film[KAPASITAS];
    private static int jumlah = 0;

    private static int totalTiketTerjual = 0;
    private static double totalPendapatan = 0;

    public static void main(String[] args) {
        isiDataAwal();
        int pilihan;

        do {
            tampilkanMenu();
            pilihan = bacaInt("Pilih menu [1-6]: ");

            switch (pilihan) {
                case 1:
                    tambahFilm();
                    break;
                case 2:
                    tampilkanSemua();
                    break;
                case 3:
                    menuPencarian();
                    break;
                case 4:
                    beliTiket();
                    break;
                case 5:
                    simulasiPemutaran();
                    break;
                case 6:
                    System.out.println("\nTerima kasih telah menggunakan Sistem Bioskop!");
                    break;
                default:
                    System.out.println("Pilihan tidak valid, coba lagi.");
            }
        } while (pilihan != 6);
    }

    static void tampilkanMenu() {
        System.out.println("\n==========================================");
        System.out.println("       SISTEM MANAJEMEN BIOSKOP");
        System.out.println("==========================================");
        System.out.println(" 1. Tambah Film Baru");
        System.out.println(" 2. Tampilkan Daftar Film");
        System.out.println(" 3. Cari Film");
        System.out.println(" 4. Beli Tiket");
        System.out.println(" 5. Simulasi Pemutaran Film");
        System.out.println(" 6. Keluar");
        System.out.println("------------------------------------------");
        System.out.println(" Total film     : " + Film.getTotalFilm());
        System.out.println(" Tiket terjual  : " + totalTiketTerjual);
        System.out.println(" Pendapatan     : " + Film.formatRupiah(totalPendapatan));
        System.out.println("==========================================");
    }

    static void tambahFilm() {
        if (jumlah >= KAPASITAS) {
            System.out.println("Kapasitas penuh, tidak bisa menambah film.");
            return;
        }

        System.out.println("\n--- Tambah Film Baru ---");
        System.out.println("Jenis film:");
        System.out.println(" 1. Reguler");
        System.out.println(" 2. 3D");
        System.out.println(" 3. IMAX");
        System.out.println(" 4. VIP");
        int jenis = bacaInt("Pilih jenis [1-4]: ");

        if (jenis < 1 || jenis > 4) {
            System.out.println("Jenis tidak valid. Penambahan dibatalkan.");
            return;
        }

        String judul = bacaTeks("Judul film   : ");
        String genre = bacaTeks("Genre        : ");
        int durasi = bacaInt("Durasi (menit): ");
        double harga = bacaDouble("Harga dasar   : ");

        try {
            Film baru;
            if (jenis == 1) {
                int studio = bacaInt("Nomor studio (1-10): ");
                baru = new FilmReguler(judul, genre, durasi, harga, studio);
            } else if (jenis == 2) {
                double kacamata = bacaDouble("Biaya kacamata: ");
                baru = new Film3D(judul, genre, durasi, harga, kacamata);
            } else if (jenis == 3) {
                double premium = bacaDouble("Biaya premium : ");
                String layar = bacaTeks("Ukuran layar  : ");
                baru = new FilmIMAX(judul, genre, durasi, harga, premium, layar);
            } else {
                double layanan = bacaDouble("Biaya layanan VIP: ");
                String kursi = bacaTeks("Jenis kursi (Recliner/Sofa): ");
                baru = new FilmVIP(judul, genre, durasi, harga, layanan, kursi);
            }
            daftar[jumlah++] = baru;
            System.out.println("\n[OK] Film \"" + baru.getJudul() + "\" berhasil ditambahkan.");
        } catch (IllegalArgumentException e) {
            System.out.println("\n[GAGAL] " + e.getMessage());
        }
    }

    static void tampilkanSemua() {
        if (jumlah == 0) {
            System.out.println("\nBelum ada film terdaftar.");
            return;
        }
        cetakHeader();
        for (int i = 0; i < jumlah; i++) {
            System.out.printf("%-3d| ", i + 1);
            daftar[i].tampilkanInfo();
        }
        cetakGaris();
        System.out.println("Total: " + jumlah + " film");
    }

    static void cetakHeader() {
        System.out.println();
        cetakGaris();
        System.out.printf("%-3s| %-10s | %-26s | %-14s | %-8s | %-11s | %s%n",
                "No", "Jenis", "Judul", "Genre", "Durasi", "Harga Tiket", "Detail");
        cetakGaris();
    }

    static void cetakGaris() {
        for (int i = 0; i < 112; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    static void menuPencarian() {
        System.out.println("\n--- Cari Film ---");
        System.out.println(" 1. Berdasarkan judul");
        System.out.println(" 2. Berdasarkan genre dan durasi maksimal");
        int mode = bacaInt("Pilih [1-2]: ");

        if (mode == 1) {
            String kata = bacaTeks("Kata kunci judul: ");
            cariFilm(kata);
        } else if (mode == 2) {
            String genre = bacaTeks("Genre: ");
            int maks = bacaInt("Durasi maksimal (menit): ");
            cariFilm(genre, maks);
        } else {
            System.out.println("Pilihan tidak valid.");
        }
    }

    static void cariFilm(String kataKunci) {
        boolean ketemu = false;
        for (int i = 0; i < jumlah; i++) {
            if (daftar[i].getJudul().toLowerCase().contains(kataKunci.toLowerCase())) {
                if (!ketemu) cetakHeader();
                System.out.printf("%-3d| ", i + 1);
                daftar[i].tampilkanInfo();
                ketemu = true;
            }
        }
        hasilPencarian(ketemu);
    }

    static void cariFilm(String genre, int durasiMaks) {
        boolean ketemu = false;
        for (int i = 0; i < jumlah; i++) {
            if (daftar[i].getGenre().equalsIgnoreCase(genre.trim())
                    && daftar[i].getDurasiMenit() <= durasiMaks) {
                if (!ketemu) cetakHeader();
                System.out.printf("%-3d| ", i + 1);
                daftar[i].tampilkanInfo();
                ketemu = true;
            }
        }
        hasilPencarian(ketemu);
    }

    static void hasilPencarian(boolean ketemu) {
        if (ketemu) cetakGaris();
        else System.out.println("Film tidak ditemukan.");
    }

    static void beliTiket() {
        if (jumlah == 0) {
            System.out.println("\nBelum ada film terdaftar.");
            return;
        }
        tampilkanSemua();
        int nomor = bacaInt("\nPilih nomor film: ");

        if (nomor < 1 || nomor > jumlah) {
            System.out.println("Nomor film tidak valid.");
            return;
        }

        int banyak = bacaInt("Jumlah tiket (1-10): ");
        if (banyak < 1 || banyak > 10) {
            System.out.println("Jumlah tiket harus antara 1 - 10.");
            return;
        }

        String kodePromo = bacaTeks("Kode promo (kosongkan jika tidak ada): ");
        Film dipilih = daftar[nomor - 1];

        if (kodePromo.trim().isEmpty()) {
            prosesTransaksi(dipilih, banyak);
        } else {
            prosesTransaksi(dipilih, banyak, kodePromo);
        }
    }

    static void prosesTransaksi(Film film, int banyak) {
        cetakStruk(film, banyak, 0);
    }

    static void prosesTransaksi(Film film, int banyak, String kodePromo) {
        double diskon = 0;
        if (kodePromo.trim().equalsIgnoreCase("HEMAT10")) {
            diskon = film.hitungHargaTiket(banyak) * 0.10;
            System.out.println("\nKode promo HEMAT10 diterapkan (diskon 10%).");
        } else {
            System.out.println("\nKode promo tidak valid, tidak ada diskon.");
        }
        cetakStruk(film, banyak, diskon);
    }

    static void cetakStruk(Film film, int banyak, double diskon) {
        double subtotal = film.hitungHargaTiket(banyak);
        double total = subtotal - diskon;

        totalTiketTerjual += banyak;
        totalPendapatan += total;

        System.out.println("\n========== STRUK PEMBELIAN ==========");
        System.out.printf("Film         : %s%n", film.getJudul());
        System.out.printf("Jenis        : %s%n", film.getJenis());
        System.out.printf("Fasilitas    : %s%n", film.getFasilitas());
        System.out.printf("Harga/tiket  : %s%n", Film.formatRupiah(film.hitungHargaTiket()));
        System.out.printf("Jumlah tiket : %d%n", banyak);
        System.out.printf("Subtotal     : %s%n", Film.formatRupiah(subtotal));
        System.out.printf("Diskon       : %s%n", Film.formatRupiah(diskon));
        System.out.printf("TOTAL BAYAR  : %s%n", Film.formatRupiah(total));
        System.out.println("=====================================");
    }

    static void simulasiPemutaran() {
        if (jumlah == 0) {
            System.out.println("\nBelum ada film terdaftar.");
            return;
        }
        System.out.println("\n--- Simulasi Pemutaran Film (Dynamic Binding) ---");
        System.out.println("Semua film disimpan di array bertipe Film, perilakunya ditentukan saat runtime.\n");
        for (int i = 0; i < jumlah; i++) {
            simulasiPutar(daftar[i]);
        }
    }

    static void simulasiPutar(Film film) {
        System.out.printf("Film         : %s%n", film.getJudul());
        System.out.printf("Tipe objek   : %s%n", film.getClass().getSimpleName());
        System.out.printf("Fasilitas    : %s%n", film.getFasilitas());
        System.out.printf("Harga tiket  : %s%n", Film.formatRupiah(film.hitungHargaTiket()));
        System.out.println("-------------------------------------------");
    }

    static void isiDataAwal() {
        daftar[jumlah++] = new FilmReguler("Langit Terakhir", "Drama", 110, 35000, 2);
        daftar[jumlah++] = new FilmReguler("Tawa di Ujung Senja", "Komedi", 95, 35000, 4);
        daftar[jumlah++] = new Film3D("Petualangan Rimba Biru", "Animasi", 100, 40000, 5000);
        daftar[jumlah++] = new FilmIMAX("Badai Samudra", "Aksi", 150, 50000, 25000, "26 x 19 m");
        daftar[jumlah++] = new FilmVIP("Malam Premiere", "Romantis", 120, 50000, 30000, "Recliner");
    }

    static String bacaTeks(String prompt) {
        System.out.print(prompt);
        return input.nextLine();
    }

    static int bacaInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka bulat!");
            }
        }
    }

    static double bacaDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(input.nextLine().trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka!");
            }
        }
    }
}
