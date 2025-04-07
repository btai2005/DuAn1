/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import model.ChatLieu;
import model.KichCo;
import model.LoaiSanPham;
import model.MauSac;
import model.NhaSanXuat;
import model.SanPhamChiTiet;
import repository.RP_ChatLieu;
import repository.RP_KichCo;
import repository.RP_LoaiSanPham;
import repository.RP_MauSac;
import repository.RP_NhaSanXuat;
import repository.RP_SanPhamChiTiet;

/**
 *
 * @author Dang
 */
public class SanPhamView extends javax.swing.JPanel {
    RP_ChatLieu CLRepo = new RP_ChatLieu();
    ArrayList<ChatLieu> dsChatLieu = new ArrayList<>();
    ArrayList<ChatLieu> dsChatLieuForComboBox = new ArrayList<>();
    RP_KichCo KCRepo = new RP_KichCo();
    ArrayList<KichCo> dsKichCo = new ArrayList<>();
    ArrayList<KichCo> dsKichCoForComboBox = new ArrayList<>();
    RP_LoaiSanPham LSPRepo = new RP_LoaiSanPham();
    ArrayList<LoaiSanPham> dsLoaiSanPham = new ArrayList<>();
    ArrayList<LoaiSanPham> dsLoaiSanPhamForComboBox = new ArrayList<>();
    RP_MauSac MSRepo = new RP_MauSac();
    ArrayList<MauSac> dsMauSac = new ArrayList<>();
    ArrayList<MauSac> dsMauSacForComboBox = new ArrayList<>();
    RP_NhaSanXuat NSXRepo = new RP_NhaSanXuat();
    ArrayList<NhaSanXuat> dsNhaSanXuat = new ArrayList<>();
    ArrayList<NhaSanXuat> dsNhaSanXuatForComboBox = new ArrayList<>();
    RP_SanPhamChiTiet SPCTRepo = new RP_SanPhamChiTiet();
    ArrayList<SanPhamChiTiet> dsSanPhamChiTiet = new ArrayList<>();
    private List<SanPhamChiTiet> dsSanPhamAn;
    
