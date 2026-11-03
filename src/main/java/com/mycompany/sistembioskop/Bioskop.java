package com.mycompany.sistembioskop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Bioskop {
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
