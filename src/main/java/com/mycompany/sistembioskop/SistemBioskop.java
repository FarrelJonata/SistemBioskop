package com.mycompany.sistembioskop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class SistemBioskop {
    private static Scanner input = new Scanner(System.in).useLocale(Locale.US);
    private static ArrayList<Film> daftar = new ArrayList<>();
    private static HashMap<String, String> dataMember = new HashMap<>();

    private static int totalTiketTerjual = 0;
    private static double totalPendapatan = 0;

    public static void main(String[] args) {
        isiDataAwal();
        int pilihan = 0;

        do {
            try {
                tampilkanMenu();
                pilihan = bacaInt("Pilih menu [1-8]: ");

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
                        layananFilm();
                        break;
                    case 7:
                        kelolaMember();
                        break;
                    case 8:
                        System.out.println("\nTerima kasih telah menggunakan Sistem Bioskop!");
                        break;
                    default:
                        throw new DataTidakValidException("Pilihan menu tidak valid, pilih angka 1 - 8.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\n[ERROR] Input harus berupa angka! Silakan coba lagi.");
                input.nextLine();
                pilihan = 0;
            } catch (DataTidakValidException e) {
                System.out.println("\n[ERROR] Data tidak valid: " + e.getMessage());
            } catch (DataTidakDitemukanException e) {
                System.out.println("\n[ERROR] Data tidak ditemukan: " + e.getMessage());
            } finally {
                System.out.println("[FINALLY] Satu proses menu selesai dijalankan.");
            }
        } while (pilihan != 8);

        input.close();
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
        System.out.println(" 6. Layanan Film (Kursi dan Rating)");
        System.out.println(" 7. Kelola Member");
        System.out.println(" 8. Keluar");
        System.out.println("------------------------------------------");
        System.out.println(" Total film     : " + daftar.size());
        System.out.println(" Total member   : " + dataMember.size());
        System.out.println(" Tiket terjual  : " + totalTiketTerjual);
        System.out.println(" Pendapatan     : " + Film.formatRupiah(totalPendapatan));
        System.out.println("==========================================");
    }

    static void tambahFilm() throws DataTidakValidException {
        System.out.println("\n--- Tambah Film Baru ---");
        System.out.println("Jenis film:");
        System.out.println(" 1. Reguler");
        System.out.println(" 2. 3D");
        System.out.println(" 3. IMAX");
        System.out.println(" 4. VIP");
        int jenis = bacaInt("Pilih jenis [1-4]: ");

        if (jenis < 1 || jenis > 4) {
            throw new DataTidakValidException("Jenis film harus antara 1 - 4.");
        }

        String judul = bacaTeks("Judul film   : ");
        String genre = bacaTeks("Genre        : ");
        int durasi = bacaInt("Durasi (menit): ");
        double harga = bacaDouble("Harga dasar   : ");

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
        daftar.add(baru);
        System.out.println("\n[OK] Film \"" + baru.getJudul() + "\" berhasil ditambahkan.");
    }

    static void tampilkanSemua() {
        if (daftar.isEmpty()) {
            System.out.println("\nBelum ada film terdaftar.");
            return;
        }
        cetakHeader();
        for (int i = 0; i < daftar.size(); i++) {
            System.out.printf("%-3d| ", i + 1);
            daftar.get(i).tampilkanInfo();
        }
        cetakGaris();
        System.out.println("Total: " + daftar.size() + " film");
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

    static void menuPencarian() throws DataTidakValidException, DataTidakDitemukanException {
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
            throw new DataTidakValidException("Pilihan pencarian harus 1 atau 2.");
        }
    }

    static void cariFilm(String kataKunci) throws DataTidakDitemukanException {
        boolean ketemu = false;
        for (int i = 0; i < daftar.size(); i++) {
            Film film = daftar.get(i);
            if (film.getJudul().toLowerCase().contains(kataKunci.toLowerCase())) {
                if (!ketemu) cetakHeader();
                System.out.printf("%-3d| ", i + 1);
                film.tampilkanInfo();
                ketemu = true;
            }
        }
        if (!ketemu) {
            throw new DataTidakDitemukanException("Film dengan judul \"" + kataKunci + "\" tidak ada.");
        }
        cetakGaris();
    }

    static void cariFilm(String genre, int durasiMaks) throws DataTidakDitemukanException {
        boolean ketemu = false;
        for (int i = 0; i < daftar.size(); i++) {
            Film film = daftar.get(i);
            if (film.getGenre().equalsIgnoreCase(genre.trim())
                    && film.getDurasiMenit() <= durasiMaks) {
                if (!ketemu) cetakHeader();
                System.out.printf("%-3d| ", i + 1);
                film.tampilkanInfo();
                ketemu = true;
            }
        }
        if (!ketemu) {
            throw new DataTidakDitemukanException("Tidak ada film genre " + genre
                    + " dengan durasi maksimal " + durasiMaks + " menit.");
        }
        cetakGaris();
    }

    static Film ambilFilm(int nomor) throws DataTidakDitemukanException {
        if (nomor < 1 || nomor > daftar.size()) {
            throw new DataTidakDitemukanException("Film nomor " + nomor + " tidak ada di daftar.");
        }
        return daftar.get(nomor - 1);
    }

    static void beliTiket() throws DataTidakValidException, DataTidakDitemukanException {
        if (daftar.isEmpty()) {
            System.out.println("\nBelum ada film terdaftar.");
            return;
        }
        tampilkanSemua();
        int nomor = bacaInt("\nPilih nomor film: ");
        Film dipilih = ambilFilm(nomor);

        int banyak = bacaInt("Jumlah tiket (1-10): ");
        if (banyak < 1 || banyak > 10) {
            throw new DataTidakValidException("Jumlah tiket harus antara 1 - 10.");
        }

        String kodePromo = bacaTeks("Kode promo (kosongkan jika tidak ada): ");

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

    static void simulasiPemutaran() throws DataTidakValidException {
        if (daftar.isEmpty()) {
            System.out.println("\nBelum ada film terdaftar.");
            return;
        }
        System.out.println("\n--- Simulasi Pemutaran Film (Abstract Method dan Interface) ---");
        System.out.println("Semua film disimpan di ArrayList bertipe Film, perilakunya ditentukan saat runtime.\n");
        for (int i = 0; i < daftar.size(); i++) {
            simulasiPutar(daftar.get(i));
            cekLayanan(daftar.get(i));
            System.out.println("-------------------------------------------");
        }
    }

    static void simulasiPutar(Film film) {
        System.out.printf("Film         : %s%n", film.getJudul());
        System.out.printf("Tipe objek   : %s%n", film.getClass().getSimpleName());
        System.out.printf("Fasilitas    : %s%n", film.getFasilitas());
        System.out.printf("Harga tiket  : %s%n", Film.formatRupiah(film.hitungHargaTiket()));
        film.tampilkanAturanTayang();
    }

    static void cekLayanan(Film film) throws DataTidakValidException {
        prosesReservasi(film, "A1");
        prosesRating(film, 5);
    }

    static void prosesReservasi(Film film, String nomorKursi) throws DataTidakValidException {
        if (film instanceof DapatDipesan) {
            if (nomorKursi == null || nomorKursi.trim().isEmpty()) {
                throw new DataTidakValidException("Nomor kursi tidak boleh kosong.");
            }
            DapatDipesan layanan = (DapatDipesan) film;
            layanan.pesanKursi(nomorKursi.trim().toUpperCase());
        } else {
            System.out.println("-> [RESERVASI] " + film.getJudul() + " (" + film.getJenis()
                    + ") tidak mendukung reservasi kursi, kursi bebas.");
        }
    }

    static void prosesRating(Film film, int bintang) throws DataTidakValidException {
        if (film instanceof DapatDinilai) {
            if (bintang < 1 || bintang > 5) {
                throw new DataTidakValidException("Rating harus antara 1 - 5 bintang.");
            }
            DapatDinilai layanan = (DapatDinilai) film;
            layanan.beriRating(bintang);
        } else {
            System.out.println("-> [RATING] " + film.getJudul() + " (" + film.getJenis()
                    + ") tidak menerima rating penonton.");
        }
    }

    static void layananFilm() throws DataTidakValidException, DataTidakDitemukanException {
        if (daftar.isEmpty()) {
            System.out.println("\nBelum ada film terdaftar.");
            return;
        }
        tampilkanSemua();
        int nomor = bacaInt("\nPilih nomor film: ");
        Film film = ambilFilm(nomor);

        System.out.println("\nLayanan untuk: " + film.getJudul() + " [" + film.getJenis() + "]");
        System.out.println(" 1. Pesan kursi");
        System.out.println(" 2. Beri rating");
        System.out.println(" 3. Lihat aturan tayang");
        int layanan = bacaInt("Pilih [1-3]: ");

        switch (layanan) {
            case 1:
                String kursi = bacaTeks("Nomor kursi yang diinginkan (contoh A5): ");
                prosesReservasi(film, kursi);
                break;
            case 2:
                int bintang = bacaInt("Rating (1-5 bintang): ");
                prosesRating(film, bintang);
                break;
            case 3:
                film.tampilkanAturanTayang();
                break;
            default:
                throw new DataTidakValidException("Pilihan layanan harus antara 1 - 3.");
        }
    }

    static void kelolaMember() throws DataTidakValidException, DataTidakDitemukanException {
        System.out.println("\n--- Kelola Member ---");
        System.out.println(" 1. Tambah member baru");
        System.out.println(" 2. Cari member");
        System.out.println(" 3. Tampilkan semua member");
        int opsi = bacaInt("Pilih [1-3]: ");

        switch (opsi) {
            case 1:
                tambahMember();
                break;
            case 2:
                cariMember();
                break;
            case 3:
                tampilkanMember();
                break;
            default:
                throw new DataTidakValidException("Pilihan menu member harus antara 1 - 3.");
        }
    }

    static void tambahMember() throws DataTidakValidException {
        String id = bacaTeks("ID member (contoh MBR004): ").trim().toUpperCase();
        String nama = bacaTeks("Nama member            : ").trim();

        if (id.isEmpty() || nama.isEmpty()) {
            throw new DataTidakValidException("ID dan nama member tidak boleh kosong.");
        }
        if (dataMember.containsKey(id)) {
            throw new DataTidakValidException("ID member " + id + " sudah terdaftar.");
        }
        dataMember.put(id, nama);
        System.out.println("\n[OK] Member " + id + " (" + nama + ") berhasil ditambahkan.");
    }

    static void cariMember() throws DataTidakDitemukanException {
        String id = bacaTeks("Masukkan ID member: ").trim().toUpperCase();

        if (!dataMember.containsKey(id)) {
            throw new DataTidakDitemukanException("Member dengan ID " + id + " tidak terdaftar.");
        }
        System.out.println("\nMember ditemukan -> ID: " + id + " | Nama: " + dataMember.get(id));
    }

    static void tampilkanMember() {
        if (dataMember.isEmpty()) {
            System.out.println("\nBelum ada member terdaftar.");
            return;
        }
        System.out.println("\n--- Daftar Member (Total: " + dataMember.size() + ") ---");
        ArrayList<String> urutanId = new ArrayList<>(dataMember.keySet());
        Collections.sort(urutanId);
        int nomor = 1;
        for (String id : urutanId) {
            System.out.printf("%-3d| %-8s | %s%n", nomor++, id, dataMember.get(id));
        }
    }

    static void isiDataAwal() {
        try {
            daftar.add(new FilmReguler("Langit Terakhir", "Drama", 110, 35000, 2));
            daftar.add(new FilmReguler("Tawa di Ujung Senja", "Komedi", 95, 35000, 4));
            daftar.add(new Film3D("Petualangan Rimba Biru", "Animasi", 100, 40000, 5000));
            daftar.add(new FilmIMAX("Badai Samudra", "Aksi", 150, 50000, 25000, "26 x 19 m"));
            daftar.add(new FilmVIP("Malam Premiere", "Romantis", 120, 50000, 30000, "Recliner"));
        } catch (DataTidakValidException e) {
            System.out.println("[ERROR] Gagal memuat data film awal: " + e.getMessage());
        }

        dataMember.put("MBR001", "Budi Santoso");
        dataMember.put("MBR002", "Siti Aminah");
        dataMember.put("MBR003", "Rizky Pratama");
    }

    static String bacaTeks(String prompt) {
        System.out.print(prompt);
        return input.nextLine();
    }

    static int bacaInt(String prompt) {
        System.out.print(prompt);
        int nilai = input.nextInt();
        input.nextLine();
        return nilai;
    }

    static double bacaDouble(String prompt) {
        System.out.print(prompt);
        double nilai = input.nextDouble();
        input.nextLine();
        return nilai;
    }
}
