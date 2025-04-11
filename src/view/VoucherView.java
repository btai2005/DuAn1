/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Voucher;
import repository.RP_Voucher;

/**
 *
 * @author duong
 */
public class VoucherView extends javax.swing.JPanel {

    private RP_Voucher r_vc = new RP_Voucher();
    private DefaultTableModel model = new DefaultTableModel();
    private int i = 0;

    /**
     * Creates new form VoucherView
     */
    public VoucherView() {
        initComponents();
        dateChooserNgayBatDau.setDateFormatString("yyyy-MM-dd");
        dateChooserNgayKetThuc.setDateFormatString("yyyy-MM-dd");
        this.filltable(r_vc.getAll());
        this.showData(r_vc.getAll().size()-1);
        showCBO();
        
        Table_Voucher.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {  // Kiểm tra xem có phải là sự kiện thay đổi chọn dòng
                int selectedRow = Table_Voucher.getSelectedRow();
                if (selectedRow != -1) {
                    showData(selectedRow);  // Hiển thị dữ liệu của dòng được chọn
                    txt_ma.setEnabled(false);  // Vô hiệu hóa txt_ma khi chọn dòng trong bảng
                    ADD.setEnabled(false);
                } else {
                    txt_ma.setEnabled(true);  // Bật lại txt_ma nếu không có dòng nào được chọn
                    ADD.setEnabled(true);
                }
            }
        });
        
    }

    public void showCBO() {
       ComboBox_loaivoucher.removeAllItems();
       ComboBox_loaivoucher.addItem("Phần trăm"); // Thêm giá trị tĩnh
       ComboBox_loaivoucher.addItem("Tiền mặt");
}
   void filltable(ArrayList<Voucher> list) {
      model = (DefaultTableModel) Table_Voucher.getModel();
    model.setRowCount(0);
    
      DecimalFormat decimalFormat = new DecimalFormat("#,###");
    
    for (Voucher m_cd : list) {
        String giaTriHienThi;

          // Kiểm tra kiểu voucher và định dạng giá trị phần trăm
        if (m_cd.getLoaiVoucher().equalsIgnoreCase("Phần trăm")) {
            giaTriHienThi = decimalFormat.format(m_cd.getGiaTri()) + " %";
        } else {
            // Định dạng số tiền (nếu không phải phần trăm) và bỏ phần .0
            giaTriHienThi = decimalFormat.format(m_cd.getGiaTri());
        }
        
         // Cập nhật trạng thái dựa trên ngày hiện tại
        String trangThaiVoucher = m_cd.tinhTrangThai() == 0 ? "Còn hạn" : "Hết hạn";
        
        // Thêm dòng vào bảng
        model.addRow(new Object[]{
            m_cd.getId(),
            m_cd.getMaVoucher(),
            m_cd.getTenVoucher(),
            m_cd.getNgayBatDau(),
            m_cd.getNgayKetThuc(),
            m_cd.getLoaiVoucher(),
            giaTriHienThi,
            trangThaiVoucher
        });
    }
}
  void showData(int index) {
    if (index >= 0 && index < r_vc.getAll().size()) {
        Voucher voucher = r_vc.getAll().get(index);
        txt_ma.setText(voucher.getMaVoucher());
        txt_ten.setText(voucher.getTenVoucher());

        // Chuyển đổi String thành Date để hiển thị trên JDateChooser
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date ngayBatDau = sdf.parse(voucher.getNgayBatDau());
            Date ngayKetThuc = sdf.parse(voucher.getNgayKetThuc());
            dateChooserNgayBatDau.setDate(ngayBatDau);
            dateChooserNgayKetThuc.setDate(ngayKetThuc);
        } catch (ParseException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        // Lấy giá trị phần trăm từ bảng và hiển thị vào txt_giatri
        String giaTriText = Table_Voucher.getValueAt(index, 6).toString();
        if (giaTriText.contains("%")) {
            giaTriText = giaTriText.replace(" %", "");  // Chỉ loại bỏ phần trăm nếu có
        }
        txt_giatri.setText(giaTriText);

        ComboBox_loaivoucher.setSelectedItem(voucher.getLoaiVoucher());
    }
}

  void searchVoucher() {
    // Lấy mã voucher phần người dùng nhập vào
    String maVoucherPart = txt_timkem.getText().trim();
    
    // Kiểm tra xem mã voucher có rỗng không
    if (!maVoucherPart.isEmpty()) {
        // Gọi phương thức tìm kiếm theo phần chuỗi từ lớp RP_Voucher
        RP_Voucher rpVoucher = new RP_Voucher();
        ArrayList<Voucher> result = rpVoucher.searchByMaVoucher(maVoucherPart);  // Tìm kiếm theo mã voucher
        
        // Kiểm tra nếu có kết quả tìm thấy
        if (result != null && !result.isEmpty()) {
            filltable(result);  // Hiển thị các kết quả tìm được lên bảng
            showData(0);  // Hiển thị chi tiết voucher đầu tiên trong kết quả tìm kiếm
        } else {
            // Nếu không tìm thấy voucher, thông báo lỗi
            JOptionPane.showMessageDialog(this, "Không tìm thấy voucher với phần mã: " + maVoucherPart, 
                                          "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    } else {
        // Nếu không nhập mã voucher, thông báo lỗi
        JOptionPane.showMessageDialog(this, "Vui lòng nhập phần mã Voucher để tìm kiếm.", 
                                      "Thông báo", JOptionPane.WARNING_MESSAGE);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        Table_Voucher = new javax.swing.JTable();
        txt_ma = new javax.swing.JTextField();
        txt_ten = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        ComboBox_loaivoucher = new javax.swing.JComboBox<>();
        ADD = new javax.swing.JButton();
        UPDATE = new javax.swing.JButton();
        Seach = new javax.swing.JButton();
        txt_timkem = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txt_giatri = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        dateChooserNgayBatDau = new com.toedter.calendar.JDateChooser();
        dateChooserNgayKetThuc = new com.toedter.calendar.JDateChooser();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        Table_Voucher.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        Table_Voucher.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Mã Voucher", "Tên Voucher", "Ngày Bắt Đầu", "Ngày Kết Thúc", "Loại Voucher", "Giá Trị", "Trạng Thái"
            }
        ));
        Table_Voucher.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Table_VoucherMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Table_Voucher);

        txt_ma.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N

        txt_ten.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel1.setText("Mã Voucher:");

        jLabel2.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel2.setText("Tên Voucher:");

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel3.setText("Ngày bắt đầu:");

        jLabel4.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel4.setText("Ngày kết thúc:");

        jLabel5.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel5.setText("Loại Voucher:");

        ComboBox_loaivoucher.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N

        ADD.setBackground(new java.awt.Color(255, 204, 204));
        ADD.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        ADD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/icon/icons8-add-20.png"))); // NOI18N
        ADD.setText("Thêm");
        ADD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ADDActionPerformed(evt);
            }
        });

        UPDATE.setBackground(new java.awt.Color(255, 204, 204));
        UPDATE.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        UPDATE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/icon/icons8-update-20 (1).png"))); // NOI18N
        UPDATE.setText("Sửa");
        UPDATE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UPDATEActionPerformed(evt);
            }
        });

        Seach.setBackground(new java.awt.Color(255, 204, 204));
        Seach.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        Seach.setText("Tìm Kiếm");
        Seach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SeachActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jLabel7.setText("Voucher");

        jLabel8.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel8.setText("Giá trị:");

        txt_giatri.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N

        jButton1.setBackground(new java.awt.Color(255, 204, 204));
        jButton1.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/icon/icons8-refresh-20.png"))); // NOI18N
        jButton1.setText("Làm Mới");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        dateChooserNgayBatDau.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N

        dateChooserNgayKetThuc.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel7))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 986, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(3, 3, 3))
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(24, 24, 24)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_ten, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ComboBox_loaivoucher, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_ma, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(59, 59, 59)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel4)
                                .addComponent(jLabel3))
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateChooserNgayBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txt_giatri, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(dateChooserNgayKetThuc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(201, 201, 201)
                            .addComponent(ADD, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(85, 85, 85)
                            .addComponent(UPDATE, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(83, 83, 83)
                            .addComponent(jButton1)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Seach)
                            .addGap(18, 18, 18)
                            .addComponent(txt_timkem, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel7)
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_ma, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(jLabel3)))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(txt_ten, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(dateChooserNgayKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(dateChooserNgayBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(64, 64, 64)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_giatri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(ComboBox_loaivoucher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))))
                .addGap(57, 57, 57)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ADD)
                    .addComponent(UPDATE)
                    .addComponent(jButton1))
                .addGap(44, 44, 44)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_timkem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Seach))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 29, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void Table_VoucherMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Table_VoucherMouseClicked
        // TODO add your handling code here:
        int selectedRow = Table_Voucher.getSelectedRow();
          showData(selectedRow);
    
    }//GEN-LAST:event_Table_VoucherMouseClicked

    private void ADDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ADDActionPerformed
   // Lấy dữ liệu từ các field trên giao diện
    String maVoucher = txt_ma.getText();
