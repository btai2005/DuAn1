/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import view.ThongKeView;

/**
 *
 * @author ADMIN
 */
public class ThongKeRepo {
    private Connection conn;

    public ThongKeRepo() {
        this.conn = Utils.Dbconnection.getConnection();
    }
    
    public void getThongKe(String ngayBatDau, String ngayKetThuc, ThongKeView view) {
        String sql = "{call sp_ThongKeTongHop(?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            // Set tham số
            if (ngayBatDau == null || ngayBatDau.isEmpty()) {
                stmt.setNull(1, java.sql.Types.DATE);
            } else {
                stmt.setDate(1, java.sql.Date.valueOf(ngayBatDau));
            }
            if (ngayKetThuc == null || ngayKetThuc.isEmpty()) {
                stmt.setNull(2, java.sql.Types.DATE);
            } else {
                stmt.setDate(2, java.sql.Date.valueOf(ngayKetThuc));
            }

            // Thực thi stored procedure
            boolean hasResults = stmt.execute();

            // Result Set 1: Doanh Thu, Số Hóa Đơn, Tổng Khách Hàng
            if (hasResults) {
                try (ResultSet rs = stmt.getResultSet()) {
                    if (rs.next()) {
                        view.setDoanhThu(rs.getLong("DoanhThu"));
                        view.setSoHoaDon(rs.getInt("SoHoaDon"));
                        view.setTongKhachHang(rs.getInt("TongKhachHang"));
                    }
                }
            }

           // Result Set 2: Chi tiết sản phẩm
            if (stmt.getMoreResults()) {
                try (ResultSet rs = stmt.getResultSet()) {
                    DefaultTableModel model = new DefaultTableModel();
                    model.addColumn("Sản Phẩm");
                    model.addColumn("Số Lượng");
                    model.addColumn("Giá Thấp Nhất");
                    model.addColumn("Giá Cao Nhất");
                    model.addColumn("Doanh Thu");

                    while (rs.next()) {
                        Object[] row = new Object[5];
                        row[0] = rs.getString("SanPham");
                        row[1] = rs.getInt("SoLuong");
                        row[2] = rs.getLong("GiaThapNhat");
                        row[3] = rs.getLong("GiaCaoNhat");
                        row[4] = rs.getLong("DoanhThu");
                        model.addRow(row);
                    }
                    view.setTableModel(model);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