    private String duongDanAnh = null;
    /**
     * Creates new form SanPhamView
     */
    public SanPhamView() {
        initComponents();
        addEventListeners();
        loadComboBoxData();
        rdoChatLieu.setSelected(true);
        loadToTableThuocTinh();
        loadToTableSanPham();
        loadToTableHiddenSanPham();
       
        enableAllRadioButtons(); // Kích hoạt lại tất cả JRadioButton
        btnThemThuocTinh.setEnabled(true); // Đảm bảo nút Thêm khả dụng ban đầu
        
        // Thêm MouseListener cho lblAnhSanPham
        lblAnhSanPham.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                chonAnh();
            }
        });
        
        
    }
    
    private void addEventListeners() {
    rdoChatLieu.addActionListener(evt -> {
        loadToTableThuocTinh();
        loadToTableHiddenThuocTinh();
        resetFormThuocTinh();
    });
    rdoKichCo.addActionListener(evt -> {
        loadToTableThuocTinh();
        loadToTableHiddenThuocTinh();
        resetFormThuocTinh(); // Làm mới form khi chọn Kích cỡ
    });
    rdoLoaiSanPham.addActionListener(evt -> {
        loadToTableThuocTinh();
        loadToTableHiddenThuocTinh();
        resetFormThuocTinh(); // Làm mới form khi chọn Loại sản phẩm
    });
    rdoMauSac.addActionListener(evt -> {
        loadToTableThuocTinh();
        loadToTableHiddenThuocTinh();
        resetFormThuocTinh(); // Làm mới form khi chọn Màu sắc
    });
     rdoNSX.addActionListener(evt -> {
        loadToTableThuocTinh();
        loadToTableHiddenThuocTinh();
        resetFormThuocTinh(); // Làm mới form khi chọn Màu sắc
    });
     
     // Thêm MouseListener cho tblSanPham
        tblSanPham.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tblSanPhamMouseClicked(e);
            }
        });

        // Thêm MouseListener cho tblThuocTinh
        tblThuocTinh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tblThuocTinhMouseClicked(e);
            }
        });
}

    // Load dữ liệu cho tblSanPham
    private void loadToTableSanPham() {
        DefaultTableModel modelSanPham = (DefaultTableModel) tblSanPham.getModel();
        modelSanPham.setRowCount(0); // Xóa dữ liệu cũ

        dsSanPhamChiTiet = SPCTRepo.search();
        if (dsSanPhamChiTiet != null) {
            for (SanPhamChiTiet spct : dsSanPhamChiTiet) {
                modelSanPham.addRow(new Object[]{
                    spct.getId(),
                    spct.getMaSanPhamChiTiet(),
                    spct.getTenSanPhamChiTiet(),
                    LSPRepo.getTenLoaiSanPham(spct.getIdLoaiSanPham()),
                    spct.getAnhSanPham(),
                    formatGia(spct.getGiaSanPham()),
                    spct.getSoLuong(),
                    MSRepo.getTenMauSac(spct.getIdMauSac()), 
                    CLRepo.getTenChatLieu(spct.getIdChatLieu()),
                    KCRepo.getSizeKichCo(spct.getIdKichCo()), 
                    NSXRepo.getTenNSX(spct.getIdNSX()),
                    spct.getMoTa(),
                });
            }
        }
    }
    
    // Phương thức tải dữ liệu cho các JComboBox
    private void loadComboBoxData() {
        // Tải dữ liệu cho cboLoaiSanPham
        dsLoaiSanPhamForComboBox = LSPRepo.getAllForComboBox();
        cboLoaiSanPham.removeAllItems();
        for (LoaiSanPham lsp : dsLoaiSanPhamForComboBox) {
            cboLoaiSanPham.addItem(lsp.getTenLoaiSanPham());
        }

        // Tải dữ liệu cho cboMauSac
        dsMauSacForComboBox = MSRepo.getAllForComboBox();
        cboMauSac.removeAllItems();
        for (MauSac ms : dsMauSacForComboBox) {
            cboMauSac.addItem(ms.getTenMau());
        }

        // Tải dữ liệu cho cboChatLieu
        dsChatLieuForComboBox = CLRepo.getAllForComboBox();
        cboChatLieu.removeAllItems();
        for (ChatLieu cl : dsChatLieuForComboBox) {
            cboChatLieu.addItem(cl.getTenChatLieu());
        }

        // Tải dữ liệu cho cboKichCo
        dsKichCoForComboBox = KCRepo.getAllForComboBox();
        cboKichCo.removeAllItems();
        for (KichCo kc : dsKichCoForComboBox) {
            cboKichCo.addItem(kc.getSize().toString());
        }

        // Tải dữ liệu cho cboNhaSanXuat
        dsNhaSanXuatForComboBox = NSXRepo.getAllForComboBox();
        cboNSX.removeAllItems();
        for (NhaSanXuat nsx : dsNhaSanXuatForComboBox) {
            cboNSX.addItem(nsx.getTenNSX());
        }
    }
    
    // Lấy dữ liệu từ form cho tblSanPham
    private SanPhamChiTiet getFormDataSanPham() {
        String maSanPham = txtMaSanPham.getText().trim();
        String tenSanPham = txtTenSanPham.getText().trim();
        String giaStr = txtGia.getText().trim();
        String soLuongStr = txtSoLuong.getText().trim();
        String moTa = txtMoTa.getText().trim();

        // Kiểm tra dữ liệu đầu vào
        if (maSanPham.isEmpty() || tenSanPham.isEmpty() || giaStr.isEmpty() || soLuongStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin bắt buộc!");
            return null;
        }
        
        // Kiểm tra ảnh sản phẩm
    if (duongDanAnh == null || duongDanAnh.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn ảnh sản phẩm!");
        return null;
    }

        BigDecimal gia;
        int soLuong;
        try {
            // Chuyển đổi giá từ chuỗi đã format (ví dụ: "2.000.000") thành BigDecimal
            String giaNum = giaStr.replace(".", ""); // Xóa dấu chấm phân tách
            gia = new BigDecimal(giaNum);
            soLuong = Integer.parseInt(soLuongStr);
            
            // Kiểm tra giá không âm
        if (gia.compareTo(BigDecimal.ZERO) < 0) {
            JOptionPane.showMessageDialog(this, "Giá không được là số âm!");
            return null;
        }

        // Kiểm tra số lượng không âm
        if (soLuong < 0) {
            JOptionPane.showMessageDialog(this, "Số lượng không được là số âm!");
            return null;
        }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá và số lượng phải là số hợp lệ!");
            return null;
        }

        // Lấy ID từ các JComboBox
        int selectedLoaiSanPhamIndex = cboLoaiSanPham.getSelectedIndex();
        int idLoaiSanPham = selectedLoaiSanPhamIndex >= 0 ? dsLoaiSanPhamForComboBox.get(selectedLoaiSanPhamIndex).getId() : -1;

        int selectedMauSacIndex = cboMauSac.getSelectedIndex();
        int idMauSac = selectedMauSacIndex >= 0 ? dsMauSacForComboBox.get(selectedMauSacIndex).getId() : -1;

        int selectedChatLieuIndex = cboChatLieu.getSelectedIndex();
        int idChatLieu = selectedChatLieuIndex >= 0 ? dsChatLieuForComboBox.get(selectedChatLieuIndex).getId() : -1;

        int selectedKichCoIndex = cboKichCo.getSelectedIndex();
        int idKichCo = selectedKichCoIndex >= 0 ? dsKichCoForComboBox.get(selectedKichCoIndex).getId() : -1;

        int selectedNhaSanXuatIndex = cboNSX.getSelectedIndex();
        int idNhaSanXuat = selectedNhaSanXuatIndex >= 0 ? dsNhaSanXuatForComboBox.get(selectedNhaSanXuatIndex).getId() : -1;

        // Kiểm tra xem có chọn giá trị hợp lệ không
        if (idLoaiSanPham == -1 || idMauSac == -1 || idChatLieu == -1 || idKichCo == -1 || idNhaSanXuat == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ các thuộc tính (Loại sản phẩm, Màu sắc, Chất liệu, Kích cỡ, Nhà sản xuất)!");
            return null;
        }

        // Tạo đối tượng SanPhamChiTiet
        return new SanPhamChiTiet(0, maSanPham, tenSanPham, gia, duongDanAnh, soLuong, moTa, idChatLieu, idKichCo, idMauSac, idNhaSanXuat, idLoaiSanPham, 0, null, null);
    }
    
    // Reset form cho tblSanPham
    private void resetFormSanPham() {
        txtMaSanPham.setText("");
        txtTenSanPham.setText("");
        txtGia.setText("");
        txtSoLuong.setText("");
        txtMoTa.setText("");
        lblAnhSanPham.setIcon(null);
        lblAnhSanPham.setText("Nhấn để chọn ảnh");
        duongDanAnh = null;

        // Reset các JComboBox
        cboLoaiSanPham.setSelectedIndex(-1);
        cboMauSac.setSelectedIndex(-1);
        cboChatLieu.setSelectedIndex(-1);
        cboKichCo.setSelectedIndex(-1);
        cboNSX.setSelectedIndex(-1);

        // Enable các trường nhập liệu
        txtMaSanPham.setEditable(true);
        btnThemSanPham.setEnabled(true);
    }
    
    
    // Hàm load dữ liệu lên bảng dựa trên JRadioButton được chọn
    private void loadToTableThuocTinh() {
        DefaultTableModel model = (DefaultTableModel) tblThuocTinh.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ trên bảng

        if (rdoChatLieu.isSelected()) {
            dsChatLieu = CLRepo.search(); // Lấy danh sách Chất liệu có TrangThai = 0
            for (ChatLieu cl : dsChatLieu) {
                model.addRow(new Object[]{
                    cl.getId(),
                    cl.getMaChatLieu(),
                    cl.getTenChatLieu()
                });
            }
        } else if (rdoKichCo.isSelected()) {
            dsKichCo = KCRepo.search(); // Lấy danh sách Kích cỡ có TrangThai = 0
            for (KichCo kc : dsKichCo) {
                model.addRow(new Object[]{
                    kc.getId(),
                    kc.getMaKichCo(),
                    kc.getSize()
                });
            }
        } else if (rdoLoaiSanPham.isSelected()) {
            dsLoaiSanPham = LSPRepo.search(); // Lấy danh sách Loại sản phẩm có TrangThai = 0
            for (LoaiSanPham lsp : dsLoaiSanPham) {
                model.addRow(new Object[]{
                    lsp.getId(),
                    lsp.getMaLoaiSanPham(),
                    lsp.getTenLoaiSanPham()
                });
            }
        } else if (rdoMauSac.isSelected()) {
            dsMauSac = MSRepo.search(); // Lấy danh sách Màu sắc có TrangThai = 0
            for (MauSac ms : dsMauSac) {
                model.addRow(new Object[]{
                    ms.getId(),
                    ms.getMaMauSac(),
                    ms.getTenMau()
                });
            }
        }else if (rdoNSX.isSelected()) {
            dsNhaSanXuat = NSXRepo.search(); // Lấy danh sách NSX có TrangThai = 0
            for (NhaSanXuat nsx : dsNhaSanXuat) {
                model.addRow(new Object[]{
                    nsx.getId(),
                    nsx.getMaNSX(),
                    nsx.getTenNSX()
                });
            }
        }
        
    }
    
    private Object getFormDataThuocTinh() {
        String maThuocTinh = txtMaThuocTinh.getText().trim();
        String tenThuocTinh = txtTenThuocTinh.getText().trim();

        // Kiểm tra dữ liệu đầu vào
        if (maThuocTinh.isEmpty() || tenThuocTinh.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ mã và tên thuộc tính!");
            return null;
        }

        if (rdoChatLieu.isSelected()) {
            return new ChatLieu(0, maThuocTinh, tenThuocTinh, null, null, 0);
        } else if (rdoKichCo.isSelected()) {
            try {
                BigDecimal size = new BigDecimal(tenThuocTinh); // Tên thuộc tính là kích cỡ (số)
                // Kiểm tra kích cỡ không âm
                 if (size.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this, "Kích cỡ không được là số âm!");
                return null;
            }
                return new KichCo(0, maThuocTinh, size, null, null, 0);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Kích cỡ phải là một số hợp lệ!");
                return null;
            }
        } else if (rdoLoaiSanPham.isSelected()) {
            return new LoaiSanPham(0, maThuocTinh, tenThuocTinh, null, null, 0);
        } else if (rdoMauSac.isSelected()) {
            return new MauSac(0, maThuocTinh, tenThuocTinh, null, null, 0);
        } else if (rdoNSX.isSelected()) {
            return new NhaSanXuat(0, maThuocTinh, tenThuocTinh, null, null, 0);
        }else{
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại thuộc tính!");
            return null;
        }
    }
    
    private void resetFormThuocTinh() {
    txtMaThuocTinh.setText(""); // Xóa nội dung trường mã thuộc tính
    txtTenThuocTinh.setText(""); // Xóa nội dung trường tên thuộc tính
    txtMaThuocTinh.setEditable(true); // Đặt lại trường mã thuộc tính thành có thể chỉnh sửa
    enableAllRadioButtons();
    btnThemThuocTinh.setEnabled(true);
}
    
    private void disableOtherRadioButtons() {
    if (rdoChatLieu.isSelected()) {
        rdoKichCo.setEnabled(false);
        rdoLoaiSanPham.setEnabled(false);
        rdoMauSac.setEnabled(false);
        rdoNSX.setEnabled(false);
    } else if (rdoKichCo.isSelected()) {
        rdoChatLieu.setEnabled(false);
        rdoLoaiSanPham.setEnabled(false);
        rdoMauSac.setEnabled(false);
        rdoNSX.setEnabled(false);
    } else if (rdoLoaiSanPham.isSelected()) {
        rdoChatLieu.setEnabled(false);
        rdoKichCo.setEnabled(false);
        rdoMauSac.setEnabled(false);
        rdoNSX.setEnabled(false);
    } else if (rdoMauSac.isSelected()) {
        rdoChatLieu.setEnabled(false);
        rdoKichCo.setEnabled(false);
        rdoLoaiSanPham.setEnabled(false);
        rdoNSX.setEnabled(false);
    }else if (rdoNSX.isSelected()) {
        rdoChatLieu.setEnabled(false);
        rdoKichCo.setEnabled(false);
        rdoLoaiSanPham.setEnabled(false);
        rdoMauSac.setEnabled(false);
    }
}
    
    private void enableAllRadioButtons() {
    rdoChatLieu.setEnabled(true);
    rdoKichCo.setEnabled(true);
    rdoLoaiSanPham.setEnabled(true);
    rdoMauSac.setEnabled(true);
    rdoNSX.setEnabled(true);
}
    
    // Phương thức chọn ảnh
    private void chonAnh() {
        JFileChooser fileChooser = new JFileChooser();
        // Chỉ cho phép chọn file ảnh (jpg, png, gif)
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            duongDanAnh = selectedFile.getAbsolutePath(); // Lưu đường dẫn ảnh

            // Hiển thị ảnh trên JLabel
            try {
                ImageIcon imageIcon = new ImageIcon(duongDanAnh);
                // Resize ảnh để vừa với JLabel (giả sử kích thước JLabel là 100x100)
                Image image = imageIcon.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH);
                lblAnhSanPham.setIcon(new ImageIcon(image));
                lblAnhSanPham.setText(""); // Xóa text "Ảnh Sản Phẩm" khi có ảnh
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải ảnh: " + e.getMessage());
            }
        }
    }
    
    private String formatGia(BigDecimal gia) {
    // Chuyển BigDecimal thành long để bỏ 2 số 0 sau dấu phẩy
    long giaLong = gia.longValue();

    // Format giá với dấu chấm phân tách
    DecimalFormat df = new DecimalFormat("#,###");
    df.setGroupingSize(3); // Phân tách mỗi 3 số bằng dấu chấm
    return df.format(giaLong).replace(",", ".");
}
    
    private void loadToTableHiddenSanPham() {
        DefaultTableModel model = (DefaultTableModel) tblSanPhamAn.getModel();
        model.setRowCount(0);

        dsSanPhamAn = SPCTRepo.searchHidden();
        if (dsSanPhamAn != null) {
            for (SanPhamChiTiet spct : dsSanPhamAn) {
                model.addRow(new Object[]{
                    spct.getId(),
                    spct.getMaSanPhamChiTiet(),
                    spct.getTenSanPhamChiTiet(),
                    LSPRepo.getTenLoaiSanPham(spct.getIdLoaiSanPham()),
                    spct.getAnhSanPham(),
                    formatGia(spct.getGiaSanPham()),
                    spct.getSoLuong(),
                    MSRepo.getTenMauSac(spct.getIdMauSac()), 
                    CLRepo.getTenChatLieu(spct.getIdChatLieu()), 
                    KCRepo.getSizeKichCo(spct.getIdKichCo()), 
                    NSXRepo.getTenNSX(spct.getIdNSX()),
                    spct.getMoTa(),
                });
            }
        }
    }
    
    private void loadToTableHiddenThuocTinh() {
       DefaultTableModel model = (DefaultTableModel) tblThuocTinhAn.getModel();
    model.setRowCount(0); // Xóa dữ liệu cũ

    ArrayList<Object[]> data = new ArrayList<>();

    if (rdoChatLieu.isSelected()) {
        data = CLRepo.getAllHidden();
    } else if (rdoMauSac.isSelected()) {
        data = MSRepo.getAllHidden();
    } else if (rdoKichCo.isSelected()) {
        data = KCRepo.getAllHidden();
    } else if (rdoLoaiSanPham.isSelected()) {
        data = LSPRepo.getAllHidden();
    } else if (rdoNSX.isSelected()) {
        data = NSXRepo.getAllHidden();
    } else {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn một loại thuộc tính để hiển thị thuộc tính ẩn!");
        return;
    }

    for (Object[] row : data) {
        model.addRow(row);
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
        SanPhamDialog = new javax.swing.JDialog();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblSanPhamAn = new javax.swing.JTable();
        btnHienThiLai = new javax.swing.JButton();
        ThuocTinhDialog = new javax.swing.JDialog();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblThuocTinhAn = new javax.swing.JTable();
        btnHienThiLaiThuocTinh = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtMaSanPham = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtTenSanPham = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cboLoaiSanPham = new javax.swing.JComboBox<>();
        lblAnhSanPham = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtGia = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        cboMauSac = new javax.swing.JComboBox<>();
        cboKichCo = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        cboChatLieu = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtMoTa = new javax.swing.JTextArea();
        jLabel14 = new javax.swing.JLabel();
        cboNSX = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSanPham = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        btnThemSanPham = new javax.swing.JButton();
        btnSuaSanPham = new javax.swing.JButton();
        btnAnSanPham = new javax.swing.JButton();
        btnLamMoiSanPham = new javax.swing.JButton();
        btnHienThiSanPhamAn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtMaThuocTinh = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtTenThuocTinh = new javax.swing.JTextField();
        btnThemThuocTinh = new javax.swing.JButton();
        btnSuaThuocTinh = new javax.swing.JButton();
        btnAnThuocTinh = new javax.swing.JButton();
        rdoMauSac = new javax.swing.JRadioButton();
        rdoKichCo = new javax.swing.JRadioButton();
        rdoLoaiSanPham = new javax.swing.JRadioButton();
        rdoChatLieu = new javax.swing.JRadioButton();
        rdoNSX = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblThuocTinh = new javax.swing.JTable();
        btnHienThiThuocTinhAn = new javax.swing.JButton();

        SanPhamDialog.setMinimumSize(new java.awt.Dimension(1000, 400));

        tblSanPhamAn.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        tblSanPhamAn.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Id", "Mã Sản Phẩm", "Tên Sản Phẩm", "Loại Sản Phẩm","Ảnh",
                "Đơn Giá","Số Lượng","Màu Sắc","Chất Liệu","Kích Cỡ","NSX","Mô Tả"
            }
        ));
        jScrollPane4.setViewportView(tblSanPhamAn);

        btnHienThiLai.setBackground(new java.awt.Color(255, 204, 204));
        btnHienThiLai.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btnHienThiLai.setText("Hiển Thị Lại");
        btnHienThiLai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHienThiLaiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout SanPhamDialogLayout = new javax.swing.GroupLayout(SanPhamDialog.getContentPane());
        SanPhamDialog.getContentPane().setLayout(SanPhamDialogLayout);
        SanPhamDialogLayout.setHorizontalGroup(
            SanPhamDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SanPhamDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 838, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SanPhamDialogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnHienThiLai, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57))
        );
        SanPhamDialogLayout.setVerticalGroup(
            SanPhamDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SanPhamDialogLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnHienThiLai)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        ThuocTinhDialog.setMinimumSize(new java.awt.Dimension(800, 400));

        tblThuocTinhAn.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        tblThuocTinhAn.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Mã Thuộc Tính", "Tên Thuộc Tính"
            }
        ));
        jScrollPane5.setViewportView(tblThuocTinhAn);

        btnHienThiLaiThuocTinh.setBackground(new java.awt.Color(255, 204, 204));
        btnHienThiLaiThuocTinh.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btnHienThiLaiThuocTinh.setText("Hiển Thị Lại");
        btnHienThiLaiThuocTinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHienThiLaiThuocTinhActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ThuocTinhDialogLayout = new javax.swing.GroupLayout(ThuocTinhDialog.getContentPane());
        ThuocTinhDialog.getContentPane().setLayout(ThuocTinhDialogLayout);
        ThuocTinhDialogLayout.setHorizontalGroup(
            ThuocTinhDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThuocTinhDialogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnHienThiLaiThuocTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64))
            .addGroup(ThuocTinhDialogLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 721, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        ThuocTinhDialogLayout.setVerticalGroup(
            ThuocTinhDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThuocTinhDialogLayout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnHienThiLaiThuocTinh)
                .addGap(14, 14, 14))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Quản lý sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 14))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel3.setText("Mã Sản Phẩm:");

        txtMaSanPham.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel4.setText("Tên Sản Phẩm:");

        txtTenSanPham.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel9.setText("Loại Sản Phẩm:");

        cboLoaiSanPham.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        cboLoaiSanPham.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblAnhSanPham.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        lblAnhSanPham.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAnhSanPham.setText("Nhấn để chọn ảnh");
        lblAnhSanPham.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel6.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel6.setText("Giá:");

        txtGia.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel7.setText("Số Lượng:");

        txtSoLuong.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N

        cboMauSac.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        cboMauSac.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboKichCo.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        cboKichCo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel13.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel13.setText("Màu Sắc:");

        jLabel10.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel10.setText("Chất Liệu:");

        jLabel12.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel12.setText("Kích Cỡ:");

        cboChatLieu.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        cboChatLieu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel8.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel8.setText("Mô Tả:");

        txtMoTa.setColumns(20);
        txtMoTa.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        txtMoTa.setRows(5);
        jScrollPane1.setViewportView(txtMoTa);

        jLabel14.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel14.setText("Nhà SX:");

        cboNSX.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        cboNSX.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(lblAnhSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel4)
                        .addComponent(jLabel3)
                        .addComponent(jLabel9))
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboLoaiSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTenSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(66, 66, 66)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel10)
                    .addComponent(jLabel12)
                    .addComponent(jLabel14)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(cboChatLieu, 0, 287, Short.MAX_VALUE)
                        .addComponent(cboMauSac, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cboKichCo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cboNSX, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(21, 21, 21))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtMaSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel13)
                                .addComponent(cboMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtTenSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel10)
                                .addComponent(cboChatLieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboLoaiSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(cboKichCo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(cboNSX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8)
                                .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(lblAnhSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 14))); // NOI18N

        tblSanPham.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        tblSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Id", "Mã Sản Phẩm", "Tên Sản Phẩm", "Loại Sản Phẩm","Ảnh",
                "Đơn Giá","Số Lượng","Màu Sắc","Chất Liệu","Kích Cỡ","NSX","Mô Tả"
            }
        ));
        tblSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSanPhamMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblSanPham);

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel1.setText("Tìm Kiếm Sản Phẩm:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnThemSanPham.setBackground(new java.awt.Color(255, 204, 204));
        btnThemSanPham.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btnThemSanPham.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/icon/icons8-add-20.png"))); // NOI18N
        btnThemSanPham.setText("Thêm");
        btnThemSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemSanPhamActionPerformed(evt);
            }
        });

        btnSuaSanPham.setBackground(new java.awt.Color(255, 204, 204));
        btnSuaSanPham.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btnSuaSanPham.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/icon/icons8-update-20 (1).png"))); // NOI18N
        btnSuaSanPham.setText("Sửa");
        btnSuaSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaSanPhamActionPerformed(evt);
            }
        });

        btnAnSanPham.setBackground(new java.awt.Color(255, 204, 204));
        btnAnSanPham.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btnAnSanPham.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/icon/icons8-hide-20.png"))); // NOI18N
        btnAnSanPham.setText("Ẩn");
        btnAnSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnSanPhamActionPerformed(evt);
            }
        });

        btnLamMoiSanPham.setBackground(new java.awt.Color(255, 204, 204));
        btnLamMoiSanPham.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btnLamMoiSanPham.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/icon/icons8-refresh-20.png"))); // NOI18N
        btnLamMoiSanPham.setText("Mới");
        btnLamMoiSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiSanPhamActionPerformed(evt);
            }
        });

        btnHienThiSanPhamAn.setBackground(new java.awt.Color(255, 204, 204));
        btnHienThiSanPhamAn.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btnHienThiSanPhamAn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/icon/icons8-show-20.png"))); // NOI18N
        btnHienThiSanPhamAn.setText("Hiển Thị Sản Phẩm Ẩn");
        btnHienThiSanPhamAn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHienThiSanPhamAnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(btnThemSanPham)
                .addGap(56, 56, 56)
                .addComponent(btnSuaSanPham)
                .addGap(62, 62, 62)
                .addComponent(btnAnSanPham)
                .addGap(59, 59, 59)
                .addComponent(btnLamMoiSanPham)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnHienThiSanPhamAn, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemSanPham)
                    .addComponent(btnSuaSanPham)
                    .addComponent(btnAnSanPham)
                    .addComponent(btnLamMoiSanPham)
                    .addComponent(btnHienThiSanPhamAn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Thông tin chi tiết sản phẩm", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thuộc tính sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 14))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel2.setText("Mã Thuộc Tính:");

        txtMaThuocTinh.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel11.setText("Tên Thuộc Tính:");

        txtTenThuocTinh.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N

        btnThemThuocTinh.setBackground(new java.awt.Color(255, 204, 204));
        btnThemThuocTinh.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btnThemThuocTinh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/icon/icons8-add-20.png"))); // NOI18N
        btnThemThuocTinh.setText("Thêm");
        btnThemThuocTinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemThuocTinhActionPerformed(evt);
            }
        });

        btnSuaThuocTinh.setBackground(new java.awt.Color(255, 204, 204));
        btnSuaThuocTinh.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btnSuaThuocTinh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/icon/icons8-update-20 (1).png"))); // NOI18N
        btnSuaThuocTinh.setText("Sửa");
        btnSuaThuocTinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaThuocTinhActionPerformed(evt);
            }
        });

        btnAnThuocTinh.setBackground(new java.awt.Color(255, 204, 204));
        btnAnThuocTinh.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btnAnThuocTinh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/icon/icons8-hide-20.png"))); // NOI18N
        btnAnThuocTinh.setText("Ẩn");
        btnAnThuocTinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnThuocTinhActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoMauSac);
        rdoMauSac.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        rdoMauSac.setText("Màu Sắc");

        buttonGroup1.add(rdoKichCo);
        rdoKichCo.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        rdoKichCo.setText("Kích Cỡ");

        buttonGroup1.add(rdoLoaiSanPham);
        rdoLoaiSanPham.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        rdoLoaiSanPham.setText("Loại Sản Phẩm");

        buttonGroup1.add(rdoChatLieu);
        rdoChatLieu.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        rdoChatLieu.setText("Chất Liệu");

        buttonGroup1.add(rdoNSX);
        rdoNSX.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        rdoNSX.setText("Nhà Sản Xuất");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMaThuocTinh)
                            .addComponent(txtTenThuocTinh, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE))
                        .addGap(139, 139, 139)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rdoChatLieu)
                            .addComponent(rdoMauSac))
                        .addGap(70, 70, 70)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(rdoKichCo)
                                .addGap(60, 60, 60)
                                .addComponent(rdoNSX))
                            .addComponent(rdoLoaiSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(btnThemThuocTinh)
                        .addGap(51, 51, 51)
                        .addComponent(btnSuaThuocTinh)
                        .addGap(58, 58, 58)
                        .addComponent(btnAnThuocTinh)))
                .addContainerGap(67, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtMaThuocTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdoMauSac)
                            .addComponent(rdoKichCo)
                            .addComponent(rdoNSX))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(txtTenThuocTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdoChatLieu)
                            .addComponent(rdoLoaiSanPham))
                        .addGap(11, 11, 11)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemThuocTinh)
                    .addComponent(btnSuaThuocTinh)
                    .addComponent(btnAnThuocTinh))
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin thuộc tính", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 14))); // NOI18N

        tblThuocTinh.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        tblThuocTinh.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Id","Mã Thuộc Tính", "Tên Thuộc Tính"
            }
        ));
        tblThuocTinh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblThuocTinhMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblThuocTinh);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnHienThiThuocTinhAn.setBackground(new java.awt.Color(255, 204, 204));
        btnHienThiThuocTinhAn.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btnHienThiThuocTinhAn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/icon/icons8-show-20.png"))); // NOI18N
        btnHienThiThuocTinhAn.setText("Hiện Thị Thuộc Tính Ẩn");
        btnHienThiThuocTinhAn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHienThiThuocTinhAnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(10, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnHienThiThuocTinhAn)
                .addGap(135, 135, 135))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnHienThiThuocTinhAn)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Thuộc tính sản phẩm", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("Thông tin chi tiết");
    }// </editor-fold>//GEN-END:initComponents

    private void btnThemSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemSanPhamActionPerformed
        SanPhamChiTiet spct = getFormDataSanPham();
        if (spct == null) {
            return;
        }

        // Kiểm tra mã trùng
        if (SPCTRepo.checkMaExists(spct.getMaSanPhamChiTiet())) {
            JOptionPane.showMessageDialog(this, "Mã sản phẩm chi tiết đã tồn tại!");
            return;
        }
        
        if (SPCTRepo.checkTenExists(spct.getTenSanPhamChiTiet())) {
                JOptionPane.showMessageDialog(this, "Tên sản phẩm đã tồn tại!");
                return;
            }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn thêm sản phẩm chi tiết này không?",
                "Xác nhận thêm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            SPCTRepo.create(spct);
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm chi tiết thành công!");
            loadToTableSanPham();
            resetFormSanPham();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm sản phẩm chi tiết: " + e.getMessage());
        }
    }//GEN-LAST:event_btnThemSanPhamActionPerformed

    private void btnThemThuocTinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemThuocTinhActionPerformed
       Object data = getFormDataThuocTinh();
        if (data == null) {
            return; // Dữ liệu không hợp lệ, dừng lại
        }
        
        // Thêm thông báo xác nhận
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn thêm thuộc tính này không?", 
                "Xác nhận thêm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return; // Nếu chọn No, dừng lại
        }
        
        String maThuocTinh = txtMaThuocTinh.getText().trim();
        String tenThuocTinh = txtTenThuocTinh.getText().trim();

        try {
            if (rdoChatLieu.isSelected()) {
                if (CLRepo.checkMaExists(maThuocTinh)) {
                    JOptionPane.showMessageDialog(this, "Mã chất liệu đã tồn tại!");
                    return;
                }
                if (CLRepo.checkTenExists(tenThuocTinh)) {
                JOptionPane.showMessageDialog(this, "Tên chất liệu đã tồn tại!");
                return;
                }
                CLRepo.create((ChatLieu) data);
                JOptionPane.showMessageDialog(this, "Thêm chất liệu thành công!");
            } else if (rdoKichCo.isSelected()) {
                if (KCRepo.checkMaExists(maThuocTinh)) {
                    JOptionPane.showMessageDialog(this, "Mã kích cỡ đã tồn tại!");
                    return;
                }
                if (KCRepo.checkTenExists(tenThuocTinh)) {
                JOptionPane.showMessageDialog(this, "Kích cỡ đã tồn tại!");
                return;
            }
                KCRepo.create((KichCo) data);
                JOptionPane.showMessageDialog(this, "Thêm kích cỡ thành công!");
            } else if (rdoLoaiSanPham.isSelected()) {
                if (LSPRepo.checkMaExists(maThuocTinh)) {
                    JOptionPane.showMessageDialog(this, "Mã loại sản phẩm đã tồn tại!");
                    return;
                }
                if (LSPRepo.checkTenExists(tenThuocTinh)) {
                JOptionPane.showMessageDialog(this, "Tên loại sản phẩm đã tồn tại!");
                return;
            }
                LSPRepo.create((LoaiSanPham) data);
                JOptionPane.showMessageDialog(this, "Thêm loại sản phẩm thành công!");
            } else if (rdoMauSac.isSelected()) {
                if (MSRepo.checkMaExists(maThuocTinh)) {
                    JOptionPane.showMessageDialog(this, "Mã màu sắc đã tồn tại!");
                    return;
                }
                if (MSRepo.checkTenExists(tenThuocTinh)) {
                JOptionPane.showMessageDialog(this, "Tên màu sắc đã tồn tại!");
                return;
            }
                MSRepo.create((MauSac) data);
                JOptionPane.showMessageDialog(this, "Thêm màu sắc thành công!");
            }else if (rdoNSX.isSelected()) {
                if (NSXRepo.checkMaExists(maThuocTinh)) {
                    JOptionPane.showMessageDialog(this, "Mã nhà sản xuất đã tồn tại!");
                    return;
                }
                if (NSXRepo.checkTenExists(tenThuocTinh)) {
                JOptionPane.showMessageDialog(this, "Tên nhà sản xuất đã tồn tại!");
                return;
            }
                NSXRepo.create((NhaSanXuat) data);
                JOptionPane.showMessageDialog(this, "Thêm nhà sản xuất thành công!");
            }
            loadToTableThuocTinh(); // Load lại bảng sau khi thêm
            loadComboBoxData(); // Làm mới JComboBox
            resetFormThuocTinh(); // Làm mới form
            enableAllRadioButtons(); // Kích hoạt lại tất cả JRadioButton
            btnThemThuocTinh.setEnabled(true); // Kích hoạt lại nút Thêm
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnThemThuocTinhActionPerformed

    private void btnSuaThuocTinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaThuocTinhActionPerformed
        int selectedRow = tblThuocTinh.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn một thuộc tính để sửa!");
        return;
    }

    // Lấy ID từ bảng
    String id = tblThuocTinh.getValueAt(selectedRow, 0).toString(); // Lấy ID từ bảng

    // Lấy giá trị mới từ các trường văn bản
    String maThuocTinh = txtMaThuocTinh.getText().trim(); // Mã thuộc tính
    String tenThuocTinh = txtTenThuocTinh.getText().trim(); // Tên thuộc tính

    // Kiểm tra dữ liệu đầu vào
    if (maThuocTinh.isEmpty() || tenThuocTinh.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ mã và tên thuộc tính!");
        return;
    }

    // Cập nhật thông tin thuộc tính mới
    Object data = getFormDataThuocTinh();
    if (data == null) {
        return; // Dữ liệu không hợp lệ, dừng lại
    }
    
    // Thêm thông báo xác nhận
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn sửa thuộc tính này không?", 
                "Xác nhận sửa", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return; // Nếu chọn No, dừng lại
        }

    try {
        int idThuocTinh = Integer.parseInt(id);
        if (rdoChatLieu.isSelected()) {
            if (CLRepo.checkTenExistsExcludingId(tenThuocTinh, idThuocTinh)) {
                JOptionPane.showMessageDialog(this, "Tên chất liệu đã tồn tại!");
                return;
            }
            ChatLieu cl = (ChatLieu) data;
            cl.setId(idThuocTinh);
            CLRepo.update(cl);
            JOptionPane.showMessageDialog(this, "Sửa chất liệu thành công!");
        } else if (rdoKichCo.isSelected()) {
            if (KCRepo.checkTenExistsExcludingId(tenThuocTinh, idThuocTinh)) {
                JOptionPane.showMessageDialog(this, "Kích cỡ đã tồn tại!");
                return;
            }
            KichCo kc = (KichCo) data;
            kc.setId(idThuocTinh);
            KCRepo.update(kc);
            JOptionPane.showMessageDialog(this, "Sửa kích cỡ thành công!");
        } else if (rdoLoaiSanPham.isSelected()) {
            if (LSPRepo.checkTenExistsExcludingId(tenThuocTinh, idThuocTinh)) {
                JOptionPane.showMessageDialog(this, "Tên loại sản phẩm đã tồn tại!");
                return;
            }
            LoaiSanPham lsp = (LoaiSanPham) data;
            lsp.setId(idThuocTinh);
            LSPRepo.update(lsp);
            JOptionPane.showMessageDialog(this, "Sửa loại sản phẩm thành công!");
        } else if (rdoMauSac.isSelected()) {
            if (MSRepo.checkTenExistsExcludingId(tenThuocTinh, idThuocTinh)) {
                JOptionPane.showMessageDialog(this, "Tên màu sắc đã tồn tại!");
                return;
            }
            MauSac ms = (MauSac) data;
            ms.setId(idThuocTinh);
            MSRepo.update(ms);
            JOptionPane.showMessageDialog(this, "Sửa màu sắc thành công!");
        }else if (rdoNSX.isSelected()) {
            if (NSXRepo.checkTenExistsExcludingId(tenThuocTinh, idThuocTinh)) {
                JOptionPane.showMessageDialog(this, "Tên nhà sản xuất đã tồn tại!");
                return;
            }
            NhaSanXuat nsx = (NhaSanXuat) data;
            nsx.setId(idThuocTinh);
            NSXRepo.update(nsx);
            JOptionPane.showMessageDialog(this, "Sửa nhà sản xuất thành công!");
        }

        loadToTableThuocTinh(); // Load lại bảng sau khi sửa
        loadToTableSanPham();
        loadToTableHiddenSanPham();
        loadComboBoxData(); // Làm mới JComboBox
        resetFormThuocTinh(); // Làm mới form
        enableAllRadioButtons(); // Kích hoạt lại tất cả JRadioButton
        btnThemThuocTinh.setEnabled(true); // Kích hoạt lại nút Thêm
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Lỗi khi sửa: " + e.getMessage());
        e.printStackTrace();
    }
    }//GEN-LAST:event_btnSuaThuocTinhActionPerformed

    private void tblThuocTinhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblThuocTinhMouseClicked
    // Kiểm tra vị trí click
    int row = tblThuocTinh.rowAtPoint(evt.getPoint());
    if (row == -1) {
        // Click vào vùng trống
        tblThuocTinh.clearSelection();
        resetFormThuocTinh();
        enableAllRadioButtons();
        btnThemThuocTinh.setEnabled(true);
        return;
    }

    int selectedRow = tblThuocTinh.getSelectedRow();
    if (selectedRow == -1) {
        // Không có dòng nào được chọn (trường hợp dự phòng)
        resetFormThuocTinh();
        enableAllRadioButtons();
        btnThemThuocTinh.setEnabled(true);
        return;
    }

    // Lấy ID, Mã và Tên thuộc tính từ bảng
    String id = tblThuocTinh.getValueAt(selectedRow, 0).toString(); // Lấy ID từ bảng
    String maThuocTinh = tblThuocTinh.getValueAt(selectedRow, 1).toString(); // Mã thuộc tính
    String tenThuocTinh = tblThuocTinh.getValueAt(selectedRow, 2).toString(); // Tên thuộc tính

    // Hiển thị ID và Mã thuộc tính lên form
    txtMaThuocTinh.setText(maThuocTinh); // Hiển thị mã thuộc tính
    txtTenThuocTinh.setText(tenThuocTinh); // Hiển thị tên thuộc tính
    txtMaThuocTinh.setEditable(false);
    
    // Vô hiệu hóa các JRadioButton khác
    disableOtherRadioButtons();
    btnThemThuocTinh.setEnabled(false); // Vô hiệu hóa nút Thêm
    }//GEN-LAST:event_tblThuocTinhMouseClicked

    private void btnAnThuocTinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnThuocTinhActionPerformed
        int selectedRow = tblThuocTinh.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một thuộc tính để ẩn!");
            return;
        }

       int id = Integer.parseInt(tblThuocTinh.getValueAt(selectedRow, 0).toString());
    boolean isUsed = false;
    String thuocTinhType = "";

    // Kiểm tra xem thuộc tính có đang được sử dụng hay không
    if (rdoChatLieu.isSelected()) {
        isUsed = CLRepo.isUsedInSanPhamChiTiet(id);
        thuocTinhType = "Chất Liệu";
    } else if (rdoMauSac.isSelected()) {
        isUsed = MSRepo.isUsedInSanPhamChiTiet(id);
        thuocTinhType = "Màu Sắc";
    } else if (rdoKichCo.isSelected()) {
        isUsed = KCRepo.isUsedInSanPhamChiTiet(id);
        thuocTinhType = "Kích Cỡ";
    } else if (rdoLoaiSanPham.isSelected()) {
        isUsed = LSPRepo.isUsedInSanPhamChiTiet(id);
        thuocTinhType = "Loại Sản Phẩm";
    } else if (rdoNSX.isSelected()) {
        isUsed = NSXRepo.isUsedInSanPhamChiTiet(id);
        thuocTinhType = "Nhà Sản Xuất";
    }

    // Nếu thuộc tính đang được sử dụng, không cho phép ẩn
    if (isUsed) {
        JOptionPane.showMessageDialog(this, thuocTinhType + " này đang được sử dụng bởi ít nhất một sản phẩm. Không thể ẩn!");
        return;
    }

    // Thêm thông báo xác nhận
    int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn ẩn " + thuocTinhType + " này không?", 
            "Xác nhận ẩn", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) {
        return; // Nếu chọn No, dừng lại
    }

    try {
        boolean hidden = false;
        if (rdoChatLieu.isSelected()) {
            hidden = CLRepo.hide(id);
            if (hidden) {
                JOptionPane.showMessageDialog(this, "Ẩn chất liệu thành công!");
            }
        } else if (rdoKichCo.isSelected()) {
            hidden = KCRepo.hide(id);
            if (hidden) {
                JOptionPane.showMessageDialog(this, "Ẩn kích cỡ thành công!");
            }
        } else if (rdoLoaiSanPham.isSelected()) {
            hidden = LSPRepo.hide(id);
            if (hidden) {
                JOptionPane.showMessageDialog(this, "Ẩn loại sản phẩm thành công!");
            }
        } else if (rdoMauSac.isSelected()) {
            hidden = MSRepo.hide(id);
            if (hidden) {
                JOptionPane.showMessageDialog(this, "Ẩn màu sắc thành công!");
            }
        } else if (rdoNSX.isSelected()) {
            hidden = NSXRepo.hide(id);
            if (hidden) {
                JOptionPane.showMessageDialog(this, "Ẩn nhà sản xuất thành công!");
            }
        }

        if (hidden) {
            loadToTableThuocTinh(); // Làm mới bảng để ẩn bản ghi
            loadToTableHiddenThuocTinh();
            loadComboBoxData(); // Làm mới JComboBox
            resetFormThuocTinh(); // Làm mới form
            enableAllRadioButtons(); // Kích hoạt lại tất cả JRadioButton
        } else {
            JOptionPane.showMessageDialog(this, "Ẩn " + thuocTinhType + " thất bại!");
        }
    } catch (RuntimeException e) {
        JOptionPane.showMessageDialog(this, "Lỗi khi ẩn " + thuocTinhType + ": " + e.getMessage());
    }
    }//GEN-LAST:event_btnAnThuocTinhActionPerformed

    private void btnSuaSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaSanPhamActionPerformed
        int selectedRow = tblSanPham.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm chi tiết để sửa!");
            return;
        }
        
        // Lấy ID từ bảng trước
        int id = Integer.parseInt(tblSanPham.getValueAt(selectedRow, 0).toString());

        SanPhamChiTiet spct = getFormDataSanPham();
        if (spct == null) {
            return;
        }
        
        // Gán ID cho spct trước khi kiểm tra trùng tên
        spct.setId(id);
        
        if (SPCTRepo.checkTenExistsExcludingId(spct.getTenSanPhamChiTiet(), spct.getId())) {
                JOptionPane.showMessageDialog(this, "Tên sản phẩm đã tồn tại!");
                return;
            }


        // Kiểm tra mã trùng (nếu mã thay đổi)
        String maCu = tblSanPham.getValueAt(selectedRow, 1).toString();
        if (!spct.getMaSanPhamChiTiet().equals(maCu) && SPCTRepo.checkMaExists(spct.getMaSanPhamChiTiet())) {
            JOptionPane.showMessageDialog(this, "Mã sản phẩm chi tiết đã tồn tại!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn sửa sản phẩm chi tiết này không?",
                "Xác nhận sửa", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            SPCTRepo.update(spct);
            JOptionPane.showMessageDialog(this, "Sửa sản phẩm chi tiết thành công!");
            loadToTableSanPham();
            resetFormSanPham();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa sản phẩm chi tiết: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSuaSanPhamActionPerformed

    private void btnAnSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnSanPhamActionPerformed
        int selectedRow = tblSanPham.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm chi tiết để ẩn!");
            return;
        }

        int id = Integer.parseInt(tblSanPham.getValueAt(selectedRow, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn ẩn sản phẩm chi tiết này không?",
                "Xác nhận ẩn", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            SPCTRepo.hide(id);
            JOptionPane.showMessageDialog(this, "Ẩn sản phẩm chi tiết thành công!");
            loadToTableSanPham();
            loadToTableHiddenSanPham();
            resetFormSanPham();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi ẩn sản phẩm chi tiết: " + e.getMessage());
        }
    }//GEN-LAST:event_btnAnSanPhamActionPerformed

    private void btnLamMoiSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiSanPhamActionPerformed
        resetFormSanPham();
        tblSanPham.clearSelection();
    }//GEN-LAST:event_btnLamMoiSanPhamActionPerformed

    private void tblSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanPhamMouseClicked
    int selectedRow = tblSanPham.getSelectedRow();
    if (selectedRow == -1) {
        return; // Không cần làm gì nếu không có dòng nào được chọn
    }

    // Lấy ID sản phẩm từ cột 0
    int idSanPham = Integer.parseInt(tblSanPham.getValueAt(selectedRow, 0).toString());

    // Tìm SanPhamChiTiet tương ứng trong dsSanPhamChiTiet
    SanPhamChiTiet spct = null;
    for (SanPhamChiTiet item : dsSanPhamChiTiet) {
        if (item.getId() == idSanPham) {
            spct = item;
            break;
        }
    }

    if (spct == null) {
        JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm!");
        return;
    }

    // Điền dữ liệu từ đối tượng SanPhamChiTiet vào form
    txtMaSanPham.setText(spct.getMaSanPhamChiTiet());
    txtTenSanPham.setText(spct.getTenSanPhamChiTiet());
    txtGia.setText(formatGia(spct.getGiaSanPham()));
    txtSoLuong.setText(String.valueOf(spct.getSoLuong()));
    txtMoTa.setText(spct.getMoTa() != null ? spct.getMoTa() : "");

    // Hiển thị ảnh nếu có
    String anhSanPham = spct.getAnhSanPham();
    if (anhSanPham != null && !anhSanPham.isEmpty()) {
        duongDanAnh = anhSanPham;
        try {
            ImageIcon imageIcon = new ImageIcon(duongDanAnh);
            Image image = imageIcon.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH);
            lblAnhSanPham.setIcon(new ImageIcon(image));
            lblAnhSanPham.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải ảnh: " + e.getMessage());
        }
    } else {
        lblAnhSanPham.setIcon(null);
        lblAnhSanPham.setText("Nhấn để chọn ảnh");
        duongDanAnh = null;
    }

    // Điền dữ liệu vào JComboBox
    int idLoaiSanPham = spct.getIdLoaiSanPham();
    int idMauSac = spct.getIdMauSac();
    int idChatLieu = spct.getIdChatLieu();
    int idKichCo = spct.getIdKichCo();
    int idNhaSanXuat = spct.getIdNSX();

    // Tìm và chọn giá trị trong JComboBox
    for (int i = 0; i < dsLoaiSanPhamForComboBox.size(); i++) {
        if (dsLoaiSanPhamForComboBox.get(i).getId() == idLoaiSanPham) {
            cboLoaiSanPham.setSelectedIndex(i);
            break;
        }
    }

    for (int i = 0; i < dsMauSacForComboBox.size(); i++) {
        if (dsMauSacForComboBox.get(i).getId() == idMauSac) {
            cboMauSac.setSelectedIndex(i);
            break;
        }
    }

    for (int i = 0; i < dsChatLieuForComboBox.size(); i++) {
        if (dsChatLieuForComboBox.get(i).getId() == idChatLieu) {
            cboChatLieu.setSelectedIndex(i);
            break;
        }
    }

    for (int i = 0; i < dsKichCoForComboBox.size(); i++) {
        if (dsKichCoForComboBox.get(i).getId() == idKichCo) {
            cboKichCo.setSelectedIndex(i);
            break;
        }
    }

    for (int i = 0; i < dsNhaSanXuatForComboBox.size(); i++) {
        if (dsNhaSanXuatForComboBox.get(i).getId() == idNhaSanXuat) {
            cboNSX.setSelectedIndex(i);
            break;
        }
    }

    // Vô hiệu hóa txtMaSanPham và btnThem
    txtMaSanPham.setEditable(false);
    btnThemSanPham.setEnabled(false);
    }//GEN-LAST:event_tblSanPhamMouseClicked

    private void btnHienThiSanPhamAnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHienThiSanPhamAnActionPerformed
        SanPhamDialog.setLocationRelativeTo(null);
        SanPhamDialog.setVisible(true);
    }//GEN-LAST:event_btnHienThiSanPhamAnActionPerformed

    private void btnHienThiLaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHienThiLaiActionPerformed
        int selectedRow = tblSanPhamAn.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm để hiển thị lại!");
            return;
        }

        int id = Integer.parseInt(tblSanPhamAn.getValueAt(selectedRow, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn hiển thị lại sản phẩm này không?",
                "Xác nhận hiển thị lại", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            SPCTRepo.show(id);
            JOptionPane.showMessageDialog(this, "Hiển thị lại sản phẩm thành công!");
            loadToTableHiddenSanPham(); // Cập nhật lại bảng sản phẩm ẩn
            loadToTableSanPham(); // Cập nhật lại bảng chính
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi hiển thị lại sản phẩm: " + e.getMessage());
        }
    }//GEN-LAST:event_btnHienThiLaiActionPerformed

    private void btnHienThiThuocTinhAnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHienThiThuocTinhAnActionPerformed
        ThuocTinhDialog.setLocationRelativeTo(null);
        ThuocTinhDialog.setVisible(true);
    }//GEN-LAST:event_btnHienThiThuocTinhAnActionPerformed

    private void btnHienThiLaiThuocTinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHienThiLaiThuocTinhActionPerformed
      int selectedRow = tblThuocTinhAn.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn một thuộc tính để hiển thị lại!");
        return;
    }

    int id = Integer.parseInt(tblThuocTinhAn.getValueAt(selectedRow, 0).toString());
    boolean success = false;
    String thuocTinhType = "";

    if (rdoChatLieu.isSelected()) {
        success = CLRepo.show(id);
        thuocTinhType = "Chất Liệu";
    } else if (rdoMauSac.isSelected()) {
        success = MSRepo.show(id);
        thuocTinhType = "Màu Sắc";
    } else if (rdoKichCo.isSelected()) {
        success = KCRepo.show(id);
        thuocTinhType = "Kích Cỡ";
    } else if (rdoLoaiSanPham.isSelected()) {
        success = LSPRepo.show(id);
        thuocTinhType = "Loại Sản Phẩm";
    } else if (rdoNSX.isSelected()) {
        success = NSXRepo.show(id);
        thuocTinhType = "Nhà Sản Xuất";
    } else {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn một loại thuộc tính!");
        return;
    }

    if (success) {
        JOptionPane.showMessageDialog(this, "Hiển thị lại " + thuocTinhType + " thành công!");
        loadToTableThuocTinh(); // Làm mới bảng tblThuocTinh (hiển thị thuộc tính hoạt động)
        loadToTableHiddenThuocTinh(); // Làm mới bảng tblThuocTinhAn (hiển thị thuộc tính ẩn)
        loadComboBoxData(); // Làm mới các JComboBox
    } else {
        JOptionPane.showMessageDialog(this, "Hiển thị lại " + thuocTinhType + " thất bại!");
    }
    
    }//GEN-LAST:event_btnHienThiLaiThuocTinhActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog SanPhamDialog;
    private javax.swing.JDialog ThuocTinhDialog;
    private javax.swing.JButton btnAnSanPham;
    private javax.swing.JButton btnAnThuocTinh;
    private javax.swing.JButton btnHienThiLai;
    private javax.swing.JButton btnHienThiLaiThuocTinh;
    private javax.swing.JButton btnHienThiSanPhamAn;
    private javax.swing.JButton btnHienThiThuocTinhAn;
    private javax.swing.JButton btnLamMoiSanPham;
    private javax.swing.JButton btnSuaSanPham;
    private javax.swing.JButton btnSuaThuocTinh;
    private javax.swing.JButton btnThemSanPham;
    private javax.swing.JButton btnThemThuocTinh;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboChatLieu;
    private javax.swing.JComboBox<String> cboKichCo;
    private javax.swing.JComboBox<String> cboLoaiSanPham;
    private javax.swing.JComboBox<String> cboMauSac;
    private javax.swing.JComboBox<String> cboNSX;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JLabel lblAnhSanPham;
    private javax.swing.JRadioButton rdoChatLieu;
    private javax.swing.JRadioButton rdoKichCo;
    private javax.swing.JRadioButton rdoLoaiSanPham;
    private javax.swing.JRadioButton rdoMauSac;
    private javax.swing.JRadioButton rdoNSX;
    private javax.swing.JTable tblSanPham;
    private javax.swing.JTable tblSanPhamAn;
    private javax.swing.JTable tblThuocTinh;
    private javax.swing.JTable tblThuocTinhAn;
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextField txtMaSanPham;
    private javax.swing.JTextField txtMaThuocTinh;
    private javax.swing.JTextArea txtMoTa;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTenSanPham;
    private javax.swing.JTextField txtTenThuocTinh;
    // End of variables declaration//GEN-END:variables
}
