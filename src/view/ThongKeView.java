/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import repository.ThongKeRepo;

/**
 *
 * @author ADMIN
 */
public class ThongKeView extends javax.swing.JPanel {

    private ThongKeRepo repo = new ThongKeRepo();
    private String ngayBatDau; // Lưu trữ ngày bắt đầu
    private String ngayKetThuc; // Lưu trữ ngày kết thúc
    private TableRowSorter<DefaultTableModel> sorter;
    /**
     * Creates new form ThongKeView
     */
    public ThongKeView() {
        initComponents();
        
         cboKieuLoc.addActionListener(e -> xuLyKieuLoc());
        
        btnLoc.addActionListener(e -> fillThongKe());
        btnGuiBaoCao.addActionListener(e -> guiBaoCaoQuaEmail());
        
        // Thêm sự kiện KeyListener cho txtSearchThongKe
        txtSearchThongKe.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTable(); // Gọi phương thức lọc bảng khi người dùng nhập/xóa
            }
        });
         xuLyKieuLoc();
        fillThongKe();
    }
    
    private void fillThongKe() {
       String kieuLoc = (String) cboKieuLoc.getSelectedItem();

     ngayBatDau = null;
     ngayKetThuc = null;

    if (kieuLoc.equalsIgnoreCase("Tất Cả")) {
        // Lọc tất cả → để null, repo sẽ xử lý
        ngayBatDau = null;
        ngayKetThuc = null;
    } else {
        // Lọc theo tháng/năm
        int thang = Integer.parseInt((String) cboThang.getSelectedItem());
        int nam = Integer.parseInt((String) cboNam.getSelectedItem());

        Calendar cal = Calendar.getInstance();
        cal.set(nam, thang - 1, 1);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        ngayBatDau = String.format("%d-%02d-01", nam, thang);
        ngayKetThuc = String.format("%d-%02d-%02d", nam, thang, lastDay);
    }

    repo.getThongKe(ngayBatDau, ngayKetThuc, this);
    
    // Khởi tạo sorter sau khi bảng được cập nhật dữ liệu
        DefaultTableModel model = (DefaultTableModel) tblThongKeSanPham.getModel();
        sorter = new TableRowSorter<>(model);
        tblThongKeSanPham.setRowSorter(sorter);
    }
    
    // Phương thức lọc bảng theo từ khóa tìm kiếm
    private void filterTable() {
        String keyword = txtSearchThongKe.getText().trim();
        if (keyword.isEmpty()) {
            sorter.setRowFilter(null); // Nếu không có từ khóa, bỏ lọc
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword, 0)); // Lọc cột "Sản Phẩm" (cột 0), không phân biệt hoa thường
        }
    }
   // Các phương thức để cập nhật giao diện
    public void setDoanhThu(long doanhThu) {
        lblDoanhThu.setText(String.format("%,d", doanhThu) + " VNĐ"); // Định dạng số với dấu phẩy
    }

    public void setSoHoaDon(int soHoaDon) {
        lblSoHoaDon.setText(String.valueOf(soHoaDon));
    }

    public void setTongKhachHang(int tongKhachHang) {
        lblTongKhachHang.setText(String.valueOf(tongKhachHang));
    }

    public void setTableModel(DefaultTableModel model) {
       tblThongKeSanPham.setModel(model); // Cập nhật bảng tblThongKeSanPham
        
        // Định dạng cột "Giá Thấp Nhất" (cột 2), "Giá Cao Nhất" (cột 3), và "Doanh Thu" (cột 4)
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 2 || column == 3 || column == 4) { // Cột "Giá Thấp Nhất", "Giá Cao Nhất", "Doanh Thu"
                    try {
                        // Chuyển giá trị thành số và định dạng với dấu ngăn cách
                        long number = Long.parseLong(value.toString());
                        setText(String.format("%,d", number) + " VNĐ");
                    } catch (NumberFormatException e) {
                        setText(value.toString()); // Nếu không phải số, giữ nguyên giá trị
                    }
                }
                return c;
            }
        };
        renderer.setHorizontalAlignment(SwingConstants.LEFT); // Căn phải cho số
        tblThongKeSanPham.getColumnModel().getColumn(2).setCellRenderer(renderer); // Áp dụng cho cột "Giá Thấp Nhất"
        tblThongKeSanPham.getColumnModel().getColumn(3).setCellRenderer(renderer); // Áp dụng cho cột "Giá Cao Nhất"
        tblThongKeSanPham.getColumnModel().getColumn(4).setCellRenderer(renderer); // Áp dụng cho cột "Doanh Thu"
    }
    
    private void xuLyKieuLoc() {
    String kieuLoc = (String) cboKieuLoc.getSelectedItem();

    boolean isTatCa = kieuLoc.equalsIgnoreCase("Tất Cả");

    cboThang.setEnabled(!isTatCa);
    cboNam.setEnabled(!isTatCa);
}
    
    // Phương thức gửi báo cáo qua email
    private void guiBaoCaoQuaEmail() {
       // Thông tin email người gửi
    final String username = "haidangpham2001@gmail.com"; // Email người gửi
    final String password = "nqwcmbbbfulahuyy"; // Mật khẩu ứng dụng

    // Hiển thị hộp thoại để người dùng nhập email người nhận
    String toEmail = JOptionPane.showInputDialog(this, "Nhập email người nhận:", "Gửi Báo Cáo", JOptionPane.PLAIN_MESSAGE);
    if (toEmail == null || toEmail.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập email người nhận!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Hiển thị hộp thoại xác nhận Yes/No
    int confirm = JOptionPane.showConfirmDialog(this, 
        "Bạn có chắc chắn muốn gửi báo cáo đến " + toEmail + " không?", 
        "Xác Nhận Gửi Báo Cáo", 
        JOptionPane.YES_NO_OPTION, 
        JOptionPane.QUESTION_MESSAGE);
    
    // Nếu người dùng chọn No, thoát phương thức
    if (confirm != JOptionPane.YES_OPTION) {
        JOptionPane.showMessageDialog(this, "Đã hủy gửi báo cáo!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        return;
    }

    // Cấu hình properties cho JavaMail
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");
    props.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // Thêm để tin cậy máy chủ Gmail
    props.put("mail.debug", "true"); // Bật debug để xem chi tiết quá trình gửi

    // Tạo phiên gửi email
    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    });

    try {
        // Tạo đối tượng MimeMessage
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));

        // Xác định thời gian hiển thị trong email
        String thoiGian;
        String kieuLoc = (String) cboKieuLoc.getSelectedItem();
        if (kieuLoc.equalsIgnoreCase("Tất Cả")) {
            thoiGian = "Tất Cả";
            message.setSubject("Báo Cáo Thống Kê - Tất Cả");
        } else {
            thoiGian = "Tháng " + cboThang.getSelectedItem() + "/" + cboNam.getSelectedItem();
            message.setSubject("Báo Cáo Thống Kê - " + thoiGian);
        }

        // Tạo nội dung email với định dạng đẹp
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<h2 style='font-family: Arial, sans-serif; color: #333;'>Báo Cáo Thống Kê</h2>");
        emailContent.append("<p style='font-family: Arial, sans-serif; color: #555;'><b>Thời gian:</b> ").append(thoiGian).append("</p>");
        emailContent.append("<p style='font-family: Arial, sans-serif; color: #555;'><b>Doanh Thu:</b> ").append(lblDoanhThu.getText()).append("</p>");
        emailContent.append("<p style='font-family: Arial, sans-serif; color: #555;'><b>Số Hóa Đơn:</b> ").append(lblSoHoaDon.getText()).append("</p>");
        emailContent.append("<p style='font-family: Arial, sans-serif; color: #555;'><b>Tổng Khách Hàng:</b> ").append(lblTongKhachHang.getText()).append("</p>");

        // Thêm bảng thống kê sản phẩm với định dạng đẹp
            emailContent.append("<h3 style='font-family: Arial, sans-serif; color: #333;'>Thống Kê Sản Phẩm</h3>");
            emailContent.append("<table style='border-collapse: collapse; font-family: Arial, sans-serif; width: 100%;'>");
            emailContent.append("<tr style='background-color: #f2f2f2;'>");
            emailContent.append("<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Sản Phẩm</th>");
            emailContent.append("<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Số Lượng</th>");
            emailContent.append("<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Giá Thấp Nhất</th>");
            emailContent.append("<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Giá Cao Nhất</th>");
            emailContent.append("<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Doanh Thu</th>");
            emailContent.append("</tr>");

            DefaultTableModel model = (DefaultTableModel) tblThongKeSanPham.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                emailContent.append("<tr>");
                emailContent.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(model.getValueAt(i, 0)).append("</td>"); // Sản Phẩm
                emailContent.append("<td style='border: 1px solid #ddd; padding: 8px; text-align: right;'>").append(model.getValueAt(i, 1)).append("</td>"); // Số Lượng

                // Định dạng cột "Giá Thấp Nhất" (cột 2)
                String giaThapNhat = model.getValueAt(i, 2).toString();
                try {
                    long giaThapNhatNumber = Long.parseLong(giaThapNhat);
                    emailContent.append("<td style='border: 1px solid #ddd; padding: 8px; text-align: right;'>").append(String.format("%,d VNĐ", giaThapNhatNumber)).append("</td>");
                } catch (NumberFormatException e) {
                    emailContent.append("<td style='border: 1px solid #ddd; padding: 8px; text-align: right;'>").append(giaThapNhat).append("</td>");
                }

                // Định dạng cột "Giá Cao Nhất" (cột 3)
                String giaCaoNhat = model.getValueAt(i, 3).toString();
                try {
                    long giaCaoNhatNumber = Long.parseLong(giaCaoNhat);
                    emailContent.append("<td style='border: 1px solid #ddd; padding: 8px; text-align: right;'>").append(String.format("%,d VNĐ", giaCaoNhatNumber)).append("</td>");
                } catch (NumberFormatException e) {
                    emailContent.append("<td style='border: 1px solid #ddd; padding: 8px; text-align: right;'>").append(giaCaoNhat).append("</td>");
                }

                // Định dạng cột "Doanh Thu" (cột 4)
                String doanhThu = model.getValueAt(i, 4).toString();
                try {
                    long doanhThuNumber = Long.parseLong(doanhThu);
                    emailContent.append("<td style='border: 1px solid #ddd; padding: 8px; text-align: right;'>").append(String.format("%,d VNĐ", doanhThuNumber)).append("</td>");
                } catch (NumberFormatException e) {
                    emailContent.append("<td style='border: 1px solid #ddd; padding: 8px; text-align: right;'>").append(doanhThu).append("</td>");
                }

                emailContent.append("</tr>");
            }
        emailContent.append("</table>");

        // Đặt nội dung email là HTML
        message.setContent(emailContent.toString(), "text/html; charset=UTF-8");

        // Gửi email
        Transport.send(message);

        JOptionPane.showMessageDialog(this, "Báo cáo đã được gửi qua email thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Lỗi khi gửi email: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblDoanhThu = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lblSoHoaDon = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblTongKhachHang = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblThongKeSanPham = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        txtSearchThongKe = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cboThang = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cboNam = new javax.swing.JComboBox<>();
        btnLoc = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        cboKieuLoc = new javax.swing.JComboBox<>();
        btnGuiBaoCao = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 204, 204));
        jPanel2.setMaximumSize(new java.awt.Dimension(200, 80));
        jPanel2.setMinimumSize(new java.awt.Dimension(200, 80));
        jPanel2.setPreferredSize(new java.awt.Dimension(300, 80));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Doanh Thu");
        jPanel2.add(jLabel1, java.awt.BorderLayout.NORTH);

        lblDoanhThu.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblDoanhThu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDoanhThu.setToolTipText("");
        jPanel2.add(lblDoanhThu, java.awt.BorderLayout.CENTER);

        jPanel3.setBackground(new java.awt.Color(255, 204, 204));
        jPanel3.setMaximumSize(new java.awt.Dimension(200, 80));
        jPanel3.setMinimumSize(new java.awt.Dimension(200, 80));
        jPanel3.setPreferredSize(new java.awt.Dimension(300, 80));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jLabel2.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Số Hóa Đơn");
        jPanel3.add(jLabel2, java.awt.BorderLayout.NORTH);

        lblSoHoaDon.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblSoHoaDon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSoHoaDon.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel3.add(lblSoHoaDon, java.awt.BorderLayout.CENTER);

        jPanel4.setBackground(new java.awt.Color(255, 204, 204));
        jPanel4.setMaximumSize(new java.awt.Dimension(200, 80));
        jPanel4.setMinimumSize(new java.awt.Dimension(200, 80));
        jPanel4.setPreferredSize(new java.awt.Dimension(300, 80));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Tổng Khách Hàng");
        jPanel4.add(jLabel3, java.awt.BorderLayout.NORTH);

        lblTongKhachHang.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblTongKhachHang.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel4.add(lblTongKhachHang, java.awt.BorderLayout.CENTER);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Hàng Hóa", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 14))); // NOI18N

        tblThongKeSanPham.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        tblThongKeSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Sản Phẩm", "Số Lượng", "Giá Thấp Nhất","Giá Cao Nhất", "Doanh Thu"
            }
        ));
        jScrollPane1.setViewportView(tblThongKeSanPham);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm Kiếm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 12))); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtSearchThongKe, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(txtSearchThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 4, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 1039, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(255, 204, 204));

        jLabel4.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel4.setText("Tháng:");

        cboThang.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        cboThang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));

        jLabel5.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel5.setText("Năm:");

        cboNam.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        cboNam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2022", "2023", "2024", "2025", "2026" }));

        btnLoc.setBackground(new java.awt.Color(255, 204, 204));
        btnLoc.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btnLoc.setText("Lọc");

        jLabel6.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel6.setText("Kiểu lọc:");

        cboKieuLoc.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        cboKieuLoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất Cả", "Tháng/Năm" }));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(cboKieuLoc, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboThang, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboNam, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                .addComponent(btnLoc)
                .addGap(17, 17, 17))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cboThang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(cboNam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLoc)
                    .addComponent(jLabel6)
                    .addComponent(cboKieuLoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        btnGuiBaoCao.setBackground(new java.awt.Color(255, 204, 204));
        btnGuiBaoCao.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btnGuiBaoCao.setText("Gửi Báo Cáo");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(39, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(71, 71, 71)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(56, 56, 56))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnGuiBaoCao, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(29, 29, 29)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnGuiBaoCao)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1156, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 611, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuiBaoCao;
    private javax.swing.JButton btnLoc;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboKieuLoc;
    private javax.swing.JComboBox<String> cboNam;
    private javax.swing.JComboBox<String> cboThang;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDoanhThu;
    private javax.swing.JLabel lblSoHoaDon;
    private javax.swing.JLabel lblTongKhachHang;
    private javax.swing.JTable tblThongKeSanPham;
    private javax.swing.JTextField txtSearchThongKe;
    // End of variables declaration//GEN-END:variables
}
