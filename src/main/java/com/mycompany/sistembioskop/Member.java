package com.mycompany.sistembioskop;

import java.io.Serializable;

public class Member implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String nama;
    private int poin;

    public Member(String id, String nama) throws DataTidakValidException {
        this.setId(id);
        this.setNama(nama);
        this.poin = 0;
    }

    public String getId() { return id; }
    public String getNama() { return nama; }
    public int getPoin() { return poin; }

    public void setId(String id) throws DataTidakValidException {
        if (id == null || id.trim().isEmpty()) {
            throw new DataTidakValidException("ID member tidak boleh kosong.");
        }
        this.id = id.trim().toUpperCase();
    }

    public void setNama(String nama) throws DataTidakValidException {
        if (nama == null || nama.trim().isEmpty()) {
            throw new DataTidakValidException("Nama member tidak boleh kosong.");
        }
        this.nama = nama.trim();
    }

    public void tambahPoin(int tambahan) {
        this.poin += tambahan;
    }
}