String tenVoucher = txt_ten.getText();

// Lấy ngày từ JDateChooser
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

if (r_vc.checkMaExists(maVoucher)) {
    JOptionPane.showMessageDialog(this, "Mã voucher đã tồn tại. Vui lòng nhập mã khác.", "Lỗi", JOptionPane.ERROR_MESSAGE);
    return;
}

// Kiểm tra xem người dùng đã chọn ngày hay chưa
if (dateChooserNgayBatDau.getDate() == null || dateChooserNgayKetThuc.getDate() == null) {
    JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày bắt đầu và ngày kết thúc.", "Lỗi", JOptionPane.ERROR_MESSAGE);
    return;
}

// Kiểm tra ngày bắt đầu phải nhỏ hơn ngày kết thúc
if (dateChooserNgayBatDau.getDate().after(dateChooserNgayKetThuc.getDate())) {
    JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải trước ngày kết thúc.", "Lỗi", JOptionPane.ERROR_MESSAGE);
    return;
}

String ngayBatDau = sdf.format(dateChooserNgayBatDau.getDate());
String ngayKetThuc = sdf.format(dateChooserNgayKetThuc.getDate());

String loaiVoucher = (String) ComboBox_loaivoucher.getSelectedItem();
String giaTriText = txt_giatri.getText();

