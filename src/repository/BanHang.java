/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import Utils.Dbconnection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import model.HoaDonChiTietModel;
import model.HoaDonModel;
import model.Model_SanPhamChiTiet;
import model.SanPhamChiTiet;

/**
 *
 * @author ADMIN
 */
public class BanHang {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    
    private int tongTien = 0;
    private int count = 0;
    
    public BanHang() {
        try {
            conn = Dbconnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Model_SanPhamChiTiet getIdMaSpct(String ma) {
        
        for (Model_SanPhamChiTiet x : getAllSanPham()) {
            
            if (x.getMaSanPham().equals(ma)) {
                
                return x;
            }
        }
        return null;
    }
    public int getSoLuongSpct(int soLuong, int idSpct) {
        try {
            ps = conn.prepareStatement("UPDATE SanPhamChiTiet SET soLuong = soLuong + ? WHERE id = ?");
            ps.setInt(1, soLuong);
            ps.setInt(2, idSpct);
            
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public ArrayList<Model_SanPhamChiTiet> getAllSanPham() {
        ArrayList<Model_SanPhamChiTiet> list = new ArrayList<>();
        
        String sql = """
                        SELECT id, maSanPhamChiTiet, soLuong FROM SanPhamChiTiet 
                        WHERE trangThai = 0 AND soLuong >= 0
                     """;
        
        try {
            ps = conn.prepareStatement(sql);
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(new Model_SanPhamChiTiet(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public int updateTrangThaiHdct(int idHoaDonChiTiet) {
        
        try {
            ps = conn.prepareStatement("UPDATE HoaDonChiTiet SET trangThai = 0 WHERE id = ?");
            
            ps.setInt(1, idHoaDonChiTiet);
            
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
     public ArrayList<HoaDonChiTietModel> getAllHoaDon(int id) {
        ArrayList<HoaDonChiTietModel> list = new ArrayList<>();
        
        try {
            ps = conn.prepareStatement("""
                                       SELECT hdct.id, spct.maSanPhamChiTiet, spct.TenSanPhamChiTiet
                                                ,spct.giaSanPham, hdct.soLuong, spct.giaSanPham * hdct.soLuong FROM HoaDonChiTiet hdct
                                                                              LEFT JOIN SanPhamChiTiet spct ON hdct.idSanPhamChiTiet = spct.id
                                                                              LEFT JOIN LoaiSanPham sp ON spct.IdLoaiSanPham = sp.id
                                                                              
                                                                              LEFT JOIN HoaDon hd ON hdct.idHoaDon = hd.id
                                                                              WHERE hd.ID = ? AND hdct.trangThai = 1""");
            
            ps.setInt(1, id);
            
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new HoaDonChiTietModel(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getInt(6)));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public ArrayList<HoaDonModel> getAllHoaDonCho() {
        ArrayList<HoaDonModel> list = new ArrayList<>();
        
        try {
            
            ps = conn.prepareStatement("""
                                       SELECT hd.id, hd.maHoaDon, hd.loaiThanhToan, hd.tongTien, kh.hoTen, kh.soDienThoai
                                                , nv.hoTen, idVoucher, hd.ngayTao, hd.trangThai FROM HoaDon hd
                                       LEFT JOIN KhachHang kh ON hd.idKhachHang = kh.id
                                       LEFT JOIN NhanVien nv ON hd.idNhanVien = nv.id
                                       WHERE hd.trangThai = 1""");
            rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(new HoaDonModel(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getString(9), rs.getInt(10)));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public void fillTableHoaDonCho(JTable table) {
        DefaultTableModel tblModel = (DefaultTableModel) table.getModel();
        tblModel.setRowCount(0);
        
        for (HoaDonModel x : getAllHoaDonCho()) {
            tblModel.addRow(x.toDataRow());
        }
    }
    
    
    
    public ArrayList<HoaDonChiTietModel> getIdSpct (int id) {
        
        ArrayList<HoaDonChiTietModel> list = new ArrayList<>();
        
        String sql = ("""
                      select id, idhoadon from hoadonchitiet
                      where idhoadon = ? and trangthai = 1
                      """);
        
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id); // Sử dụng biến kiểu int trong truy vấn

            rs = ps.executeQuery();
            
            while (rs.next()) {                
                list.add(new HoaDonChiTietModel(rs.getInt(1), rs.getInt(2)));}       
        return list;
        
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<SanPhamChiTiet> getDataTableSp (String search) {
        List<SanPhamChiTiet> list= new ArrayList<>();
        String query = """
                       Select spct.ID, AnhSanPham, TenSanPhamChiTiet, GiaSanPham, SoLuong, kc.Size, ms.TenMau
                       from SanPhamChiTiet spct
                       LEFT JOIN KichCo kc ON kc.ID = spct.IdKichCo
                       LEFT JOIN MauSac ms ON ms.ID = spct.IdMauSac
                       WHERE spct.trangThai = 0 AND soLuong > 0 AND (TenSanPhamChiTiet LIKE ? OR ms.TenMau LIKE ?)
                       """;
        try {
            ps = conn.prepareStatement(query);
            
            ps.setString(1, "%" + search + "%");
            ps.setString(2, "%" + search + "%");

            rs = ps.executeQuery();
            while(rs.next()){
                SanPhamChiTiet cd = new SanPhamChiTiet();
                cd.setId(rs.getInt(1));
                cd.setAnhSanPham(rs.getString(2));
                cd.setTenSanPhamChiTiet(rs.getString(3));
                cd.setGiaSanPham(rs.getBigDecimal(4));
                cd.setSoLuong(rs.getInt(5));
                cd.setKichCo(rs.getInt(6));
                cd.setTenMau(rs.getString(7));
                list.add(cd);
            }
            return list;
        } catch (Exception e) {
        }
        return null;
    }
    
    public int themSpct (int soLuong, double tongTien, int idHoaDon, int idSp){
        String query ="""
                      INSERT INTO HoaDonChiTiet (MaHoaDonChiTiet, SoLuong, TongTien, NgayTao, TrangThai, IdHoaDon, IdSanPhamChiTiet) 
                      VALUES ('""" + createMaHdct() + "', ?, ?, GETDATE(), 1, ?, ?)";
        try {
            ps = conn.prepareStatement(query);
            
            ps.setInt(1, soLuong);
            ps.setDouble(2, tongTien);
            ps.setInt(3, idHoaDon);
            ps.setInt(4, idSp);
            
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public boolean kiemTraSpct (int idHoaDon, int idSp){
        String query ="""
                      SELECT * FROM HoaDonChiTiet WHERE IdHoaDon = ? AND IdSanPhamChiTiet = ? AND TrangThai = 1
                      """;
        try {
            ps = conn.prepareStatement(query);
            
            ps.setInt(1, idHoaDon);
            ps.setInt(2, idSp);
            
            rs = ps.executeQuery();
            
            return !rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    
    public int updateSoLuongHdct(int soLuong, int idHoaDon, int idSp) {
        try {
            ps = conn.prepareStatement("UPDATE HoaDonChiTiet SET soLuong = soLuong + ? WHERE IdHoaDon = ? AND IdSanPhamChiTiet = ?");
            ps.setInt(1, soLuong);
            ps.setInt(2, idHoaDon);
            ps.setInt(3, idSp);
            
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int getTongTien (int idHoaDon){
        String query ="""
                        SELECT SUM(spct.GiaSanPham * hdct.SoLuong) FROM HoaDonChiTiet hdct
                        LEFT JOIN SanPhamChiTiet spct ON spct.ID = hdct.IdSanPhamChiTiet
                        WHERE IdHoaDon = ? AND hdct.TrangThai = 1
                      """;
        try {
            ps = conn.prepareStatement(query);
            
            ps.setInt(1, idHoaDon);
            
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public HoaDonModel getHoaDon(int index) {
        
        return new HoaDonModel(getAllHoaDonCho().get(index).getId(), getAllHoaDonCho().get(index).getMaHoaDon());
    }
    
    public int sttHd () {
        try {
            ps = conn.prepareStatement("SELECT * FROM HoaDon");
            
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
    
    public String createMaHoaDon() {
        String ma;
        
        if (sttHd () < 10) {
            ma = "HD00" + String.valueOf(sttHd());
        } else if (sttHd () < 100) {
            ma = "HD0" + String.valueOf(sttHd());
        } else {
            ma = "HD" + String.valueOf(sttHd());
        }
        return ma;
    }
    
    public int sttHdct () {
        try {
            ps = conn.prepareStatement("SELECT * FROM HoaDonChiTiet");
            
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
    
    public String createMaHdct () {
        String ma;
        
        if (sttHdct() < 10) {
            ma = "HDCT00" + String.valueOf(sttHdct());
        } else if (sttHdct() < 100) {
            ma = "HDCT0" + String.valueOf(sttHdct());
        } else {
            ma = "HDCT" + String.valueOf(sttHdct());
        }
        return ma;
    }

    // Code san pham
//    public ArrayList<SanPhamChiTiet> getAllSanPham() {
//        ArrayList<SanPhamChiTiet> list = new ArrayList<>();
//        
//        String sql = """
//                        SELECT id, maSanPhamChiTiet, soLuong FROM SanPhamChiTiet 
//                        WHERE trangThai = 0 AND soLuong >= 0
//                     """;
//        
//        try {
//            ps = conn.prepareStatement(sql);
//            
//            rs = ps.executeQuery();
//            
//            while (rs.next()) {
//                list.add(new Model_SanPhamChiTiet(rs.getInt(1), rs.getString(2), rs.getInt(3)));
//            }
//            return list;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
    
//    public Model_SanPhamChiTiet getIdMaSpct(String ma) {
//        
//        for (Model_SanPhamChiTiet x : getdata())) {
//            
//            if (x.getMaSanPham().equals(ma)) {
//                
//                return x;
//            }
//        }
//        return null;
//    }
    
    public int updateSoLuong(int soLuongCu, int soLuongMoi, int idSpct) {
        try {
            ps = conn.prepareStatement("UPDATE SanPhamChiTiet SET soLuong = soLuong + ? - ? WHERE id = ? ");
            
            ps.setInt(1, soLuongCu);
            ps.setInt(2, soLuongMoi);
            ps.setInt(3, idSpct);
            
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int updateSoLuongSpct(int soLuong, int idSpct) {
        try {
            ps = conn.prepareStatement("UPDATE SanPhamChiTiet SET soLuong = soLuong - ? WHERE id = ?");
            ps.setInt(1, soLuong);
            ps.setInt(2, idSpct);
            
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public boolean check(int idHoaDon, int idSpct) {
        try {
            ps = conn.prepareStatement("SELECT idSanPhamChiTiet FROM HoaDonChiTiet WHERE trangThai = 0 AND idHoaDon = ? AND idSanPhamChiTiet = ?");
            
            ps.setInt(1, idHoaDon);
            ps.setInt(2, idSpct);
            
            rs = ps.executeQuery();
            
            return !rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public JPanel mainPanel(JPanel mainPanel) {
        
        mainPanel.setLayout(new GridLayout(0, 4, 10, 10));
        
        if (mainPanel.getComponentCount() % 4 == 0) {
            mainPanel.setPreferredSize(new Dimension(mainPanel.getPreferredSize().width, 200 * (mainPanel.getComponentCount() / 4)));
        } else {
            mainPanel.setPreferredSize(new Dimension(mainPanel.getPreferredSize().width, 200 * (mainPanel.getComponentCount() / 4) + 225));
        }
        return mainPanel;
    }
    
    public JPanel newPanel(String anh, String ten, int gia, int soLuong) {
        JPanel newPanel = new JPanel();
        newPanel.setBackground(new Color(0, 153, 153));
        newPanel.setPreferredSize(new Dimension(190, 200));
        newPanel.setLayout(new BorderLayout());
        
        ImageIcon icon = new ImageIcon(new ImageIcon(anh).getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
        
        JLabel imgLabel = new JLabel(icon);
        imgLabel.setHorizontalAlignment(JLabel.CENTER);
        imgLabel.setVerticalAlignment(JLabel.CENTER);
        
        JLabel textName = new JLabel(ten, SwingConstants.CENTER);
        textName.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JLabel textLabel = new JLabel(gia + " VNĐ", SwingConstants.LEFT);
        textLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 0));
        
        JLabel textSL = new JLabel("SL: " + soLuong, SwingConstants.RIGHT);
        textSL.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 20));
        
        JPanel southPanel = new JPanel(new GridLayout(1, 0));
        southPanel.setBackground(new Color(0, 153, 153));
        southPanel.add(textLabel, BorderLayout.WEST);
        southPanel.add(textSL, BorderLayout.EAST);
        
        southPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        newPanel.add(imgLabel, BorderLayout.NORTH);
        newPanel.add(textName, BorderLayout.CENTER);
        newPanel.add(southPanel, BorderLayout.SOUTH);
        
        return newPanel;
    }
    
//    public void tongTien(JTextField tongTien, JTextField thanhTien) {
//        DecimalFormat format = new DecimalFormat("#,###");
//        String numberFormat = format.format(this.tongTien);
//        tongTien.setText(numberFormat);
//        thanhTien.setText(tongTien.getText());
//    }
    
    public ArrayList<String> modelComboBox() {
        ArrayList<String> model = new ArrayList<>();
        
        try {
            ps = conn.prepareStatement("SELECT tenDanhMuc FROM DanhMuc");
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                model.add(rs.getString(1));
            }
            return model;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Code khach hang 
    public int suaKhachHangHoaDon(int idHoaDon) {
        
        try {
            ps = conn.prepareStatement("UPDATE HoaDon SET idKhachHang = NULL WHERE id = ?");
            
            ps.setInt(1, idHoaDon);
            
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Code voucher
    public int suaVoucherHoaDon(int idHoaDon) {
        
        try {
            ps = conn.prepareStatement("UPDATE HoaDon SET idVoucher = NULL WHERE id = ?");
            
            ps.setInt(1, idHoaDon);
            
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public int updateThanhToanHoaDon(String loai, long tongTien, int idHd) {
        
        try {
            ps = conn.prepareStatement("UPDATE HoaDon SET loaiThanhToan = ?, tongTien = ?, trangThai = ? WHERE id = ?");
            
            ps.setString(1, loai);
            ps.setLong(2, tongTien);
            ps.setInt(3, 0);
            ps.setInt(4, idHd);
            
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public int themHoaDonMoi(int idNhanVien) {
        try {
            ps = conn.prepareStatement("""
                                       INSERT INTO HoaDon
                                            (maHoaDon, loaiThanhToan, tongTien, ngayTao, idKhachHang, idNhanVien, idVoucher, trangThai)
                                       VALUES
                                            ('""" + createMaHoaDon() + "', NULL, NULL, GETDATE(), null, ?, NULL, 1)");
            ps.setInt(1, idNhanVien);
            
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int updateSoLuongHdct(int soLuong, int idHdct) {
        try {
            ps = conn.prepareStatement("UPDATE HoaDonChiTiet SET soLuong = ? WHERE id = ?");
            ps.setInt(1, soLuong);
            ps.setInt(2, idHdct);
            
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
   
}
