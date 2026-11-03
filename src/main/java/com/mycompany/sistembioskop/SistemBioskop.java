package com.mycompany.sistembioskop;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class SistemBioskop {
    private static Scanner input = new Scanner(System.in).useLocale(Locale.US);
    private static Bioskop bioskop;

    public static void main(String[] args) {
        try {
            bioskop = new Bioskop("CineTix Lampung");
            isiDataAwal();
        } catch (DataTidakValidException e) {
            System.out.println("[ERROR] Gagal menyiapkan bioskop: " + e.getMessage());
            return;
        }
        bioskop.bukaBioskop();

        int pilihan = 0;

        do {
            try {
                tampilkanMenu();
                pilihan = bacaInt("Pilih menu [1-9]: ");

                switch (pilihan) {
                    case 1:
                        tambahFilm();
                        break;
                    case 2:
                        bioskop.lihatSemuaFilm();
                        break;
                    case 3:
                        menuPencarian();
                        break;
                    case 4:
                        transaksiTiket();
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
                        bioskop.bukaBioskop();
                        break;
                    case 9:
                        System.out.println("\nTerima kasih telah menggunakan Sistem Bioskop!");
                        break;
                    default:
                        throw new DataTidakValidException("Pilihan menu tidak valid, pilih angka 1 - 9.");
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
        } while (pilihan != 9);

        input.close();
    }

    static void tampilkanMenu() {
        System.out.println("\n==========================================");
        System.out.println("   " + bioskop.getNama().toUpperCase() + " - MENU UTAMA");
        System.out.println("==========================================");
        System.out.println(" 1. Tambah Film Baru");
        System.out.println(" 2. Tampilkan Daftar Film");
        System.out.println(" 3. Cari Film");
        System.out.println(" 4. Transaksi Tiket Member");
        System.out.println(" 5. Simulasi Pemutaran Film");
        System.out.println(" 6. Layanan Film (Kursi dan Rating)");
        System.out.println(" 7. Kelola Member");
        System.out.println(" 8. Info Bioskop dan Studio");
        System.out.println(" 9. Keluar");
        System.out.println("------------------------------------------");
        System.out.println(" Total film     : " + bioskop.getJumlahFilm());
        System.out.println(" Total member   : " + bioskop.getJumlahMember());
        System.out.println(" Tiket terjual  : " + TransaksiTiket.getTotalTiketTerjual());
        System.out.println(" Pendapatan     : " + Film.formatRupiah(TransaksiTiket.getTotalPendapatan()));
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
        bioskop.tambahFilm(baru);
        System.out.println("\n[OK] Film \"" + baru.getJudul() + "\" berhasil ditambahkan.");
    }

    static void menuPencarian() throws DataTidakValidException, DataTidakDitemukanException {
        System.out.println("\n--- Cari Film ---");
        System.out.println(" 1. Berdasarkan judul");
        System.out.println(" 2. Berdasarkan genre dan durasi maksimal");
        int mode = bacaInt("Pilih [1-2]: ");

        if (mode == 1) {
            String kata = bacaTeks("Kata kunci judul: ");
            bioskop.cariFilm(kata);
        } else if (mode == 2) {
            String genre = bacaTeks("Genre: ");
            int maks = bacaInt("Durasi maksimal (menit): ");
            bioskop.cariFilm(genre, maks);
        } else {
            throw new DataTidakValidException("Pilihan pencarian harus 1 atau 2.");
        }
    }

    static void transaksiTiket() throws DataTidakValidException, DataTidakDitemukanException {
        if (bioskop.getJumlahFilm() == 0) {
            System.out.println("\nBelum ada film terdaftar.");
            return;
        }
        System.out.println("\n--- Transaksi Tiket Member ---");
        String idMember = bacaTeks("Masukkan ID member: ");
        Member pembeli = bioskop.getMember(idMember);
        System.out.println("Selamat datang, " + pembeli.getNama() + " (poin: " + pembeli.getPoin() + ")");

        bioskop.lihatSemuaFilm();
        int nomor = bacaInt("\nPilih nomor film: ");
        Film dipilih = bioskop.getFilm(nomor);

        int banyak = bacaInt("Jumlah tiket (1-10): ");
        if (banyak < 1 || banyak > 10) {
            throw new DataTidakValidException("Jumlah tiket harus antara 1 - 10.");
        }

        String kodePromo = bacaTeks("Kode promo (kosongkan jika tidak ada): ");

        if (kodePromo.trim().isEmpty()) {
            TransaksiTiket.prosesBeli(pembeli, dipilih, banyak);
        } else {
            TransaksiTiket.prosesBeli(pembeli, dipilih, banyak, kodePromo);
        }
    }

    static void simulasiPemutaran() throws DataTidakValidException, DataTidakDitemukanException {
        if (bioskop.getJumlahFilm() == 0) {
            System.out.println("\nBelum ada film terdaftar.");
            return;
        }
        System.out.println("\n--- Simulasi Pemutaran Film (Abstract Method dan Interface) ---");
        System.out.println("Semua film disimpan di ArrayList bertipe Film, perilakunya ditentukan saat runtime.\n");
        for (int i = 1; i <= bioskop.getJumlahFilm(); i++) {
            Film film = bioskop.getFilm(i);
            simulasiPutar(film);
            cekLayanan(film);
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
        if (bioskop.getJumlahFilm() == 0) {
            System.out.println("\nBelum ada film terdaftar.");
            return;
        }
        bioskop.lihatSemuaFilm();
        int nomor = bacaInt("\nPilih nomor film: ");
        Film film = bioskop.getFilm(nomor);

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
                String id = bacaTeks("ID member (contoh MBR004): ");
                String nama = bacaTeks("Nama member            : ");
                Member baru = new Member(id, nama);
                bioskop.tambahMember(baru);
                System.out.println("\n[OK] Member " + baru.getId() + " (" + baru.getNama() + ") berhasil ditambahkan.");
                break;
            case 2:
                String cariId = bacaTeks("Masukkan ID member: ");
                bioskop.cariMember(cariId);
                break;
            case 3:
                bioskop.lihatSemuaMember();
                break;
            default:
                throw new DataTidakValidException("Pilihan menu member harus antara 1 - 3.");
        }
    }

    static void isiDataAwal() throws DataTidakValidException {
        bioskop.tambahFilm(new FilmReguler("Langit Terakhir", "Drama", 110, 35000, 2));
        bioskop.tambahFilm(new FilmReguler("Tawa di Ujung Senja", "Komedi", 95, 35000, 4));
        bioskop.tambahFilm(new Film3D("Petualangan Rimba Biru", "Animasi", 100, 40000, 5000));
        bioskop.tambahFilm(new FilmIMAX("Badai Samudra", "Aksi", 150, 50000, 25000, "26 x 19 m"));
        bioskop.tambahFilm(new FilmVIP("Malam Premiere", "Romantis", 120, 50000, 30000, "Recliner"));

        bioskop.tambahMember(new Member("MBR001", "Budi Santoso"));
        bioskop.tambahMember(new Member("MBR002", "Siti Aminah"));
        bioskop.tambahMember(new Member("MBR003", "Rizky Pratama"));
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
