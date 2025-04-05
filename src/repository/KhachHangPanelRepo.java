/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import Utils.Dbconnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.KhachHangPanelModel;

/**
 *
 * @author ADMIN
 */
public class KhachHangPanelRepo {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    
    public KhachHangPanelRepo() {
        try {
            conn = Dbconnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<KhachHangPanelModel> getAll(String search) {
        ArrayList<KhachHangPanelModel> list = new ArrayList<>();
        
        try {
            ps = conn.prepareStatement("SELECT id, maKhachHang, hoTen, soDienThoai FROM KhachHang"
                                        + " WHERE maKhachHang LIKE ? OR hoTen LIKE ? OR soDienThoai LIKE ?");
            
            ps.setString(1, "%" + search + "%");
            ps.setString(2, "%" + search + "%");
            ps.setString(3, "%" + search + "%");
            
            rs = ps.executeQuery();
            
            while (rs.next()) {                
                list.add(new KhachHangPanelModel(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public int them(KhachHangPanelModel m, String ma) {
        
        try {
            ps = conn.prepareStatement("INSERT INTO KhachHang (maKhachHang, hoTen, soDienThoai, trangThai, ngayTao) VALUES (? , ?, ?, 0, GETDATE())");
            
            ps.setString(1, ma);
            ps.setString(2, m.getTen());
            ps.setString(3, m.getSoDienThoai());
            
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int suaKhachHangHoaDon(int idKh, int idHoaDon) {
        
        try {
            ps = conn.prepareStatement("UPDATE HoaDon SET idKhachHang = ? WHERE id = ?");
            
            ps.setInt(1, idKh);
            ps.setInt(2, idHoaDon);
            
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int stt() {
        try {
            ps = conn.prepareStatement("SELECT * FROM KhachHang");
            
            rs = ps.executeQuery();
            
            int index = 0;
            
            while (rs.next()) {                
                index++;
            }
            return index + 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public String createMaKhachHang() {
        String ma;
        
        if (stt() < 10) {
            ma = "KH00" + String.valueOf(stt());
        } else if (stt() < 100) {
            ma = "KH0" + String.valueOf(stt());
        }else {
            ma = "KH" + String.valueOf(stt());
        }
        return ma;
    }
}