// Kiểm tra dữ liệu đầu vào
if (maVoucher.isEmpty() || tenVoucher.isEmpty() || loaiVoucher == null || giaTriText.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin voucher.", "Lỗi", JOptionPane.ERROR_MESSAGE);
    return;
}

try {
    double giaTri = Double.parseDouble(giaTriText);
    
    if (giaTri <= 0) {
        JOptionPane.showMessageDialog(this, "Giá trị voucher phải lớn hơn 0.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Kiểm tra nếu là loại "Phần trăm" thì giá trị không được vượt quá 100
    if (loaiVoucher.equalsIgnoreCase("Phần trăm") && giaTri > 100) {
        JOptionPane.showMessageDialog(this, "Giá trị phần trăm không được vượt quá 100%.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Tạo đối tượng Voucher mới
    Voucher newVoucher = new Voucher(0, maVoucher, tenVoucher, ngayBatDau, ngayKetThuc, loaiVoucher, giaTri, 0);

    // Tính trạng thái dựa trên ngày bắt đầu và ngày kết thúc
    int trangThai = newVoucher.tinhTrangThai();
    newVoucher.setTrangThai(trangThai);

    // Hiển thị hộp thoại xác nhận trước khi thêm
    int confirmResult = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn thêm voucher này?", "Xác nhận thêm voucher", JOptionPane.YES_NO_OPTION);
    if (confirmResult == JOptionPane.YES_OPTION) {
        if (r_vc.add(newVoucher) > 0) {
            JOptionPane.showMessageDialog(this, "Thêm voucher thành công.", "Thành công", JOptionPane.INFORMATION_MESSAGE);

            // Cập nhật lại bảng hiển thị
            filltable(r_vc.getAll());

            // Hiển thị dữ liệu của voucher vừa thêm
            showData(r_vc.getAll().size() - 1);

            // Reset các field nhập liệu
            txt_ma.setText("");
            txt_ten.setText("");
            dateChooserNgayBatDau.setDate(null);
            dateChooserNgayKetThuc.setDate(null);
            txt_giatri.setText("");
            ComboBox_loaivoucher.setSelectedIndex(0);
        } else {
            JOptionPane.showMessageDialog(this, "Thêm voucher thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
} catch (NumberFormatException e) {
    JOptionPane.showMessageDialog(this, "Giá trị voucher phải là một số hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
}

  
    }//GEN-LAST:event_ADDActionPerformed

    private void UPDATEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UPDATEActionPerformed
        String maVoucher = txt_ma.getText().trim();
String tenVoucher = txt_ten.getText().trim();

// Kiểm tra ngày bắt đầu và kết thúc
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

String ngayBatDau = sdf.format(dateChooserNgayBatDau.getDate());
String ngayKetThuc = sdf.format(dateChooserNgayKetThuc.getDate());

// Kiểm tra ngày bắt đầu phải nhỏ hơn ngày kết thúc
if (dateChooserNgayBatDau.getDate().after(dateChooserNgayKetThuc.getDate())) {
    JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải trước ngày kết thúc.", "Lỗi", JOptionPane.ERROR_MESSAGE);
    return;
}

// Kiểm tra loại voucher và giá trị voucher
String loaiVoucher = (String) ComboBox_loaivoucher.getSelectedItem();
String giaTriText = txt_giatri.getText().trim().replace(",", "");

// Kiểm tra nếu không chọn voucher
int selectedRow = Table_Voucher.getSelectedRow();
if (selectedRow < 0) {
    JOptionPane.showMessageDialog(this, "Vui lòng chọn một voucher để cập nhật.", "Lỗi", JOptionPane.ERROR_MESSAGE);
    return;
}

// Lấy mã voucher của voucher đã chọn
int voucherId = (int) Table_Voucher.getValueAt(selectedRow, 0);

// Kiểm tra nếu các thông tin quan trọng bị thiếu
if (maVoucher.isEmpty() || tenVoucher.isEmpty() || ngayBatDau == null || ngayKetThuc == null || loaiVoucher == null || giaTriText.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin voucher.", "Lỗi", JOptionPane.ERROR_MESSAGE);
    return;
}

// Kiểm tra giá trị voucher hợp lệ
double giaTri = 0;
try {
    giaTri = Double.parseDouble(giaTriText);
    if (giaTri <= 0) {
        JOptionPane.showMessageDialog(this, "Giá trị voucher phải lớn hơn 0.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Kiểm tra nếu là loại "Phần trăm" thì giá trị không được vượt quá 100
    if (loaiVoucher.equalsIgnoreCase("Phần trăm") && giaTri > 100) {
        JOptionPane.showMessageDialog(this, "Giá trị phần trăm không được vượt quá 100%.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }
} catch (NumberFormatException e) {
    JOptionPane.showMessageDialog(this, "Giá trị voucher phải là một số hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
    return;
}

// Tính trạng thái mới của voucher
Voucher tempVoucher = new Voucher(0, maVoucher, tenVoucher, ngayBatDau, ngayKetThuc, loaiVoucher, giaTri, 0);
int trangThai = tempVoucher.tinhTrangThai();

// Tạo đối tượng voucher mới với trạng thái đã tính
Voucher updatedVoucher = new Voucher(voucherId, maVoucher, tenVoucher, ngayBatDau, ngayKetThuc, loaiVoucher, giaTri, trangThai);

// Thêm hộp thoại xác nhận Yes/No
int confirm = JOptionPane.showConfirmDialog(
    this,
    "Bạn có chắc chắn muốn cập nhật voucher này không?\n" +
    "Mã Voucher: " + maVoucher + "\n" +
    "Tên Voucher: " + tenVoucher,
    "Xác nhận cập nhật",
    JOptionPane.YES_NO_OPTION,
    JOptionPane.QUESTION_MESSAGE
);

// Nếu người dùng chọn Yes, tiến hành cập nhật
if (confirm == JOptionPane.YES_OPTION) {
    // Cập nhật voucher vào cơ sở dữ liệu
    if (r_vc.update(updatedVoucher, voucherId) > 0) {
        JOptionPane.showMessageDialog(this, "Cập nhật voucher thành công.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        filltable(r_vc.getAll());
        Table_Voucher.setRowSelectionInterval(selectedRow, selectedRow);
    } else {
        JOptionPane.showMessageDialog(this, "Cập nhật voucher thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
} else {
    // Nếu người dùng chọn No, hủy thao tác
    JOptionPane.showMessageDialog(this, "Đã hủy cập nhật voucher.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
}

    }//GEN-LAST:event_UPDATEActionPerformed

    private void SeachActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SeachActionPerformed
        // TODO add your handling code here:
        
        searchVoucher();
    }//GEN-LAST:event_SeachActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Đặt lại các trường văn bản về trống
    txt_ma.setText("");
    txt_timkem.setText("");
    txt_ten.setText("");
    dateChooserNgayBatDau.setDate(null); // Xóa ngày trong JDateChooser
    dateChooserNgayKetThuc.setDate(null); // Xóa ngày trong JDateChooser
    txt_giatri.setText("");
    
    // Đặt lại combo box về lựa chọn mặc định
    ComboBox_loaivoucher.setSelectedIndex(0); // Chọn phần tử đầu tiên ("Phần trăm")

    // Đặt lại lựa chọn của bảng (nếu cần)
    Table_Voucher.clearSelection();
    
    // Làm mới bảng với dữ liệu gốc
    filltable(r_vc.getAll());
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ADD;
    private javax.swing.JComboBox<String> ComboBox_loaivoucher;
    private javax.swing.JButton Seach;
    private javax.swing.JTable Table_Voucher;
    private javax.swing.JButton UPDATE;
    private javax.swing.ButtonGroup buttonGroup1;
    private com.toedter.calendar.JDateChooser dateChooserNgayBatDau;
    private com.toedter.calendar.JDateChooser dateChooserNgayKetThuc;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txt_giatri;
    private javax.swing.JTextField txt_ma;
    private javax.swing.JTextField txt_ten;
    private javax.swing.JTextField txt_timkem;
    // End of variables declaration//GEN-END:variables

    
}
