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
            pilihan = bacaInt("Pilih menu [1-5]: ");

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
                    System.out.println("\nTerima kasih telah menggunakan Sistem Bioskop!");
                    break;
                default:
                    System.out.println("Pilihan tidak valid, coba lagi.");
            }
        } while (pilihan != 5);
    }

    static void tampilkanMenu() {
        System.out.println("\n==========================================");
        System.out.println("       SISTEM MANAJEMEN BIOSKOP");
        System.out.println("==========================================");
        System.out.println(" 1. Tambah Film Baru");
        System.out.println(" 2. Tampilkan Daftar Film");
        System.out.println(" 3. Cari Film");
        System.out.println(" 4. Beli Tiket");
        System.out.println(" 5. Keluar");
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
        int jenis = bacaInt("Pilih jenis [1-3]: ");

        if (jenis < 1 || jenis > 3) {
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
            } else {
                double premium = bacaDouble("Biaya premium : ");
                String layar = bacaTeks("Ukuran layar  : ");
                baru = new FilmIMAX(judul, genre, durasi, harga, premium, layar);
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

        Film dipilih = daftar[nomor - 1];
        double hargaSatuan = dipilih.hitungHargaTiket();
        double total = hargaSatuan * banyak;

        totalTiketTerjual += banyak;
        totalPendapatan += total;

        System.out.println("\n========== STRUK PEMBELIAN ==========");
        System.out.printf("Film         : %s%n", dipilih.getJudul());
        System.out.printf("Jenis        : %s%n", dipilih.getJenis());
        System.out.printf("Harga/tiket  : %s%n", Film.formatRupiah(hargaSatuan));
        System.out.printf("Jumlah tiket : %d%n", banyak);
        System.out.printf("TOTAL BAYAR  : %s%n", Film.formatRupiah(total));
        System.out.println("=====================================");
    }

    static void isiDataAwal() {
        daftar[jumlah++] = new FilmReguler("Langit Terakhir", "Drama", 110, 35000, 2);
        daftar[jumlah++] = new FilmReguler("Tawa di Ujung Senja", "Komedi", 95, 35000, 4);
        daftar[jumlah++] = new Film3D("Petualangan Rimba Biru", "Animasi", 100, 40000, 5000);
        daftar[jumlah++] = new Film3D("Galaksi Merah", "Fiksi Ilmiah", 130, 45000, 7000);
        daftar[jumlah++] = new FilmIMAX("Badai Samudra", "Aksi", 150, 50000, 25000, "26 x 19 m");
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
