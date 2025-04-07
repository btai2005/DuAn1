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
import model.HoaDonChiTietModel;

/**
 *
 * @author ADMIN
 */
public class HoaDonChiTietRepo {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public HoaDonChiTietRepo() {
        this.conn = Dbconnection.getConnection();
    }
    
    public ArrayList<HoaDonChiTietModel> getHdct(int id) {
    ArrayList<HoaDonChiTietModel> list = new ArrayList<>();

    String sql = ("""
                  select hdct.id, spct.maSanPhamChiTiet, kh.hoTen, kh.soDienThoai, hdct.ngayTao, 
                      			hd.loaiThanhToan,hdct.trangThai,hd.tongTien,
                      			sp.TenLoaiSanPham tenGiay, spct.giaSanPham, hdct.soLuong,
                      			spct.giaSanPham * hdct.soLuong thanhTien 
                  from HoaDonChiTiet hdct
                      	LEFT JOIN SanPhamChiTiet spct ON hdct.idSanPhamChiTiet = spct.id
                      	LEFT JOIN LoaiSanPham sp ON spct.ID = sp.id
                      	LEFT JOIN HoaDon hd ON hdct.idHoaDon = hd.id
                      	LEFT JOIN KhachHang kh ON hd.idKhachHang = kh.id
                  where hd.ID = ?
                  ORDER BY hdct.ngayTao DESC
                  """);

    try {
        ps = conn.prepareStatement(sql);
        ps.setInt(1, id); // Sử dụng biến kiểu int trong truy vấn

        rs = ps.executeQuery();

        while (rs.next()) {
            list.add(new HoaDonChiTietModel(rs.getInt(1), rs.getString(2), rs.getString(3),
                                             rs.getString(4), rs.getString(5), rs.getString(6),
                                             rs.getInt(7), rs.getInt(8), rs.getString(9), rs.getInt(10),
                                             rs.getInt(11), rs.getInt(12)));
        }
        return list;

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

    
    public int UpdateSL(String ma,int soLuong) {
        
        ArrayList<HoaDonChiTietModel> list = new ArrayList<>();
        
        String sql = ("""
        update SanPhamChiTiet set SoLuong=SoLuong - ? where ID = ? """);
        
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, ma); // Sử dụng biến kiểu int trong truy vấn
            ps.setInt(2,soLuong);
            return ps.executeUpdate();
            
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
     public ArrayList<HoaDonChiTietModel> getIdHDCT() {
        
        ArrayList<HoaDonChiTietModel> list = new ArrayList<>();
        
        String sql = ("""
        select hdct.id, spct.maSanPhamChiTiet,kh.hoTen,kh.soDienThoai,hd.ngayTao, hd.loaiThanhToan,spct.trangThai,hd.tongTien from HoaDonChiTiet hdct
                LEFT JOIN HoaDon hd ON hdct.idHoaDon = hd.id
                LEFT JOIN KhachHang kh ON hd.idKhachHang = kh.id
                where spct.trangThai = 0""");
        
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {                
                list.add(new HoaDonChiTietModel(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)
                        , rs.getString(6), rs.getInt(7), rs.getInt(8)));     
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        return list;
    }
}
