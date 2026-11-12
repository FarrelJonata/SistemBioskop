package com.mycompany.sistembioskop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class Bioskop {
    public static final String FILE_DATA = "data_bioskop.dat";

    private String nama;
    private Studio studioReguler;
    private Studio studioPremium;
    private ArrayList<Film> daftarFilm;
    private HashMap<String, Member> dataMember;

    public Bioskop(String nama) throws DataTidakValidException {
        if (nama == null || nama.trim().isEmpty()) {
            throw new DataTidakValidException("Nama bioskop tidak boleh kosong.");
        }
        this.nama = nama.trim();
        this.studioReguler = new Studio("Studio Reguler", 80);
        this.studioPremium = new Studio("Studio Premium", 120);
        this.daftarFilm = new ArrayList<>();
        this.dataMember = new HashMap<>();
    }

    public String getNama() { return nama; }
    public int getJumlahFilm() { return daftarFilm.size(); }
    public int getJumlahMember() { return dataMember.size(); }

    public void bukaBioskop() {
        System.out.println("\n========== " + nama.toUpperCase() + " RESMI DIBUKA ==========");
        System.out.println("Komponen internal bioskop (Composition):");
        studioReguler.tampilkanInfo();
        studioPremium.tampilkanInfo();
        System.out.println("Total kapasitas  : "
                + (studioReguler.getKapasitasKursi() + studioPremium.getKapasitasKursi()) + " kursi");
        System.out.println("Film tayang      : " + daftarFilm.size() + " film");
        System.out.println("Member terdaftar : " + dataMember.size() + " orang");
        System.out.println("=============================================");
    }

    public void tambahFilm(Film film) {
        daftarFilm.add(film);
    }

    public Film getFilm(int nomor) throws DataTidakDitemukanException {
        if (nomor < 1 || nomor > daftarFilm.size()) {
            throw new DataTidakDitemukanException("Film nomor " + nomor + " tidak ada di daftar.");
        }
        return daftarFilm.get(nomor - 1);
    }

    public void lihatSemuaFilm() {
        if (daftarFilm.isEmpty()) {
            System.out.println("\nBelum ada film terdaftar.");
            return;
        }
        cetakHeader();
        for (int i = 0; i < daftarFilm.size(); i++) {
            System.out.printf("%-3d| ", i + 1);
            daftarFilm.get(i).tampilkanInfo();
        }
        cetakGaris();
        System.out.println("Total: " + daftarFilm.size() + " film");
    }

    public void cariFilm(String kataKunci) throws DataTidakDitemukanException {
        boolean ketemu = false;
        for (int i = 0; i < daftarFilm.size(); i++) {
            Film film = daftarFilm.get(i);
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

    public void cariFilm(String genre, int durasiMaks) throws DataTidakDitemukanException {
        boolean ketemu = false;
        for (int i = 0; i < daftarFilm.size(); i++) {
            Film film = daftarFilm.get(i);
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

    public void tambahMember(Member member) throws DataTidakValidException {
        if (dataMember.containsKey(member.getId())) {
            throw new DataTidakValidException("ID member " + member.getId() + " sudah terdaftar.");
        }
        dataMember.put(member.getId(), member);
    }

    public Member getMember(String id) throws DataTidakDitemukanException {
        Member member = dataMember.get(id.trim().toUpperCase());
        if (member == null) {
            throw new DataTidakDitemukanException("Member dengan ID " + id.trim().toUpperCase() + " tidak terdaftar.");
        }
        return member;
    }

    public void cariMember(String id) throws DataTidakDitemukanException {
        Member member = getMember(id);
        System.out.println("\nMember ditemukan -> ID: " + member.getId()
                + " | Nama: " + member.getNama() + " | Poin: " + member.getPoin());
    }

    public void lihatSemuaMember() {
        if (dataMember.isEmpty()) {
            System.out.println("\nBelum ada member terdaftar.");
            return;
        }
        System.out.println("\n--- Daftar Member (Total: " + dataMember.size() + ") ---");
        ArrayList<String> urutanId = new ArrayList<>(dataMember.keySet());
        Collections.sort(urutanId);
        int nomor = 1;
        for (String id : urutanId) {
            Member m = dataMember.get(id);
            System.out.printf("%-3d| %-8s | %-18s | %d poin%n", nomor++, m.getId(), m.getNama(), m.getPoin());
        }
    }

    public void simpanDataKeFile() {
        try {
            FileOutputStream fileKeluar = new FileOutputStream(FILE_DATA);
            ObjectOutputStream mesinPembeku = new ObjectOutputStream(fileKeluar);

            mesinPembeku.writeObject(daftarFilm);
            mesinPembeku.writeObject(dataMember);
            mesinPembeku.writeInt(TransaksiTiket.getTotalTiketTerjual());
            mesinPembeku.writeDouble(TransaksiTiket.getTotalPendapatan());

            mesinPembeku.close();
            System.out.println("[FILE] Data berhasil disimpan ke " + FILE_DATA
                    + " (" + daftarFilm.size() + " film, " + dataMember.size() + " member).");
        } catch (IOException e) {
            System.out.println("[FILE] Gagal menyimpan data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public boolean muatDataDariFile() {
        File fileData = new File(FILE_DATA);
        if (!fileData.exists()) {
            System.out.println("[FILE] " + FILE_DATA + " belum ada, tidak ada data yang dimuat.");
            return false;
        }

        try {
            FileInputStream fileMasuk = new FileInputStream(fileData);
            ObjectInputStream mesinPembangkit = new ObjectInputStream(fileMasuk);

            ArrayList<Film> filmDimuat = (ArrayList<Film>) mesinPembangkit.readObject();
            HashMap<String, Member> memberDimuat = (HashMap<String, Member>) mesinPembangkit.readObject();
            int tiketDimuat = mesinPembangkit.readInt();
            double pendapatanDimuat = mesinPembangkit.readDouble();

            mesinPembangkit.close();

            daftarFilm = filmDimuat;
            dataMember = memberDimuat;
            TransaksiTiket.setStatistik(tiketDimuat, pendapatanDimuat);
            System.out.println("[FILE] Data berhasil dimuat dari " + FILE_DATA
                    + " (" + daftarFilm.size() + " film, " + dataMember.size() + " member).");
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[FILE] Gagal memuat data: " + e.getMessage());
            return false;
        }
    }

    public void bacaLogTransaksi() {
        File fileLog = new File(TransaksiTiket.FILE_LOG);
        if (!fileLog.exists()) {
            System.out.println("\n[FILE] Belum ada log transaksi (" + TransaksiTiket.FILE_LOG + " belum dibuat).");
            return;
        }

        try {
            Scanner mesinBaca = new Scanner(fileLog);
            System.out.println("\n--- Isi " + TransaksiTiket.FILE_LOG + " ---");
            int jumlahCatatan = 0;
            while (mesinBaca.hasNextLine()) {
                System.out.println(mesinBaca.nextLine());
                jumlahCatatan++;
            }
            mesinBaca.close();
            System.out.println("--- Total " + jumlahCatatan + " catatan ---");
        } catch (FileNotFoundException e) {
            System.out.println("[FILE] Gagal: file log tidak ditemukan.");
        }
    }

    private static void cetakHeader() {
        System.out.println();
        cetakGaris();
        System.out.printf("%-3s| %-10s | %-26s | %-14s | %-8s | %-11s | %s%n",
                "No", "Jenis", "Judul", "Genre", "Durasi", "Harga Tiket", "Detail");
        cetakGaris();
    }

    private static void cetakGaris() {
        for (int i = 0; i < 112; i++) {
            System.out.print("-");
        }
        System.out.println();
    }
}
