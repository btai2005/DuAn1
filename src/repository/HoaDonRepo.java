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
import model.HoaDonModel;

/**
 *
 * @author ADMIN
 */
public class HoaDonRepo {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public HoaDonRepo() {
        try {
            conn = Dbconnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<HoaDonModel> getAllHoaDon(String search, int trangThai, String bd, String kt) {
    ArrayList<HoaDonModel> list = new ArrayList<>();

    StringBuilder sql = new StringBuilder("""
        SELECT hd.id, maHoaDon, nv.hoTen, kh.hoTen, kh.soDienThoai,
               hd.ngayTao, loaiThanhToan, tongTien, vc.giaTri, hd.trangThai, hd.voucherGiam 
        FROM HoaDon hd
        LEFT JOIN NhanVien nv ON hd.idNhanVien = nv.id
        LEFT JOIN KhachHang kh ON hd.idKhachHang = kh.id
        LEFT JOIN Voucher vc ON hd.idVoucher = vc.id
        WHERE (hd.maHoaDon LIKE ? OR kh.hoTen LIKE ? OR nv.hoTen LIKE ?)
    """);

    if (trangThai != 0) {
        sql.append(" AND hd.trangThai = ").append(trangThai - 1);
    } 
    if (!bd.equals("abc") || !kt.equals("abc")) {
        sql.append(" AND hd.ngayTao BETWEEN '").append(bd).append("' AND '").append(kt).append("' ");
    }

    sql.append(" ORDER BY hd.ngayTao DESC"); 

    try {
        ps = conn.prepareStatement(sql.toString());
        ps.setString(1, "%" + search + "%");
        ps.setString(2, "%" + search + "%");
        ps.setString(3, "%" + search + "%");

        rs = ps.executeQuery();

        while (rs.next()) {
            list.add(new HoaDonModel(
                rs.getInt(1), rs.getString(2), rs.getString(3),
                rs.getString(4), rs.getString(5), rs.getString(6),
                rs.getString(7), rs.getInt(8), rs.getInt(9), rs.getInt(10), rs.getString(11)
            ));
        }
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }

    return list;
}

    
    public ArrayList<HoaDonChiTietModel> getAllHdct(int id) {
        ArrayList<HoaDonChiTietModel> list = new ArrayList<>();
        
        try {
            ps = conn.prepareStatement("""
                                       SELECT
                                                           spct.maSanPhamChiTiet, 
                                                           sp.tenLoaiSanPham + ' ' + CAST(spct.giaSanPham AS NVARCHAR(50)) AS tenGiay, 
                                                           spct.giaSanPham, 
                                                           hdct.soLuong, 
                                                           spct.giaSanPham * hdct.soLuong AS thanhTien 
                                                       FROM HoaDonChiTiet hdct
                                                       LEFT JOIN HoaDon hd ON hdct.idHoaDon = hd.id
                                                       LEFT JOIN SanPhamChiTiet spct ON hdct.idSanPhamChiTiet = spct.id
                                                       LEFT JOIN LoaiSanPham sp ON spct.IdLoaiSanPham = sp.id
                                                       WHERE hd.id = ?""");
            ps.setInt(1, id);
            
            rs = ps.executeQuery();
            
            while (rs.next()) {                
                list.add(new HoaDonChiTietModel(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getInt(5)));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public ArrayList<HoaDonModel> getIdHD() {
        
        ArrayList<HoaDonModel> list = new ArrayList<>();
        
        String sql = """
                     Select hd.id, maHoaDon,nv.hoTen,kh.hoTen,kh.soDienThoai,
                                                             hd.ngayTao,loaiThanhToan,tongTien, vc.tenVoucher, hd.trangThai, vc.loaiVoucher from HoaDon hd
                                                             LEFT JOIN NhanVien nv ON hd.idNhanVien = nv.id
                                                             LEFT JOIN KhachHang kh ON hd.idKhachHang = kh.id
                                                             LEFT JOIN Voucher vc ON hd.idVoucher = vc.id
                     """;
        try {
            
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {                
            list.add(new HoaDonModel(rs.getInt(1), rs.getString(2), rs.getString(3)
                    , rs.getString(4), rs.getString(5), rs.getString(6), 
                    rs.getString(7), rs.getInt(8), rs.getInt(9), rs.getInt(10), rs.getString(11)));           
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        return list;
    }
    public ArrayList<HoaDonModel> getIdHDon(String ma) {
        
        ArrayList<HoaDonModel> list = new ArrayList<>();
        
        String sql = """
                                       select hd.id, maHoaDon,nv.hoTen,kh.hoTen,kh.soDienThoai,
                                        hd.ngayTao,loaiThanhToan,tongTien, vc.tenVoucher, hd.trangThai from HoaDon hd
                                        LEFT JOIN NhanVien nv ON hd.idNhanVien = nv.id
                                        LEFT JOIN KhachHang kh ON hd.idKhachHang = kh.id
                                        LEFT JOIN Voucher vc ON hd.idVoucher = vc.id
                                        where maHoaDon = ?""";
        try {
            
            ps = conn.prepareStatement(sql);
            ps.setString(1, ma);
            rs = ps.executeQuery();
            
            while (rs.next()) {                
            list.add(new HoaDonModel(rs.getInt(1), rs.getString(2), rs.getString(3)
                    , rs.getString(4), rs.getString(5), rs.getString(6), 
                    rs.getString(7), rs.getInt(8), rs.getString(9), rs.getInt(10)));           
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        return list;
    }
}
