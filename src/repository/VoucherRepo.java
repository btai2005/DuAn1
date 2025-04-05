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
import model.VoucherModel;

/**
 *
 * @author ADMIN
 */
public class VoucherRepo {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
     
    public VoucherRepo(){
         try {
             conn = Dbconnection.getConnection();
         } catch (Exception e) {
             e.printStackTrace();
         }
     }

    public ArrayList<VoucherModel> getAllVoucher(String keyword, String loaiVoucher, int trangThai) {
        ArrayList<VoucherModel> list = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder(
                "SELECT maVoucher, tenVoucher, loaiVoucher, ngayBatDau, ngayKetThuc, giaTri, ngayTao, ngaySua, trangThai "
                        + "FROM Voucher WHERE (maVoucher LIKE '%" + keyword + "%' OR tenVoucher LIKE '%" + keyword + "%')");
        
        if (!loaiVoucher.equals("Tất Cả")) {
            sql.append(" AND loaiVoucher = N'").append(loaiVoucher).append("'");
        }
        
        if (trangThai != 0) {
            sql.append(" AND trangThai = ").append(trangThai - 1);
        }
        
        try {
            
            ps = conn.prepareStatement(sql.toString());
            
            rs = ps.executeQuery();
            
            while (rs.next()) {                
                list.add(new VoucherModel(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getString(7), rs.getString(8), rs.getInt(9)));
            }
            
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public int themVoucher(VoucherModel m) {
        try {
            
            ps = conn.prepareStatement("""
                                       INSERT INTO Voucher (maVoucher, tenVoucher, loaiVoucher, ngayBatDau, ngayKetThuc, giaTri, ngayTao, ngaySua, trangThai)
                                       VALUES (?, ?, ?, ?, ?, ?, GETDATE(), GETDATE(), ?)""");
            
            ps.setString(1, m.getMaVoucher());
            ps.setString(2, m.getTenVoucher());
            ps.setString(3, m.getLoaiVoucher());
            ps.setString(4, m.getNgayBatDau());
            ps.setString(5, m.getNgayKetThuc());
            ps.setDouble(6, m.getGiaTri());
            ps.setInt(7, m.getTrangThai());
            
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int capNhatVoucher(VoucherModel m) {
        try {
            
            ps = conn.prepareStatement("""
                                       UPDATE Voucher SET tenVoucher = ?, loaiVoucher = ?, ngayBatDau = ?, ngayKetThuc = ?, giaTri = ?, ngaySua = GETDATE(), trangThai = ?
                                                WHERE maVoucher = ?""");
            
            ps.setString(1, m.getTenVoucher());
            ps.setString(2, m.getLoaiVoucher());
            ps.setString(3, m.getNgayBatDau());
            ps.setString(4, m.getNgayKetThuc());
            ps.setDouble(5, m.getGiaTri());
            ps.setInt(6, m.getTrangThai());
            ps.setString(7, m.getMaVoucher());
            
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public VoucherModel getIdVoucher(int id) {
        VoucherModel model = new VoucherModel();
        
        try {
            
            ps = conn.prepareStatement("""
                                       SELECT maVoucher, tenVoucher, loaiVoucher, ngayBatDau, ngayKetThuc, giaTri, ngayTao, ngaySua, trangThai "
                                                               + "FROM Voucher WHERE id = ?""");
            
            ps.setInt(1, id);
            
            rs = ps.executeQuery();
            
            while (rs.next()) { 
                
                model = new VoucherModel(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getString(7), rs.getString(8), rs.getInt(9));
            }
            return model;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
