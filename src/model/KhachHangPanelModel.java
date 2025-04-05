/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ADMIN
 */
public class KhachHangPanelModel {
    private int id;
    private String ma;
    private String ten;
    private String soDienThoai;

    public KhachHangPanelModel() {
    }
    
    public KhachHangPanelModel(String ten, String soDienThoai) {
        this.ten = ten;
        this.soDienThoai = soDienThoai;
    }

    public KhachHangPanelModel(int id, String ma, String ten, String soDienThoai) {
        this.id = id;
        this.ma = ma;
        this.ten = ten;
        this.soDienThoai = soDienThoai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
    
    public Object[] toDataRow(int stt) {
        return new Object[] {stt, getMa(), getTen(), getSoDienThoai()};
    }
}
