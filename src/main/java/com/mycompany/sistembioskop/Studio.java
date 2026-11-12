package com.mycompany.sistembioskop;

import java.io.Serializable;

public class Studio implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nama;
    private int kapasitasKursi;

    public Studio(String nama, int kapasitasKursi) throws DataTidakValidException {
        this.setNama(nama);
        this.setKapasitasKursi(kapasitasKursi);
    }

    public String getNama() { return nama; }
    public int getKapasitasKursi() { return kapasitasKursi; }

    public void setNama(String nama) throws DataTidakValidException {
        if (nama == null || nama.trim().isEmpty()) {
            throw new DataTidakValidException("Nama studio tidak boleh kosong.");
        }
        this.nama = nama.trim();
    }

    public void setKapasitasKursi(int kapasitasKursi) throws DataTidakValidException {
        if (kapasitasKursi <= 0) {
            throw new DataTidakValidException("Kapasitas kursi harus lebih dari 0.");
        }
        this.kapasitasKursi = kapasitasKursi;
    }

    public void tampilkanInfo() {
        System.out.printf("-> [STUDIO] %-18s | kapasitas %d kursi%n", nama, kapasitasKursi);
    }
}
