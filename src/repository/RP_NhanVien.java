package repository;

import Utils.Dbconnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.NhanVien;

public class RP_NhanVien {

    private Connection con = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    // Lấy toàn bộ dữ liệu từ database sang list
    public List<NhanVien> getAll() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE TrangThai = 0";
        try {
            con = Dbconnection.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                NhanVien nv;
                // Chuyển đổi java.sql.Date thành java.util.Date
                java.sql.Date sqlNgaySinh = rs.getDate(5);
                java.util.Date ngaySinh = (sqlNgaySinh != null) ? new java.util.Date(sqlNgaySinh.getTime()) : null;

                java.sql.Timestamp sqlNgayTao = rs.getTimestamp(12);
                java.util.Date ngayTao = (sqlNgayTao != null) ? new java.util.Date(sqlNgayTao.getTime()) : null;

                java.sql.Timestamp sqlNgaySua = rs.getTimestamp(13);
                java.util.Date ngaySua = (sqlNgaySua != null) ? new java.util.Date(sqlNgaySua.getTime()) : null;
                
                nv = new NhanVien(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        ngaySinh,
                        rs.getString(6), // DiaChi
                        rs.getString(7), // SoCCCD
                        rs.getString(8), // SoDienThoai
                        rs.getString(9), // MatKhau
                        rs.getString(10), // VaiTro
                        rs.getString(11), // GhiChu
                        ngayTao, // NgayTao
                        ngaySua, // NgayCapNhat
                        rs.getInt(14) // TrangThai
                );
                list.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean kiemTraTrungMa(String maNV) {
        String sql = "SELECT COUNT(*) FROM NhanVien WHERE MaNhanVien = ?";
        try (Connection con = Dbconnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maNV);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public String validateNhanVien(NhanVien nv, boolean isInsert) {
       // Kiểm tra Mã nhân viên
    if (nv.getMaNhanVien() == null || nv.getMaNhanVien().trim().isEmpty()) {
        return "Mã nhân viên không được để trống!";
    }
    if (isInsert && kiemTraTrungMa(nv.getMaNhanVien())) {
        return "Mã nhân viên đã tồn tại!";
    }

    // Kiểm tra Họ tên
    if (nv.getHoTen() == null || nv.getHoTen().trim().isEmpty()) {
        return "Họ tên không được để trống!";
    }

    // Kiểm tra Giới tính
    if (nv.getGioiTinh() == null || nv.getGioiTinh().trim().isEmpty()) {
        return "Giới tính không được để trống!";
    }
    if (!nv.getGioiTinh().equals("Nam") && !nv.getGioiTinh().equals("Nữ")) {
        return "Giới tính phải là 'Nam' hoặc 'Nữ'!";
    }

    // Kiểm tra Ngày sinh
    if (nv.getNgaySinh() == null) {
        return "Ngày sinh không được để trống!";
    }
    // Kiểm tra Địa chỉ
    if (nv.getDiaChi() == null || nv.getDiaChi().trim().isEmpty()) {
        return "Địa chỉ không được để trống!";
    }

    // Kiểm tra Số CCCD
    if (nv.getSoCCCD() == null || !nv.getSoCCCD().matches("^\\d{12}$")) {
        return "Số CCCD không hợp lệ! Phải có đúng 12 chữ số.";
    }
    
    // Kiểm tra trùng Số CCCD
    if (kiemTraTrungSoCCCD(nv.getSoCCCD(), nv.getId(), isInsert)) {
        return "Số CCCD đã tồn tại! Vui lòng nhập số CCCD khác.";
    }

    // Kiểm tra Số điện thoại
    if (nv.getSoDienThoai() == null || !nv.getSoDienThoai().matches("^0\\d{9}$")) {
        return "Số điện thoại không hợp lệ! Phải bắt đầu bằng 0 và có đúng 10 chữ số.";
    }
    
    if (kiemTraTrungSoDienThoai(nv.getSoDienThoai(), nv.getId(), isInsert)) {
            return "Số điện thoại đã tồn tại! Vui lòng nhập số điện thoại khác.";
        }

    // Kiểm tra Mật khẩu
    if (nv.getMatKhau() == null || nv.getMatKhau().trim().isEmpty()) {
        return "Mật khẩu không được để trống!";
    }
    if (nv.getMatKhau().length() < 6) {
        return "Mật khẩu phải có ít nhất 6 ký tự!";
    }

    // Kiểm tra Vai trò
    if (nv.getVaiTro() == null || nv.getVaiTro().trim().isEmpty()) {
        return "Vai trò không được để trống!";
    }
    if (!nv.getVaiTro().equals("Quản Lý") && !nv.getVaiTro().equals("Nhân Viên")) {
        return "Vai trò phải là 'Quản Lý' hoặc 'Nhân Viên'!";
    }

    // Nếu không có lỗi, trả về null
    return null;
    }

    public int addNhanVien(NhanVien nv) {
        String err = validateNhanVien(nv, true);
        if (err != null) {
            JOptionPane.showMessageDialog(null, err);
            return 0;
        }

        String sql = "INSERT INTO NhanVien (MaNhanVien, HoTen, GioiTinh, NgaySinh, DiaChi, SoCCCD, SoDienThoai, MatKhau, VaiTro, GhiChu) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Dbconnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, nv.getMaNhanVien());
            ps.setObject(2, nv.getHoTen());
            ps.setObject(3, nv.getGioiTinh());
            // Chuyển đổi java.util.Date thành java.sql.Date
            java.util.Date utilNgaySinh = nv.getNgaySinh();
            java.sql.Date sqlNgaySinh = (utilNgaySinh != null) ? new java.sql.Date(utilNgaySinh.getTime()) : null;
            ps.setObject(4, sqlNgaySinh);
            ps.setObject(5, nv.getDiaChi());
            ps.setObject(6, nv.getSoCCCD());
            ps.setObject(7, nv.getSoDienThoai());
            ps.setObject(8, nv.getMatKhau());
            ps.setObject(9, nv.getVaiTro());
            ps.setObject(10, nv.getGhiChu());

            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int updateNhanVien(NhanVien nv) {
        String err = validateNhanVien(nv, false);
        if (err != null) {
            JOptionPane.showMessageDialog(null, err);
            return 0;
        }

        String sql = "UPDATE NhanVien SET MaNhanVien = ?, HoTen = ?, GioiTinh = ?, NgaySinh = ?, DiaChi = ?, "
                + "SoCCCD = ?, SoDienThoai = ?, MatKhau = ?, VaiTro = ?, GhiChu = ? WHERE ID = ?";
        try (Connection con = Dbconnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, nv.getMaNhanVien());
            ps.setObject(2, nv.getHoTen());
            ps.setObject(3, nv.getGioiTinh());
            // Chuyển đổi java.util.Date thành java.sql.Date
            java.util.Date utilNgaySinh = nv.getNgaySinh();
            java.sql.Date sqlNgaySinh = (utilNgaySinh != null) ? new java.sql.Date(utilNgaySinh.getTime()) : null;
            ps.setObject(4, sqlNgaySinh);
            ps.setObject(5, nv.getDiaChi());
            ps.setObject(6, nv.getSoCCCD());
            ps.setObject(7, nv.getSoDienThoai());
            ps.setObject(8, nv.getMatKhau());
            ps.setObject(9, nv.getVaiTro());
            ps.setObject(10, nv.getGhiChu());
            ps.setObject(11, nv.getId());

            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Ẩn nhân viên (trạng thái = 1)
public boolean anNhanVien(int id) {
    String sql = "UPDATE NhanVien SET TrangThai = 1, NgaySua = GETDATE() WHERE ID = ?";
    try (Connection con = Dbconnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, id);
        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}


    public List<NhanVien> timKiemNhanVien(String tuKhoa) {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE HoTen LIKE ? OR MaNhanVien LIKE ?";
        try (Connection con = Dbconnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            String keyword = "%" + tuKhoa + "%";
            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setId(rs.getInt("ID"));
                nv.setMaNhanVien(rs.getString("MaNhanVien"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setGioiTinh(rs.getString("GioiTinh"));
                nv.setNgaySinh(rs.getDate("NgaySinh"));
                nv.setDiaChi(rs.getString("DiaChi"));
                nv.setSoCCCD(rs.getString("SoCCCD"));
                nv.setSoDienThoai(rs.getString("SoDienThoai"));
                nv.setMatKhau(rs.getString("MatKhau"));
                nv.setVaiTro(rs.getString("VaiTro"));
                nv.setGhiChu(rs.getString("GhiChu"));
                nv.setTrangThai(rs.getInt("TrangThai"));
                list.add(nv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Kiểm tra trùng Số CCCD
public boolean kiemTraTrungSoCCCD(String soCCCD, int idNhanVien, boolean isInsert) {
    String sql = "SELECT COUNT(*) FROM NhanVien WHERE SoCCCD = ? AND ID != ?";
    try (Connection con = Dbconnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, soCCCD);
        ps.setInt(2, isInsert ? -1 : idNhanVien); // Nếu là thêm mới, bỏ qua kiểm tra ID
        ResultSet rs = ps.executeQuery();
        return rs.next() && rs.getInt(1) > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return true;
    }
}

// Kiểm tra trùng Số điện thoại
public boolean kiemTraTrungSoDienThoai(String soDienThoai, int idNhanVien, boolean isInsert) {
    String sql = "SELECT COUNT(*) FROM NhanVien WHERE SoDienThoai = ? AND ID != ?";
    try (Connection con = Dbconnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, soDienThoai);
        ps.setInt(2, isInsert ? -1 : idNhanVien); // Nếu là thêm mới, bỏ qua kiểm tra ID
        ResultSet rs = ps.executeQuery();
        return rs.next() && rs.getInt(1) > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return true;
    }
}

    // Lấy danh sách nhân viên ẩn
public List<NhanVien> getNhanVienAn() {
    List<NhanVien> list = new ArrayList<>();
    String sql = "SELECT * FROM NhanVien WHERE trangThai = 1";
    try  
    {
         con = Dbconnection.getConnection();
          ps = con.prepareStatement(sql);
          rs = ps.executeQuery();

        while (rs.next()) {
            NhanVien nv = new NhanVien();
            nv.setId(rs.getInt("ID"));
            nv.setMaNhanVien(rs.getString("MaNhanVien"));
            nv.setHoTen(rs.getString("HoTen"));
            nv.setGioiTinh(rs.getString("GioiTinh"));
            nv.setNgaySinh(rs.getDate("NgaySinh"));
            nv.setDiaChi(rs.getString("DiaChi"));
            nv.setSoCCCD(rs.getString("SoCCCD"));
            nv.setSoDienThoai(rs.getString("SoDienThoai"));
            nv.setMatKhau(rs.getString("MatKhau"));
            nv.setVaiTro(rs.getString("VaiTro"));
            nv.setGhiChu(rs.getString("GhiChu"));
            nv.setNgayTao(rs.getDate("NgayTao"));
            nv.setNgaySua(rs.getDate("NgaySua"));
            nv.setTrangThai(rs.getInt("TrangThai"));
            list.add(nv);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}

    // Hiển thị lại nhân viên (đặt lại trạng thái = 0)
public boolean hienThiLaiNhanVien(int id) {
    String sql = "UPDATE NhanVien SET TrangThai = 0, ngaySua = GETDATE() WHERE ID = ?";
    try (Connection con = Dbconnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, id);
        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
}
