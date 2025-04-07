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
import model.VoucherPanelModel;

/**
 *
 * @author ADMIN
 */
public class VoucherPanelRepo {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    
    public VoucherPanelRepo() {
        try {
//            conn = DBContext.DBContext.getConnection();
                conn = Dbconnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<VoucherPanelModel> getAll(String search, String loai) {
        ArrayList<VoucherPanelModel> list = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder("""
                                              SELECT id, maVoucher, tenVoucher, loaiVoucher, giaTri FROM Voucher
                                              WHERE (GETDATE() BETWEEN ngayBatDau AND ngayKetThuc) AND (maVoucher LIKE ? OR tenVoucher LIKE ?)
                                              """);
        if (!loai.equals("Loại Voucher")) {
            sql.append(" AND loaiVoucher = N'").append(loai).append("'");
        }
        try {
            ps = conn.prepareStatement(sql.toString());
            
            ps.setString(1, "%" + search + "%");
            ps.setString(2, "%" + search + "%");
            
            rs = ps.executeQuery();
            
            while (rs.next()) {                
                list.add(new VoucherPanelModel(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5)));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    

    
    public int suaVoucherHoaDon(int idVoucher, int idHoaDon) {
        
        try {
            ps = conn.prepareStatement("UPDATE HoaDon SET idVoucher = ? WHERE id = ?");
            
            ps.setInt(1, idVoucher);
            ps.setInt(2, idHoaDon);
            
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
