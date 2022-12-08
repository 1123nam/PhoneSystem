/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phonesystem.edu.view;

import com.formdev.flatlaf.FlatIntelliJLaf;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import phonesystem.edu.dao.DiemDanhDAO;
import phonesystem.edu.dao.HoaDonChiTietDAO;
import phonesystem.edu.dao.HoaDonDAO;
import phonesystem.edu.dao.KhachHangDAO;
import phonesystem.edu.dao.KhuyenMaiDAO;
import phonesystem.edu.dao.LuongDAO;
import phonesystem.edu.dao.NhanVienDAO;
import phonesystem.edu.dao.SanPhamDAO;
import phonesystem.edu.dao.TaiKhoanDAO;
import phonesystem.edu.dao.ThongKeDAO;
import phonesystem.edu.entity.DiemDanh;
import phonesystem.edu.entity.HoaDon;
import phonesystem.edu.entity.HoaDonChiTiet;
import phonesystem.edu.entity.KhachHang;
import phonesystem.edu.entity.KhuyenMai;
import phonesystem.edu.entity.Luong;
import phonesystem.edu.entity.NhanVien;
import phonesystem.edu.entity.SanPham;
import phonesystem.edu.entity.TaiKhoan;
import phonesystem.edu.ultil.Auth;
import phonesystem.edu.ultil.GenerateID;
import phonesystem.edu.ultil.ModelPieChart;
import phonesystem.edu.ultil.MsgBox;
import phonesystem.edu.ultil.TableEdit;
import phonesystem.edu.ultil.XDate;
import phonesystem.edu.ultil.XImage;
import phonesystem.edu.ultil.jdbcHelper;

/**
 *
 * @author HienTran
 */
public class Main_Frame extends javax.swing.JFrame {
//Khai bao bien global 

    String tenKH_KhachHang = "";

    String maSanPham_KhuyenMai = "";

    boolean isRemove = false;

    // Bien tinh luong
    double tongLuong = 0;
    double tienThuong = 0;
    double luongTrenCa = 0;
    int tongCa = 0;
    boolean isNhanLuong = false;
//    DAO variable 
    LuongDAO luongDAO = new LuongDAO();

//    Hien
    ArrayList listThanhTien;
    /**
     * Creates new form Main_Frame
     */
//    Account recent check
    static Boolean checkBox1 = false;
    static Boolean checkBox2 = false;

//    get Username account recent login
    static String LoginAccount1;
    static String LoginAccount2;

    public Main_Frame() {
        initComponents();
        this.setIconImage(XImage.getAppIcon());
        lblOpen.setVisible(true);

        this.init();

//        Minh Duong
        this.init_SanPham();
        this.init_DiemDanh();
        this.init_KhuyenMai();

//        Hoai Nam
        this.initCardNhanVien();
        this.initCardKhachHang();
        this.initCardLuong();
        this.initThongKe();

//        Trong Hien
        this.init_HeThong();
        this.init_TaiKhoan();
        this.init_BanHang();
    }

    public void init() {
//      Display UserName
        lbl_TenNhanVien.setText(Auth.getNameNhanVien());
        lbl_VaiTro.setText(Auth.isManager() == true ? "Quản lí" : "Nhân viên");

        isManager(Auth.isManager());

//      Get user Login recent
        LoginAccount1 = lblLoginAccount1.getText();
        LoginAccount2 = lblLoginAcout2.getText();

//        Timer
        this.Clock();

//        Default menu active
        onClick(pnl_ThongKe);
        onLeaveClick(pnl_KhachHang);
        onLeaveClick(pnl_BanHang);
        onLeaveClick(pnl_SanPham);
        onLeaveClick(pnl_HeThong);
        onLeaveClick(pnl_DiemDanh);
        onLeaveClick(pnl_TaiKhoan);
        onLeaveClick(pnl_NhanVien);
        onLeaveClick(pnl_Luong);
        onLeaveClick(pnl_KhuyenMai);

        onClickLabel(lbl_ThongKe);
        onLeaveClickLabel(lbl_KhachHang);
        onLeaveClickLabel(lbl_BanHang);
        onLeaveClickLabel(lbl_SanPham);
        onLeaveClickLabel(lbl_HeThong);
        onLeaveClickLabel(lbl_DiemDanh);
        onLeaveClickLabel(lbl_TaiKhoan);
        onLeaveClickLabel(lbl_NhanVien);
        onLeaveClickLabel(lbl_Luong);
        onLeaveClickLabel(lbl_KhuyenMai);

//        indicators
        indicator1.setOpaque(true);
        indicator2.setOpaque(false);
        indicator3.setOpaque(false);
        indicator4.setOpaque(false);
        indicator6.setOpaque(false);
        indicator7.setOpaque(false);
        indicator8.setOpaque(false);
        indicator9.setOpaque(false);
        indicator10.setOpaque(false);
        indicator11.setOpaque(false);

//        Card playout
        CardLayout playout = (CardLayout) pnl_MainDisplayCard.getLayout();
        playout.show(pnl_MainDisplayCard, "card_ThongKe");

    }

//        Timer on right top
    private void Clock() {
        new Timer(1000, new ActionListener() {
            SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss a");

            @Override
            public void actionPerformed(ActionEvent e) {
                lblTimer.setText(format.format(new Date()));
            }
        }).start();
    }

    private void isManager(boolean isM) {
        if (!isM) {
            pnl_TaiKhoan.setVisible(isM);
            pnl_NhanVien.setVisible(isM);
            pnl_Luong.setVisible(isM);
            pnl_KhuyenMai.setVisible(isM);
            tabs_thongke.remove(1);
        } else {
            pnl_TaiKhoan.setVisible(isM);
            pnl_NhanVien.setVisible(isM);
            pnl_Luong.setVisible(isM);
            pnl_KhuyenMai.setVisible(isM);
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

        btn_group_VaiTro = new javax.swing.ButtonGroup();
        btn_group_GioiTinh_NhanVien = new javax.swing.ButtonGroup();
        btn_group_TrangThai_NhanVien = new javax.swing.ButtonGroup();
        buttonGroup1 = new javax.swing.ButtonGroup();
        pnl_MainChinh = new javax.swing.JPanel();
        pnl_Top = new javax.swing.JPanel();
        lblOpen = new javax.swing.JLabel();
        lbl_Close_Windows = new javax.swing.JLabel();
        lblTimer = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lbl_VaiTro = new javax.swing.JLabel();
        lbl_TenNhanVien = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        pnl_Menu = new javax.swing.JPanel();
        lbl_Logo = new javax.swing.JLabel();
        lbl_DangXuat = new javax.swing.JLabel();
        pnl_ThongKe = new javax.swing.JPanel();
        indicator1 = new javax.swing.JPanel();
        lbl_ThongKe = new javax.swing.JLabel();
        pnl_KhachHang = new javax.swing.JPanel();
        indicator2 = new javax.swing.JPanel();
        lbl_KhachHang = new javax.swing.JLabel();
        pnl_BanHang = new javax.swing.JPanel();
        indicator4 = new javax.swing.JPanel();
        lbl_BanHang = new javax.swing.JLabel();
        pnl_SanPham = new javax.swing.JPanel();
        indicator3 = new javax.swing.JPanel();
        lbl_SanPham = new javax.swing.JLabel();
        pnl_HeThong = new javax.swing.JPanel();
        indicator6 = new javax.swing.JPanel();
        lbl_HeThong = new javax.swing.JLabel();
        pnl_DiemDanh = new javax.swing.JPanel();
        indicator7 = new javax.swing.JPanel();
        lbl_DiemDanh = new javax.swing.JLabel();
        pnl_TaiKhoan = new javax.swing.JPanel();
        indicator8 = new javax.swing.JPanel();
        lbl_TaiKhoan = new javax.swing.JLabel();
        pnl_NhanVien = new javax.swing.JPanel();
        indicator9 = new javax.swing.JPanel();
        lbl_NhanVien = new javax.swing.JLabel();
        pnl_Luong = new javax.swing.JPanel();
        indicator10 = new javax.swing.JPanel();
        lbl_Luong = new javax.swing.JLabel();
        pnl_KhuyenMai = new javax.swing.JPanel();
        indicator11 = new javax.swing.JPanel();
        lbl_KhuyenMai = new javax.swing.JLabel();
        pnl_MainDisplayCard = new javax.swing.JPanel();
        card_ThongKe = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        tabs_thongke = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane23 = new javax.swing.JScrollPane();
        tbl_ThongKe_ThongKe = new javax.swing.JTable();
        jPanel20 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        dcs_NgayBatDau_ThongKe = new com.toedter.calendar.JDateChooser();
        jLabel13 = new javax.swing.JLabel();
        dcs_NgayKetThuc_ThongKe = new com.toedter.calendar.JDateChooser();
        jLabel15 = new javax.swing.JLabel();
        cbo_ChonHang_ThongKe = new javax.swing.JComboBox<>();
        btn_Top5BanChay = new javax.swing.JButton();
        btn_LamMoi = new javax.swing.JButton();
        jPanel53 = new javax.swing.JPanel();
        btn_First_ThongKe = new javax.swing.JButton();
        btn_Prev_ThongKe = new javax.swing.JButton();
        btn_Next_ThongKe = new javax.swing.JButton();
        btn_Last_ThongKe = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        lbl_Ngay_DoanhThu = new javax.swing.JLabel();
        lbl_SoLuong_NgayHienTai_DoanhThu = new javax.swing.JLabel();
        lbl_TongTien_NgayHienTai_DoanhThu = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        lbl_Thang_DoanhThu = new javax.swing.JLabel();
        lbl_SoLuong_ThangHienTai_DoanhThu = new javax.swing.JLabel();
        lbl_TongTien_ThangHienTai_DoanhThu = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        lbl_Nam_DoanhThu = new javax.swing.JLabel();
        lbl_SoLuong_NamHienTai_DoanhThu = new javax.swing.JLabel();
        lbl_TongTien_NamHienTai_DoanhThu = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        pieChart_Tong = new phonesystem.edu.ultil.PieChart();
        jPanel13 = new javax.swing.JPanel();
        dcs_NgayKetThuc_ThongKe_DoanhThu = new com.toedter.calendar.JDateChooser();
        dcs_NgayBatDau_ThongKe_DoanhThu = new com.toedter.calendar.JDateChooser();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        pieChart_TheoNgay = new phonesystem.edu.ultil.PieChart();
        card_KhachHang = new javax.swing.JPanel();
        tbp_KhachHang = new javax.swing.JTabbedPane();
        jPanel14 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel127 = new javax.swing.JLabel();
        txt_MaKhachHang_KhachHang = new javax.swing.JTextField();
        jLabel128 = new javax.swing.JLabel();
        txt_TenKhachHang_KhachHang = new javax.swing.JTextField();
        rdo_Nam_KhachHang = new javax.swing.JRadioButton();
        rdo_Nu_KhachHang = new javax.swing.JRadioButton();
        jLabel129 = new javax.swing.JLabel();
        jLabel130 = new javax.swing.JLabel();
        txt_SoDienThoaiKhachHang_KhachHang = new javax.swing.JTextField();
        jLabel131 = new javax.swing.JLabel();
        jScrollPane17 = new javax.swing.JScrollPane();
        txt_GhiChuKhachHang_KhachHang = new javax.swing.JTextArea();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane18 = new javax.swing.JScrollPane();
        tbl_DSKhachHang_KhachHang = new javax.swing.JTable();
        jPanel50 = new javax.swing.JPanel();
        btn_First_KhachHang = new javax.swing.JButton();
        btn_Prev_KhachHang = new javax.swing.JButton();
        btn_Next_KhachHang = new javax.swing.JButton();
        btn_Last_KhachHang = new javax.swing.JButton();
        txt_TimSoDienThoaiKhachHang_KhachHang = new javax.swing.JTextField();
        btn_ThemKhachHangVaoHoaDon_KhachHang = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        btn_ThemKhachHang_KhachHang = new javax.swing.JButton();
        btn_CapNhatKhachHang_KhachHang = new javax.swing.JButton();
        btn_XoaKhachHang_KhachHang = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane19 = new javax.swing.JScrollPane();
        tbl_DSKhachHangDaXoa_KhachHang = new javax.swing.JTable();
        txt_TimSoDienThoaiKhachHangDaXoa_KhachHang = new javax.swing.JTextField();
        btn_KhoiPhucKhachHang_KhachHang = new javax.swing.JButton();
        card_HeThong = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        btn_submit_HeThong = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txt_TenDangNhap_HeThong = new javax.swing.JTextField();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        txt_MatKhau_HeThong = new javax.swing.JPasswordField();
        txt_MatKhauMoi_HeThong = new javax.swing.JPasswordField();
        txt_Confirm_HeThong = new javax.swing.JPasswordField();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        lblLoginAccount1 = new javax.swing.JLabel();
        lblLoginAcout2 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        jLabel113 = new javax.swing.JLabel();
        jLabel114 = new javax.swing.JLabel();
        card_TaiKhoan = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        jLabel56 = new javax.swing.JLabel();
        jLabel112 = new javax.swing.JLabel();
        jLabel115 = new javax.swing.JLabel();
        jLabel116 = new javax.swing.JLabel();
        jLabel117 = new javax.swing.JLabel();
        jPanel39 = new javax.swing.JPanel();
        btn_Them_TaiKhoan = new javax.swing.JButton();
        btn_CapNhat_TaiKhoan = new javax.swing.JButton();
        btn_Xoa_TaiKhoan = new javax.swing.JButton();
        txt_MaNV_TaiKhoan = new javax.swing.JTextField();
        txt_TenDangNhap_TaiKhoan = new javax.swing.JTextField();
        txt_MatKhau_TaiKhoan = new javax.swing.JPasswordField();
        txt_Confirm_TaiKhoan = new javax.swing.JPasswordField();
        rdo_QuanLi_TaiKhoan = new javax.swing.JRadioButton();
        rdo_NhanVien_TaiKhoan = new javax.swing.JRadioButton();
        jPanel40 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        tbl_DSDaCoTaiKhoan_TaiKhoan = new javax.swing.JTable();
        jPanel38 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        tbl_DSChuaTaiKhoan_TaiKhoan = new javax.swing.JTable();
        btn_First_DSChuaTaiKhoan_taikhoan = new javax.swing.JButton();
        btn_prev_DSChuaTaiKhoan_taikhoan = new javax.swing.JButton();
        btn_next_DSChuaTaiKhoan_taikhoan = new javax.swing.JButton();
        btn_last_DSChuaTaiKhoan_taikhoan = new javax.swing.JButton();
        card_DiemDanh = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        lbl_MaNhanVien_DiemDanh = new javax.swing.JLabel();
        lbl_TenNhanVien_DiemDanh = new javax.swing.JLabel();
        lbl_CaLam_DiemDanh = new javax.swing.JLabel();
        lbl_NgayLamViec_DiemDanh = new javax.swing.JLabel();
        btn_DiemDanh_DiemDanh = new javax.swing.JButton();
        jLabel87 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        txa_GhiChu_DiemDanh = new javax.swing.JTextArea();
        jLabel92 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tbl_DanhSachDiemDanh_DiemDanh = new javax.swing.JTable();
        btn_First_DiemDanh = new javax.swing.JButton();
        btn_Previous_DiemDanh = new javax.swing.JButton();
        btn_Next_DiemDanh = new javax.swing.JButton();
        btn_Last_DiemDanh = new javax.swing.JButton();
        jLabel136 = new javax.swing.JLabel();
        cbo_DiemDanh_DiemDanh = new javax.swing.JComboBox<>();
        txt_TimDiemDanh_DiemDanh = new javax.swing.JTextField();
        card_Luong = new javax.swing.JPanel();
        jPanel47 = new javax.swing.JPanel();
        jLabel64 = new javax.swing.JLabel();
        jLabel118 = new javax.swing.JLabel();
        jLabel119 = new javax.swing.JLabel();
        jLabel120 = new javax.swing.JLabel();
        jPanel51 = new javax.swing.JPanel();
        btn_TinhLuong_Luong = new javax.swing.JButton();
        btn_CapNhatLuong_Luong = new javax.swing.JButton();
        btn_NhanLuong_Luong = new javax.swing.JButton();
        cbo_MaNhanVien_Luong = new javax.swing.JComboBox<>();
        lbl_TenNhanVien_Luong = new javax.swing.JLabel();
        lbl_ChucVu_Luong = new javax.swing.JLabel();
        lbl_TongCaLam_Luong = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        txt_Luong_Luong = new javax.swing.JTextField();
        txt_TienThuong_Luong = new javax.swing.JTextField();
        lbl_TongLuong_Luong = new javax.swing.JLabel();
        lbl_NgayNhan_Luong = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        txt_GhiChu_Luong = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jPanel52 = new javax.swing.JPanel();
        jScrollPane20 = new javax.swing.JScrollPane();
        tbl_DSLuong_Luong = new javax.swing.JTable();
        jLabel86 = new javax.swing.JLabel();
        dcs_LocTheoNgayNhan_Luong = new com.toedter.calendar.JDateChooser();
        btn_TimKiemLuongTheoNgay_Luong = new javax.swing.JButton();
        jPanel54 = new javax.swing.JPanel();
        btn_First_NhanVien3 = new javax.swing.JButton();
        btn_Prev_NhanVien3 = new javax.swing.JButton();
        btn_Next_NhanVien3 = new javax.swing.JButton();
        btn_Last_NhanVien3 = new javax.swing.JButton();
        card_NhanVien = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel48 = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        tbl_DSNhanVien_NhanVien = new javax.swing.JTable();
        txt_TimDiaChi_NhanVien = new javax.swing.JTextField();
        cbo_NhanVien_NhanVien = new javax.swing.JComboBox<>();
        jPanel49 = new javax.swing.JPanel();
        btn_First_NhanVien = new javax.swing.JButton();
        btn_Prev_NhanVien = new javax.swing.JButton();
        btn_Next_NhanVien = new javax.swing.JButton();
        btn_Last_NhanVien = new javax.swing.JButton();
        jLabel132 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel41 = new javax.swing.JPanel();
        jPanel42 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        jPanel43 = new javax.swing.JPanel();
        txt_MaNhanVien_NhanVien = new javax.swing.JTextField();
        txt_TenNhanVien_NhanVien = new javax.swing.JTextField();
        txt_SoDienThoai_NhanVien = new javax.swing.JTextField();
        txt_Email_NhanVien = new javax.swing.JTextField();
        dc_NgaySinh_NhanVien = new com.toedter.calendar.JDateChooser();
        rdo_Nam_NhanVien = new javax.swing.JRadioButton();
        rdo_Nu_NhanVien = new javax.swing.JRadioButton();
        jPanel44 = new javax.swing.JPanel();
        btn_Them_NhanVien = new javax.swing.JButton();
        btn_CapNhat_NhanVien = new javax.swing.JButton();
        btn_Moi_NhanVien = new javax.swing.JButton();
        jPanel46 = new javax.swing.JPanel();
        jLabel121 = new javax.swing.JLabel();
        jLabel122 = new javax.swing.JLabel();
        jLabel123 = new javax.swing.JLabel();
        jLabel124 = new javax.swing.JLabel();
        jLabel125 = new javax.swing.JLabel();
        jLabel126 = new javax.swing.JLabel();
        jPanel45 = new javax.swing.JPanel();
        txt_CCCD_NhanVien = new javax.swing.JTextField();
        rdo_DangLam_NhanVien = new javax.swing.JRadioButton();
        rdo_NghiViec_NhanVien = new javax.swing.JRadioButton();
        jScrollPane14 = new javax.swing.JScrollPane();
        txt_GhiChu_NhanVien = new javax.swing.JTextArea();
        lbl_Anh_NhanVien = new javax.swing.JLabel();
        jScrollPane24 = new javax.swing.JScrollPane();
        txt_DiaChi_NhanVien = new javax.swing.JTextArea();
        card_SanPham = new javax.swing.JPanel();
        tab_SanPham = new javax.swing.JTabbedPane();
        pnlThongTinSanPham = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        lblHinhAnh_SanPham = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        btn_Them_SanPham = new javax.swing.JButton();
        btn_CapNhat_SanPham = new javax.swing.JButton();
        btn_Xoa_SanPham = new javax.swing.JButton();
        btn_Moi_SanPham = new javax.swing.JButton();
        txt_MaSanPham_SanPham = new javax.swing.JTextField();
        txt_TenSanPham_SanPham = new javax.swing.JTextField();
        txt_SoLuong_SanPham = new javax.swing.JTextField();
        txt_DonGia_SanPham = new javax.swing.JTextField();
        cbo_HangSanXuat_SanPham = new javax.swing.JComboBox<>();
        cbo_MauSac_SanPham = new javax.swing.JComboBox<>();
        cbo_XuatXu_SanPham = new javax.swing.JComboBox<>();
        jLabel60 = new javax.swing.JLabel();
        txt_GhiChu_SanPham = new javax.swing.JTextField();
        jLabel94 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_DanhSachSanPham_SanPham = new javax.swing.JTable();
        btn_First_SanPham = new javax.swing.JButton();
        btn_Previous_SanPham = new javax.swing.JButton();
        btn_Next_SanPham = new javax.swing.JButton();
        btn_Last_SanPham = new javax.swing.JButton();
        jLabel133 = new javax.swing.JLabel();
        cbo_SanPham_SanPham = new javax.swing.JComboBox<>();
        txt_TimSanPham_SanPham = new javax.swing.JTextField();
        jPanel30 = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        tbl_DanhSachSanPham_DaXoa_SanPham = new javax.swing.JTable();
        btn_First_SanPham_DaXoa = new javax.swing.JButton();
        btn_Previous_SanPham_DaXoa = new javax.swing.JButton();
        btn_Next_SanPham_DaXoa = new javax.swing.JButton();
        btn_Last_SanPham_DaXoa = new javax.swing.JButton();
        btn_KhoiPhuc_SanPham = new javax.swing.JButton();
        card_BanHang = new javax.swing.JPanel();
        tabs_HoaDon = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        txt_fillter_by_TenSP_BanHang = new javax.swing.JTextField();
        jScrollPane7 = new javax.swing.JScrollPane();
        tbl_DS_SanPham_BanHang = new javax.swing.JTable();
        jPanel35 = new javax.swing.JPanel();
        jLabel61 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jLabel96 = new javax.swing.JLabel();
        jLabel97 = new javax.swing.JLabel();
        lbl_MaHoaDon_BanHang = new javax.swing.JLabel();
        lbl_NgayTao_BanHang = new javax.swing.JLabel();
        lbl_NguoiTao_BanHang = new javax.swing.JLabel();
        lbl_TenKhachHang_BanHang = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tbl_HoaDon_BanHang = new javax.swing.JTable();
        jLabel102 = new javax.swing.JLabel();
        jLabel103 = new javax.swing.JLabel();
        jLabel104 = new javax.swing.JLabel();
        jLabel105 = new javax.swing.JLabel();
        lbl_TongTienThanhToan_BanHang = new javax.swing.JLabel();
        txt_TienKhachDua_BanHang = new javax.swing.JTextField();
        jLabel108 = new javax.swing.JLabel();
        txt_giaGiam_BanHang = new javax.swing.JTextField();
        txt_TienTraLai_Banhang = new javax.swing.JTextField();
        jLabel109 = new javax.swing.JLabel();
        jLabel110 = new javax.swing.JLabel();
        btn_HuyGioHang_BanHang = new javax.swing.JButton();
        btn_LamMoi_BanHang = new javax.swing.JButton();
        btn_ThanhToan_BanHang = new javax.swing.JButton();
        jScrollPane21 = new javax.swing.JScrollPane();
        txt_GhiChu_BanHang = new javax.swing.JTextArea();
        jLabel47 = new javax.swing.JLabel();
        jScrollPane22 = new javax.swing.JScrollPane();
        txt_GhiChu_BanHang1 = new javax.swing.JTextArea();
        jLabel48 = new javax.swing.JLabel();
        jLabel134 = new javax.swing.JLabel();
        jPanel34 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tbl_DSHoaDon_BanHang = new javax.swing.JTable();
        jButton8 = new javax.swing.JButton();
        jPanel36 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        tbl_DSHoaDonChiTiet_BanHang = new javax.swing.JTable();
        btn_First_DSHoaDon_BanHang = new javax.swing.JButton();
        btn_prev_DSHoaDon_BanHang = new javax.swing.JButton();
        btn_next_DSHoaDon_BanHang = new javax.swing.JButton();
        btn_last_DSHoaDon_BanHang = new javax.swing.JButton();
        cbo_loc_DSHoaDon_BanHang = new javax.swing.JComboBox<>();
        jLabel137 = new javax.swing.JLabel();
        txt_Loc_DSHoaDon_BanHang = new javax.swing.JTextField();
        card_KhuyenMai = new javax.swing.JPanel();
        tab_KhuyenMai = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbl_DanhSachSanPham_KhuyenMai = new javax.swing.JTable();
        cbo_LocSanPham_KhuyenMai = new javax.swing.JComboBox<>();
        txt_TimSanPham_KhuyenMai = new javax.swing.JTextField();
        jPanel32 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbl_DanhSachKhuyenMai_KhuyenMai = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        txt_TenKhuyenMai_KhuyenMai = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        txt_GiamGia_KhuyenMai = new javax.swing.JTextField();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        dcs_NgayBatDau_KhuyenMai = new com.toedter.calendar.JDateChooser();
        jLabel77 = new javax.swing.JLabel();
        dcs_NgayKetThuc_KhuyenMai = new com.toedter.calendar.JDateChooser();
        jLabel78 = new javax.swing.JLabel();
        rdo_DangHoatDong_KhuyenMai = new javax.swing.JRadioButton();
        rdo_NgungHoatDong = new javax.swing.JRadioButton();
        jLabel79 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txa_GhiChu_KhuyenMai = new javax.swing.JTextArea();
        btn_Them_KhuyenMai = new javax.swing.JButton();
        btn_CapNhat_KhuyenMai = new javax.swing.JButton();
        btn_Xoa_KhuyenMai = new javax.swing.JButton();
        btn_Moi_KhuyenMai = new javax.swing.JButton();
        jLabel95 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_DanhSachKhuyenMaiCoSanPham_KhuyenMai = new javax.swing.JTable();
        jLabel135 = new javax.swing.JLabel();
        cbo_KhuyenMai_KhuyenMai = new javax.swing.JComboBox<>();
        txt_TimKhuyenMai_KhuyenMai = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnl_MainChinh.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnl_Top.setBackground(new java.awt.Color(250, 250, 250));
        pnl_Top.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(228, 228, 228)));
        pnl_Top.setPreferredSize(new java.awt.Dimension(1000, 70));
        pnl_Top.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblOpen.setForeground(new java.awt.Color(97, 88, 152));
        lblOpen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/phonesystem/edu/img/icons8_menu_35px_2.png"))); // NOI18N
        lblOpen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnl_Top.add(lblOpen, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 70, 70));

        lbl_Close_Windows.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Close_Windows.setIcon(new javax.swing.ImageIcon(getClass().getResource("/phonesystem/edu/img/icons8_close_window_35px_2.png"))); // NOI18N
        lbl_Close_Windows.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_Close_Windows.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_Close_WindowsMouseClicked(evt);
            }
        });
        pnl_Top.add(lbl_Close_Windows, new org.netbeans.lib.awtextra.AbsoluteConstraints(1230, 0, 50, 70));

        lblTimer.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTimer.setForeground(new java.awt.Color(255, 255, 255));
        lblTimer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pnl_Top.add(lblTimer, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 0, 110, 33));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/phonesystem/edu/img/Group 17.png"))); // NOI18N
        pnl_Top.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 0, -1, 70));

        lbl_VaiTro.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lbl_VaiTro.setForeground(new java.awt.Color(34, 51, 102));
        lbl_VaiTro.setText("Quản lí");
        pnl_Top.add(lbl_VaiTro, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, -1, -1));

        lbl_TenNhanVien.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbl_TenNhanVien.setForeground(new java.awt.Color(34, 51, 102));
        lbl_TenNhanVien.setText("Hiến");
        pnl_Top.add(lbl_TenNhanVien, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, -1, -1));

        jLabel14.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(34, 51, 102));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Cửa hàng điện thoại Vũ trụ");
        pnl_Top.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, 1050, 60));

        jLabel5.setText("jLabel5");
        pnl_Top.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 0, 110, 30));

        pnl_MainChinh.add(pnl_Top, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, -1));

        pnl_Menu.setBackground(new java.awt.Color(255, 255, 255));
        pnl_Menu.setPreferredSize(new java.awt.Dimension(150, 555));

        lbl_Logo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/phonesystem/edu/img/icons8_Old_Live_Journal_logo_100px.png"))); // NOI18N

        lbl_DangXuat.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbl_DangXuat.setForeground(new java.awt.Color(221, 109, 84));
        lbl_DangXuat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/phonesystem/edu/img/icons8_logout_35px.png"))); // NOI18N
        lbl_DangXuat.setText("Đăng xuất");
        lbl_DangXuat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_DangXuat.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lbl_DangXuat.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        lbl_DangXuat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_DangXuatMouseClicked(evt);
            }
        });

        pnl_ThongKe.setBackground(new java.awt.Color(255, 255, 255));
        pnl_ThongKe.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnl_ThongKe.setMinimumSize(new java.awt.Dimension(180, 43));
        pnl_ThongKe.setPreferredSize(new java.awt.Dimension(180, 43));
        pnl_ThongKe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnl_ThongKeMouseClicked(evt);
            }
        });

        indicator1.setBackground(new java.awt.Color(87, 88, 152));
        indicator1.setOpaque(false);

        javax.swing.GroupLayout indicator1Layout = new javax.swing.GroupLayout(indicator1);
        indicator1.setLayout(indicator1Layout);
        indicator1Layout.setHorizontalGroup(
            indicator1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        indicator1Layout.setVerticalGroup(
            indicator1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        lbl_ThongKe.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbl_ThongKe.setForeground(new java.awt.Color(97, 88, 152));
        lbl_ThongKe.setText("Thống Kê");

        javax.swing.GroupLayout pnl_ThongKeLayout = new javax.swing.GroupLayout(pnl_ThongKe);
        pnl_ThongKe.setLayout(pnl_ThongKeLayout);
        pnl_ThongKeLayout.setHorizontalGroup(
            pnl_ThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_ThongKeLayout.createSequentialGroup()
                .addComponent(indicator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_ThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );
        pnl_ThongKeLayout.setVerticalGroup(
            pnl_ThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(indicator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_ThongKe, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnl_KhachHang.setBackground(new java.awt.Color(255, 255, 255));
        pnl_KhachHang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnl_KhachHang.setMinimumSize(new java.awt.Dimension(180, 43));
        pnl_KhachHang.setPreferredSize(new java.awt.Dimension(180, 43));
        pnl_KhachHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnl_KhachHangMouseClicked(evt);
            }
        });

        indicator2.setBackground(new java.awt.Color(87, 88, 152));
        indicator2.setOpaque(false);

        javax.swing.GroupLayout indicator2Layout = new javax.swing.GroupLayout(indicator2);
        indicator2.setLayout(indicator2Layout);
        indicator2Layout.setHorizontalGroup(
            indicator2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        indicator2Layout.setVerticalGroup(
            indicator2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        lbl_KhachHang.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbl_KhachHang.setForeground(new java.awt.Color(97, 88, 152));
        lbl_KhachHang.setText("Khách Hàng");

        javax.swing.GroupLayout pnl_KhachHangLayout = new javax.swing.GroupLayout(pnl_KhachHang);
        pnl_KhachHang.setLayout(pnl_KhachHangLayout);
        pnl_KhachHangLayout.setHorizontalGroup(
            pnl_KhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_KhachHangLayout.createSequentialGroup()
                .addComponent(indicator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_KhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );
        pnl_KhachHangLayout.setVerticalGroup(
            pnl_KhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(indicator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_KhachHang, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnl_BanHang.setBackground(new java.awt.Color(255, 255, 255));
        pnl_BanHang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnl_BanHang.setMinimumSize(new java.awt.Dimension(180, 43));
        pnl_BanHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnl_BanHangMouseClicked(evt);
            }
        });

        indicator4.setBackground(new java.awt.Color(87, 88, 152));
        indicator4.setOpaque(false);

        javax.swing.GroupLayout indicator4Layout = new javax.swing.GroupLayout(indicator4);
        indicator4.setLayout(indicator4Layout);
        indicator4Layout.setHorizontalGroup(
            indicator4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        indicator4Layout.setVerticalGroup(
            indicator4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        lbl_BanHang.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbl_BanHang.setForeground(new java.awt.Color(97, 88, 152));
        lbl_BanHang.setText("Bán Hàng");

        javax.swing.GroupLayout pnl_BanHangLayout = new javax.swing.GroupLayout(pnl_BanHang);
        pnl_BanHang.setLayout(pnl_BanHangLayout);
        pnl_BanHangLayout.setHorizontalGroup(
            pnl_BanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_BanHangLayout.createSequentialGroup()
                .addComponent(indicator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_BanHang, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );
        pnl_BanHangLayout.setVerticalGroup(
            pnl_BanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(indicator4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnl_BanHangLayout.createSequentialGroup()
                .addComponent(lbl_BanHang, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnl_SanPham.setBackground(new java.awt.Color(255, 255, 255));
        pnl_SanPham.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnl_SanPham.setMinimumSize(new java.awt.Dimension(180, 43));
        pnl_SanPham.setPreferredSize(new java.awt.Dimension(180, 43));
        pnl_SanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnl_SanPhamMouseClicked(evt);
            }
        });

        indicator3.setBackground(new java.awt.Color(87, 88, 152));
        indicator3.setOpaque(false);

        javax.swing.GroupLayout indicator3Layout = new javax.swing.GroupLayout(indicator3);
        indicator3.setLayout(indicator3Layout);
        indicator3Layout.setHorizontalGroup(
            indicator3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        indicator3Layout.setVerticalGroup(
            indicator3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        lbl_SanPham.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbl_SanPham.setForeground(new java.awt.Color(97, 88, 152));
        lbl_SanPham.setText("Sản Phẩm");

        javax.swing.GroupLayout pnl_SanPhamLayout = new javax.swing.GroupLayout(pnl_SanPham);
        pnl_SanPham.setLayout(pnl_SanPhamLayout);
        pnl_SanPhamLayout.setHorizontalGroup(
            pnl_SanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_SanPhamLayout.createSequentialGroup()
                .addComponent(indicator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(pnl_SanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_SanPhamLayout.createSequentialGroup()
                    .addContainerGap(47, Short.MAX_VALUE)
                    .addComponent(lbl_SanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(26, 26, 26)))
        );
        pnl_SanPhamLayout.setVerticalGroup(
            pnl_SanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(indicator3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnl_SanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnl_SanPhamLayout.createSequentialGroup()
                    .addComponent(lbl_SanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 8, Short.MAX_VALUE)))
        );

        pnl_HeThong.setBackground(new java.awt.Color(255, 255, 255));
        pnl_HeThong.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnl_HeThong.setMinimumSize(new java.awt.Dimension(180, 43));
        pnl_HeThong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnl_HeThongMouseClicked(evt);
            }
        });

        indicator6.setBackground(new java.awt.Color(87, 88, 152));
        indicator6.setOpaque(false);

        javax.swing.GroupLayout indicator6Layout = new javax.swing.GroupLayout(indicator6);
        indicator6.setLayout(indicator6Layout);
        indicator6Layout.setHorizontalGroup(
            indicator6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        indicator6Layout.setVerticalGroup(
            indicator6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        lbl_HeThong.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbl_HeThong.setForeground(new java.awt.Color(97, 88, 152));
        lbl_HeThong.setText("Hệ Thống");
        lbl_HeThong.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout pnl_HeThongLayout = new javax.swing.GroupLayout(pnl_HeThong);
        pnl_HeThong.setLayout(pnl_HeThongLayout);
        pnl_HeThongLayout.setHorizontalGroup(
            pnl_HeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_HeThongLayout.createSequentialGroup()
                .addComponent(indicator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_HeThong, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );
        pnl_HeThongLayout.setVerticalGroup(
            pnl_HeThongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(indicator6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_HeThong, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnl_DiemDanh.setBackground(new java.awt.Color(255, 255, 255));
        pnl_DiemDanh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnl_DiemDanh.setMinimumSize(new java.awt.Dimension(180, 43));
        pnl_DiemDanh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnl_DiemDanhMouseClicked(evt);
            }
        });

        indicator7.setBackground(new java.awt.Color(87, 88, 152));
        indicator7.setOpaque(false);

        javax.swing.GroupLayout indicator7Layout = new javax.swing.GroupLayout(indicator7);
        indicator7.setLayout(indicator7Layout);
        indicator7Layout.setHorizontalGroup(
            indicator7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        indicator7Layout.setVerticalGroup(
            indicator7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        lbl_DiemDanh.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbl_DiemDanh.setForeground(new java.awt.Color(97, 88, 152));
        lbl_DiemDanh.setText("Điểm Danh");

        javax.swing.GroupLayout pnl_DiemDanhLayout = new javax.swing.GroupLayout(pnl_DiemDanh);
        pnl_DiemDanh.setLayout(pnl_DiemDanhLayout);
        pnl_DiemDanhLayout.setHorizontalGroup(
            pnl_DiemDanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_DiemDanhLayout.createSequentialGroup()
                .addComponent(indicator7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(lbl_DiemDanh)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl_DiemDanhLayout.setVerticalGroup(
            pnl_DiemDanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(indicator7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_DiemDanh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnl_TaiKhoan.setBackground(new java.awt.Color(255, 255, 255));
        pnl_TaiKhoan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnl_TaiKhoan.setMinimumSize(new java.awt.Dimension(180, 43));
        pnl_TaiKhoan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnl_TaiKhoanMouseClicked(evt);
            }
        });

        indicator8.setBackground(new java.awt.Color(87, 88, 152));
        indicator8.setOpaque(false);

        javax.swing.GroupLayout indicator8Layout = new javax.swing.GroupLayout(indicator8);
        indicator8.setLayout(indicator8Layout);
        indicator8Layout.setHorizontalGroup(
            indicator8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        indicator8Layout.setVerticalGroup(
            indicator8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        lbl_TaiKhoan.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbl_TaiKhoan.setForeground(new java.awt.Color(97, 88, 152));
        lbl_TaiKhoan.setText("Tài Khoản");

        javax.swing.GroupLayout pnl_TaiKhoanLayout = new javax.swing.GroupLayout(pnl_TaiKhoan);
        pnl_TaiKhoan.setLayout(pnl_TaiKhoanLayout);
        pnl_TaiKhoanLayout.setHorizontalGroup(
            pnl_TaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_TaiKhoanLayout.createSequentialGroup()
                .addComponent(indicator8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_TaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        pnl_TaiKhoanLayout.setVerticalGroup(
            pnl_TaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(indicator8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_TaiKhoan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnl_NhanVien.setBackground(new java.awt.Color(255, 255, 255));
        pnl_NhanVien.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnl_NhanVien.setMinimumSize(new java.awt.Dimension(180, 43));
        pnl_NhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnl_NhanVienMouseClicked(evt);
            }
        });

        indicator9.setBackground(new java.awt.Color(87, 88, 152));
        indicator9.setOpaque(false);

        javax.swing.GroupLayout indicator9Layout = new javax.swing.GroupLayout(indicator9);
        indicator9.setLayout(indicator9Layout);
        indicator9Layout.setHorizontalGroup(
            indicator9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        indicator9Layout.setVerticalGroup(
            indicator9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        lbl_NhanVien.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbl_NhanVien.setForeground(new java.awt.Color(97, 88, 152));
        lbl_NhanVien.setText("Nhân Viên");

        javax.swing.GroupLayout pnl_NhanVienLayout = new javax.swing.GroupLayout(pnl_NhanVien);
        pnl_NhanVien.setLayout(pnl_NhanVienLayout);
        pnl_NhanVienLayout.setHorizontalGroup(
            pnl_NhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_NhanVienLayout.createSequentialGroup()
                .addComponent(indicator9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_NhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );
        pnl_NhanVienLayout.setVerticalGroup(
            pnl_NhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(indicator9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_NhanVien, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnl_Luong.setBackground(new java.awt.Color(255, 255, 255));
        pnl_Luong.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnl_Luong.setMinimumSize(new java.awt.Dimension(180, 43));
        pnl_Luong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnl_LuongMouseClicked(evt);
            }
        });

        indicator10.setBackground(new java.awt.Color(87, 88, 152));
        indicator10.setOpaque(false);

        javax.swing.GroupLayout indicator10Layout = new javax.swing.GroupLayout(indicator10);
        indicator10.setLayout(indicator10Layout);
        indicator10Layout.setHorizontalGroup(
            indicator10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        indicator10Layout.setVerticalGroup(
            indicator10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        lbl_Luong.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbl_Luong.setForeground(new java.awt.Color(97, 88, 152));
        lbl_Luong.setText("Lương");

        javax.swing.GroupLayout pnl_LuongLayout = new javax.swing.GroupLayout(pnl_Luong);
        pnl_Luong.setLayout(pnl_LuongLayout);
        pnl_LuongLayout.setHorizontalGroup(
            pnl_LuongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_LuongLayout.createSequentialGroup()
                .addComponent(indicator10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_Luong, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );
        pnl_LuongLayout.setVerticalGroup(
            pnl_LuongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(indicator10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_Luong, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnl_KhuyenMai.setBackground(new java.awt.Color(255, 255, 255));
        pnl_KhuyenMai.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnl_KhuyenMai.setMinimumSize(new java.awt.Dimension(180, 43));
        pnl_KhuyenMai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnl_KhuyenMaiMouseClicked(evt);
            }
        });

        indicator11.setBackground(new java.awt.Color(87, 88, 152));
        indicator11.setOpaque(false);

        javax.swing.GroupLayout indicator11Layout = new javax.swing.GroupLayout(indicator11);
        indicator11.setLayout(indicator11Layout);
        indicator11Layout.setHorizontalGroup(
            indicator11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        indicator11Layout.setVerticalGroup(
            indicator11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        lbl_KhuyenMai.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbl_KhuyenMai.setForeground(new java.awt.Color(97, 88, 152));
        lbl_KhuyenMai.setText("Khuyến Mãi");

        javax.swing.GroupLayout pnl_KhuyenMaiLayout = new javax.swing.GroupLayout(pnl_KhuyenMai);
        pnl_KhuyenMai.setLayout(pnl_KhuyenMaiLayout);
        pnl_KhuyenMaiLayout.setHorizontalGroup(
            pnl_KhuyenMaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_KhuyenMaiLayout.createSequentialGroup()
                .addComponent(indicator11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_KhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );
        pnl_KhuyenMaiLayout.setVerticalGroup(
            pnl_KhuyenMaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(indicator11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_KhuyenMai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnl_MenuLayout = new javax.swing.GroupLayout(pnl_Menu);
        pnl_Menu.setLayout(pnl_MenuLayout);
        pnl_MenuLayout.setHorizontalGroup(
            pnl_MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_ThongKe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnl_KhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnl_SanPham, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnl_BanHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_Logo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnl_HeThong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnl_DiemDanh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnl_TaiKhoan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnl_NhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnl_Luong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnl_KhuyenMai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_MenuLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_DangXuat)
                .addGap(67, 67, 67))
        );
        pnl_MenuLayout.setVerticalGroup(
            pnl_MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_MenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_Logo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnl_ThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl_KhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(pnl_BanHang, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl_SanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl_HeThong, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl_DiemDanh, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl_TaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl_NhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl_Luong, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl_KhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(lbl_DangXuat)
                .addGap(14, 14, 14))
        );

        pnl_MainChinh.add(pnl_Menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 180, 590));

        pnl_MainDisplayCard.setBackground(new java.awt.Color(228, 228, 228));
        pnl_MainDisplayCard.setForeground(new java.awt.Color(255, 255, 255));
        pnl_MainDisplayCard.setPreferredSize(new java.awt.Dimension(850, 555));
        pnl_MainDisplayCard.setLayout(new java.awt.CardLayout());

        card_ThongKe.setBackground(new java.awt.Color(228, 228, 228));
        card_ThongKe.setLayout(new java.awt.GridLayout(1, 0));

        tabs_thongke.setForeground(new java.awt.Color(97, 88, 152));
        tabs_thongke.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        tabs_thongke.setInheritsPopupMenu(true);

        jPanel7.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        tbl_ThongKe_ThongKe.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MÃ SẢN PHẨM", "TÊN SẢN PHẨM ", "HÃNG SẢN XUẤT", "ĐƠN GIÁ", "TỔNG SỐ LƯỢNG", "TỔNG TIỀN ", "NGÀY BÁN"
            }
        ));
        jScrollPane23.setViewportView(tbl_ThongKe_ThongKe);

        jPanel20.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel20.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 3));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel4.setText("Ngày bắt đầu:");
        jPanel20.add(jLabel4);

        dcs_NgayBatDau_ThongKe.setDateFormatString("dd/MM/yyyy");
        dcs_NgayBatDau_ThongKe.setPreferredSize(new java.awt.Dimension(89, 30));
        jPanel20.add(dcs_NgayBatDau_ThongKe);

        jLabel13.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel13.setText("Ngày kết thúc:");
        jPanel20.add(jLabel13);

        dcs_NgayKetThuc_ThongKe.setDateFormatString("dd/MM/yyyy");
        dcs_NgayKetThuc_ThongKe.setPreferredSize(new java.awt.Dimension(89, 30));
        jPanel20.add(dcs_NgayKetThuc_ThongKe);

        jLabel15.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel15.setText("Chọn hãng sản xuất:");
        jLabel15.setPreferredSize(new java.awt.Dimension(143, 30));
        jPanel20.add(jLabel15);

        cbo_ChonHang_ThongKe.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbo_ChonHang_ThongKe.setPreferredSize(new java.awt.Dimension(150, 32));
        cbo_ChonHang_ThongKe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_ChonHang_ThongKeActionPerformed(evt);
            }
        });
        jPanel20.add(cbo_ChonHang_ThongKe);

        btn_Top5BanChay.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btn_Top5BanChay.setText("Top 5 bán chạy");
        btn_Top5BanChay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Top5BanChayActionPerformed(evt);
            }
        });
        jPanel20.add(btn_Top5BanChay);

        btn_LamMoi.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btn_LamMoi.setText("X");
        btn_LamMoi.setPreferredSize(new java.awt.Dimension(50, 35));
        btn_LamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LamMoiActionPerformed(evt);
            }
        });
        jPanel20.add(btn_LamMoi);

        jPanel53.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel53.setLayout(new java.awt.GridLayout(1, 0, 20, 0));

        btn_First_ThongKe.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btn_First_ThongKe.setText("|<");
        btn_First_ThongKe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_First_ThongKeActionPerformed(evt);
            }
        });
        jPanel53.add(btn_First_ThongKe);

        btn_Prev_ThongKe.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btn_Prev_ThongKe.setText("<<");
        btn_Prev_ThongKe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Prev_ThongKeActionPerformed(evt);
            }
        });
        jPanel53.add(btn_Prev_ThongKe);

        btn_Next_ThongKe.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btn_Next_ThongKe.setText(">>");
        btn_Next_ThongKe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Next_ThongKeActionPerformed(evt);
            }
        });
        jPanel53.add(btn_Next_ThongKe);

        btn_Last_ThongKe.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btn_Last_ThongKe.setText(">|");
        btn_Last_ThongKe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Last_ThongKeActionPerformed(evt);
            }
        });
        jPanel53.add(btn_Last_ThongKe);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane23)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jPanel53, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 361, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, 1071, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel53, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        tabs_thongke.addTab("Thống kê", jPanel7);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Doanh Thu Hôm Nay", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14))); // NOI18N
        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 30, 5));

        jPanel9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel9.setPreferredSize(new java.awt.Dimension(270, 120));

        lbl_Ngay_DoanhThu.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbl_Ngay_DoanhThu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Ngay_DoanhThu.setText("Ngày");

        lbl_SoLuong_NgayHienTai_DoanhThu.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbl_SoLuong_NgayHienTai_DoanhThu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_SoLuong_NgayHienTai_DoanhThu.setText("Số lượng sản phẩm bán được");

        lbl_TongTien_NgayHienTai_DoanhThu.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbl_TongTien_NgayHienTai_DoanhThu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_TongTien_NgayHienTai_DoanhThu.setText("Tổng tiền : ");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_SoLuong_NgayHienTai_DoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_TongTien_NgayHienTai_DoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(lbl_Ngay_DoanhThu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lbl_Ngay_DoanhThu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_SoLuong_NgayHienTai_DoanhThu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_TongTien_NgayHienTai_DoanhThu)
                .addContainerGap())
        );

        jPanel3.add(jPanel9);

        jPanel10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel10.setPreferredSize(new java.awt.Dimension(270, 120));

        lbl_Thang_DoanhThu.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbl_Thang_DoanhThu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Thang_DoanhThu.setText("Tháng");

        lbl_SoLuong_ThangHienTai_DoanhThu.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbl_SoLuong_ThangHienTai_DoanhThu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_SoLuong_ThangHienTai_DoanhThu.setText("Số lượng sản phẩm bán được");

        lbl_TongTien_ThangHienTai_DoanhThu.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbl_TongTien_ThangHienTai_DoanhThu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_TongTien_ThangHienTai_DoanhThu.setText("Tổng tiền : ");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbl_SoLuong_ThangHienTai_DoanhThu, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                    .addComponent(lbl_TongTien_ThangHienTai_DoanhThu, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                    .addComponent(lbl_Thang_DoanhThu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lbl_Thang_DoanhThu)
                .addGap(18, 18, 18)
                .addComponent(lbl_SoLuong_ThangHienTai_DoanhThu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_TongTien_ThangHienTai_DoanhThu)
                .addContainerGap())
        );

        jPanel3.add(jPanel10);

        jPanel11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel11.setPreferredSize(new java.awt.Dimension(270, 120));

        lbl_Nam_DoanhThu.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbl_Nam_DoanhThu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Nam_DoanhThu.setText("Năm");

        lbl_SoLuong_NamHienTai_DoanhThu.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbl_SoLuong_NamHienTai_DoanhThu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_SoLuong_NamHienTai_DoanhThu.setText("Số lượng sản phẩm bán được");

        lbl_TongTien_NamHienTai_DoanhThu.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbl_TongTien_NamHienTai_DoanhThu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_TongTien_NamHienTai_DoanhThu.setText("Tổng tiền : ");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbl_SoLuong_NamHienTai_DoanhThu, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                    .addComponent(lbl_TongTien_NamHienTai_DoanhThu, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                    .addComponent(lbl_Nam_DoanhThu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lbl_Nam_DoanhThu)
                .addGap(18, 18, 18)
                .addComponent(lbl_SoLuong_NamHienTai_DoanhThu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_TongTien_NamHienTai_DoanhThu)
                .addContainerGap())
        );

        jPanel3.add(jPanel11);

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Tổng doanh thu", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14))); // NOI18N

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pieChart_Tong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pieChart_Tong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Tổng doanh thu theo thời gian", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14))); // NOI18N

        dcs_NgayKetThuc_ThongKe_DoanhThu.setDateFormatString("dd/MM/yyyy");
        dcs_NgayKetThuc_ThongKe_DoanhThu.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        dcs_NgayKetThuc_ThongKe_DoanhThu.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dcs_NgayKetThuc_ThongKe_DoanhThuPropertyChange(evt);
            }
        });

        dcs_NgayBatDau_ThongKe_DoanhThu.setDateFormatString("dd/MM/yyyy");
        dcs_NgayBatDau_ThongKe_DoanhThu.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        dcs_NgayBatDau_ThongKe_DoanhThu.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dcs_NgayBatDau_ThongKe_DoanhThuPropertyChange(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("Ngày kết thúc:");

        jLabel28.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("Ngày bắt đầu:");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(pieChart_TheoNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dcs_NgayBatDau_ThongKe_DoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dcs_NgayKetThuc_ThongKe_DoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(142, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(pieChart_TheoNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(dcs_NgayBatDau_ThongKe_DoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(dcs_NgayKetThuc_ThongKe_DoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 1071, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(29, 29, 29))
        );

        tabs_thongke.addTab("Doanh thu", jPanel8);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs_thongke, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(tabs_thongke, javax.swing.GroupLayout.PREFERRED_SIZE, 599, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        card_ThongKe.add(jPanel6);

        pnl_MainDisplayCard.add(card_ThongKe, "card_ThongKe");

        tbp_KhachHang.setForeground(new java.awt.Color(97, 88, 152));
        tbp_KhachHang.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N

        jLabel127.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel127.setText("Mã khách hàng:");

        txt_MaKhachHang_KhachHang.setEditable(false);
        txt_MaKhachHang_KhachHang.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_MaKhachHang_KhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_MaKhachHang_KhachHangActionPerformed(evt);
            }
        });

        jLabel128.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel128.setText("Tên khách hàng: ");

        txt_TenKhachHang_KhachHang.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_TenKhachHang_KhachHang.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_TenKhachHang_KhachHangFocusGained(evt);
            }
        });
        txt_TenKhachHang_KhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_TenKhachHang_KhachHangActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdo_Nam_KhachHang);
        rdo_Nam_KhachHang.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        rdo_Nam_KhachHang.setText("Nam");

        buttonGroup1.add(rdo_Nu_KhachHang);
        rdo_Nu_KhachHang.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        rdo_Nu_KhachHang.setText("Nữ");

        jLabel129.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel129.setText("Giới tính: ");

        jLabel130.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel130.setText("Số điện thoại:");

        txt_SoDienThoaiKhachHang_KhachHang.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_SoDienThoaiKhachHang_KhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_SoDienThoaiKhachHang_KhachHangActionPerformed(evt);
            }
        });

        jLabel131.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel131.setText("Ghi chú:");

        txt_GhiChuKhachHang_KhachHang.setColumns(20);
        txt_GhiChuKhachHang_KhachHang.setRows(5);
        jScrollPane17.setViewportView(txt_GhiChuKhachHang_KhachHang);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel127)
                    .addComponent(jLabel128)
                    .addComponent(jLabel129, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel130)
                    .addComponent(jLabel131))
                .addGap(32, 32, 32)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(rdo_Nam_KhachHang)
                        .addGap(80, 80, 80)
                        .addComponent(rdo_Nu_KhachHang))
                    .addComponent(txt_TenKhachHang_KhachHang)
                    .addComponent(txt_MaKhachHang_KhachHang)
                    .addComponent(txt_SoDienThoaiKhachHang_KhachHang)
                    .addComponent(jScrollPane17))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_MaKhachHang_KhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel127, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel128, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_TenKhachHang_KhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel129, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rdo_Nam_KhachHang)
                        .addComponent(rdo_Nu_KhachHang)))
                .addGap(31, 31, 31)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel130, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_SoDienThoaiKhachHang_KhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel131, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Danh Sách Khách Hàng", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 16), new java.awt.Color(34, 51, 102))); // NOI18N

        tbl_DSKhachHang_KhachHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MÃ KHÁCH HÀNG", "TÊN KHÁCH HÀNG", "GIỚI TÍNH", "SỐ ĐIỆN THOẠI", "GHI CHÚ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_DSKhachHang_KhachHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_DSKhachHang_KhachHangMouseClicked(evt);
            }
        });
        jScrollPane18.setViewportView(tbl_DSKhachHang_KhachHang);

        jPanel50.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel50.setLayout(new java.awt.GridLayout(1, 0, 20, 0));

        btn_First_KhachHang.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btn_First_KhachHang.setText("|<");
        btn_First_KhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_First_KhachHangActionPerformed(evt);
            }
        });
        jPanel50.add(btn_First_KhachHang);

        btn_Prev_KhachHang.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btn_Prev_KhachHang.setText("<<");
        btn_Prev_KhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Prev_KhachHangActionPerformed(evt);
            }
        });
        jPanel50.add(btn_Prev_KhachHang);

        btn_Next_KhachHang.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btn_Next_KhachHang.setText(">>");
        btn_Next_KhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Next_KhachHangActionPerformed(evt);
            }
        });
        jPanel50.add(btn_Next_KhachHang);

        btn_Last_KhachHang.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btn_Last_KhachHang.setText(">|");
        btn_Last_KhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Last_KhachHangActionPerformed(evt);
            }
        });
        jPanel50.add(btn_Last_KhachHang);

        txt_TimSoDienThoaiKhachHang_KhachHang.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_TimSoDienThoaiKhachHang_KhachHang.setText("Nhập số điện thoại khách hàng");
        txt_TimSoDienThoaiKhachHang_KhachHang.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_TimSoDienThoaiKhachHang_KhachHangFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_TimSoDienThoaiKhachHang_KhachHangFocusLost(evt);
            }
        });
        txt_TimSoDienThoaiKhachHang_KhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_TimSoDienThoaiKhachHang_KhachHangActionPerformed(evt);
            }
        });
        txt_TimSoDienThoaiKhachHang_KhachHang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_TimSoDienThoaiKhachHang_KhachHangKeyReleased(evt);
            }
        });

        btn_ThemKhachHangVaoHoaDon_KhachHang.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btn_ThemKhachHangVaoHoaDon_KhachHang.setText("Tạo hóa đơn");
        btn_ThemKhachHangVaoHoaDon_KhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ThemKhachHangVaoHoaDon_KhachHangActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane18, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel17Layout.createSequentialGroup()
                        .addComponent(txt_TimSoDienThoaiKhachHang_KhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                        .addComponent(btn_ThemKhachHangVaoHoaDon_KhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel50, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_TimSoDienThoaiKhachHang_KhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_ThemKhachHangVaoHoaDon_KhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane18, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel18.setLayout(new java.awt.GridLayout(1, 0, 25, 0));

        btn_ThemKhachHang_KhachHang.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btn_ThemKhachHang_KhachHang.setText("Thêm");
        btn_ThemKhachHang_KhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ThemKhachHang_KhachHangActionPerformed(evt);
            }
        });
        jPanel18.add(btn_ThemKhachHang_KhachHang);

        btn_CapNhatKhachHang_KhachHang.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btn_CapNhatKhachHang_KhachHang.setText("Cập nhật");
        btn_CapNhatKhachHang_KhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CapNhatKhachHang_KhachHangActionPerformed(evt);
            }
        });
        jPanel18.add(btn_CapNhatKhachHang_KhachHang);

        btn_XoaKhachHang_KhachHang.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btn_XoaKhachHang_KhachHang.setText("Xóa");
        btn_XoaKhachHang_KhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XoaKhachHang_KhachHangActionPerformed(evt);
            }
        });
        jPanel18.add(btn_XoaKhachHang_KhachHang);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65)
                        .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tbp_KhachHang.addTab("Thông tin chi tiết", jPanel14);

        tbl_DSKhachHangDaXoa_KhachHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MÃ KHÁCH HÀNG", "TÊN KHÁCH HÀNG", "GIỚI TÍNH", "SỐ ĐIỆN THOẠI"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane19.setViewportView(tbl_DSKhachHangDaXoa_KhachHang);

        txt_TimSoDienThoaiKhachHangDaXoa_KhachHang.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_TimSoDienThoaiKhachHangDaXoa_KhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_TimSoDienThoaiKhachHangDaXoa_KhachHangActionPerformed(evt);
            }
        });
        txt_TimSoDienThoaiKhachHangDaXoa_KhachHang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_TimSoDienThoaiKhachHangDaXoa_KhachHangKeyReleased(evt);
            }
        });

        btn_KhoiPhucKhachHang_KhachHang.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btn_KhoiPhucKhachHang_KhachHang.setText("Khôi phục");
        btn_KhoiPhucKhachHang_KhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_KhoiPhucKhachHang_KhachHangActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane19))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(202, 202, 202)
                        .addComponent(txt_TimSoDienThoaiKhachHangDaXoa_KhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                        .addComponent(btn_KhoiPhucKhachHang_KhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 250, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_TimSoDienThoaiKhachHangDaXoa_KhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_KhoiPhucKhachHang_KhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tbp_KhachHang.addTab("Khách hàng đã xóa", jPanel15);

        javax.swing.GroupLayout card_KhachHangLayout = new javax.swing.GroupLayout(card_KhachHang);
        card_KhachHang.setLayout(card_KhachHangLayout);
        card_KhachHangLayout.setHorizontalGroup(
            card_KhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tbp_KhachHang)
        );
        card_KhachHangLayout.setVerticalGroup(
            card_KhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card_KhachHangLayout.createSequentialGroup()
                .addComponent(tbp_KhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 583, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 13, Short.MAX_VALUE))
        );

        pnl_MainDisplayCard.add(card_KhachHang, "card_KhachHang");

        card_HeThong.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel16.setFont(new java.awt.Font("Arial", 1, 28)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(97, 88, 152));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Chào mừng trở lại");
        card_HeThong.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 6, 1100, -1));

        jLabel50.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel50.setText("Đăng nhập gần đây");
        card_HeThong.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 50, 170, -1));

        jPanel21.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 204)));
        jPanel21.setPreferredSize(new java.awt.Dimension(360, 293));

        btn_submit_HeThong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/phonesystem/edu/img/submit.png"))); // NOI18N
        btn_submit_HeThong.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_submit_HeThong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_submit_HeThongMouseClicked(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(97, 88, 152));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Đặt mật khẩu mới tại đây!!!");

        txt_TenDangNhap_HeThong.setEditable(false);

        jLabel71.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel71.setForeground(new java.awt.Color(51, 51, 51));
        jLabel71.setText("Tên đăng nhập:");

        jLabel72.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(51, 51, 51));
        jLabel72.setText("Mật khẩu:");

        jLabel73.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(51, 51, 51));
        jLabel73.setText("Mật khẩu mới:");

        jLabel74.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(51, 51, 51));
        jLabel74.setText("Nhập lại mật khẩu:");

        txt_MatKhau_HeThong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_MatKhau_HeThongActionPerformed(evt);
            }
        });

        txt_MatKhauMoi_HeThong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_MatKhauMoi_HeThongActionPerformed(evt);
            }
        });

        txt_Confirm_HeThong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_Confirm_HeThongActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel71)
                            .addComponent(txt_TenDangNhap_HeThong, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel72)
                            .addComponent(txt_MatKhau_HeThong, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel73)
                            .addComponent(txt_MatKhauMoi_HeThong, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel74)))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btn_submit_HeThong)
                            .addComponent(txt_Confirm_HeThong, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addComponent(jLabel17)
                .addGap(28, 28, 28)
                .addComponent(jLabel71)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_TenDangNhap_HeThong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel72)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_MatKhau_HeThong, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel73)
                .addGap(7, 7, 7)
                .addComponent(txt_MatKhauMoi_HeThong, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel74)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_Confirm_HeThong, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(btn_submit_HeThong, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        card_HeThong.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 230, 360, 330));
        card_HeThong.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 400, 10));
        card_HeThong.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 210, 370, 10));

        lblLoginAccount1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblLoginAccount1.setForeground(new java.awt.Color(97, 88, 152));
        lblLoginAccount1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLoginAccount1.setText("hientrantrong");
        card_HeThong.add(lblLoginAccount1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 150, 80, -1));

        lblLoginAcout2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblLoginAcout2.setForeground(new java.awt.Color(97, 88, 152));
        lblLoginAcout2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLoginAcout2.setText("duongminhle");
        card_HeThong.add(lblLoginAcout2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 150, 80, -1));

        jLabel70.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel70.setForeground(new java.awt.Color(97, 88, 152));
        jLabel70.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel70.setText("Quên mật khẩu của bạn?");
        card_HeThong.add(jLabel70, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, 1030, -1));

        jLabel75.setIcon(new javax.swing.ImageIcon(getClass().getResource("/phonesystem/edu/img/recent1.png"))); // NOI18N
        jLabel75.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel75.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel75MouseClicked(evt);
            }
        });
        card_HeThong.add(jLabel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 90, 100, 90));

        jLabel113.setIcon(new javax.swing.ImageIcon(getClass().getResource("/phonesystem/edu/img/recent2.png"))); // NOI18N
        jLabel113.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel113.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel113MouseClicked(evt);
            }
        });
        card_HeThong.add(jLabel113, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 90, 100, 90));

        jLabel114.setIcon(new javax.swing.ImageIcon(getClass().getResource("/phonesystem/edu/img/recent3.png"))); // NOI18N
        jLabel114.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel114.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel114MouseClicked(evt);
            }
        });
        card_HeThong.add(jLabel114, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 90, 100, 90));

        pnl_MainDisplayCard.add(card_HeThong, "card_HeThong");

        jPanel37.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(153, 153, 153)));

        jLabel56.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel56.setText("Mã NV:");

        jLabel112.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel112.setText("Tên Đăng Nhập:");

        jLabel115.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel115.setText("Mật Khẩu:");

        jLabel116.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel116.setText("Nhập lại mật khẩu:");

        jLabel117.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel117.setText("Vai trò:");

        jPanel39.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(153, 153, 153)));
        jPanel39.setLayout(new java.awt.GridLayout(1, 0, 30, 0));

        btn_Them_TaiKhoan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/phonesystem/edu/img/icons8_add_new_30px.png"))); // NOI18N
        btn_Them_TaiKhoan.setText("Thêm");
        btn_Them_TaiKhoan.setMaximumSize(new java.awt.Dimension(80, 46));
        btn_Them_TaiKhoan.setMinimumSize(new java.awt.Dimension(80, 46));
        btn_Them_TaiKhoan.setPreferredSize(new java.awt.Dimension(80, 46));
        btn_Them_TaiKhoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Them_TaiKhoanActionPerformed(evt);
            }
        });
        jPanel39.add(btn_Them_TaiKhoan);

        btn_CapNhat_TaiKhoan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/phonesystem/edu/img/icons8_restart_30px.png"))); // NOI18N
        btn_CapNhat_TaiKhoan.setText("Cập Nhật");
        btn_CapNhat_TaiKhoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CapNhat_TaiKhoanActionPerformed(evt);
            }
        });
        jPanel39.add(btn_CapNhat_TaiKhoan);

        btn_Xoa_TaiKhoan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/phonesystem/edu/img/icons8_delete_30px.png"))); // NOI18N
        btn_Xoa_TaiKhoan.setText("Xóa");
        btn_Xoa_TaiKhoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Xoa_TaiKhoanActionPerformed(evt);
            }
        });
        jPanel39.add(btn_Xoa_TaiKhoan);

        txt_MaNV_TaiKhoan.setEditable(false);

        txt_TenDangNhap_TaiKhoan.setToolTipText("tên đăng nhập");
        txt_TenDangNhap_TaiKhoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_TenDangNhap_TaiKhoanActionPerformed(evt);
            }
        });

        txt_MatKhau_TaiKhoan.setToolTipText("mật khẩu");
        txt_MatKhau_TaiKhoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_MatKhau_TaiKhoanActionPerformed(evt);
            }
        });

        txt_Confirm_TaiKhoan.setToolTipText("xác nhận mật khẩu");
        txt_Confirm_TaiKhoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_Confirm_TaiKhoanActionPerformed(evt);
            }
        });

        btn_group_VaiTro.add(rdo_QuanLi_TaiKhoan);
        rdo_QuanLi_TaiKhoan.setText("Quản lí");

        btn_group_VaiTro.add(rdo_NhanVien_TaiKhoan);
        rdo_NhanVien_TaiKhoan.setSelected(true);
        rdo_NhanVien_TaiKhoan.setText("Nhân Viên");

        jPanel40.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh Sách Nhân Viên Chưa Có Tài Khoản", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 16), new java.awt.Color(34, 51, 102))); // NOI18N
        jPanel40.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbl_DSDaCoTaiKhoan_TaiKhoan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MÃ NV", "TÊN ĐĂNG NHẬP", "VAI TRÒ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_DSDaCoTaiKhoan_TaiKhoan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_DSDaCoTaiKhoan_TaiKhoanMouseClicked(evt);
            }
        });
        jScrollPane13.setViewportView(tbl_DSDaCoTaiKhoan_TaiKhoan);

        jPanel40.add(jScrollPane13, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 22, 500, 220));

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel117)
                            .addComponent(jLabel116)
                            .addComponent(jLabel115)
                            .addComponent(jLabel112)
                            .addComponent(jLabel56))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel37Layout.createSequentialGroup()
                                    .addComponent(rdo_QuanLi_TaiKhoan)
                                    .addGap(18, 18, 18)
                                    .addComponent(rdo_NhanVien_TaiKhoan))
                                .addComponent(txt_MaNV_TaiKhoan, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                                .addComponent(txt_TenDangNhap_TaiKhoan)
                                .addComponent(txt_MatKhau_TaiKhoan))
                            .addComponent(txt_Confirm_TaiKhoan)))
                    .addComponent(jPanel39, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE))
                .addGap(40, 40, 40)
                .addComponent(jPanel40, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel37Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel56)
                            .addComponent(txt_MaNV_TaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel112))
                    .addComponent(txt_TenDangNhap_TaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel115)
                    .addComponent(txt_MatKhau_TaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel116)
                    .addComponent(txt_Confirm_TaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel117)
                    .addComponent(rdo_QuanLi_TaiKhoan)
                    .addComponent(rdo_NhanVien_TaiKhoan))
                .addGap(18, 18, 18)
                .addComponent(jPanel39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel38.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh Sách Nhân Viên Chưa Có Tài Khoản", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 16), new java.awt.Color(97, 88, 152))); // NOI18N

        tbl_DSChuaTaiKhoan_TaiKhoan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MÃ NV", "TÊN NV", "NGÀY SINH", "GIỚI TÍNH", "SĐT", "EMAIL", "ĐỊA CHỈ", "CCCD", "TRẠNG THÁI", "HÌNH ẢNH", "GHI CHÚ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_DSChuaTaiKhoan_TaiKhoan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_DSChuaTaiKhoan_TaiKhoanMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(tbl_DSChuaTaiKhoan_TaiKhoan);
        if (tbl_DSChuaTaiKhoan_TaiKhoan.getColumnModel().getColumnCount() > 0) {
            tbl_DSChuaTaiKhoan_TaiKhoan.getColumnModel().getColumn(10).setResizable(false);
        }

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 1037, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btn_First_DSChuaTaiKhoan_taikhoan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_First_DSChuaTaiKhoan_taikhoan.setForeground(new java.awt.Color(97, 88, 152));
        btn_First_DSChuaTaiKhoan_taikhoan.setText("|<");
        btn_First_DSChuaTaiKhoan_taikhoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_First_DSChuaTaiKhoan_taikhoanActionPerformed(evt);
            }
        });

        btn_prev_DSChuaTaiKhoan_taikhoan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_prev_DSChuaTaiKhoan_taikhoan.setForeground(new java.awt.Color(97, 88, 152));
        btn_prev_DSChuaTaiKhoan_taikhoan.setText("<<");
        btn_prev_DSChuaTaiKhoan_taikhoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_prev_DSChuaTaiKhoan_taikhoanActionPerformed(evt);
            }
        });

        btn_next_DSChuaTaiKhoan_taikhoan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_next_DSChuaTaiKhoan_taikhoan.setForeground(new java.awt.Color(97, 88, 152));
        btn_next_DSChuaTaiKhoan_taikhoan.setText(">>");
        btn_next_DSChuaTaiKhoan_taikhoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_next_DSChuaTaiKhoan_taikhoanActionPerformed(evt);
            }
        });

        btn_last_DSChuaTaiKhoan_taikhoan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_last_DSChuaTaiKhoan_taikhoan.setForeground(new java.awt.Color(97, 88, 152));
        btn_last_DSChuaTaiKhoan_taikhoan.setText(">|");
        btn_last_DSChuaTaiKhoan_taikhoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_last_DSChuaTaiKhoan_taikhoanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout card_TaiKhoanLayout = new javax.swing.GroupLayout(card_TaiKhoan);
        card_TaiKhoan.setLayout(card_TaiKhoanLayout);
        card_TaiKhoanLayout.setHorizontalGroup(
            card_TaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card_TaiKhoanLayout.createSequentialGroup()
                .addGroup(card_TaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(card_TaiKhoanLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(card_TaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(card_TaiKhoanLayout.createSequentialGroup()
                        .addGap(385, 385, 385)
                        .addComponent(btn_First_DSChuaTaiKhoan_taikhoan)
                        .addGap(41, 41, 41)
                        .addComponent(btn_prev_DSChuaTaiKhoan_taikhoan)
                        .addGap(38, 38, 38)
                        .addComponent(btn_next_DSChuaTaiKhoan_taikhoan)
                        .addGap(37, 37, 37)
                        .addComponent(btn_last_DSChuaTaiKhoan_taikhoan)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        card_TaiKhoanLayout.setVerticalGroup(
            card_TaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card_TaiKhoanLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(card_TaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_First_DSChuaTaiKhoan_taikhoan)
                    .addComponent(btn_prev_DSChuaTaiKhoan_taikhoan)
                    .addComponent(btn_next_DSChuaTaiKhoan_taikhoan)
                    .addComponent(btn_last_DSChuaTaiKhoan_taikhoan))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnl_MainDisplayCard.add(card_TaiKhoan, "card_TaiKhoan");

        jLabel51.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel51.setText("Mã Nhân Viên:");

        jLabel57.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel57.setText("Tên Nhân Viên:");

        jLabel80.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel80.setText("Ca Làm:");

        jLabel81.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel81.setText("Ngày Làm Việc:");

        jLabel82.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel82.setText("Ghi Chú:");

        lbl_MaNhanVien_DiemDanh.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbl_MaNhanVien_DiemDanh.setText(" ");

        lbl_TenNhanVien_DiemDanh.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbl_TenNhanVien_DiemDanh.setText(" ");

        lbl_CaLam_DiemDanh.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbl_CaLam_DiemDanh.setText(" ");

        lbl_NgayLamViec_DiemDanh.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbl_NgayLamViec_DiemDanh.setText(" ");

        btn_DiemDanh_DiemDanh.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_DiemDanh_DiemDanh.setForeground(new java.awt.Color(97, 88, 152));
        btn_DiemDanh_DiemDanh.setText("Điểm Danh");
        btn_DiemDanh_DiemDanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DiemDanh_DiemDanhActionPerformed(evt);
            }
        });

        jLabel87.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel87.setText("<html>Quy định: Điểm danh trong 30 phút đầu của ca làm<html>");

        jLabel88.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel88.setText("Ca Sáng: Từ 7:00 - 11:00");

        jLabel89.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel89.setText("Ca Chiều: Từ 13:00 - 17:00");

        txa_GhiChu_DiemDanh.setColumns(20);
        txa_GhiChu_DiemDanh.setRows(5);
        jScrollPane8.setViewportView(txa_GhiChu_DiemDanh);

        jLabel92.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel92.setForeground(new java.awt.Color(97, 88, 152));
        jLabel92.setText("Danh sách Điểm Danh");

        tbl_DanhSachDiemDanh_DiemDanh.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "STT", "MÃ NHÂN VIÊN", "TÊN NHÂN VIÊN", "CA LÀM", "NGÀY LÀM VIỆC", "GHI CHÚ"
            }
        ));
        tbl_DanhSachDiemDanh_DiemDanh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_DanhSachDiemDanh_DiemDanhMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tbl_DanhSachDiemDanh_DiemDanh);

        btn_First_DiemDanh.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_First_DiemDanh.setForeground(new java.awt.Color(97, 88, 152));
        btn_First_DiemDanh.setText("|<");
        btn_First_DiemDanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_First_DiemDanhActionPerformed(evt);
            }
        });

        btn_Previous_DiemDanh.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_Previous_DiemDanh.setForeground(new java.awt.Color(97, 88, 152));
        btn_Previous_DiemDanh.setText("<<");
        btn_Previous_DiemDanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Previous_DiemDanhActionPerformed(evt);
            }
        });

        btn_Next_DiemDanh.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_Next_DiemDanh.setForeground(new java.awt.Color(97, 88, 152));
        btn_Next_DiemDanh.setText(">>");
        btn_Next_DiemDanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Next_DiemDanhActionPerformed(evt);
            }
        });

        btn_Last_DiemDanh.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_Last_DiemDanh.setForeground(new java.awt.Color(97, 88, 152));
        btn_Last_DiemDanh.setText(">|");
        btn_Last_DiemDanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Last_DiemDanhActionPerformed(evt);
            }
        });

        jLabel136.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel136.setText("Tìm theo:");

        cbo_DiemDanh_DiemDanh.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        cbo_DiemDanh_DiemDanh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "STT", "Mã Nhân Viên", "Tên Nhân Viên", "Ca Làm Việc", "Ngày Làm Việc", "Ghi Chú" }));

        txt_TimDiemDanh_DiemDanh.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        txt_TimDiemDanh_DiemDanh.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_TimDiemDanh_DiemDanhKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout card_DiemDanhLayout = new javax.swing.GroupLayout(card_DiemDanh);
        card_DiemDanh.setLayout(card_DiemDanhLayout);
        card_DiemDanhLayout.setHorizontalGroup(
            card_DiemDanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card_DiemDanhLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(card_DiemDanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel89)
                    .addComponent(jLabel88)
                    .addGroup(card_DiemDanhLayout.createSequentialGroup()
                        .addGroup(card_DiemDanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel51)
                            .addComponent(jLabel57)
                            .addComponent(jLabel80)
                            .addComponent(jLabel81)
                            .addComponent(jLabel82))
                        .addGap(37, 37, 37)
                        .addGroup(card_DiemDanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbl_MaNhanVien_DiemDanh, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                            .addComponent(lbl_NgayLamViec_DiemDanh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_CaLam_DiemDanh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_TenNhanVien_DiemDanh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(card_DiemDanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel87, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                        .addComponent(btn_DiemDanh_DiemDanh, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(card_DiemDanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(card_DiemDanhLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(card_DiemDanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card_DiemDanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card_DiemDanhLayout.createSequentialGroup()
                                    .addComponent(jLabel92)
                                    .addGap(528, 528, 528))
                                .addGroup(card_DiemDanhLayout.createSequentialGroup()
                                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 668, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap()))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card_DiemDanhLayout.createSequentialGroup()
                                .addComponent(btn_First_DiemDanh)
                                .addGap(65, 65, 65)
                                .addComponent(btn_Previous_DiemDanh)
                                .addGap(63, 63, 63)
                                .addComponent(btn_Next_DiemDanh)
                                .addGap(75, 75, 75)
                                .addComponent(btn_Last_DiemDanh)
                                .addGap(138, 138, 138))))
                    .addGroup(card_DiemDanhLayout.createSequentialGroup()
                        .addComponent(jLabel136)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbo_DiemDanh_DiemDanh, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_TimDiemDanh_DiemDanh, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        card_DiemDanhLayout.setVerticalGroup(
            card_DiemDanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card_DiemDanhLayout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addGroup(card_DiemDanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card_DiemDanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel51)
                        .addComponent(lbl_MaNhanVien_DiemDanh))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card_DiemDanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                        .addComponent(txt_TimDiemDanh_DiemDanh, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                        .addComponent(jLabel136, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbo_DiemDanh_DiemDanh, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(card_DiemDanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel57)
                    .addComponent(lbl_TenNhanVien_DiemDanh)
                    .addComponent(jLabel92))
                .addGroup(card_DiemDanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(card_DiemDanhLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(card_DiemDanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel80)
                            .addComponent(lbl_CaLam_DiemDanh))
                        .addGap(18, 18, 18)
                        .addGroup(card_DiemDanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel81)
                            .addComponent(lbl_NgayLamViec_DiemDanh))
                        .addGap(18, 18, 18)
                        .addGroup(card_DiemDanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel82)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37)
                        .addComponent(btn_DiemDanh_DiemDanh, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel87, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel88)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel89)
                        .addGap(32, 32, 32))
                    .addGroup(card_DiemDanhLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(card_DiemDanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_First_DiemDanh)
                            .addComponent(btn_Last_DiemDanh)
                            .addComponent(btn_Previous_DiemDanh)
                            .addComponent(btn_Next_DiemDanh))
                        .addGap(30, 30, 30))))
        );

        pnl_MainDisplayCard.add(card_DiemDanh, "card_DiemDanh");

        jPanel47.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Quản lý Lương", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 16), new java.awt.Color(34, 51, 102))); // NOI18N

        jLabel64.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel64.setText("Mã Nhân Viên:");

        jLabel118.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel118.setText("Tên Nhân Viên:");

        jLabel119.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel119.setText("Chức Vụ:");

        jLabel120.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel120.setText("Tổng Ca Làm:");

        jPanel51.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(153, 153, 153)));
        jPanel51.setLayout(new java.awt.GridLayout(0, 1, 0, 10));

        btn_TinhLuong_Luong.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_TinhLuong_Luong.setText("Tính Lương");
        btn_TinhLuong_Luong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_TinhLuong_LuongActionPerformed(evt);
            }
        });
        jPanel51.add(btn_TinhLuong_Luong);

        btn_CapNhatLuong_Luong.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_CapNhatLuong_Luong.setText("Cập Nhật Lương");
        btn_CapNhatLuong_Luong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CapNhatLuong_LuongActionPerformed(evt);
            }
        });
        jPanel51.add(btn_CapNhatLuong_Luong);

        btn_NhanLuong_Luong.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_NhanLuong_Luong.setText("Nhận Lương");
        btn_NhanLuong_Luong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_NhanLuong_LuongActionPerformed(evt);
            }
        });
        jPanel51.add(btn_NhanLuong_Luong);

        cbo_MaNhanVien_Luong.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbo_MaNhanVien_Luong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_MaNhanVien_LuongActionPerformed(evt);
            }
        });

        lbl_TenNhanVien_Luong.setText("--");

        lbl_ChucVu_Luong.setText("--");

        lbl_TongCaLam_Luong.setText("--");

        jLabel67.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel67.setText("Lương/Ca:");

        jLabel76.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel76.setText("Tiền Thưởng:");

        jLabel84.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel84.setText("Tổng Lương:");

        jLabel85.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel85.setText("Ngày Nhận:");

        txt_Luong_Luong.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_Luong_LuongKeyReleased(evt);
            }
        });

        txt_TienThuong_Luong.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_TienThuong_LuongKeyReleased(evt);
            }
        });

        lbl_TongLuong_Luong.setText("--");

        lbl_NgayNhan_Luong.setText("--");

        jLabel45.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel45.setText("VND");

        jLabel46.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel46.setText("VND");

        jLabel101.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel101.setText("Ghi Chú:");

        txt_GhiChu_Luong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_GhiChu_LuongActionPerformed(evt);
            }
        });

        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel49.setText("VND");

        javax.swing.GroupLayout jPanel47Layout = new javax.swing.GroupLayout(jPanel47);
        jPanel47.setLayout(jPanel47Layout);
        jPanel47Layout.setHorizontalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel47Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel47Layout.createSequentialGroup()
                        .addGap(128, 128, 128)
                        .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_Luong_Luong)
                            .addGroup(jPanel47Layout.createSequentialGroup()
                                .addComponent(lbl_TongCaLam_Luong, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 84, Short.MAX_VALUE))
                            .addComponent(txt_TienThuong_Luong)))
                    .addGroup(jPanel47Layout.createSequentialGroup()
                        .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel120)
                            .addComponent(jLabel67)
                            .addComponent(jLabel76))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel47Layout.createSequentialGroup()
                        .addComponent(jLabel64)
                        .addGap(28, 28, 28)
                        .addComponent(cbo_MaNhanVien_Luong, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel45)
                    .addComponent(jLabel46))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel118)
                    .addComponent(jLabel84)
                    .addComponent(jLabel85, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel119)
                    .addComponent(jLabel101))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel47Layout.createSequentialGroup()
                        .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbl_NgayNhan_Luong, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                            .addComponent(lbl_TongLuong_Luong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_GhiChu_Luong))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel49)
                        .addGap(9, 9, 9))
                    .addGroup(jPanel47Layout.createSequentialGroup()
                        .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lbl_TenNhanVien_Luong, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                            .addComponent(lbl_ChucVu_Luong, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jPanel51, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel47Layout.setVerticalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel47Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel47Layout.createSequentialGroup()
                        .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel47Layout.createSequentialGroup()
                                .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel119, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_ChucVu_Luong, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                                    .addComponent(lbl_TenNhanVien_Luong, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel118, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lbl_TongLuong_Luong, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel84, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel85, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_NgayNhan_Luong, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel101, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_GhiChu_Luong)))
                            .addGroup(jPanel47Layout.createSequentialGroup()
                                .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(cbo_MaNhanVien_Luong, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel47Layout.createSequentialGroup()
                                        .addGap(77, 77, 77)
                                        .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel47Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(lbl_TongCaLam_Luong, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel120, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txt_Luong_Luong, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel47Layout.createSequentialGroup()
                                        .addGap(11, 11, 11)
                                        .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel47Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel76)
                                            .addComponent(txt_TienThuong_Luong, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGap(0, 1, Short.MAX_VALUE))
                    .addComponent(jPanel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel52.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách nhận lương", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 16), new java.awt.Color(34, 51, 102))); // NOI18N

        tbl_DSLuong_Luong.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "MÃ LƯƠNG", "MÃ NV", "TÊN NV", "TỔNG CA LÀM", "LƯƠNG/CA", "TIỀN THƯỞNG", "TỔNG LƯƠNG", "NGÀY NHẬN", "TRẠNG THÁI", "GHI CHÚ"
            }
        ));
        tbl_DSLuong_Luong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_DSLuong_LuongMouseClicked(evt);
            }
        });
        jScrollPane20.setViewportView(tbl_DSLuong_Luong);

        jLabel86.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel86.setForeground(new java.awt.Color(97, 88, 152));
        jLabel86.setText("Lọc Ngày nhận Lương");

        dcs_LocTheoNgayNhan_Luong.setDateFormatString("dd/MM/yyyy");
        dcs_LocTheoNgayNhan_Luong.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dcs_LocTheoNgayNhan_LuongPropertyChange(evt);
            }
        });

        btn_TimKiemLuongTheoNgay_Luong.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btn_TimKiemLuongTheoNgay_Luong.setForeground(new java.awt.Color(97, 88, 152));
        btn_TimKiemLuongTheoNgay_Luong.setText("X");

        jPanel54.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel54.setLayout(new java.awt.GridLayout(1, 0, 20, 0));

        btn_First_NhanVien3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btn_First_NhanVien3.setText("|<");
        btn_First_NhanVien3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_First_NhanVien3ActionPerformed(evt);
            }
        });
        jPanel54.add(btn_First_NhanVien3);

        btn_Prev_NhanVien3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btn_Prev_NhanVien3.setText("<<");
        jPanel54.add(btn_Prev_NhanVien3);

        btn_Next_NhanVien3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btn_Next_NhanVien3.setText(">>");
        btn_Next_NhanVien3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Next_NhanVien3ActionPerformed(evt);
            }
        });
        jPanel54.add(btn_Next_NhanVien3);

        btn_Last_NhanVien3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btn_Last_NhanVien3.setText(">|");
        jPanel54.add(btn_Last_NhanVien3);

        javax.swing.GroupLayout jPanel52Layout = new javax.swing.GroupLayout(jPanel52);
        jPanel52.setLayout(jPanel52Layout);
        jPanel52Layout.setHorizontalGroup(
            jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel52Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel52Layout.createSequentialGroup()
                        .addComponent(jLabel86)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dcs_LocTheoNgayNhan_Luong, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_TimKiemLuongTheoNgay_Luong)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel54, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane20))
                .addContainerGap())
        );
        jPanel52Layout.setVerticalGroup(
            jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel52Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel86, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dcs_LocTheoNgayNhan_Luong, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_TimKiemLuongTheoNgay_Luong, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel54, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane20, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout card_LuongLayout = new javax.swing.GroupLayout(card_Luong);
        card_Luong.setLayout(card_LuongLayout);
        card_LuongLayout.setHorizontalGroup(
            card_LuongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card_LuongLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(card_LuongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        card_LuongLayout.setVerticalGroup(
            card_LuongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card_LuongLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnl_MainDisplayCard.add(card_Luong, "card_Luong");

        card_NhanVien.setLayout(new java.awt.GridLayout(1, 0));

        jPanel48.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Danh Sách Nhân Viên", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 16), new java.awt.Color(34, 51, 102))); // NOI18N

        tbl_DSNhanVien_NhanVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MÃ NHÂN VIÊN", "TÊN NHÂN VIÊN", "NGÀY SINH", "GIỚI TÍNH", "SỐ ĐIỆN THOẠI", "EMAIL", "ĐỊA CHỈ", "CCCD", "HÌNH ẢNH", "TRẠNG THÁI", "GHI CHÚ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_DSNhanVien_NhanVien.setFillsViewportHeight(true);
        tbl_DSNhanVien_NhanVien.setPreferredSize(new java.awt.Dimension(600, 64));
        tbl_DSNhanVien_NhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_DSNhanVien_NhanVienMouseClicked(evt);
            }
        });
        jScrollPane15.setViewportView(tbl_DSNhanVien_NhanVien);

        txt_TimDiaChi_NhanVien.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        txt_TimDiaChi_NhanVien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_TimDiaChi_NhanVienKeyReleased(evt);
            }
        });

        cbo_NhanVien_NhanVien.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        cbo_NhanVien_NhanVien.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mã", "Tên", "Ngày Sinh", "Giới Tính", "SDT", "Email", "Địa Chỉ", "CCCD", "Hình", "Trạng Thái" }));

        jPanel49.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel49.setLayout(new java.awt.GridLayout(1, 0, 20, 0));

        btn_First_NhanVien.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btn_First_NhanVien.setText("|<");
        btn_First_NhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_First_NhanVienActionPerformed(evt);
            }
        });
        jPanel49.add(btn_First_NhanVien);

        btn_Prev_NhanVien.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btn_Prev_NhanVien.setText("<<");
        btn_Prev_NhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Prev_NhanVienActionPerformed(evt);
            }
        });
        jPanel49.add(btn_Prev_NhanVien);

        btn_Next_NhanVien.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btn_Next_NhanVien.setText(">>");
        btn_Next_NhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Next_NhanVienActionPerformed(evt);
            }
        });
        jPanel49.add(btn_Next_NhanVien);

        btn_Last_NhanVien.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btn_Last_NhanVien.setText(">|");
        btn_Last_NhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Last_NhanVienActionPerformed(evt);
            }
        });
        jPanel49.add(btn_Last_NhanVien);

        jLabel132.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel132.setText("Tìm theo:");

        jButton1.setText("X");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel48Layout = new javax.swing.GroupLayout(jPanel48);
        jPanel48.setLayout(jPanel48Layout);
        jPanel48Layout.setHorizontalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel48Layout.createSequentialGroup()
                .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel48Layout.createSequentialGroup()
                        .addComponent(jLabel132)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbo_NhanVien_NhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_TimDiaChi_NhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addGap(136, 136, 136)
                        .addComponent(jPanel49, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 1012, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 37, Short.MAX_VALUE))
        );
        jPanel48Layout.setVerticalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel48Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel49, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_TimDiaChi_NhanVien)
                        .addComponent(jLabel132, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbo_NhanVien_NhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE))
        );

        jPanel41.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Thông tin chi tiết", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 16), new java.awt.Color(34, 51, 102))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Địa chỉ:");

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("CCCD:");

        jLabel59.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel59.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel59.setText("Trạng thái:");

        jLabel83.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel83.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel83.setText("Ghi chú:");

        javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
        jPanel42.setLayout(jPanel42Layout);
        jPanel42Layout.setHorizontalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel83, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel42Layout.setVerticalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel83, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txt_MaNhanVien_NhanVien.setEditable(false);
        txt_MaNhanVien_NhanVien.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_MaNhanVien_NhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_MaNhanVien_NhanVienMouseClicked(evt);
            }
        });
        txt_MaNhanVien_NhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_MaNhanVien_NhanVienActionPerformed(evt);
            }
        });

        txt_TenNhanVien_NhanVien.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_TenNhanVien_NhanVien.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_TenNhanVien_NhanVienFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_TenNhanVien_NhanVienFocusLost(evt);
            }
        });
        txt_TenNhanVien_NhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_TenNhanVien_NhanVienMouseClicked(evt);
            }
        });
        txt_TenNhanVien_NhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_TenNhanVien_NhanVienActionPerformed(evt);
            }
        });

        txt_SoDienThoai_NhanVien.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_SoDienThoai_NhanVien.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_SoDienThoai_NhanVienFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_SoDienThoai_NhanVienFocusLost(evt);
            }
        });
        txt_SoDienThoai_NhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_SoDienThoai_NhanVienMouseClicked(evt);
            }
        });
        txt_SoDienThoai_NhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_SoDienThoai_NhanVienActionPerformed(evt);
            }
        });

        txt_Email_NhanVien.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_Email_NhanVien.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_Email_NhanVienFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_Email_NhanVienFocusLost(evt);
            }
        });
        txt_Email_NhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_Email_NhanVienMouseClicked(evt);
            }
        });
        txt_Email_NhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_Email_NhanVienActionPerformed(evt);
            }
        });

        dc_NgaySinh_NhanVien.setDateFormatString("dd/MM/yyyy");
        dc_NgaySinh_NhanVien.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        dc_NgaySinh_NhanVien.setMinSelectableDate(new java.util.Date(-62135791099000L));
        dc_NgaySinh_NhanVien.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dc_NgaySinh_NhanVienPropertyChange(evt);
            }
        });

        btn_group_GioiTinh_NhanVien.add(rdo_Nam_NhanVien);
        rdo_Nam_NhanVien.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        rdo_Nam_NhanVien.setSelected(true);
        rdo_Nam_NhanVien.setText("Nam");

        btn_group_GioiTinh_NhanVien.add(rdo_Nu_NhanVien);
        rdo_Nu_NhanVien.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        rdo_Nu_NhanVien.setText("Nữ");

        javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
        jPanel43.setLayout(jPanel43Layout);
        jPanel43Layout.setHorizontalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dc_NgaySinh_NhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel43Layout.createSequentialGroup()
                .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_MaNhanVien_NhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_TenNhanVien_NhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_SoDienThoai_NhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Email_NhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel43Layout.createSequentialGroup()
                .addComponent(rdo_Nam_NhanVien)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(rdo_Nu_NhanVien)
                .addGap(49, 49, 49))
        );
        jPanel43Layout.setVerticalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel43Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(txt_MaNhanVien_NhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_TenNhanVien_NhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dc_NgaySinh_NhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdo_Nam_NhanVien)
                    .addComponent(rdo_Nu_NhanVien))
                .addGap(18, 18, 18)
                .addComponent(txt_SoDienThoai_NhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_Email_NhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel44.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel44.setLayout(new java.awt.GridLayout(0, 1, 0, 40));

        btn_Them_NhanVien.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btn_Them_NhanVien.setText("Thêm");
        btn_Them_NhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Them_NhanVienActionPerformed(evt);
            }
        });
        jPanel44.add(btn_Them_NhanVien);

        btn_CapNhat_NhanVien.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btn_CapNhat_NhanVien.setText("Cập nhật");
        btn_CapNhat_NhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CapNhat_NhanVienActionPerformed(evt);
            }
        });
        jPanel44.add(btn_CapNhat_NhanVien);

        btn_Moi_NhanVien.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btn_Moi_NhanVien.setText("Mới");
        btn_Moi_NhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Moi_NhanVienActionPerformed(evt);
            }
        });
        jPanel44.add(btn_Moi_NhanVien);

        jLabel121.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel121.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel121.setText("Mã nhân viên: ");

        jLabel122.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel122.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel122.setText("Tên nhân viên:");

        jLabel123.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel123.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel123.setText("Ngày sinh: ");

        jLabel124.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel124.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel124.setText("Giới tính: ");

        jLabel125.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel125.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel125.setText("Số điện thoại:");

        jLabel126.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel126.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel126.setText("Email:");

        javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
        jPanel46.setLayout(jPanel46Layout);
        jPanel46Layout.setHorizontalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel121, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel122)
            .addComponent(jLabel123, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel124, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel125, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel126, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel46Layout.setVerticalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel46Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jLabel121, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel122, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel123, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel124, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel125, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel126, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txt_CCCD_NhanVien.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_CCCD_NhanVien.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_CCCD_NhanVienFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_CCCD_NhanVienFocusLost(evt);
            }
        });
        txt_CCCD_NhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_CCCD_NhanVienMouseClicked(evt);
            }
        });
        txt_CCCD_NhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_CCCD_NhanVienActionPerformed(evt);
            }
        });
        txt_CCCD_NhanVien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_CCCD_NhanVienKeyReleased(evt);
            }
        });

        btn_group_TrangThai_NhanVien.add(rdo_DangLam_NhanVien);
        rdo_DangLam_NhanVien.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        rdo_DangLam_NhanVien.setSelected(true);
        rdo_DangLam_NhanVien.setText("Đang làm");

        btn_group_TrangThai_NhanVien.add(rdo_NghiViec_NhanVien);
        rdo_NghiViec_NhanVien.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        rdo_NghiViec_NhanVien.setText("Nghỉ việc");
        rdo_NghiViec_NhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdo_NghiViec_NhanVienActionPerformed(evt);
            }
        });

        txt_GhiChu_NhanVien.setColumns(20);
        txt_GhiChu_NhanVien.setRows(5);
        jScrollPane14.setViewportView(txt_GhiChu_NhanVien);

        lbl_Anh_NhanVien.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbl_Anh_NhanVien.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Anh_NhanVien.setText("Click vào để chọn ảnh");
        lbl_Anh_NhanVien.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lbl_Anh_NhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_Anh_NhanVienMouseClicked(evt);
            }
        });

        txt_DiaChi_NhanVien.setColumns(20);
        txt_DiaChi_NhanVien.setRows(5);
        txt_DiaChi_NhanVien.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_DiaChi_NhanVienFocusLost(evt);
            }
        });
        jScrollPane24.setViewportView(txt_DiaChi_NhanVien);

        javax.swing.GroupLayout jPanel45Layout = new javax.swing.GroupLayout(jPanel45);
        jPanel45.setLayout(jPanel45Layout);
        jPanel45Layout.setHorizontalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel45Layout.createSequentialGroup()
                .addGroup(jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel45Layout.createSequentialGroup()
                        .addComponent(rdo_DangLam_NhanVien)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rdo_NghiViec_NhanVien))
                    .addComponent(jScrollPane14)
                    .addComponent(txt_CCCD_NhanVien)
                    .addComponent(jScrollPane24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_Anh_NhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 18, Short.MAX_VALUE))
        );
        jPanel45Layout.setVerticalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel45Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lbl_Anh_NhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel45Layout.createSequentialGroup()
                        .addComponent(jScrollPane24, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txt_CCCD_NhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdo_DangLam_NhanVien)
                            .addComponent(rdo_NghiViec_NhanVien))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(31, 31, 31))
        );

        javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addContainerGap(58, Short.MAX_VALUE)
                .addComponent(jPanel46, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel44, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel41Layout.setVerticalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel41Layout.createSequentialGroup()
                        .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel46, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel41Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(jPanel41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        card_NhanVien.add(jPanel19);

        pnl_MainDisplayCard.add(card_NhanVien, "card_NhanVien");

        card_SanPham.setForeground(new java.awt.Color(97, 88, 152));

        tab_SanPham.setForeground(new java.awt.Color(97, 88, 152));
        tab_SanPham.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        tab_SanPham.setPreferredSize(new java.awt.Dimension(1062, 620));

        pnlThongTinSanPham.setPreferredSize(new java.awt.Dimension(1069, 593));

        jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Quản lý Sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 16), new java.awt.Color(34, 51, 102))); // NOI18N
        jPanel24.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel7.setText("Mã sản phẩm");
        jPanel24.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 43, -1, 26));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel8.setText("Tên sản phẩm");
        jPanel24.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 75, -1, 25));

        jLabel10.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel10.setText("Số lượng");
        jPanel24.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, 107, -1, 23));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel11.setText("Đơn giá");
        jPanel24.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(53, 136, -1, 25));

        jLabel12.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel12.setText("Hãng sản xuất");
        jPanel24.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 43, -1, 27));

        jLabel52.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel52.setText("Màu sắc");
        jPanel24.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 76, -1, 27));

        jLabel53.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel53.setText("Xuất sứ");
        jPanel24.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(395, 109, -1, 22));

        lblHinhAnh_SanPham.setText("Click vào để chọn ảnh");
        lblHinhAnh_SanPham.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblHinhAnh_SanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHinhAnh_SanPhamMouseClicked(evt);
            }
        });
        jPanel24.add(lblHinhAnh_SanPham, new org.netbeans.lib.awtextra.AbsoluteConstraints(695, 43, 137, 131));

        jPanel25.setBorder(new javax.swing.border.MatteBorder(null));

        btn_Them_SanPham.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btn_Them_SanPham.setForeground(new java.awt.Color(97, 88, 152));
        btn_Them_SanPham.setText("Thêm");
        btn_Them_SanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Them_SanPhamActionPerformed(evt);
            }
        });

        btn_CapNhat_SanPham.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btn_CapNhat_SanPham.setForeground(new java.awt.Color(97, 88, 152));
        btn_CapNhat_SanPham.setText("Cập nhật");
        btn_CapNhat_SanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CapNhat_SanPhamActionPerformed(evt);
            }
        });

        btn_Xoa_SanPham.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btn_Xoa_SanPham.setForeground(new java.awt.Color(97, 88, 152));
        btn_Xoa_SanPham.setText("Xóa");
        btn_Xoa_SanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Xoa_SanPhamActionPerformed(evt);
            }
        });

        btn_Moi_SanPham.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btn_Moi_SanPham.setForeground(new java.awt.Color(97, 88, 152));
        btn_Moi_SanPham.setText("Mới");
        btn_Moi_SanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Moi_SanPhamActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btn_Xoa_SanPham, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_CapNhat_SanPham, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Them_SanPham, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Moi_SanPham, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(26, 26, 26))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_Them_SanPham)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_CapNhat_SanPham)
                .addGap(11, 11, 11)
                .addComponent(btn_Xoa_SanPham)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_Moi_SanPham)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel24.add(jPanel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(872, 19, -1, -1));

        txt_MaSanPham_SanPham.setEditable(false);
        jPanel24.add(txt_MaSanPham_SanPham, new org.netbeans.lib.awtextra.AbsoluteConstraints(139, 47, 183, -1));

        txt_TenSanPham_SanPham.setToolTipText("Vui lòng nhập Tên sản phẩm !!!");
        jPanel24.add(txt_TenSanPham_SanPham, new org.netbeans.lib.awtextra.AbsoluteConstraints(139, 75, 183, -1));

        txt_SoLuong_SanPham.setToolTipText("Vui lòng nhập Số lượng !!!");
        jPanel24.add(txt_SoLuong_SanPham, new org.netbeans.lib.awtextra.AbsoluteConstraints(139, 109, 183, -1));

        txt_DonGia_SanPham.setToolTipText("Vui lòng nhập Đơn giá !!!");
        jPanel24.add(txt_DonGia_SanPham, new org.netbeans.lib.awtextra.AbsoluteConstraints(139, 139, 183, -1));

        cbo_HangSanXuat_SanPham.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        cbo_HangSanXuat_SanPham.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SamSung", "Apple", "Iphone", "Khác" }));
        cbo_HangSanXuat_SanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_HangSanXuat_SanPhamActionPerformed(evt);
            }
        });
        jPanel24.add(cbo_HangSanXuat_SanPham, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 45, 211, -1));

        cbo_MauSac_SanPham.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        cbo_MauSac_SanPham.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Vàng", "Tím", "Xanh", "Khác" }));
        cbo_MauSac_SanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_MauSac_SanPhamActionPerformed(evt);
            }
        });
        jPanel24.add(cbo_MauSac_SanPham, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 78, 211, -1));

        cbo_XuatXu_SanPham.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        cbo_XuatXu_SanPham.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hàn Quốc", "Trung Quốc", "Khác" }));
        cbo_XuatXu_SanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_XuatXu_SanPhamActionPerformed(evt);
            }
        });
        jPanel24.add(cbo_XuatXu_SanPham, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 109, 211, -1));

        jLabel60.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel60.setText("Ghi chú");
        jPanel24.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(394, 138, -1, 22));
        jPanel24.add(txt_GhiChu_SanPham, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 140, 211, -1));

        jLabel94.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel94.setText("VND");
        jPanel24.add(jLabel94, new org.netbeans.lib.awtextra.AbsoluteConstraints(332, 143, -1, -1));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách Sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 16), new java.awt.Color(34, 51, 102))); // NOI18N
        jPanel2.setForeground(new java.awt.Color(97, 88, 152));

        tbl_DanhSachSanPham_SanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "MÃ SẢN PHẨM", "TÊN SẢN PHẨM", "HÃNG SẢN XUẤT", "SỐ LƯỢNG", "ĐƠN GIÁ", "MÀU SẮC", "XUẤT XỨ", "HÌNH ẢNH", "GHI CHÚ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_DanhSachSanPham_SanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_DanhSachSanPham_SanPhamMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_DanhSachSanPham_SanPham);

        btn_First_SanPham.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_First_SanPham.setForeground(new java.awt.Color(97, 88, 152));
        btn_First_SanPham.setText("|<");
        btn_First_SanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_First_SanPhamActionPerformed(evt);
            }
        });

        btn_Previous_SanPham.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_Previous_SanPham.setForeground(new java.awt.Color(97, 88, 152));
        btn_Previous_SanPham.setText("<<");
        btn_Previous_SanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Previous_SanPhamActionPerformed(evt);
            }
        });

        btn_Next_SanPham.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_Next_SanPham.setForeground(new java.awt.Color(97, 88, 152));
        btn_Next_SanPham.setText(">>");
        btn_Next_SanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Next_SanPhamActionPerformed(evt);
            }
        });

        btn_Last_SanPham.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_Last_SanPham.setForeground(new java.awt.Color(97, 88, 152));
        btn_Last_SanPham.setText(">|");
        btn_Last_SanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Last_SanPhamActionPerformed(evt);
            }
        });

        jLabel133.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel133.setText("Tìm theo:");

        cbo_SanPham_SanPham.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        cbo_SanPham_SanPham.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mã", "Tên", "Hãng Sản Xuất", "Số Lượng", "Đơn Giá", "Màu Sắc", "Xuất Xứ", "Hình Ảnh" }));

        txt_TimSanPham_SanPham.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        txt_TimSanPham_SanPham.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_TimSanPham_SanPhamKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 513, Short.MAX_VALUE)
                        .addComponent(jLabel133)
                        .addGap(18, 18, 18)
                        .addComponent(cbo_SanPham_SanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_TimSanPham_SanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(269, 269, 269)
                .addComponent(btn_First_SanPham)
                .addGap(74, 74, 74)
                .addComponent(btn_Previous_SanPham)
                .addGap(62, 62, 62)
                .addComponent(btn_Next_SanPham)
                .addGap(51, 51, 51)
                .addComponent(btn_Last_SanPham)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(txt_TimSanPham_SanPham, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(jLabel133, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbo_SanPham_SanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_First_SanPham)
                    .addComponent(btn_Last_SanPham)
                    .addComponent(btn_Previous_SanPham)
                    .addComponent(btn_Next_SanPham))
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout pnlThongTinSanPhamLayout = new javax.swing.GroupLayout(pnlThongTinSanPham);
        pnlThongTinSanPham.setLayout(pnlThongTinSanPhamLayout);
        pnlThongTinSanPhamLayout.setHorizontalGroup(
            pnlThongTinSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThongTinSanPhamLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlThongTinSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlThongTinSanPhamLayout.setVerticalGroup(
            pnlThongTinSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThongTinSanPhamLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tab_SanPham.addTab("Thông tin chi tiết", pnlThongTinSanPham);

        tbl_DanhSachSanPham_DaXoa_SanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "MÃ SẢN PHẨM", "TÊN SẢN PHẨM", "HÃNG SẢN XUẤT", "SỐ LƯỢNG", "ĐƠN GIÁ", "MÀU SẮC", "XUẤT XỨ", "HÌNH ẢNH", "GHI CHÚ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_DanhSachSanPham_DaXoa_SanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_DanhSachSanPham_DaXoa_SanPhamMouseClicked(evt);
            }
        });
        jScrollPane16.setViewportView(tbl_DanhSachSanPham_DaXoa_SanPham);

        btn_First_SanPham_DaXoa.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_First_SanPham_DaXoa.setForeground(new java.awt.Color(97, 88, 152));
        btn_First_SanPham_DaXoa.setText("<<");
        btn_First_SanPham_DaXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_First_SanPham_DaXoaActionPerformed(evt);
            }
        });

        btn_Previous_SanPham_DaXoa.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_Previous_SanPham_DaXoa.setForeground(new java.awt.Color(97, 88, 152));
        btn_Previous_SanPham_DaXoa.setText("|<");
        btn_Previous_SanPham_DaXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Previous_SanPham_DaXoaActionPerformed(evt);
            }
        });

        btn_Next_SanPham_DaXoa.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_Next_SanPham_DaXoa.setForeground(new java.awt.Color(97, 88, 152));
        btn_Next_SanPham_DaXoa.setText(">|");
        btn_Next_SanPham_DaXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Next_SanPham_DaXoaActionPerformed(evt);
            }
        });

        btn_Last_SanPham_DaXoa.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_Last_SanPham_DaXoa.setForeground(new java.awt.Color(97, 88, 152));
        btn_Last_SanPham_DaXoa.setText(">>");
        btn_Last_SanPham_DaXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Last_SanPham_DaXoaActionPerformed(evt);
            }
        });

        btn_KhoiPhuc_SanPham.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_KhoiPhuc_SanPham.setText("Khôi phục");
        btn_KhoiPhuc_SanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_KhoiPhuc_SanPhamActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane16, javax.swing.GroupLayout.DEFAULT_SIZE, 1037, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGap(277, 277, 277)
                .addComponent(btn_First_SanPham_DaXoa)
                .addGap(74, 74, 74)
                .addComponent(btn_Previous_SanPham_DaXoa)
                .addGap(62, 62, 62)
                .addComponent(btn_Next_SanPham_DaXoa)
                .addGap(64, 64, 64)
                .addComponent(btn_Last_SanPham_DaXoa)
                .addGap(175, 175, 175)
                .addComponent(btn_KhoiPhuc_SanPham)
                .addContainerGap(82, Short.MAX_VALUE))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_First_SanPham_DaXoa)
                    .addComponent(btn_Last_SanPham_DaXoa)
                    .addComponent(btn_Previous_SanPham_DaXoa)
                    .addComponent(btn_Next_SanPham_DaXoa)
                    .addComponent(btn_KhoiPhuc_SanPham))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 486, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tab_SanPham.addTab("Sản phẩm đã xóa", jPanel30);

        javax.swing.GroupLayout card_SanPhamLayout = new javax.swing.GroupLayout(card_SanPham);
        card_SanPham.setLayout(card_SanPhamLayout);
        card_SanPhamLayout.setHorizontalGroup(
            card_SanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card_SanPhamLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(tab_SanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );
        card_SanPhamLayout.setVerticalGroup(
            card_SanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card_SanPhamLayout.createSequentialGroup()
                .addComponent(tab_SanPham, javax.swing.GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnl_MainDisplayCard.add(card_SanPham, "card_SanPham");

        tabs_HoaDon.setForeground(new java.awt.Color(97, 88, 152));
        tabs_HoaDon.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N

        txt_fillter_by_TenSP_BanHang.setText("Tìm kiếm theo tên");
        txt_fillter_by_TenSP_BanHang.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_fillter_by_TenSP_BanHangFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_fillter_by_TenSP_BanHangFocusLost(evt);
            }
        });
        txt_fillter_by_TenSP_BanHang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_fillter_by_TenSP_BanHangKeyReleased(evt);
            }
        });

        tbl_DS_SanPham_BanHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "TÊN SP", "MÀU SẮC", "SỐ LƯỢNG"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_DS_SanPham_BanHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_DS_SanPham_BanHangMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tbl_DS_SanPham_BanHang);

        jPanel35.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin hóa đơn", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 16), new java.awt.Color(34, 51, 102))); // NOI18N
        jPanel35.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel61.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel61.setText("Mã hóa đơn:");
        jPanel35.add(jLabel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        jLabel93.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel93.setText("Ngày tạo:");
        jPanel35.add(jLabel93, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        jLabel96.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel96.setText("Người tạo:");
        jPanel35.add(jLabel96, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 29, -1, -1));

        jLabel97.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel97.setText("Tên khách hàng:");
        jPanel35.add(jLabel97, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 80, -1, -1));

        lbl_MaHoaDon_BanHang.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lbl_MaHoaDon_BanHang.setText("HD01");
        jPanel35.add(lbl_MaHoaDon_BanHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 32, 59, -1));

        lbl_NgayTao_BanHang.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lbl_NgayTao_BanHang.setText("2022-11-16");
        jPanel35.add(lbl_NgayTao_BanHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 82, -1, -1));

        lbl_NguoiTao_BanHang.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lbl_NguoiTao_BanHang.setText("Nguyễn Hoài Nam");
        jPanel35.add(lbl_NguoiTao_BanHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(364, 32, -1, -1));

        lbl_TenKhachHang_BanHang.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lbl_TenKhachHang_BanHang.setText("Nguyễn Văn An");
        jPanel35.add(lbl_TenKhachHang_BanHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(413, 83, -1, -1));

        tbl_HoaDon_BanHang.setAutoCreateRowSorter(true);
        tbl_HoaDon_BanHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MÃ SP", "TÊN SP", "HÃNG", "MÀU SẮC", "ĐƠN GIÁ", "SỐ LƯỢNG", "THÀNH TIỀN", "TĂNG", "GIẢM", "XÓA"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_HoaDon_BanHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_HoaDon_BanHangMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(tbl_HoaDon_BanHang);

        jPanel35.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 117, 750, 221));

        jLabel102.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel102.setText("Tiền thanh toán:");
        jPanel35.add(jLabel102, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, -1, -1));

        jLabel103.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel103.setText("Khách đưa:");
        jPanel35.add(jLabel103, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 390, -1, -1));

        jLabel104.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel104.setText("Giá giảm:");
        jPanel35.add(jLabel104, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 348, -1, -1));

        jLabel105.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel105.setText("Trả lại:");
        jPanel35.add(jLabel105, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 390, -1, -1));

        lbl_TongTienThanhToan_BanHang.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lbl_TongTienThanhToan_BanHang.setText("0");
        jPanel35.add(lbl_TongTienThanhToan_BanHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 352, 70, -1));

        txt_TienKhachDua_BanHang.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_TienKhachDua_BanHang.setText("0");
        txt_TienKhachDua_BanHang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_TienKhachDua_BanHangKeyReleased(evt);
            }
        });
        jPanel35.add(txt_TienKhachDua_BanHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 385, 120, 30));

        jLabel108.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel108.setText("VND");
        jPanel35.add(jLabel108, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 390, -1, -1));

        txt_giaGiam_BanHang.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_giaGiam_BanHang.setText("0");
        txt_giaGiam_BanHang.setEnabled(false);
        jPanel35.add(txt_giaGiam_BanHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(608, 345, 120, -1));

        txt_TienTraLai_Banhang.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_TienTraLai_Banhang.setText("0");
        txt_TienTraLai_Banhang.setEnabled(false);
        jPanel35.add(txt_TienTraLai_Banhang, new org.netbeans.lib.awtextra.AbsoluteConstraints(608, 388, 120, -1));

        jLabel109.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel109.setText("VND");
        jPanel35.add(jLabel109, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 350, -1, -1));

        jLabel110.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel110.setText("VND");
        jPanel35.add(jLabel110, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 390, -1, -1));

        btn_HuyGioHang_BanHang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_HuyGioHang_BanHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/phonesystem/edu/img/icons8_cancel_30px_1.png"))); // NOI18N
        btn_HuyGioHang_BanHang.setText("Hủy giỏ hàng");
        btn_HuyGioHang_BanHang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_HuyGioHang_BanHang.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_HuyGioHang_BanHang.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_HuyGioHang_BanHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_HuyGioHang_BanHangActionPerformed(evt);
            }
        });
        jPanel35.add(btn_HuyGioHang_BanHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 450, 152, 62));

        btn_LamMoi_BanHang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_LamMoi_BanHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/phonesystem/edu/img/icons8_close_25px_1.png"))); // NOI18N
        btn_LamMoi_BanHang.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_LamMoi_BanHang.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_LamMoi_BanHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LamMoi_BanHangActionPerformed(evt);
            }
        });
        jPanel35.add(btn_LamMoi_BanHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(285, 385, 30, 30));

        btn_ThanhToan_BanHang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_ThanhToan_BanHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/phonesystem/edu/img/icons8_bill_30px.png"))); // NOI18N
        btn_ThanhToan_BanHang.setText("Thanh toán");
        btn_ThanhToan_BanHang.setToolTipText("");
        btn_ThanhToan_BanHang.setEnabled(false);
        btn_ThanhToan_BanHang.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_ThanhToan_BanHang.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_ThanhToan_BanHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ThanhToan_BanHangActionPerformed(evt);
            }
        });
        jPanel35.add(btn_ThanhToan_BanHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 440, 250, 70));

        txt_GhiChu_BanHang.setColumns(20);
        txt_GhiChu_BanHang.setRows(5);
        jScrollPane21.setViewportView(txt_GhiChu_BanHang);

        jPanel35.add(jScrollPane21, new org.netbeans.lib.awtextra.AbsoluteConstraints(562, 57, 210, 54));

        jLabel47.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel47.setText("Ghi chú:");
        jPanel35.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(557, 29, -1, -1));

        txt_GhiChu_BanHang1.setColumns(20);
        txt_GhiChu_BanHang1.setRows(5);
        jScrollPane22.setViewportView(txt_GhiChu_BanHang1);

        jPanel35.add(jScrollPane22, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 0, 0));

        jLabel48.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel48.setText("Ghi chú:");
        jPanel35.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 0, 0));

        jLabel134.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel134.setText("VND");
        jPanel35.add(jLabel134, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 352, -1, -1));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(txt_fillter_by_TenSP_BanHang, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, 789, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txt_fillter_by_TenSP_BanHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7)))
                .addContainerGap())
        );

        tabs_HoaDon.addTab("Bán Hàng", jPanel1);

        tbl_DSHoaDon_BanHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MÃ HÓA ĐƠN", "NGƯỜI TẠO", "TÊN KHÁCH HÀNG", "NGÀY TẠO", "TỔNG TIỀN"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_DSHoaDon_BanHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_DSHoaDon_BanHangMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(tbl_DSHoaDon_BanHang);

        jButton8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/phonesystem/edu/img/icons8_delete_30px.png"))); // NOI18N
        jButton8.setText("Xóa Hóa Đơn");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jPanel36.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông Tin Chi Tiết Hóa Đơn", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 16), new java.awt.Color(34, 51, 102))); // NOI18N

        tbl_DSHoaDonChiTiet_BanHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MÃ HÓA ĐƠN", "TÊN SẢN PHẨM", "ĐƠN GIÁ", "SỐ LƯỢNG", "THÀNH TIỀN"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane11.setViewportView(tbl_DSHoaDonChiTiet_BanHang);

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 1002, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                .addContainerGap())
        );

        btn_First_DSHoaDon_BanHang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_First_DSHoaDon_BanHang.setForeground(new java.awt.Color(97, 88, 152));
        btn_First_DSHoaDon_BanHang.setText("|<");
        btn_First_DSHoaDon_BanHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_First_DSHoaDon_BanHangActionPerformed(evt);
            }
        });

        btn_prev_DSHoaDon_BanHang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_prev_DSHoaDon_BanHang.setForeground(new java.awt.Color(97, 88, 152));
        btn_prev_DSHoaDon_BanHang.setText("<<");
        btn_prev_DSHoaDon_BanHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_prev_DSHoaDon_BanHangActionPerformed(evt);
            }
        });

        btn_next_DSHoaDon_BanHang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_next_DSHoaDon_BanHang.setForeground(new java.awt.Color(97, 88, 152));
        btn_next_DSHoaDon_BanHang.setText(">>");
        btn_next_DSHoaDon_BanHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_next_DSHoaDon_BanHangActionPerformed(evt);
            }
        });

        btn_last_DSHoaDon_BanHang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_last_DSHoaDon_BanHang.setForeground(new java.awt.Color(97, 88, 152));
        btn_last_DSHoaDon_BanHang.setText(">|");
        btn_last_DSHoaDon_BanHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_last_DSHoaDon_BanHangActionPerformed(evt);
            }
        });

        cbo_loc_DSHoaDon_BanHang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mã hóa đơn", "Người tạo", "Tên khách hàng", "Ngày Tạo", "Tổng tiền" }));

        jLabel137.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel137.setForeground(new java.awt.Color(34, 51, 102));
        jLabel137.setText("Lọc hóa đơn theo");

        txt_Loc_DSHoaDon_BanHang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_Loc_DSHoaDon_BanHangKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel34Layout.createSequentialGroup()
                                .addComponent(jLabel137)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbo_loc_DSHoaDon_BanHang, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_Loc_DSHoaDon_BanHang, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton8))
                            .addComponent(jPanel36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 1022, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addGap(350, 350, 350)
                        .addComponent(btn_First_DSHoaDon_BanHang)
                        .addGap(41, 41, 41)
                        .addComponent(btn_prev_DSHoaDon_BanHang)
                        .addGap(38, 38, 38)
                        .addComponent(btn_next_DSHoaDon_BanHang)
                        .addGap(37, 37, 37)
                        .addComponent(btn_last_DSHoaDon_BanHang)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel137)
                        .addComponent(cbo_loc_DSHoaDon_BanHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_Loc_DSHoaDon_BanHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_First_DSHoaDon_BanHang)
                    .addComponent(btn_prev_DSHoaDon_BanHang)
                    .addComponent(btn_next_DSHoaDon_BanHang)
                    .addComponent(btn_last_DSHoaDon_BanHang))
                .addGap(11, 11, 11)
                .addComponent(jPanel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabs_HoaDon.addTab("Danh Sách Hóa Đơn", jPanel34);

        javax.swing.GroupLayout card_BanHangLayout = new javax.swing.GroupLayout(card_BanHang);
        card_BanHang.setLayout(card_BanHangLayout);
        card_BanHangLayout.setHorizontalGroup(
            card_BanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card_BanHangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabs_HoaDon)
                .addContainerGap())
        );
        card_BanHangLayout.setVerticalGroup(
            card_BanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card_BanHangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabs_HoaDon))
        );

        pnl_MainDisplayCard.add(card_BanHang, "card_BanHang");

        tab_KhuyenMai.setForeground(new java.awt.Color(97, 88, 152));
        tab_KhuyenMai.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N

        jPanel31.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 16), new java.awt.Color(34, 51, 102))); // NOI18N

        jLabel9.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(34, 51, 102));
        jLabel9.setText("Lọc Sản phẩm theo:");

        tbl_DanhSachSanPham_KhuyenMai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "MÃ SẢN PHẨM", "TÊN SẢN PHẨM", "ĐƠN GIÁ", "SỐ LƯỢNG", "HÃNG SẢN XUẤT"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_DanhSachSanPham_KhuyenMai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_DanhSachSanPham_KhuyenMaiMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tbl_DanhSachSanPham_KhuyenMai);

        cbo_LocSanPham_KhuyenMai.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbo_LocSanPham_KhuyenMai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mã Sản Phẩm", "Tên Sản Phẩm", "Đơn Giá", "Số Lượng", "Hãng Sản Xuất" }));

        txt_TimSanPham_KhuyenMai.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_TimSanPham_KhuyenMaiKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(cbo_LocSanPham_KhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(txt_TimSanPham_KhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 413, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cbo_LocSanPham_KhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_TimSanPham_KhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel32.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách khuyến mãi", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 16), new java.awt.Color(34, 51, 102))); // NOI18N

        tbl_DanhSachKhuyenMai_KhuyenMai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "TÊN KHUYẾN MÃI", "GIÁ GIẢM", "NGÀY BẮT ĐẦU", "NGÀY KẾT THÚC", "TRẠNG THÁI", "GHI CHÚ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tbl_DanhSachKhuyenMai_KhuyenMai);

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addContainerGap())
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tab_KhuyenMai.addTab("Thông tin Khuyến Mãi", jPanel4);

        jPanel33.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Quản lý khuyến mãi", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 16), new java.awt.Color(34, 51, 102))); // NOI18N
        jPanel33.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_TenKhuyenMai_KhuyenMai.setToolTipText("Vui lòng nhập Tên khuyến mãi !!!");
        txt_TenKhuyenMai_KhuyenMai.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_TenKhuyenMai_KhuyenMaiKeyReleased(evt);
            }
        });
        jPanel33.add(txt_TenKhuyenMai_KhuyenMai, new org.netbeans.lib.awtextra.AbsoluteConstraints(109, 29, 183, -1));

        jLabel65.setText("Tên khuyến mãi");
        jPanel33.add(jLabel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 32, -1, -1));

        txt_GiamGia_KhuyenMai.setToolTipText("Vui lòng nhập Giảm giá !!!");
        jPanel33.add(txt_GiamGia_KhuyenMai, new org.netbeans.lib.awtextra.AbsoluteConstraints(109, 67, 183, -1));

        jLabel68.setText("Giảm giá");
        jPanel33.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 70, -1, -1));

        jLabel69.setText("Ngày bắt đầu");
        jPanel33.add(jLabel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 105, -1, -1));

        dcs_NgayBatDau_KhuyenMai.setDateFormatString("dd/MM/yyyy");
        jPanel33.add(dcs_NgayBatDau_KhuyenMai, new org.netbeans.lib.awtextra.AbsoluteConstraints(109, 105, 187, -1));

        jLabel77.setText("Ngày kết thúc");
        jPanel33.add(jLabel77, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 136, -1, -1));

        dcs_NgayKetThuc_KhuyenMai.setDateFormatString("dd/MM/yyyy");
        jPanel33.add(dcs_NgayKetThuc_KhuyenMai, new org.netbeans.lib.awtextra.AbsoluteConstraints(109, 136, 187, -1));

        jLabel78.setText("Trạng thái");
        jPanel33.add(jLabel78, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 177, -1, -1));

        buttonGroup1.add(rdo_DangHoatDong_KhuyenMai);
        rdo_DangHoatDong_KhuyenMai.setSelected(true);
        rdo_DangHoatDong_KhuyenMai.setText("Đang hoạt động");
        jPanel33.add(rdo_DangHoatDong_KhuyenMai, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 200, -1, -1));

        buttonGroup1.add(rdo_NgungHoatDong);
        rdo_NgungHoatDong.setText("Ngừng hoạt động");
        jPanel33.add(rdo_NgungHoatDong, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 200, 110, -1));

        jLabel79.setText("Ghi chú");
        jPanel33.add(jLabel79, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, -1, -1));

        txa_GhiChu_KhuyenMai.setColumns(20);
        txa_GhiChu_KhuyenMai.setRows(5);
        jScrollPane5.setViewportView(txa_GhiChu_KhuyenMai);

        jPanel33.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 260, 187, 90));

        btn_Them_KhuyenMai.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_Them_KhuyenMai.setForeground(new java.awt.Color(97, 88, 152));
        btn_Them_KhuyenMai.setText("Thêm");
        btn_Them_KhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Them_KhuyenMaiActionPerformed(evt);
            }
        });
        jPanel33.add(btn_Them_KhuyenMai, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 370, 106, 42));

        btn_CapNhat_KhuyenMai.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_CapNhat_KhuyenMai.setForeground(new java.awt.Color(97, 88, 152));
        btn_CapNhat_KhuyenMai.setText("Cập nhật");
        btn_CapNhat_KhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CapNhat_KhuyenMaiActionPerformed(evt);
            }
        });
        jPanel33.add(btn_CapNhat_KhuyenMai, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 370, 106, 42));

        btn_Xoa_KhuyenMai.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_Xoa_KhuyenMai.setForeground(new java.awt.Color(97, 88, 152));
        btn_Xoa_KhuyenMai.setText("Xóa");
        btn_Xoa_KhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Xoa_KhuyenMaiActionPerformed(evt);
            }
        });
        jPanel33.add(btn_Xoa_KhuyenMai, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 430, 106, 37));

        btn_Moi_KhuyenMai.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_Moi_KhuyenMai.setForeground(new java.awt.Color(97, 88, 152));
        btn_Moi_KhuyenMai.setText("Mới");
        btn_Moi_KhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Moi_KhuyenMaiActionPerformed(evt);
            }
        });
        jPanel33.add(btn_Moi_KhuyenMai, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 430, 106, 37));

        jLabel95.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel95.setText("VND");
        jPanel33.add(jLabel95, new org.netbeans.lib.awtextra.AbsoluteConstraints(296, 69, -1, -1));

        tbl_DanhSachKhuyenMaiCoSanPham_KhuyenMai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "TÊN KHUYẾN MÃI", "GIẢM GIÁ", "NGÀY BẮT ĐẦU", "NGÀY KẾT THÚC", "TRẠNG THÁI", "GHI CHÚ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_DanhSachKhuyenMaiCoSanPham_KhuyenMai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_DanhSachKhuyenMaiCoSanPham_KhuyenMaiMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_DanhSachKhuyenMaiCoSanPham_KhuyenMai);

        jLabel135.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel135.setText("Tìm theo:");

        cbo_KhuyenMai_KhuyenMai.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        cbo_KhuyenMai_KhuyenMai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tên Khuyến Mãi", "Giảm Giá", "Ngày Bắt Đầu", "Ngày Kết Thúc", "Trạng Thái", "Ghi Chú" }));

        txt_TimKhuyenMai_KhuyenMai.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        txt_TimKhuyenMai_KhuyenMai.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_TimKhuyenMai_KhuyenMaiKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 654, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel135)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbo_KhuyenMai_KhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_TimKhuyenMai_KhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                            .addComponent(txt_TimKhuyenMai_KhuyenMai, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                            .addComponent(jLabel135, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbo_KhuyenMai_KhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(56, Short.MAX_VALUE))
        );

        tab_KhuyenMai.addTab("Quản lý Khuyến Mãi", jPanel5);

        javax.swing.GroupLayout card_KhuyenMaiLayout = new javax.swing.GroupLayout(card_KhuyenMai);
        card_KhuyenMai.setLayout(card_KhuyenMaiLayout);
        card_KhuyenMaiLayout.setHorizontalGroup(
            card_KhuyenMaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tab_KhuyenMai)
        );
        card_KhuyenMaiLayout.setVerticalGroup(
            card_KhuyenMaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tab_KhuyenMai)
        );

        pnl_MainDisplayCard.add(card_KhuyenMai, "card_KhuyenMai");

        pnl_MainChinh.add(pnl_MainDisplayCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 70, 1100, 590));

        getContentPane().add(pnl_MainChinh, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lbl_Close_WindowsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_Close_WindowsMouseClicked
        System.exit(0);
    }//GEN-LAST:event_lbl_Close_WindowsMouseClicked

    private void pnl_ThongKeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnl_ThongKeMouseClicked
        onClick(pnl_ThongKe);
        onLeaveClick(pnl_KhachHang);
        onLeaveClick(pnl_BanHang);
        onLeaveClick(pnl_SanPham);
        onLeaveClick(pnl_HeThong);
        onLeaveClick(pnl_DiemDanh);
        onLeaveClick(pnl_TaiKhoan);
        onLeaveClick(pnl_NhanVien);
        onLeaveClick(pnl_Luong);
        onLeaveClick(pnl_KhuyenMai);

        onClickLabel(lbl_ThongKe);
        onLeaveClickLabel(lbl_KhachHang);
        onLeaveClickLabel(lbl_BanHang);
        onLeaveClickLabel(lbl_SanPham);
        onLeaveClickLabel(lbl_HeThong);
        onLeaveClickLabel(lbl_DiemDanh);
        onLeaveClickLabel(lbl_TaiKhoan);
        onLeaveClickLabel(lbl_NhanVien);
        onLeaveClickLabel(lbl_Luong);
        onLeaveClickLabel(lbl_KhuyenMai);

//        indicators
        indicator1.setOpaque(true);
        indicator2.setOpaque(false);
        indicator3.setOpaque(false);
        indicator4.setOpaque(false);
        indicator6.setOpaque(false);
        indicator7.setOpaque(false);
        indicator8.setOpaque(false);
        indicator9.setOpaque(false);
        indicator10.setOpaque(false);
        indicator11.setOpaque(false);

//        Card playout
        CardLayout playout = (CardLayout) pnl_MainDisplayCard.getLayout();
        playout.show(pnl_MainDisplayCard, "card_ThongKe");
    }//GEN-LAST:event_pnl_ThongKeMouseClicked

    private void pnl_KhachHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnl_KhachHangMouseClicked
        onClick(pnl_KhachHang);
        onLeaveClick(pnl_ThongKe);
        onLeaveClick(pnl_BanHang);
        onLeaveClick(pnl_SanPham);
        onLeaveClick(pnl_HeThong);
        onLeaveClick(pnl_DiemDanh);
        onLeaveClick(pnl_TaiKhoan);
        onLeaveClick(pnl_NhanVien);
        onLeaveClick(pnl_Luong);
        onLeaveClick(pnl_KhuyenMai);

        onClickLabel(lbl_KhachHang);
        onLeaveClickLabel(lbl_ThongKe);
        onLeaveClickLabel(lbl_BanHang);
        onLeaveClickLabel(lbl_SanPham);
        onLeaveClickLabel(lbl_HeThong);
        onLeaveClickLabel(lbl_DiemDanh);
        onLeaveClickLabel(lbl_TaiKhoan);
        onLeaveClickLabel(lbl_NhanVien);
        onLeaveClickLabel(lbl_Luong);
        onLeaveClickLabel(lbl_KhuyenMai);

        //        indicators
        indicator1.setOpaque(false);
        indicator2.setOpaque(true);
        indicator3.setOpaque(false);
        indicator4.setOpaque(false);
        indicator6.setOpaque(false);
        indicator7.setOpaque(false);
        indicator8.setOpaque(false);
        indicator9.setOpaque(false);
        indicator10.setOpaque(false);
        indicator11.setOpaque(false);

        //        Card playout
        CardLayout playout = (CardLayout) pnl_MainDisplayCard.getLayout();
        playout.show(pnl_MainDisplayCard, "card_KhachHang");
    }//GEN-LAST:event_pnl_KhachHangMouseClicked

    private void pnl_SanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnl_SanPhamMouseClicked
        onClick(pnl_SanPham);
        onLeaveClick(pnl_ThongKe);
        onLeaveClick(pnl_BanHang);
        onLeaveClick(pnl_KhachHang);
        onLeaveClick(pnl_HeThong);
        onLeaveClick(pnl_DiemDanh);
        onLeaveClick(pnl_TaiKhoan);
        onLeaveClick(pnl_NhanVien);
        onLeaveClick(pnl_Luong);
        onLeaveClick(pnl_KhuyenMai);

        onClickLabel(lbl_SanPham);
        onLeaveClickLabel(lbl_ThongKe);
        onLeaveClickLabel(lbl_BanHang);
        onLeaveClickLabel(lbl_KhachHang);
        onLeaveClickLabel(lbl_HeThong);
        onLeaveClickLabel(lbl_DiemDanh);
        onLeaveClickLabel(lbl_TaiKhoan);
        onLeaveClickLabel(lbl_NhanVien);
        onLeaveClickLabel(lbl_Luong);
        onLeaveClickLabel(lbl_KhuyenMai);

        //        indicators
        indicator1.setOpaque(false);
        indicator2.setOpaque(false);
        indicator3.setOpaque(true);
        indicator4.setOpaque(false);
        indicator6.setOpaque(false);
        indicator7.setOpaque(false);
        indicator8.setOpaque(false);
        indicator9.setOpaque(false);
        indicator10.setOpaque(false);
        indicator11.setOpaque(false);

        //        Card playout
        CardLayout playout = (CardLayout) pnl_MainDisplayCard.getLayout();
        playout.show(pnl_MainDisplayCard, "card_SanPham");
    }//GEN-LAST:event_pnl_SanPhamMouseClicked

    private void pnl_BanHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnl_BanHangMouseClicked
        onClick(pnl_BanHang);
        onLeaveClick(pnl_KhachHang);
        onLeaveClick(pnl_ThongKe);
        onLeaveClick(pnl_SanPham);
        onLeaveClick(pnl_HeThong);
        onLeaveClick(pnl_DiemDanh);
        onLeaveClick(pnl_TaiKhoan);
        onLeaveClick(pnl_NhanVien);
        onLeaveClick(pnl_Luong);
        onLeaveClick(pnl_KhuyenMai);

        onClickLabel(lbl_BanHang);
        onLeaveClickLabel(lbl_KhachHang);
        onLeaveClickLabel(lbl_ThongKe);
        onLeaveClickLabel(lbl_SanPham);
        onLeaveClickLabel(lbl_HeThong);
        onLeaveClickLabel(lbl_DiemDanh);
        onLeaveClickLabel(lbl_TaiKhoan);
        onLeaveClickLabel(lbl_NhanVien);
        onLeaveClickLabel(lbl_Luong);
        onLeaveClickLabel(lbl_KhuyenMai);

        //        indicators
        indicator1.setOpaque(false);
        indicator2.setOpaque(false);
        indicator3.setOpaque(false);
        indicator4.setOpaque(true);
        indicator6.setOpaque(false);
        indicator7.setOpaque(false);
        indicator8.setOpaque(false);
        indicator9.setOpaque(false);
        indicator10.setOpaque(false);
        indicator11.setOpaque(false);

        //        Card playout
        CardLayout playout = (CardLayout) pnl_MainDisplayCard.getLayout();
        playout.show(pnl_MainDisplayCard, "card_BanHang");
    }//GEN-LAST:event_pnl_BanHangMouseClicked

    private void pnl_HeThongMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnl_HeThongMouseClicked
        onClick(pnl_HeThong);
        onLeaveClick(pnl_KhachHang);
        onLeaveClick(pnl_ThongKe);
        onLeaveClick(pnl_SanPham);
        onLeaveClick(pnl_BanHang);
        onLeaveClick(pnl_DiemDanh);
        onLeaveClick(pnl_TaiKhoan);
        onLeaveClick(pnl_NhanVien);
        onLeaveClick(pnl_Luong);
        onLeaveClick(pnl_KhuyenMai);

        onClickLabel(lbl_HeThong);
        onLeaveClickLabel(lbl_KhachHang);
        onLeaveClickLabel(lbl_ThongKe);
        onLeaveClickLabel(lbl_SanPham);
        onLeaveClickLabel(lbl_BanHang);
        onLeaveClickLabel(lbl_DiemDanh);
        onLeaveClickLabel(lbl_TaiKhoan);
        onLeaveClickLabel(lbl_NhanVien);
        onLeaveClickLabel(lbl_Luong);
        onLeaveClickLabel(lbl_KhuyenMai);

        //        indicators
        indicator1.setOpaque(false);
        indicator2.setOpaque(false);
        indicator3.setOpaque(false);
        indicator4.setOpaque(false);
        indicator6.setOpaque(true);
        indicator7.setOpaque(false);
        indicator8.setOpaque(false);
        indicator9.setOpaque(false);
        indicator10.setOpaque(false);
        indicator11.setOpaque(false);

        //        Card playout
        CardLayout playout = (CardLayout) pnl_MainDisplayCard.getLayout();
        playout.show(pnl_MainDisplayCard, "card_HeThong");

    }//GEN-LAST:event_pnl_HeThongMouseClicked

    private void pnl_DiemDanhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnl_DiemDanhMouseClicked
        onClick(pnl_DiemDanh);
        onLeaveClick(pnl_KhachHang);
        onLeaveClick(pnl_ThongKe);
        onLeaveClick(pnl_SanPham);
        onLeaveClick(pnl_BanHang);
        onLeaveClick(pnl_HeThong);
        onLeaveClick(pnl_TaiKhoan);
        onLeaveClick(pnl_NhanVien);
        onLeaveClick(pnl_Luong);
        onLeaveClick(pnl_KhuyenMai);

        onClickLabel(lbl_DiemDanh);
        onLeaveClickLabel(lbl_KhachHang);
        onLeaveClickLabel(lbl_ThongKe);
        onLeaveClickLabel(lbl_SanPham);
        onLeaveClickLabel(lbl_BanHang);
        onLeaveClickLabel(lbl_HeThong);
        onLeaveClickLabel(lbl_TaiKhoan);
        onLeaveClickLabel(lbl_NhanVien);
        onLeaveClickLabel(lbl_Luong);
        onLeaveClickLabel(lbl_KhuyenMai);

        //        indicators
        indicator1.setOpaque(false);
        indicator2.setOpaque(false);
        indicator3.setOpaque(false);
        indicator4.setOpaque(false);
        indicator6.setOpaque(false);
        indicator7.setOpaque(true);
        indicator8.setOpaque(false);
        indicator9.setOpaque(false);
        indicator10.setOpaque(false);
        indicator11.setOpaque(false);

        //        Card playout
        CardLayout playout = (CardLayout) pnl_MainDisplayCard.getLayout();
        playout.show(pnl_MainDisplayCard, "card_DiemDanh");

    }//GEN-LAST:event_pnl_DiemDanhMouseClicked

    private void pnl_TaiKhoanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnl_TaiKhoanMouseClicked
        onClick(pnl_TaiKhoan);
        onLeaveClick(pnl_KhachHang);
        onLeaveClick(pnl_ThongKe);
        onLeaveClick(pnl_SanPham);
        onLeaveClick(pnl_BanHang);
        onLeaveClick(pnl_DiemDanh);
        onLeaveClick(pnl_HeThong);
        onLeaveClick(pnl_NhanVien);
        onLeaveClick(pnl_Luong);
        onLeaveClick(pnl_KhuyenMai);

        onClickLabel(lbl_TaiKhoan);
        onLeaveClickLabel(lbl_KhachHang);
        onLeaveClickLabel(lbl_ThongKe);
        onLeaveClickLabel(lbl_SanPham);
        onLeaveClickLabel(lbl_BanHang);
        onLeaveClickLabel(lbl_DiemDanh);
        onLeaveClickLabel(lbl_HeThong);
        onLeaveClickLabel(lbl_NhanVien);
        onLeaveClickLabel(lbl_Luong);
        onLeaveClickLabel(lbl_KhuyenMai);

        //        indicators
        indicator1.setOpaque(false);
        indicator2.setOpaque(false);
        indicator3.setOpaque(false);
        indicator4.setOpaque(false);
        indicator6.setOpaque(false);
        indicator7.setOpaque(false);
        indicator8.setOpaque(true);
        indicator9.setOpaque(false);
        indicator10.setOpaque(false);
        indicator11.setOpaque(false);

        //        Card playout
        CardLayout playout = (CardLayout) pnl_MainDisplayCard.getLayout();
        playout.show(pnl_MainDisplayCard, "card_TaiKhoan");
    }//GEN-LAST:event_pnl_TaiKhoanMouseClicked

    private void pnl_NhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnl_NhanVienMouseClicked
        onClick(pnl_NhanVien);
        onLeaveClick(pnl_KhachHang);
        onLeaveClick(pnl_ThongKe);
        onLeaveClick(pnl_SanPham);
        onLeaveClick(pnl_BanHang);
        onLeaveClick(pnl_DiemDanh);
        onLeaveClick(pnl_HeThong);
        onLeaveClick(pnl_TaiKhoan);
        onLeaveClick(pnl_Luong);
        onLeaveClick(pnl_KhuyenMai);

        onClickLabel(lbl_NhanVien);
        onLeaveClickLabel(lbl_KhachHang);
        onLeaveClickLabel(lbl_ThongKe);
        onLeaveClickLabel(lbl_SanPham);
        onLeaveClickLabel(lbl_BanHang);
        onLeaveClickLabel(lbl_DiemDanh);
        onLeaveClickLabel(lbl_HeThong);
        onLeaveClickLabel(lbl_TaiKhoan);
        onLeaveClickLabel(lbl_Luong);
        onLeaveClickLabel(lbl_KhuyenMai);

        //        indicators
        indicator1.setOpaque(false);
        indicator2.setOpaque(false);
        indicator3.setOpaque(false);
        indicator4.setOpaque(false);
        indicator6.setOpaque(false);
        indicator7.setOpaque(false);
        indicator8.setOpaque(false);
        indicator9.setOpaque(true);
        indicator10.setOpaque(false);
        indicator11.setOpaque(false);

        //        Card playout
        CardLayout playout = (CardLayout) pnl_MainDisplayCard.getLayout();
        playout.show(pnl_MainDisplayCard, "card_NhanVien");
    }//GEN-LAST:event_pnl_NhanVienMouseClicked

    private void pnl_LuongMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnl_LuongMouseClicked
        onClick(pnl_Luong);
        onLeaveClick(pnl_KhachHang);
        onLeaveClick(pnl_ThongKe);
        onLeaveClick(pnl_SanPham);
        onLeaveClick(pnl_BanHang);
        onLeaveClick(pnl_DiemDanh);
        onLeaveClick(pnl_HeThong);
        onLeaveClick(pnl_NhanVien);
        onLeaveClick(pnl_TaiKhoan);
        onLeaveClick(pnl_KhuyenMai);

        onClickLabel(lbl_Luong);
        onLeaveClickLabel(lbl_KhachHang);
        onLeaveClickLabel(lbl_ThongKe);
        onLeaveClickLabel(lbl_SanPham);
        onLeaveClickLabel(lbl_BanHang);
        onLeaveClickLabel(lbl_DiemDanh);
        onLeaveClickLabel(lbl_HeThong);
        onLeaveClickLabel(lbl_NhanVien);
        onLeaveClickLabel(lbl_TaiKhoan);
        onLeaveClickLabel(lbl_KhuyenMai);

        //        indicators
        indicator1.setOpaque(false);
        indicator2.setOpaque(false);
        indicator3.setOpaque(false);
        indicator4.setOpaque(false);
        indicator6.setOpaque(false);
        indicator7.setOpaque(false);
        indicator8.setOpaque(false);
        indicator9.setOpaque(false);
        indicator10.setOpaque(true);
        indicator11.setOpaque(false);

        //        Card playout
        CardLayout playout = (CardLayout) pnl_MainDisplayCard.getLayout();
        playout.show(pnl_MainDisplayCard, "card_Luong");

    }//GEN-LAST:event_pnl_LuongMouseClicked

    private void pnl_KhuyenMaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnl_KhuyenMaiMouseClicked
        onClick(pnl_KhuyenMai);
        onLeaveClick(pnl_KhachHang);
        onLeaveClick(pnl_ThongKe);
        onLeaveClick(pnl_SanPham);
        onLeaveClick(pnl_BanHang);
        onLeaveClick(pnl_DiemDanh);
        onLeaveClick(pnl_HeThong);
        onLeaveClick(pnl_NhanVien);
        onLeaveClick(pnl_Luong);
        onLeaveClick(pnl_TaiKhoan);

        onClickLabel(lbl_KhuyenMai);
        onLeaveClickLabel(lbl_KhachHang);
        onLeaveClickLabel(lbl_ThongKe);
        onLeaveClickLabel(lbl_SanPham);
        onLeaveClickLabel(lbl_BanHang);
        onLeaveClickLabel(lbl_DiemDanh);
        onLeaveClickLabel(lbl_HeThong);
        onLeaveClickLabel(lbl_NhanVien);
        onLeaveClickLabel(lbl_Luong);
        onLeaveClickLabel(lbl_TaiKhoan);

        //        indicators
        indicator1.setOpaque(false);
        indicator2.setOpaque(false);
        indicator3.setOpaque(false);
        indicator4.setOpaque(false);
        indicator6.setOpaque(false);
        indicator7.setOpaque(false);
        indicator8.setOpaque(false);
        indicator9.setOpaque(false);
        indicator10.setOpaque(false);
        indicator11.setOpaque(true);

        //        Card playout
        CardLayout playout = (CardLayout) pnl_MainDisplayCard.getLayout();
        playout.show(pnl_MainDisplayCard, "card_KhuyenMai");
    }//GEN-LAST:event_pnl_KhuyenMaiMouseClicked

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        this.delete_DSHoaDon_BanHang();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jLabel75MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel75MouseClicked
        checkBox1 = true;
        checkBox2 = false;

        new Login_Frame().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jLabel75MouseClicked

    private void jLabel113MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel113MouseClicked
        checkBox2 = true;
        checkBox1 = false;

        new Login_Frame().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jLabel113MouseClicked

    private void txt_MaNhanVien_NhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_MaNhanVien_NhanVienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_MaNhanVien_NhanVienActionPerformed

    private void txt_TenNhanVien_NhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_TenNhanVien_NhanVienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_TenNhanVien_NhanVienActionPerformed

    private void txt_SoDienThoai_NhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_SoDienThoai_NhanVienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_SoDienThoai_NhanVienActionPerformed

    private void txt_Email_NhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_Email_NhanVienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_Email_NhanVienActionPerformed

    private void btn_Them_NhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Them_NhanVienActionPerformed
        addNhanVien();
    }//GEN-LAST:event_btn_Them_NhanVienActionPerformed

    private void btn_CapNhat_NhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CapNhat_NhanVienActionPerformed
        if (this.isTrueNumPhone(txt_SoDienThoai_NhanVien.getText())) {
            this.updateNhanVien();
        }
    }//GEN-LAST:event_btn_CapNhat_NhanVienActionPerformed

    private void txt_CCCD_NhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_CCCD_NhanVienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_CCCD_NhanVienActionPerformed

    private void btn_First_NhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_First_NhanVienActionPerformed
        this.first_NhanVien_NhanVien();
    }//GEN-LAST:event_btn_First_NhanVienActionPerformed

    private void btn_Prev_NhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Prev_NhanVienActionPerformed
        if (tbl_DSNhanVien_NhanVien.getSelectedRow() != -1) {
            row_NhanVien = tbl_DSNhanVien_NhanVien.getSelectedRow();
        }
        this.prev_NhanVien_NhanVien(row_NhanVien);
    }//GEN-LAST:event_btn_Prev_NhanVienActionPerformed

    private void lblHinhAnh_SanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHinhAnh_SanPhamMouseClicked
        this.chonAnh(lblHinhAnh_SanPham);
    }//GEN-LAST:event_lblHinhAnh_SanPhamMouseClicked

    private void btn_Them_SanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Them_SanPhamActionPerformed
        if (isNullOnTextFiled(txt_TenSanPham_SanPham, txt_SoLuong_SanPham, txt_DonGia_SanPham)) {
            if (kiemTraSanPham() == true) {
                this.insert_SanPham();
            }
        }
    }//GEN-LAST:event_btn_Them_SanPhamActionPerformed

    private void btn_CapNhat_SanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CapNhat_SanPhamActionPerformed
        if (row_tbl_SanPham > 0) {
            if (isNullOnTextFiled(txt_TenSanPham_SanPham, txt_SoLuong_SanPham, txt_DonGia_SanPham)) {
                if (kiemTraSanPham() == true) {
                    this.update_SanPham();
                }
            }
        } else {
            MsgBox.alert(this, "Bạn chưa chọn Sản phẩm !!!");
        }
    }//GEN-LAST:event_btn_CapNhat_SanPhamActionPerformed

    private void btn_Xoa_SanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Xoa_SanPhamActionPerformed
        if (row_tbl_SanPham > 0) {
            this.delete_SanPham();
        } else {
            MsgBox.alert(this, "Bạn chưa chọn Sản phẩm !!!");
        }
    }//GEN-LAST:event_btn_Xoa_SanPhamActionPerformed

    private void btn_Moi_SanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Moi_SanPhamActionPerformed
        this.clearForm_SanPham();
    }//GEN-LAST:event_btn_Moi_SanPhamActionPerformed

    private void cbo_HangSanXuat_SanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_HangSanXuat_SanPhamActionPerformed
        try {
            if (cbo_HangSanXuat_SanPham.getItemCount() > 1) {
                if (cbo_HangSanXuat_SanPham.getSelectedItem().toString().equalsIgnoreCase("Khác")) {
                    new Them_Hang(this, true).setVisible(true);
                }
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cbo_HangSanXuat_SanPhamActionPerformed

    private void tbl_DanhSachSanPham_SanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_DanhSachSanPham_SanPhamMouseClicked
        this.row_tbl_SanPham = tbl_DanhSachSanPham_SanPham.getSelectedRow();
        this.edit_SanPham();
    }//GEN-LAST:event_tbl_DanhSachSanPham_SanPhamMouseClicked

    private void btn_First_SanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_First_SanPhamActionPerformed
        if (tbl_DanhSachSanPham_SanPham.getRowCount() != 0) {
            this.row_tbl_SanPham = 0;
            tbl_DanhSachSanPham_SanPham.setRowSelectionInterval(row_tbl_SanPham, row_tbl_SanPham);
            this.edit_SanPham();
        }
    }//GEN-LAST:event_btn_First_SanPhamActionPerformed

    private void btn_Previous_SanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Previous_SanPhamActionPerformed
        if (tbl_DanhSachSanPham_SanPham.getRowCount() != 0) {
            if (row_tbl_SanPham < 0) {
                btn_Last_SanPhamActionPerformed(evt);
            } else if (row_tbl_SanPham == 0) {
                btn_Last_SanPhamActionPerformed(evt);
            } else {
                row_tbl_SanPham--;
            }
            tbl_DanhSachSanPham_SanPham.setRowSelectionInterval(row_tbl_SanPham, row_tbl_SanPham);
            this.edit_SanPham();
        }
    }//GEN-LAST:event_btn_Previous_SanPhamActionPerformed

    private void btn_Next_SanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Next_SanPhamActionPerformed
        if (tbl_DanhSachSanPham_SanPham.getRowCount() != 0) {
            if (row_tbl_SanPham == tbl_DanhSachSanPham_SanPham.getRowCount() - 1) {
                btn_First_SanPhamActionPerformed(evt);
            } else {
                row_tbl_SanPham++;
            }
            tbl_DanhSachSanPham_SanPham.setRowSelectionInterval(row_tbl_SanPham, row_tbl_SanPham);
            this.edit_SanPham();
        }
    }//GEN-LAST:event_btn_Next_SanPhamActionPerformed

    private void btn_Last_SanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Last_SanPhamActionPerformed
        if (tbl_DanhSachSanPham_SanPham.getRowCount() != 0) {
            row_tbl_SanPham = tbl_DanhSachSanPham_SanPham.getRowCount() - 1;
            tbl_DanhSachSanPham_SanPham.setRowSelectionInterval(row_tbl_SanPham, row_tbl_SanPham);
            this.edit_SanPham();
        }
    }//GEN-LAST:event_btn_Last_SanPhamActionPerformed

    private void tbl_DanhSachSanPham_DaXoa_SanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_DanhSachSanPham_DaXoa_SanPhamMouseClicked
        this.row_tbl_SanPham_DaXoa = tbl_DanhSachSanPham_DaXoa_SanPham.getSelectedRow();
    }//GEN-LAST:event_tbl_DanhSachSanPham_DaXoa_SanPhamMouseClicked

    private void btn_First_SanPham_DaXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_First_SanPham_DaXoaActionPerformed
        if (tbl_DanhSachSanPham_DaXoa_SanPham.getRowCount() != 0) {
            this.row_tbl_SanPham = 0;
            tbl_DanhSachSanPham_DaXoa_SanPham.setRowSelectionInterval(row_tbl_SanPham, row_tbl_SanPham);
            this.edit_SanPham_DaXoa();
        }
    }//GEN-LAST:event_btn_First_SanPham_DaXoaActionPerformed

    private void btn_Previous_SanPham_DaXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Previous_SanPham_DaXoaActionPerformed
        if (tbl_DanhSachSanPham_DaXoa_SanPham.getRowCount() != 0) {
            if (row_tbl_SanPham < 0) {
                btn_Last_SanPham_DaXoaActionPerformed(evt);
            } else if (row_tbl_SanPham == 0) {
                btn_Last_SanPham_DaXoaActionPerformed(evt);
            } else {
                row_tbl_SanPham--;
            }
            tbl_DanhSachSanPham_DaXoa_SanPham.setRowSelectionInterval(row_tbl_SanPham, row_tbl_SanPham);
            this.edit_SanPham_DaXoa();
        }
    }//GEN-LAST:event_btn_Previous_SanPham_DaXoaActionPerformed

    private void btn_Next_SanPham_DaXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Next_SanPham_DaXoaActionPerformed
        if (tbl_DanhSachSanPham_DaXoa_SanPham.getRowCount() != 0) {
            if (row_tbl_SanPham == tbl_DanhSachSanPham_DaXoa_SanPham.getRowCount() - 1) {
                btn_First_SanPham_DaXoaActionPerformed(evt);
            } else {
                row_tbl_SanPham++;
            }
            tbl_DanhSachSanPham_DaXoa_SanPham.setRowSelectionInterval(row_tbl_SanPham, row_tbl_SanPham);
            this.edit_SanPham_DaXoa();
        }
    }//GEN-LAST:event_btn_Next_SanPham_DaXoaActionPerformed

    private void btn_Last_SanPham_DaXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Last_SanPham_DaXoaActionPerformed
        if (tbl_DanhSachSanPham_DaXoa_SanPham.getRowCount() != 0) {
            row_tbl_SanPham = tbl_DanhSachSanPham_DaXoa_SanPham.getRowCount() - 1;
            tbl_DanhSachSanPham_DaXoa_SanPham.setRowSelectionInterval(row_tbl_SanPham, row_tbl_SanPham);
            this.edit_SanPham_DaXoa();
        }
    }//GEN-LAST:event_btn_Last_SanPham_DaXoaActionPerformed

    private void btn_KhoiPhuc_SanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_KhoiPhuc_SanPhamActionPerformed
        if (row_tbl_SanPham_DaXoa != -1) {
            this.khoiPhuc_SanPham_DaXoa();
        } else {
            MsgBox.alert(this, "Bạn chưa chọn Sản phẩm !!!");
        }
    }//GEN-LAST:event_btn_KhoiPhuc_SanPhamActionPerformed

    private void rdo_NghiViec_NhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdo_NghiViec_NhanVienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdo_NghiViec_NhanVienActionPerformed

    private void tbl_DSNhanVien_NhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_DSNhanVien_NhanVienMouseClicked
        setFormNhanVien();

    }//GEN-LAST:event_tbl_DSNhanVien_NhanVienMouseClicked

    private void lbl_Anh_NhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_Anh_NhanVienMouseClicked
        chonAnh(lbl_Anh_NhanVien);
    }//GEN-LAST:event_lbl_Anh_NhanVienMouseClicked

    private void btn_Moi_NhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Moi_NhanVienActionPerformed
        clearFormNhanVien();
    }//GEN-LAST:event_btn_Moi_NhanVienActionPerformed

    private void txt_TimDiaChi_NhanVienKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_TimDiaChi_NhanVienKeyReleased
        int selectedIndex = cbo_NhanVien_NhanVien.getSelectedIndex();
        String keyWord = txt_TimDiaChi_NhanVien.getText();
        filterOnTextfield(tableModelNhanVien, tbl_DSNhanVien_NhanVien, keyWord, selectedIndex);
    }//GEN-LAST:event_txt_TimDiaChi_NhanVienKeyReleased

    private void cbo_MauSac_SanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_MauSac_SanPhamActionPerformed
        try {
            if (cbo_MauSac_SanPham.getItemCount() > 1) {
                if (cbo_MauSac_SanPham.getSelectedItem().toString().equalsIgnoreCase("Khác")) {
                    new Them_MauSac(this, true).setVisible(true);
                }
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cbo_MauSac_SanPhamActionPerformed

    private void cbo_XuatXu_SanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_XuatXu_SanPhamActionPerformed
        try {
            if (cbo_XuatXu_SanPham.getItemCount() > 1) {
                if (cbo_XuatXu_SanPham.getSelectedItem().toString().equalsIgnoreCase("Khác")) {
                    new Them_XuatXu(this, true).setVisible(true);
                }
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cbo_XuatXu_SanPhamActionPerformed

    private void txt_MaKhachHang_KhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_MaKhachHang_KhachHangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_MaKhachHang_KhachHangActionPerformed

    private void txt_TenKhachHang_KhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_TenKhachHang_KhachHangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_TenKhachHang_KhachHangActionPerformed

    private void txt_SoDienThoaiKhachHang_KhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_SoDienThoaiKhachHang_KhachHangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_SoDienThoaiKhachHang_KhachHangActionPerformed

    private void btn_CapNhatKhachHang_KhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CapNhatKhachHang_KhachHangActionPerformed
        capNhatKhachHang();
    }//GEN-LAST:event_btn_CapNhatKhachHang_KhachHangActionPerformed

    private void btn_First_KhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_First_KhachHangActionPerformed
        this.first_KhachHang_KhachHang();
    }//GEN-LAST:event_btn_First_KhachHangActionPerformed

    private void btn_Prev_KhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Prev_KhachHangActionPerformed

        if (tbl_DSKhachHang_KhachHang.getSelectedRow() != -1) {
            row_KhachHang = tbl_DSKhachHang_KhachHang.getSelectedRow();
        }
        this.prev_KhachHang_KhachHang(row_KhachHang);
    }//GEN-LAST:event_btn_Prev_KhachHangActionPerformed

    private void txt_TimSoDienThoaiKhachHang_KhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_TimSoDienThoaiKhachHang_KhachHangActionPerformed

    }//GEN-LAST:event_txt_TimSoDienThoaiKhachHang_KhachHangActionPerformed

    private void btn_ThemKhachHangVaoHoaDon_KhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ThemKhachHangVaoHoaDon_KhachHangActionPerformed
        fillFormHoaDon();
    }//GEN-LAST:event_btn_ThemKhachHangVaoHoaDon_KhachHangActionPerformed

    private void txt_TimSoDienThoaiKhachHangDaXoa_KhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_TimSoDienThoaiKhachHangDaXoa_KhachHangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_TimSoDienThoaiKhachHangDaXoa_KhachHangActionPerformed

    private void btn_KhoiPhucKhachHang_KhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_KhoiPhucKhachHang_KhachHangActionPerformed
        khoiPhucKhachHang_KhachHang();
    }//GEN-LAST:event_btn_KhoiPhucKhachHang_KhachHangActionPerformed

    private void btn_Them_TaiKhoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Them_TaiKhoanActionPerformed
        if (isNullOnTextFiled(txt_TenDangNhap_TaiKhoan, txt_MatKhau_TaiKhoan, txt_Confirm_TaiKhoan)) {
            this.insert_tbl_DaCoTaiKhoan_TaiKhoan();
        }
    }//GEN-LAST:event_btn_Them_TaiKhoanActionPerformed

    private void btn_CapNhat_TaiKhoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CapNhat_TaiKhoanActionPerformed
        this.update_tbl_DaCoTaiKhoan_TaiKhoan();
    }//GEN-LAST:event_btn_CapNhat_TaiKhoanActionPerformed

    private void btn_Xoa_TaiKhoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Xoa_TaiKhoanActionPerformed
        this.delete_tbl_DaCoTaiKhoan_TaiKhoan();
    }//GEN-LAST:event_btn_Xoa_TaiKhoanActionPerformed

    private void tbl_DSDaCoTaiKhoan_TaiKhoanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_DSDaCoTaiKhoan_TaiKhoanMouseClicked
        if (evt.getClickCount() == 1) {
            this.row_tbl_TaiKhoan = tbl_DSDaCoTaiKhoan_TaiKhoan.getSelectedRow();
            this.edit_DS_DaCoTaiKhoan_TaiKhoan();
            this.checkManager_action();
        }
    }//GEN-LAST:event_tbl_DSDaCoTaiKhoan_TaiKhoanMouseClicked

    private void tbl_DSChuaTaiKhoan_TaiKhoanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_DSChuaTaiKhoan_TaiKhoanMouseClicked
        btn_Them_TaiKhoan.setEnabled(true);
        btn_CapNhat_TaiKhoan.setEnabled(false);
        btn_Xoa_TaiKhoan.setEnabled(false);

        if (evt.getClickCount() == 1) {
            this.row_tbl_TaiKhoan = tbl_DSChuaTaiKhoan_TaiKhoan.getSelectedRow();
            this.edit_DS_ChuaCoTaiKhoan_TaiKhoan();
            this.defaultData(true);
        }
        txt_TenDangNhap_TaiKhoan.requestFocus();

    }//GEN-LAST:event_tbl_DSChuaTaiKhoan_TaiKhoanMouseClicked

    private void btn_DiemDanh_DiemDanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DiemDanh_DiemDanhActionPerformed
        if (kiemTraDiemDanh() == true) {
            this.insert_DiemDanh();
            btn_DiemDanh_DiemDanh.setEnabled(false);
        } else {
            MsgBox.alert(this, "Bạn đã điểm danh rồi !!!");
            btn_DiemDanh_DiemDanh.setEnabled(false);
        }
    }//GEN-LAST:event_btn_DiemDanh_DiemDanhActionPerformed

    private void btn_First_DiemDanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_First_DiemDanhActionPerformed
        if (tbl_DanhSachDiemDanh_DiemDanh.getRowCount() != 0) {
            this.row_tbl_DiemDanh = 0;
            tbl_DanhSachDiemDanh_DiemDanh.setRowSelectionInterval(row_tbl_DiemDanh, row_tbl_DiemDanh);
        }
    }//GEN-LAST:event_btn_First_DiemDanhActionPerformed

    private void btn_Previous_DiemDanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Previous_DiemDanhActionPerformed
        if (tbl_DanhSachDiemDanh_DiemDanh.getRowCount() != 0) {
            if (row_tbl_DiemDanh < 0) {
                btn_Last_DiemDanhActionPerformed(evt);
            } else if (row_tbl_DiemDanh == 0) {
                btn_Last_DiemDanhActionPerformed(evt);
            } else {
                row_tbl_DiemDanh--;
            }
            tbl_DanhSachDiemDanh_DiemDanh.setRowSelectionInterval(row_tbl_DiemDanh, row_tbl_DiemDanh);
        }
    }//GEN-LAST:event_btn_Previous_DiemDanhActionPerformed

    private void btn_Next_DiemDanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Next_DiemDanhActionPerformed
        if (tbl_DanhSachDiemDanh_DiemDanh.getRowCount() != 0) {
            if (row_tbl_DiemDanh == tbl_DanhSachDiemDanh_DiemDanh.getRowCount() - 1) {
                btn_First_DiemDanhActionPerformed(evt);
            } else {
                row_tbl_DiemDanh++;
            }
            tbl_DanhSachDiemDanh_DiemDanh.setRowSelectionInterval(row_tbl_DiemDanh, row_tbl_DiemDanh);
        }
    }//GEN-LAST:event_btn_Next_DiemDanhActionPerformed

    private void btn_Last_DiemDanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Last_DiemDanhActionPerformed
        if (tbl_DanhSachDiemDanh_DiemDanh.getRowCount() != 0) {
            row_tbl_DiemDanh = tbl_DanhSachDiemDanh_DiemDanh.getRowCount() - 1;
            tbl_DanhSachDiemDanh_DiemDanh.setRowSelectionInterval(row_tbl_DiemDanh, row_tbl_DiemDanh);
        }
    }//GEN-LAST:event_btn_Last_DiemDanhActionPerformed

    private void btn_ThemKhachHang_KhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ThemKhachHang_KhachHangActionPerformed
        if (isNotNullInTextfiled(txt_TenKhachHang_KhachHang)) {
            themKhachHang_KhachHang();
        }
    }//GEN-LAST:event_btn_ThemKhachHang_KhachHangActionPerformed

    private void tbl_DSKhachHang_KhachHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_DSKhachHang_KhachHangMouseClicked
        setFormKhachHang();
    }//GEN-LAST:event_tbl_DSKhachHang_KhachHangMouseClicked

    private void btn_XoaKhachHang_KhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XoaKhachHang_KhachHangActionPerformed
        xoaKhachHang_KhachHang();
    }//GEN-LAST:event_btn_XoaKhachHang_KhachHangActionPerformed

    private void txt_TimSoDienThoaiKhachHang_KhachHangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_TimSoDienThoaiKhachHang_KhachHangKeyReleased
        timSoDienThoaiKhachHang(evt);
    }//GEN-LAST:event_txt_TimSoDienThoaiKhachHang_KhachHangKeyReleased

    private void txt_TimSoDienThoaiKhachHang_KhachHangFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_TimSoDienThoaiKhachHang_KhachHangFocusGained
        txt_SoDienThoaiKhachHang_KhachHang.setText("");
        if (txt_TimSoDienThoaiKhachHang_KhachHang.getText().length() > 10) {
            txt_TimSoDienThoaiKhachHang_KhachHang.setText("");
        }
    }//GEN-LAST:event_txt_TimSoDienThoaiKhachHang_KhachHangFocusGained

    private void tbl_DS_SanPham_BanHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_DS_SanPham_BanHangMouseClicked
        this.FillTable_HoaDon_BanHang();
    }//GEN-LAST:event_tbl_DS_SanPham_BanHangMouseClicked

    private void tbl_HoaDon_BanHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_HoaDon_BanHangMouseClicked

        int col = tbl_HoaDon_BanHang.columnAtPoint(evt.getPoint());
        if (tbl_banHang_mouseClick(col)) {
            return;
        }
    }//GEN-LAST:event_tbl_HoaDon_BanHangMouseClicked

    private boolean tbl_banHang_mouseClick(int col) {
        if (col == 7) {
            this.TangSoLuong_tblHoaDon_BanHang();
        } else if (col == 8) {
            this.GiamSoLuong_tblHoaDon_BanHang();
        } else if (col == 9) {
            this.XoaSP_tblHoaDon_BanHang();
            return true;
        }

        this.ThanhTien_HoaDon_BanHang();
        this.getSumGiaGiam_BanHang();
        this.tinhTienTraLai_HoaDon_BanHang();

        return false;
    }

    private void btn_HuyGioHang_BanHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_HuyGioHang_BanHangActionPerformed
        this.HuyGioHang_BanHang();
    }//GEN-LAST:event_btn_HuyGioHang_BanHangActionPerformed

    private void btn_LamMoi_BanHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LamMoi_BanHangActionPerformed
        this.clearForm_BanHang();

    }//GEN-LAST:event_btn_LamMoi_BanHangActionPerformed

    private void btn_ThanhToan_BanHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ThanhToan_BanHangActionPerformed
        this.ThanhToan_BanHang_button_click();
    }//GEN-LAST:event_btn_ThanhToan_BanHangActionPerformed

    private void tbl_DSLuong_LuongMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_DSLuong_LuongMouseClicked
        setFormLuong();
    }//GEN-LAST:event_tbl_DSLuong_LuongMouseClicked

    private void txt_TimSoDienThoaiKhachHang_KhachHangFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_TimSoDienThoaiKhachHang_KhachHangFocusLost
        if (txt_TimSoDienThoaiKhachHang_KhachHang.getText().equals("")) {
            txt_TimSoDienThoaiKhachHang_KhachHang.setText("");
            filterOnTextfield(tableModelKhachHang_KhachHang, tbl_DSKhachHang_KhachHang, txt_TimSoDienThoaiKhachHang_KhachHang.getText(), 3);
        }

    }//GEN-LAST:event_txt_TimSoDienThoaiKhachHang_KhachHangFocusLost

    private void txt_TenKhachHang_KhachHangFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_TenKhachHang_KhachHangFocusGained
        txt_TenKhachHang_KhachHang.setBorder(new LineBorder(Color.BLACK));
    }//GEN-LAST:event_txt_TenKhachHang_KhachHangFocusGained

    private void txt_TenNhanVien_NhanVienFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_TenNhanVien_NhanVienFocusGained
        isNew_NhanVien = true;
    }//GEN-LAST:event_txt_TenNhanVien_NhanVienFocusGained

    private void txt_SoDienThoai_NhanVienFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_SoDienThoai_NhanVienFocusGained

    }//GEN-LAST:event_txt_SoDienThoai_NhanVienFocusGained

    private void txt_Email_NhanVienFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_Email_NhanVienFocusGained

    }//GEN-LAST:event_txt_Email_NhanVienFocusGained

    private void txt_CCCD_NhanVienFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_CCCD_NhanVienFocusGained

    }//GEN-LAST:event_txt_CCCD_NhanVienFocusGained

    private void txt_MaNhanVien_NhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_MaNhanVien_NhanVienMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_MaNhanVien_NhanVienMouseClicked

    private void txt_TenNhanVien_NhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_TenNhanVien_NhanVienMouseClicked
        txt_TenNhanVien_NhanVien.setBorder(new LineBorder(Color.BLACK));
    }//GEN-LAST:event_txt_TenNhanVien_NhanVienMouseClicked

    private void txt_SoDienThoai_NhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_SoDienThoai_NhanVienMouseClicked
        txt_SoDienThoai_NhanVien.setBorder(new LineBorder(Color.BLACK));
    }//GEN-LAST:event_txt_SoDienThoai_NhanVienMouseClicked

    private void txt_Email_NhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_Email_NhanVienMouseClicked
        txt_Email_NhanVien.setBorder(new LineBorder(Color.BLACK));
    }//GEN-LAST:event_txt_Email_NhanVienMouseClicked

    private void txt_CCCD_NhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_CCCD_NhanVienMouseClicked
        txt_CCCD_NhanVien.setBorder(new LineBorder(Color.BLACK));
    }//GEN-LAST:event_txt_CCCD_NhanVienMouseClicked

    private void cbo_MaNhanVien_LuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_MaNhanVien_LuongActionPerformed

        if (cbo_MaNhanVien_Luong.getSelectedIndex() >= 1) {
            String id = cbo_MaNhanVien_Luong.getSelectedItem().toString();
            System.out.println(id);

            fillToFormLuongByID(id);
        }

    }//GEN-LAST:event_cbo_MaNhanVien_LuongActionPerformed

    private void txt_TienKhachDua_BanHangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_TienKhachDua_BanHangKeyReleased
        tinhTienTraLai_HoaDon_BanHang();
    }//GEN-LAST:event_txt_TienKhachDua_BanHangKeyReleased

    private void txt_TimSanPham_SanPhamKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_TimSanPham_SanPhamKeyReleased
        int selectedIndex = cbo_SanPham_SanPham.getSelectedIndex();
        String keyWord = txt_TimSanPham_SanPham.getText();
        filterOnTextfield(tableModelSanPham, tbl_DanhSachSanPham_SanPham, keyWord, selectedIndex);
    }//GEN-LAST:event_txt_TimSanPham_SanPhamKeyReleased

    private void txt_TimSoDienThoaiKhachHangDaXoa_KhachHangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_TimSoDienThoaiKhachHangDaXoa_KhachHangKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_TimSoDienThoaiKhachHangDaXoa_KhachHangKeyReleased

    private void txt_Luong_LuongKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_Luong_LuongKeyReleased
        tinhLuongOnTextfiled(txt_Luong_Luong, tongCa, txt_TienThuong_Luong);
    }//GEN-LAST:event_txt_Luong_LuongKeyReleased

    private void txt_TienThuong_LuongKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_TienThuong_LuongKeyReleased
        tinhLuongOnTextfiled(txt_Luong_Luong, tongCa, txt_TienThuong_Luong);
    }//GEN-LAST:event_txt_TienThuong_LuongKeyReleased

    private void btn_TinhLuong_LuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_TinhLuong_LuongActionPerformed
        tinhLuong_Luong();
    }//GEN-LAST:event_btn_TinhLuong_LuongActionPerformed

    private void tbl_DSHoaDon_BanHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_DSHoaDon_BanHangMouseClicked
        if (evt.getClickCount() == 1) {
            this.fillToTableDSHoaDonChiTiet_BanHang();
        } else if (evt.getClickCount() == 2) {
            this.generateInvoice();
            this.printInvoice();
        }
    }//GEN-LAST:event_tbl_DSHoaDon_BanHangMouseClicked

    private void btn_First_DSHoaDon_BanHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_First_DSHoaDon_BanHangActionPerformed
        this.first_DSHoaDon_BanHang();
    }//GEN-LAST:event_btn_First_DSHoaDon_BanHangActionPerformed

    private void btn_prev_DSHoaDon_BanHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_prev_DSHoaDon_BanHangActionPerformed
        this.prev_DSHoaDon_BanHang();
    }//GEN-LAST:event_btn_prev_DSHoaDon_BanHangActionPerformed

    private void btn_next_DSHoaDon_BanHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_next_DSHoaDon_BanHangActionPerformed
        this.next_DSHoaDon_BanHang();
    }//GEN-LAST:event_btn_next_DSHoaDon_BanHangActionPerformed

    private void btn_last_DSHoaDon_BanHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_last_DSHoaDon_BanHangActionPerformed
        this.last_DSHoaDon_BanHang();
    }//GEN-LAST:event_btn_last_DSHoaDon_BanHangActionPerformed

    private void btn_Them_KhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Them_KhuyenMaiActionPerformed
        if (isNullOnTextFiled(txt_TenKhuyenMai_KhuyenMai, txt_GiamGia_KhuyenMai)) {
            if (kiemTraKhuyenMai() == true) {
                this.insert_KhuyenMai();
            }
        }
    }//GEN-LAST:event_btn_Them_KhuyenMaiActionPerformed

    private void btn_Moi_KhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Moi_KhuyenMaiActionPerformed
        this.clearForm_KhuyenMai();
    }//GEN-LAST:event_btn_Moi_KhuyenMaiActionPerformed

    private void dcs_LocTheoNgayNhan_LuongPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dcs_LocTheoNgayNhan_LuongPropertyChange
        this.locNgayNhanLuong(evt);
    }//GEN-LAST:event_dcs_LocTheoNgayNhan_LuongPropertyChange

    private void txt_TimSanPham_KhuyenMaiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_TimSanPham_KhuyenMaiKeyReleased
        int selectedIndex = cbo_LocSanPham_KhuyenMai.getSelectedIndex();
        String keyWord = txt_TimSanPham_KhuyenMai.getText();
        filterOnTextfield(tableModelSanPham_KhuyenMai, tbl_DanhSachSanPham_KhuyenMai, keyWord, selectedIndex);
    }//GEN-LAST:event_txt_TimSanPham_KhuyenMaiKeyReleased

    private void btn_submit_HeThongMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_submit_HeThongMouseClicked
        this.CapNhatMatKhau_HeThong();
    }//GEN-LAST:event_btn_submit_HeThongMouseClicked

    private void btn_NhanLuong_LuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_NhanLuong_LuongActionPerformed
        this.isNhanLuong = true;
        this.capNhatLuong_Luong();
    }//GEN-LAST:event_btn_NhanLuong_LuongActionPerformed

    private void btn_CapNhatLuong_LuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CapNhatLuong_LuongActionPerformed
        this.capNhatLuong_Luong();
    }//GEN-LAST:event_btn_CapNhatLuong_LuongActionPerformed

    private void txt_fillter_by_TenSP_BanHangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_fillter_by_TenSP_BanHangKeyReleased
        int selectedIndex = 0;
        String keyWord = txt_fillter_by_TenSP_BanHang.getText();
        filterOnTextfield(model_tbl_DS_SanPham_BanHang, tbl_DS_SanPham_BanHang, keyWord, selectedIndex);
    }//GEN-LAST:event_txt_fillter_by_TenSP_BanHangKeyReleased

    private void txt_fillter_by_TenSP_BanHangFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_fillter_by_TenSP_BanHangFocusGained
        if (txt_fillter_by_TenSP_BanHang.getText().equals("Tìm kiếm theo tên")) {
            txt_fillter_by_TenSP_BanHang.setText("");
        }
    }//GEN-LAST:event_txt_fillter_by_TenSP_BanHangFocusGained

    private void btn_First_DSChuaTaiKhoan_taikhoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_First_DSChuaTaiKhoan_taikhoanActionPerformed
        if (tbl_DSChuaTaiKhoan_TaiKhoan.getRowCount() != 0) {
            this.row_tbl_TaiKhoan = 0;
            tbl_DSChuaTaiKhoan_TaiKhoan.setRowSelectionInterval(row_tbl_TaiKhoan, row_tbl_TaiKhoan);
            this.updateStatus_DS_DaCoTaiKhoan_TaiKhoan();
        }
    }//GEN-LAST:event_btn_First_DSChuaTaiKhoan_taikhoanActionPerformed

    private void btn_prev_DSChuaTaiKhoan_taikhoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_prev_DSChuaTaiKhoan_taikhoanActionPerformed
        if (tbl_DSChuaTaiKhoan_TaiKhoan.getRowCount() != 0) {
            if (row_tbl_TaiKhoan < 0) {
                btn_last_DSChuaTaiKhoan_taikhoanActionPerformed(evt);
            } else if (row_tbl_TaiKhoan == 0) {
                btn_last_DSChuaTaiKhoan_taikhoanActionPerformed(evt);
            } else {
                row_tbl_TaiKhoan--;
            }
            tbl_DSChuaTaiKhoan_TaiKhoan.setRowSelectionInterval(row_tbl_TaiKhoan, row_tbl_TaiKhoan);
            this.updateStatus_DS_DaCoTaiKhoan_TaiKhoan();
        }
    }//GEN-LAST:event_btn_prev_DSChuaTaiKhoan_taikhoanActionPerformed

    private void btn_next_DSChuaTaiKhoan_taikhoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_next_DSChuaTaiKhoan_taikhoanActionPerformed
        if (tbl_DSChuaTaiKhoan_TaiKhoan.getRowCount() != 0) {
            if (row_tbl_TaiKhoan == tbl_DSChuaTaiKhoan_TaiKhoan.getRowCount() - 1) {
                btn_First_DSChuaTaiKhoan_taikhoanActionPerformed(evt);
            } else {
                row_tbl_TaiKhoan++;
            }
            tbl_DSChuaTaiKhoan_TaiKhoan.setRowSelectionInterval(row_tbl_TaiKhoan, row_tbl_TaiKhoan);
            this.updateStatus_DS_DaCoTaiKhoan_TaiKhoan();
        }
    }//GEN-LAST:event_btn_next_DSChuaTaiKhoan_taikhoanActionPerformed

    private void btn_last_DSChuaTaiKhoan_taikhoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_last_DSChuaTaiKhoan_taikhoanActionPerformed
        if (tbl_DSChuaTaiKhoan_TaiKhoan.getRowCount() != 0) {
            row_tbl_TaiKhoan = tbl_DSChuaTaiKhoan_TaiKhoan.getRowCount() - 1;
            tbl_DSChuaTaiKhoan_TaiKhoan.setRowSelectionInterval(row_tbl_TaiKhoan, row_tbl_TaiKhoan);
            this.updateStatus_DS_DaCoTaiKhoan_TaiKhoan();
        }
    }//GEN-LAST:event_btn_last_DSChuaTaiKhoan_taikhoanActionPerformed

    private void jLabel114MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel114MouseClicked
        new Login_Frame().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jLabel114MouseClicked

    private void lbl_DangXuatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_DangXuatMouseClicked
        if (MsgBox.confirm(this, "Bạn có chắc muốn thoát ứng dụng?")) {
            new Login_Frame().setVisible(true);
            this.dispose();
        } else {

        }
    }//GEN-LAST:event_lbl_DangXuatMouseClicked

    private void tbl_DanhSachSanPham_KhuyenMaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_DanhSachSanPham_KhuyenMaiMouseClicked
        int i = tbl_DanhSachSanPham_KhuyenMai.getSelectedRow();
        String maSP = "";
        if (evt.getClickCount() == 2) {
            maSP = tbl_DanhSachSanPham_KhuyenMai.getValueAt(i, 0).toString();
            ChonKhuyenMai.maSanPham = maSP;
            new ChonKhuyenMai(this, true).setVisible(true);
            this.fillTableSanPhamCo_KhuyenMai(i);
        }
        if (evt.getClickCount() == 1) {
            this.fillTableSanPhamCo_KhuyenMai(i);
        }
    }//GEN-LAST:event_tbl_DanhSachSanPham_KhuyenMaiMouseClicked

    private void txt_TenKhuyenMai_KhuyenMaiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_TenKhuyenMai_KhuyenMaiKeyReleased
        List<KhuyenMai> listKM = kmDAO.selectLenTextFielKhuyenMaiByTenKM(txt_TenKhuyenMai_KhuyenMai.getText());
        for (KhuyenMai km : listKM) {
            String ngayBatDau = XDate.toString(km.getNgayBatDau(), "dd/MM/yyyy");
            String ngayKetThuc = XDate.toString(km.getNgayKetThuc(), "dd/MM/yyyy");
            txt_GiamGia_KhuyenMai.setText(km.getGiaGiam() + "");
            dcs_NgayBatDau_KhuyenMai.setDate(XDate.toDate(ngayBatDau, "dd/MM/yyyy"));
            dcs_NgayKetThuc_KhuyenMai.setDate(XDate.toDate(ngayKetThuc, "dd/MM/yyyy"));
            if (km.getTrangThai() == true) {
                rdo_DangHoatDong_KhuyenMai.setSelected(true);
            } else {
                rdo_NgungHoatDong.setSelected(true);
            }
            txa_GhiChu_KhuyenMai.setText(km.getGhiChu());
        }
    }//GEN-LAST:event_txt_TenKhuyenMai_KhuyenMaiKeyReleased

    private void tbl_DanhSachKhuyenMaiCoSanPham_KhuyenMaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_DanhSachKhuyenMaiCoSanPham_KhuyenMaiMouseClicked
        row_tbl_KhuyenMai = tbl_DanhSachKhuyenMaiCoSanPham_KhuyenMai.getSelectedRow();
        this.edit_KhuyenMai();
        txt_TenKhuyenMai_KhuyenMai.setEnabled(false);
    }//GEN-LAST:event_tbl_DanhSachKhuyenMaiCoSanPham_KhuyenMaiMouseClicked

    private void btn_CapNhat_KhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CapNhat_KhuyenMaiActionPerformed
        if (row_tbl_KhuyenMai > 0) {
            if (isNullOnTextFiled(txt_GiamGia_KhuyenMai)) {
                if (kiemTraKhuyenMai() == true) {
                    this.update_KhuyenMai();
                }
            }
        } else {
            MsgBox.alert(this, "Bạn chưa chọn Khuyến mãi !!!");
        }
    }//GEN-LAST:event_btn_CapNhat_KhuyenMaiActionPerformed

    private void btn_Xoa_KhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Xoa_KhuyenMaiActionPerformed
        if (row_tbl_KhuyenMai > 0) {
            this.delete_KhuyenMai();
        } else {
            MsgBox.alert(this, "Bạn chưa chọn Khuyến mãi !!!");
        }
    }//GEN-LAST:event_btn_Xoa_KhuyenMaiActionPerformed

    private void txt_TimKhuyenMai_KhuyenMaiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_TimKhuyenMai_KhuyenMaiKeyReleased
        int selectedIndex = cbo_KhuyenMai_KhuyenMai.getSelectedIndex();
        String keyWord = txt_TimKhuyenMai_KhuyenMai.getText();
        filterOnTextfield(model_KhuyenMai, tbl_DanhSachKhuyenMaiCoSanPham_KhuyenMai, keyWord, selectedIndex);
    }//GEN-LAST:event_txt_TimKhuyenMai_KhuyenMaiKeyReleased

    private void txt_TimDiemDanh_DiemDanhKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_TimDiemDanh_DiemDanhKeyReleased
        int selectedIndex = cbo_DiemDanh_DiemDanh.getSelectedIndex();
        String keyWord = txt_TimDiemDanh_DiemDanh.getText();
        filterOnTextfield(model_DiemDanh, tbl_DanhSachDiemDanh_DiemDanh, keyWord, selectedIndex);
    }//GEN-LAST:event_txt_TimDiemDanh_DiemDanhKeyReleased

    private void txt_GhiChu_LuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_GhiChu_LuongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_GhiChu_LuongActionPerformed

    private void btn_Next_NhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Next_NhanVienActionPerformed
        if (tbl_DSNhanVien_NhanVien.getSelectedRow() != -1) {
            row_NhanVien = tbl_DSNhanVien_NhanVien.getSelectedRow();
        }
        this.next_NhanVien_NhanVien(row_NhanVien);
    }//GEN-LAST:event_btn_Next_NhanVienActionPerformed

    private void cbo_ChonHang_ThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_ChonHang_ThongKeActionPerformed
        this.fillTable_TheoHang_ThongKe();
    }//GEN-LAST:event_cbo_ChonHang_ThongKeActionPerformed

    private void btn_Top5BanChayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Top5BanChayActionPerformed
        if (dcs_NgayBatDau_ThongKe.getDate() != null && dcs_NgayKetThuc_ThongKe.getDate() != null) {
            this.fillTableThongKe_Top5BanChay();
        }
    }//GEN-LAST:event_btn_Top5BanChayActionPerformed

    private void btn_LamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LamMoiActionPerformed
        this.fillTableThongKe();
    }//GEN-LAST:event_btn_LamMoiActionPerformed

    private void btn_First_ThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_First_ThongKeActionPerformed
        this.first_ThongKe();
    }//GEN-LAST:event_btn_First_ThongKeActionPerformed

    private void btn_Prev_ThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Prev_ThongKeActionPerformed
        if (tbl_ThongKe_ThongKe.getSelectedRow() != -1) {
            row_ThongKe = tbl_ThongKe_ThongKe.getSelectedRow();
        }
        this.prev_ThongKe(row_ThongKe);

    }//GEN-LAST:event_btn_Prev_ThongKeActionPerformed

    private void btn_Next_ThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Next_ThongKeActionPerformed
        if (tbl_ThongKe_ThongKe.getSelectedRow() != -1) {
            row_ThongKe = tbl_ThongKe_ThongKe.getSelectedRow();
        }
        this.next_ThongKe(row_ThongKe);
    }//GEN-LAST:event_btn_Next_ThongKeActionPerformed

    private void dcs_NgayBatDau_ThongKe_DoanhThuPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dcs_NgayBatDau_ThongKe_DoanhThuPropertyChange
        this.fillChart_TheoNgay();
    }//GEN-LAST:event_dcs_NgayBatDau_ThongKe_DoanhThuPropertyChange

    private void dcs_NgayKetThuc_ThongKe_DoanhThuPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dcs_NgayKetThuc_ThongKe_DoanhThuPropertyChange
        this.fillChart_TheoNgay();

    }//GEN-LAST:event_dcs_NgayKetThuc_ThongKe_DoanhThuPropertyChange

    private void btn_Next_NhanVien3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Next_NhanVien3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_Next_NhanVien3ActionPerformed

    private void btn_Last_NhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Last_NhanVienActionPerformed
        this.last_NhanVien_NhanVien();
    }//GEN-LAST:event_btn_Last_NhanVienActionPerformed

    private void btn_Next_KhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Next_KhachHangActionPerformed
        if (tbl_DSKhachHang_KhachHang.getSelectedRow() != -1) {
            row_KhachHang = tbl_DSKhachHang_KhachHang.getSelectedRow();
        }
        this.next_KhachHang_KhachHang(row_KhachHang);
    }//GEN-LAST:event_btn_Next_KhachHangActionPerformed

    private void btn_Last_KhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Last_KhachHangActionPerformed
        this.last_KhachHang_KhachHang();
    }//GEN-LAST:event_btn_Last_KhachHangActionPerformed

    private void btn_Last_ThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Last_ThongKeActionPerformed
        this.last_ThongKe();
    }//GEN-LAST:event_btn_Last_ThongKeActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.filterOnTextfield(tableModelNhanVien, tbl_DSNhanVien_NhanVien, "", 0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void dc_NgaySinh_NhanVienPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dc_NgaySinh_NhanVienPropertyChange
        if (!isNew_NhanVien) {
            if (dc_NgaySinh_NhanVien.getDate() != null) {
                System.out.println(dc_NgaySinh_NhanVien.getDate());
                if (!XDate.isAgeEnough(dc_NgaySinh_NhanVien.getDate())) {
                    MsgBox.alert(this, "Tuổi nhân viên không hợp lệ !");
                    return;
                }
                isNew_NhanVien = false;
            }
        }
    }//GEN-LAST:event_dc_NgaySinh_NhanVienPropertyChange

    private void txt_SoDienThoai_NhanVienFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_SoDienThoai_NhanVienFocusLost
        if (!this.isTrueNumPhone(txt_SoDienThoai_NhanVien.getText())) {
            return;
        };

    }//GEN-LAST:event_txt_SoDienThoai_NhanVienFocusLost

    private void txt_DiaChi_NhanVienFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_DiaChi_NhanVienFocusLost
        this.isNullAddress(txt_DiaChi_NhanVien.getText());
    }//GEN-LAST:event_txt_DiaChi_NhanVienFocusLost

    private void txt_Email_NhanVienFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_Email_NhanVienFocusLost
        this.checkEmail(txt_Email_NhanVien.getText());
    }//GEN-LAST:event_txt_Email_NhanVienFocusLost

    private void txt_CCCD_NhanVienKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_CCCD_NhanVienKeyReleased
        if (txt_CCCD_NhanVien.getText().length() > 12) {
            txt_CCCD_NhanVien.setText(txt_CCCD_NhanVien.getText().substring(0, 12));
            return;
        }
    }//GEN-LAST:event_txt_CCCD_NhanVienKeyReleased

    private void txt_CCCD_NhanVienFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_CCCD_NhanVienFocusLost
        this.checkCCCD(txt_CCCD_NhanVien.getText());
    }//GEN-LAST:event_txt_CCCD_NhanVienFocusLost

    private void txt_TenNhanVien_NhanVienFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_TenNhanVien_NhanVienFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_TenNhanVien_NhanVienFocusLost

    private void tbl_DanhSachDiemDanh_DiemDanhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_DanhSachDiemDanh_DiemDanhMouseClicked
        this.row_tbl_DiemDanh = tbl_DanhSachDiemDanh_DiemDanh.getSelectedRow();
    }//GEN-LAST:event_tbl_DanhSachDiemDanh_DiemDanhMouseClicked

    private void txt_Loc_DSHoaDon_BanHangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_Loc_DSHoaDon_BanHangKeyReleased
        int selectedIndex = cbo_loc_DSHoaDon_BanHang.getSelectedIndex();
        String keyWord = txt_Loc_DSHoaDon_BanHang.getText();
        filterOnTextfield(model_tbl_DSHoaDon, tbl_DSHoaDon_BanHang, keyWord, selectedIndex);
    }//GEN-LAST:event_txt_Loc_DSHoaDon_BanHangKeyReleased

    private void txt_fillter_by_TenSP_BanHangFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_fillter_by_TenSP_BanHangFocusLost
        if (txt_fillter_by_TenSP_BanHang.getText().length() == 0) {
            txt_fillter_by_TenSP_BanHang.setText("Tìm kiếm theo tên");
        } else {

        }
    }//GEN-LAST:event_txt_fillter_by_TenSP_BanHangFocusLost

    private void txt_TenDangNhap_TaiKhoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_TenDangNhap_TaiKhoanActionPerformed
        if (isNullOnTextFiled(txt_TenDangNhap_TaiKhoan, txt_MatKhau_TaiKhoan, txt_Confirm_TaiKhoan)) {
            this.insert_tbl_DaCoTaiKhoan_TaiKhoan();
        }
    }//GEN-LAST:event_txt_TenDangNhap_TaiKhoanActionPerformed

    private void txt_MatKhau_TaiKhoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_MatKhau_TaiKhoanActionPerformed
        if (isNullOnTextFiled(txt_TenDangNhap_TaiKhoan, txt_MatKhau_TaiKhoan, txt_Confirm_TaiKhoan)) {
            this.insert_tbl_DaCoTaiKhoan_TaiKhoan();
        }
    }//GEN-LAST:event_txt_MatKhau_TaiKhoanActionPerformed

    private void txt_Confirm_TaiKhoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_Confirm_TaiKhoanActionPerformed
        if (isNullOnTextFiled(txt_TenDangNhap_TaiKhoan, txt_MatKhau_TaiKhoan, txt_Confirm_TaiKhoan)) {
            this.insert_tbl_DaCoTaiKhoan_TaiKhoan();
        }
    }//GEN-LAST:event_txt_Confirm_TaiKhoanActionPerformed

    private void btn_First_NhanVien3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_First_NhanVien3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_First_NhanVien3ActionPerformed

    private void txt_MatKhau_HeThongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_MatKhau_HeThongActionPerformed
        this.CapNhatMatKhau_HeThong();
    }//GEN-LAST:event_txt_MatKhau_HeThongActionPerformed

    private void txt_MatKhauMoi_HeThongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_MatKhauMoi_HeThongActionPerformed
        this.CapNhatMatKhau_HeThong();
    }//GEN-LAST:event_txt_MatKhauMoi_HeThongActionPerformed

    private void txt_Confirm_HeThongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_Confirm_HeThongActionPerformed
        this.CapNhatMatKhau_HeThong();
    }//GEN-LAST:event_txt_Confirm_HeThongActionPerformed

//    Mouse event
    public void onClick(JPanel jpanel) {
        jpanel.setBackground(new Color(34, 51, 102));
    }

    public void onLeaveClick(JPanel jpanel) {
        jpanel.setBackground(new Color(255, 255, 255));
    }

//    Mouose event - label click
    public void onClickLabel(JLabel jlabel) {
//        jlabel.setForeground(new Color(181, 77, 180));
        jlabel.setForeground(new Color(228, 228, 228));

    }

    public void onLeaveClickLabel(JLabel jlabel) {
        jlabel.setForeground(new Color(97, 88, 152));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Windows".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Main_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Main_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Main_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Main_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }

        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException e) {
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Main_Frame().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_CapNhatKhachHang_KhachHang;
    private javax.swing.JButton btn_CapNhatLuong_Luong;
    private javax.swing.JButton btn_CapNhat_KhuyenMai;
    private javax.swing.JButton btn_CapNhat_NhanVien;
    private javax.swing.JButton btn_CapNhat_SanPham;
    private javax.swing.JButton btn_CapNhat_TaiKhoan;
    private javax.swing.JButton btn_DiemDanh_DiemDanh;
    private javax.swing.JButton btn_First_DSChuaTaiKhoan_taikhoan;
    private javax.swing.JButton btn_First_DSHoaDon_BanHang;
    private javax.swing.JButton btn_First_DiemDanh;
    private javax.swing.JButton btn_First_KhachHang;
    private javax.swing.JButton btn_First_NhanVien;
    private javax.swing.JButton btn_First_NhanVien3;
    private javax.swing.JButton btn_First_SanPham;
    private javax.swing.JButton btn_First_SanPham_DaXoa;
    private javax.swing.JButton btn_First_ThongKe;
    private javax.swing.JButton btn_HuyGioHang_BanHang;
    private javax.swing.JButton btn_KhoiPhucKhachHang_KhachHang;
    private javax.swing.JButton btn_KhoiPhuc_SanPham;
    private javax.swing.JButton btn_LamMoi;
    private javax.swing.JButton btn_LamMoi_BanHang;
    private javax.swing.JButton btn_Last_DiemDanh;
    private javax.swing.JButton btn_Last_KhachHang;
    private javax.swing.JButton btn_Last_NhanVien;
    private javax.swing.JButton btn_Last_NhanVien3;
    private javax.swing.JButton btn_Last_SanPham;
    private javax.swing.JButton btn_Last_SanPham_DaXoa;
    private javax.swing.JButton btn_Last_ThongKe;
    private javax.swing.JButton btn_Moi_KhuyenMai;
    private javax.swing.JButton btn_Moi_NhanVien;
    private javax.swing.JButton btn_Moi_SanPham;
    private javax.swing.JButton btn_Next_DiemDanh;
    private javax.swing.JButton btn_Next_KhachHang;
    private javax.swing.JButton btn_Next_NhanVien;
    private javax.swing.JButton btn_Next_NhanVien3;
    private javax.swing.JButton btn_Next_SanPham;
    private javax.swing.JButton btn_Next_SanPham_DaXoa;
    private javax.swing.JButton btn_Next_ThongKe;
    private javax.swing.JButton btn_NhanLuong_Luong;
    private javax.swing.JButton btn_Prev_KhachHang;
    private javax.swing.JButton btn_Prev_NhanVien;
    private javax.swing.JButton btn_Prev_NhanVien3;
    private javax.swing.JButton btn_Prev_ThongKe;
    private javax.swing.JButton btn_Previous_DiemDanh;
    private javax.swing.JButton btn_Previous_SanPham;
    private javax.swing.JButton btn_Previous_SanPham_DaXoa;
    private javax.swing.JButton btn_ThanhToan_BanHang;
    private javax.swing.JButton btn_ThemKhachHangVaoHoaDon_KhachHang;
    private javax.swing.JButton btn_ThemKhachHang_KhachHang;
    private javax.swing.JButton btn_Them_KhuyenMai;
    private javax.swing.JButton btn_Them_NhanVien;
    private javax.swing.JButton btn_Them_SanPham;
    private javax.swing.JButton btn_Them_TaiKhoan;
    private javax.swing.JButton btn_TimKiemLuongTheoNgay_Luong;
    private javax.swing.JButton btn_TinhLuong_Luong;
    private javax.swing.JButton btn_Top5BanChay;
    private javax.swing.JButton btn_XoaKhachHang_KhachHang;
    private javax.swing.JButton btn_Xoa_KhuyenMai;
    private javax.swing.JButton btn_Xoa_SanPham;
    private javax.swing.JButton btn_Xoa_TaiKhoan;
    private javax.swing.ButtonGroup btn_group_GioiTinh_NhanVien;
    private javax.swing.ButtonGroup btn_group_TrangThai_NhanVien;
    private javax.swing.ButtonGroup btn_group_VaiTro;
    private javax.swing.JButton btn_last_DSChuaTaiKhoan_taikhoan;
    private javax.swing.JButton btn_last_DSHoaDon_BanHang;
    private javax.swing.JButton btn_next_DSChuaTaiKhoan_taikhoan;
    private javax.swing.JButton btn_next_DSHoaDon_BanHang;
    private javax.swing.JButton btn_prev_DSChuaTaiKhoan_taikhoan;
    private javax.swing.JButton btn_prev_DSHoaDon_BanHang;
    private javax.swing.JLabel btn_submit_HeThong;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel card_BanHang;
    private javax.swing.JPanel card_DiemDanh;
    private javax.swing.JPanel card_HeThong;
    private javax.swing.JPanel card_KhachHang;
    private javax.swing.JPanel card_KhuyenMai;
    private javax.swing.JPanel card_Luong;
    private javax.swing.JPanel card_NhanVien;
    private javax.swing.JPanel card_SanPham;
    private javax.swing.JPanel card_TaiKhoan;
    private javax.swing.JPanel card_ThongKe;
    private javax.swing.JComboBox<String> cbo_ChonHang_ThongKe;
    private javax.swing.JComboBox<String> cbo_DiemDanh_DiemDanh;
    public static javax.swing.JComboBox<String> cbo_HangSanXuat_SanPham;
    private javax.swing.JComboBox<String> cbo_KhuyenMai_KhuyenMai;
    private javax.swing.JComboBox<String> cbo_LocSanPham_KhuyenMai;
    private javax.swing.JComboBox<String> cbo_MaNhanVien_Luong;
    public static javax.swing.JComboBox<String> cbo_MauSac_SanPham;
    private javax.swing.JComboBox<String> cbo_NhanVien_NhanVien;
    private javax.swing.JComboBox<String> cbo_SanPham_SanPham;
    public static javax.swing.JComboBox<String> cbo_XuatXu_SanPham;
    private javax.swing.JComboBox<String> cbo_loc_DSHoaDon_BanHang;
    private com.toedter.calendar.JDateChooser dc_NgaySinh_NhanVien;
    private com.toedter.calendar.JDateChooser dcs_LocTheoNgayNhan_Luong;
    private com.toedter.calendar.JDateChooser dcs_NgayBatDau_KhuyenMai;
    private com.toedter.calendar.JDateChooser dcs_NgayBatDau_ThongKe;
    private com.toedter.calendar.JDateChooser dcs_NgayBatDau_ThongKe_DoanhThu;
    private com.toedter.calendar.JDateChooser dcs_NgayKetThuc_KhuyenMai;
    private com.toedter.calendar.JDateChooser dcs_NgayKetThuc_ThongKe;
    private com.toedter.calendar.JDateChooser dcs_NgayKetThuc_ThongKe_DoanhThu;
    private javax.swing.JPanel indicator1;
    private javax.swing.JPanel indicator10;
    private javax.swing.JPanel indicator11;
    private javax.swing.JPanel indicator2;
    private javax.swing.JPanel indicator3;
    private javax.swing.JPanel indicator4;
    private javax.swing.JPanel indicator6;
    private javax.swing.JPanel indicator7;
    private javax.swing.JPanel indicator8;
    private javax.swing.JPanel indicator9;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel115;
    private javax.swing.JLabel jLabel116;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel121;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel123;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel125;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel129;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel130;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lblHinhAnh_SanPham;
    private javax.swing.JLabel lblLoginAccount1;
    private javax.swing.JLabel lblLoginAcout2;
    private javax.swing.JLabel lblOpen;
    private javax.swing.JLabel lblTimer;
    private javax.swing.JLabel lbl_Anh_NhanVien;
    private javax.swing.JLabel lbl_BanHang;
    private javax.swing.JLabel lbl_CaLam_DiemDanh;
    private javax.swing.JLabel lbl_ChucVu_Luong;
    private javax.swing.JLabel lbl_Close_Windows;
    private javax.swing.JLabel lbl_DangXuat;
    private javax.swing.JLabel lbl_DiemDanh;
    private javax.swing.JLabel lbl_HeThong;
    private javax.swing.JLabel lbl_KhachHang;
    private javax.swing.JLabel lbl_KhuyenMai;
    private javax.swing.JLabel lbl_Logo;
    private javax.swing.JLabel lbl_Luong;
    private javax.swing.JLabel lbl_MaHoaDon_BanHang;
    private javax.swing.JLabel lbl_MaNhanVien_DiemDanh;
    private javax.swing.JLabel lbl_Nam_DoanhThu;
    private javax.swing.JLabel lbl_NgayLamViec_DiemDanh;
    private javax.swing.JLabel lbl_NgayNhan_Luong;
    private javax.swing.JLabel lbl_NgayTao_BanHang;
    private javax.swing.JLabel lbl_Ngay_DoanhThu;
    private javax.swing.JLabel lbl_NguoiTao_BanHang;
    private javax.swing.JLabel lbl_NhanVien;
    private javax.swing.JLabel lbl_SanPham;
    private javax.swing.JLabel lbl_SoLuong_NamHienTai_DoanhThu;
    private javax.swing.JLabel lbl_SoLuong_NgayHienTai_DoanhThu;
    private javax.swing.JLabel lbl_SoLuong_ThangHienTai_DoanhThu;
    private javax.swing.JLabel lbl_TaiKhoan;
    private javax.swing.JLabel lbl_TenKhachHang_BanHang;
    public static javax.swing.JLabel lbl_TenNhanVien;
    private javax.swing.JLabel lbl_TenNhanVien_DiemDanh;
    private javax.swing.JLabel lbl_TenNhanVien_Luong;
    private javax.swing.JLabel lbl_Thang_DoanhThu;
    private javax.swing.JLabel lbl_ThongKe;
    private javax.swing.JLabel lbl_TongCaLam_Luong;
    private javax.swing.JLabel lbl_TongLuong_Luong;
    private javax.swing.JLabel lbl_TongTienThanhToan_BanHang;
    private javax.swing.JLabel lbl_TongTien_NamHienTai_DoanhThu;
    private javax.swing.JLabel lbl_TongTien_NgayHienTai_DoanhThu;
    private javax.swing.JLabel lbl_TongTien_ThangHienTai_DoanhThu;
    private javax.swing.JLabel lbl_VaiTro;
    private phonesystem.edu.ultil.PieChart pieChart_TheoNgay;
    private phonesystem.edu.ultil.PieChart pieChart_Tong;
    private javax.swing.JPanel pnlThongTinSanPham;
    private javax.swing.JPanel pnl_BanHang;
    private javax.swing.JPanel pnl_DiemDanh;
    private javax.swing.JPanel pnl_HeThong;
    private javax.swing.JPanel pnl_KhachHang;
    private javax.swing.JPanel pnl_KhuyenMai;
    private javax.swing.JPanel pnl_Luong;
    private javax.swing.JPanel pnl_MainChinh;
    private javax.swing.JPanel pnl_MainDisplayCard;
    private javax.swing.JPanel pnl_Menu;
    private javax.swing.JPanel pnl_NhanVien;
    private javax.swing.JPanel pnl_SanPham;
    private javax.swing.JPanel pnl_TaiKhoan;
    private javax.swing.JPanel pnl_ThongKe;
    private javax.swing.JPanel pnl_Top;
    private javax.swing.JRadioButton rdo_DangHoatDong_KhuyenMai;
    private javax.swing.JRadioButton rdo_DangLam_NhanVien;
    private javax.swing.JRadioButton rdo_Nam_KhachHang;
    private javax.swing.JRadioButton rdo_Nam_NhanVien;
    private javax.swing.JRadioButton rdo_NghiViec_NhanVien;
    private javax.swing.JRadioButton rdo_NgungHoatDong;
    private javax.swing.JRadioButton rdo_NhanVien_TaiKhoan;
    private javax.swing.JRadioButton rdo_Nu_KhachHang;
    private javax.swing.JRadioButton rdo_Nu_NhanVien;
    private javax.swing.JRadioButton rdo_QuanLi_TaiKhoan;
    private javax.swing.JTabbedPane tab_KhuyenMai;
    private javax.swing.JTabbedPane tab_SanPham;
    private javax.swing.JTabbedPane tabs_HoaDon;
    private javax.swing.JTabbedPane tabs_thongke;
    private javax.swing.JTable tbl_DSChuaTaiKhoan_TaiKhoan;
    private javax.swing.JTable tbl_DSDaCoTaiKhoan_TaiKhoan;
    private javax.swing.JTable tbl_DSHoaDonChiTiet_BanHang;
    private javax.swing.JTable tbl_DSHoaDon_BanHang;
    private javax.swing.JTable tbl_DSKhachHangDaXoa_KhachHang;
    private javax.swing.JTable tbl_DSKhachHang_KhachHang;
    private javax.swing.JTable tbl_DSLuong_Luong;
    private javax.swing.JTable tbl_DSNhanVien_NhanVien;
    private javax.swing.JTable tbl_DS_SanPham_BanHang;
    private javax.swing.JTable tbl_DanhSachDiemDanh_DiemDanh;
    private javax.swing.JTable tbl_DanhSachKhuyenMaiCoSanPham_KhuyenMai;
    private javax.swing.JTable tbl_DanhSachKhuyenMai_KhuyenMai;
    private javax.swing.JTable tbl_DanhSachSanPham_DaXoa_SanPham;
    private javax.swing.JTable tbl_DanhSachSanPham_KhuyenMai;
    private javax.swing.JTable tbl_DanhSachSanPham_SanPham;
    private javax.swing.JTable tbl_HoaDon_BanHang;
    private javax.swing.JTable tbl_ThongKe_ThongKe;
    private javax.swing.JTabbedPane tbp_KhachHang;
    private javax.swing.JTextArea txa_GhiChu_DiemDanh;
    private javax.swing.JTextArea txa_GhiChu_KhuyenMai;
    private javax.swing.JTextField txt_CCCD_NhanVien;
    private javax.swing.JPasswordField txt_Confirm_HeThong;
    private javax.swing.JPasswordField txt_Confirm_TaiKhoan;
    private javax.swing.JTextArea txt_DiaChi_NhanVien;
    private javax.swing.JTextField txt_DonGia_SanPham;
    private javax.swing.JTextField txt_Email_NhanVien;
    private javax.swing.JTextArea txt_GhiChuKhachHang_KhachHang;
    private javax.swing.JTextArea txt_GhiChu_BanHang;
    private javax.swing.JTextArea txt_GhiChu_BanHang1;
    private javax.swing.JTextField txt_GhiChu_Luong;
    private javax.swing.JTextArea txt_GhiChu_NhanVien;
    private javax.swing.JTextField txt_GhiChu_SanPham;
    private javax.swing.JTextField txt_GiamGia_KhuyenMai;
    private javax.swing.JTextField txt_Loc_DSHoaDon_BanHang;
    private javax.swing.JTextField txt_Luong_Luong;
    private javax.swing.JTextField txt_MaKhachHang_KhachHang;
    private javax.swing.JTextField txt_MaNV_TaiKhoan;
    private javax.swing.JTextField txt_MaNhanVien_NhanVien;
    private javax.swing.JTextField txt_MaSanPham_SanPham;
    private javax.swing.JPasswordField txt_MatKhauMoi_HeThong;
    private javax.swing.JPasswordField txt_MatKhau_HeThong;
    private javax.swing.JPasswordField txt_MatKhau_TaiKhoan;
    private javax.swing.JTextField txt_SoDienThoaiKhachHang_KhachHang;
    private javax.swing.JTextField txt_SoDienThoai_NhanVien;
    private javax.swing.JTextField txt_SoLuong_SanPham;
    private javax.swing.JTextField txt_TenDangNhap_HeThong;
    private javax.swing.JTextField txt_TenDangNhap_TaiKhoan;
    private javax.swing.JTextField txt_TenKhachHang_KhachHang;
    private javax.swing.JTextField txt_TenKhuyenMai_KhuyenMai;
    private javax.swing.JTextField txt_TenNhanVien_NhanVien;
    private javax.swing.JTextField txt_TenSanPham_SanPham;
    private javax.swing.JTextField txt_TienKhachDua_BanHang;
    private javax.swing.JTextField txt_TienThuong_Luong;
    private javax.swing.JTextField txt_TienTraLai_Banhang;
    private javax.swing.JTextField txt_TimDiaChi_NhanVien;
    private javax.swing.JTextField txt_TimDiemDanh_DiemDanh;
    private javax.swing.JTextField txt_TimKhuyenMai_KhuyenMai;
    private javax.swing.JTextField txt_TimSanPham_KhuyenMai;
    private javax.swing.JTextField txt_TimSanPham_SanPham;
    private javax.swing.JTextField txt_TimSoDienThoaiKhachHangDaXoa_KhachHang;
    private javax.swing.JTextField txt_TimSoDienThoaiKhachHang_KhachHang;
    private javax.swing.JTextField txt_fillter_by_TenSP_BanHang;
    private javax.swing.JTextField txt_giaGiam_BanHang;
    // End of variables declaration//GEN-END:variables

// CODE HERE //
    HoaDonChiTietDAO hdctDAO = new HoaDonChiTietDAO();
    HoaDonDAO hdDAO = new HoaDonDAO();
    NhanVienDAO nvDAO = new NhanVienDAO();
    SanPhamDAO spDAO = new SanPhamDAO();
    TaiKhoanDAO tkDAO = new TaiKhoanDAO();

//   ********************** MINH DUONG**********************
    JFileChooser fileChooser = new JFileChooser("D:\\DuAn_1\\code\\Phone_Sys\\Phone System\\src\\phoneSys\\edu\\view\\img");

//   ********************** MINH DUONG**********************
//                         START_CARD_SANPHAM
    int row_tbl_SanPham = -1;
    DefaultTableModel tableModelSanPham;
    int row_tbl_SanPham_DaXoa = -1;

    private void init_SanPham() {
        fillTable_SanPham();
        fillTable_SanPham_DaXoa();
        fillComBoBox_HangSanXuat();

        TableEdit.centerRendererTable(tbl_DanhSachSanPham_SanPham);
        TableEdit.centerRendererTable(tbl_DanhSachSanPham_DaXoa_SanPham);
    }

    private void fillTable_SanPham() {
        tableModelSanPham = (DefaultTableModel) tbl_DanhSachSanPham_SanPham.getModel();
        tableModelSanPham.setRowCount(0);
        try {
            List<SanPham> list = spDAO.selectAll();
            for (SanPham sp : list) {
                Object[] row = {sp.getMaSanPham(), sp.getTenSanPham(), sp.getHangSanXuat(), sp.getSoLuong(), sp.getDonGia(),
                    sp.getMauSac(), sp.getXuatXu(), sp.getHinhAnh(), sp.getGhiChu()};
                if (sp.isTrangThai() == true) {
                    tableModelSanPham.addRow(row);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    private void chonAnh_SanPham() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            XImage.save(file);// lưu hình vào thư mục logos
            ImageIcon icon = XImage.read(file.getName()); // đọc hình từ logos
            lblHinhAnh_SanPham.setIcon(icon);
            lblHinhAnh_SanPham.setToolTipText(file.getName()); // giữ tên hình trong tooltip          
        }
    }

    private void setForm_SanPham(SanPham sp) {
        txt_MaSanPham_SanPham.setText(sp.getMaSanPham());
        txt_TenSanPham_SanPham.setText(sp.getTenSanPham());
        cbo_HangSanXuat_SanPham.setSelectedItem(sp.getHangSanXuat());
        cbo_XuatXu_SanPham.setSelectedItem(sp.getXuatXu());
        cbo_MauSac_SanPham.setSelectedItem(sp.getMauSac());
        txt_SoLuong_SanPham.setText(sp.getSoLuong() + "");
        txt_DonGia_SanPham.setText(sp.getDonGia() + "");
        this.docAnh(sp.getHinhAnh(), lblHinhAnh_SanPham);
        txt_GhiChu_SanPham.setText(sp.getGhiChu());
    }

    private SanPham getForm_SanPham() {
        SanPham sp = new SanPham();
        sp.setMaSanPham(txt_MaSanPham_SanPham.getText());
        sp.setTenSanPham(txt_TenSanPham_SanPham.getText());
        sp.setHangSanXuat(cbo_HangSanXuat_SanPham.getSelectedItem().toString());
        sp.setMauSac(cbo_MauSac_SanPham.getSelectedItem().toString());
        sp.setXuatXu(cbo_XuatXu_SanPham.getSelectedItem().toString());
        sp.setSoLuong(Integer.parseInt(txt_SoLuong_SanPham.getText()));
        sp.setDonGia(Float.parseFloat(txt_DonGia_SanPham.getText()));
        sp.setHinhAnh(lblHinhAnh_SanPham.getToolTipText()); // Lấy tên thị hình từ Tooltip của lable chứa hình
        sp.setTrangThai(true);
        sp.setGhiChu(txt_GhiChu_SanPham.getText());
        return sp;
    }

    private void clearForm_SanPham() {
        txt_MaSanPham_SanPham.setText(GenerateID.genareteID(spDAO.getID_SanPham()));
        txt_TenSanPham_SanPham.setText("");
        cbo_HangSanXuat_SanPham.setSelectedItem("");
        cbo_XuatXu_SanPham.setSelectedItem("");
        cbo_MauSac_SanPham.setSelectedItem("");
        txt_SoLuong_SanPham.setText("");
        txt_DonGia_SanPham.setText("");
        txt_GhiChu_SanPham.setText("");
        lblHinhAnh_SanPham.setText("HinhAnh");
        this.row_tbl_SanPham = -1;
        this.updateStatus_DS_SanPham_SanPham();
    }

    public void updateStatus_DS_SanPham_SanPham() {
        boolean edit = (this.row_tbl_SanPham >= 0);
//        Trạng thái form
        btn_Them_SanPham.setEnabled(!edit);
        btn_CapNhat_SanPham.setEnabled(edit);
        btn_Xoa_SanPham.setEnabled(edit);
    }

    private void edit_SanPham() {
        String masp = (String) tbl_DanhSachSanPham_SanPham.getValueAt(this.row_tbl_SanPham, 0);
        SanPham sp = spDAO.selectByid(masp);
        if (sp != null) {
            this.setForm_SanPham(sp);
            this.updateStatus_DS_SanPham_SanPham();
        }
    }

    private void insert_SanPham() {
        SanPham sp = getForm_SanPham();
        try {
            spDAO.insert(sp);
            this.fillTable_SanPham();
            MsgBox.alert(this, "Thêm mới Sản phẩm thành công!");
            this.FillTable_DS_SanPham_BanHang();
        } catch (Exception e) {
            MsgBox.alert(this, "Thêm mới Sản phẩm thất bại!");
            e.printStackTrace();
        }
    }

    private void update_SanPham() {
        SanPham sp = getForm_SanPham();
        try {
            spDAO.update(sp);
            this.fillTable_SanPham();
            this.FillTable_DS_SanPham_BanHang();
            MsgBox.alert(this, "Cập nhật Sản phẩm thành công!");
        } catch (Exception e) {
            MsgBox.alert(this, "Cập nhật Sản phẩm thất bại!");
            e.printStackTrace();
        }
    }

    private void khoiPhuc_SanPham_DaXoa() {
        int indexSelectedRow = tbl_DanhSachSanPham_DaXoa_SanPham.getSelectedRow();
        String maSanPham = tbl_DanhSachSanPham_DaXoa_SanPham.getValueAt(indexSelectedRow, 0).toString();
        spDAO.restore(maSanPham);
        this.fillTable_SanPham();
        this.fillTable_SanPham_DaXoa();
        this.FillTable_DS_SanPham_BanHang();
        tab_SanPham.setSelectedIndex(0);
        MsgBox.alert(this, "Khôi phục Sản Phẩm thành công !");

    }

    private void delete_SanPham() {
        SanPham sp = getForm_SanPham();
        if (MsgBox.confirm(this, "Bạn thực sự muốn xóa Sản phẩm này?")) {
            try {
                spDAO.delete_SanPham(sp);
                this.fillTable_SanPham();
                this.fillTable_SanPham_DaXoa();
                this.FillTable_DS_SanPham_BanHang();
                tab_SanPham.setSelectedIndex(1);
                MsgBox.alert(this, "Xóa Sản phẩm thành công!");
                this.clearForm_SanPham();
            } catch (Exception e) {

                e.printStackTrace();
            }
        } else {
            MsgBox.alert(this, "Xóa Sản phẩm thất bại!");
        }
    }

    private void fillTable_SanPham_DaXoa() {
        DefaultTableModel model = (DefaultTableModel) tbl_DanhSachSanPham_DaXoa_SanPham.getModel();
        model.setRowCount(0);
        try {
            List<SanPham> list = spDAO.selectAll();
            for (SanPham sp : list) {
                Object[] row = {sp.getMaSanPham(), sp.getTenSanPham(), sp.getHangSanXuat(), sp.getSoLuong(), sp.getDonGia(),
                    sp.getMauSac(), sp.getXuatXu(), sp.getHinhAnh(), sp.getGhiChu()};
                if (sp.isTrangThai() == false) {
                    model.addRow(row);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    private void edit_SanPham_DaXoa() {
        String masp = (String) tbl_DanhSachSanPham_DaXoa_SanPham.getValueAt(this.row_tbl_SanPham, 0);
        SanPham sp = spDAO.selectByid(masp);
        if (sp != null) {
            this.setForm_SanPham(sp);
        }
    }

    private void fillComBoBox_HangSanXuat() {
        SanPhamDAO spDAO = new SanPhamDAO();
        List<Object> listHang = spDAO.selectByHang();
        DefaultComboBoxModel model = (DefaultComboBoxModel) this.cbo_HangSanXuat_SanPham.getModel();
        model.removeAllElements();
        for (Object sp : listHang) {
            model.addElement(sp);
        }
        model.addElement("Khác");
    }

    private void fillComBoBox_MauSac() {
        SanPhamDAO spDAO = new SanPhamDAO();
        List<Object> listHang = spDAO.selectByMauSac();
        DefaultComboBoxModel model = (DefaultComboBoxModel) this.cbo_MauSac_SanPham.getModel();
        model.removeAllElements();
        for (Object mausac : listHang) {
            model.addElement(mausac);
        }
        model.addElement("Khác");
    }

    private void fillComBoBox_XuatXu() {
        SanPhamDAO spDAO = new SanPhamDAO();
        List<Object> listHang = spDAO.selectByXuatXu();
        DefaultComboBoxModel model = (DefaultComboBoxModel) this.cbo_XuatXu_SanPham.getModel();
        model.removeAllElements();
        for (Object xuatxu : listHang) {
            model.addElement(xuatxu);
        }
        model.addElement("Khác");
    }

    public boolean kiemTraSanPham() {
        try {
            if (Integer.parseInt(txt_SoLuong_SanPham.getText()) < 0) {
                MsgBox.alert(this, "Số lượng Sản phẩm không được nhỏ hơn 0 !!!");
                return false;
            }
        } catch (NumberFormatException e) {
            MsgBox.alert(this, "Số lượng Sản phẩm phải là giá trị số nguyên !!!");
            return false;
        }
        try {
            if (Float.parseFloat(txt_DonGia_SanPham.getText()) < 0) {
                MsgBox.alert(this, "Đơn giá Sản phẩm không được nhỏ hơn 0 !!!");
                return false;
            }
        } catch (NumberFormatException e) {
            MsgBox.alert(this, "Đơn giá Sản phẩm phải là giá trị số !!!");
            return false;
        }
        return true;
    }

//                          END_CARD_SANPHAM  
    DiemDanhDAO ddDao = new DiemDanhDAO();
    int row_tbl_DiemDanh = -1;

    private void init_DiemDanh() {
        fillTable_DiemDanh();
        fillLable_DiemDanh();

        TableEdit.centerRendererTable(tbl_DanhSachDiemDanh_DiemDanh);
    }

    DefaultTableModel model_DiemDanh;

    private boolean kiemTraDiemDanh() {
        String dateSt = XDate.toString(XDate.now(), "HH:mm aa");

        String hSt = dateSt.substring(0, 2);
        String mSt = dateSt.substring(3, 5);

        int hInt = Integer.parseInt(hSt);
        int mInt = Integer.parseInt(mSt);
        boolean isAM = dateSt.endsWith("AM");

        String maNV = Auth.user.getMaNhanVien();
        String caLam = "";
        if (isAM == true) {
            caLam = "Ca Sáng";
        }
        if (isAM == false) {
            caLam = "Ca Chiều";
        }
        String ngayLamViec = (XDate.toString(XDate.now(), "dd/MM/yyyy"));

        for (int i = 0; i < tbl_DanhSachDiemDanh_DiemDanh.getRowCount(); i++) {
            if (tbl_DanhSachDiemDanh_DiemDanh.getValueAt(i, 1).toString().equals(maNV) && tbl_DanhSachDiemDanh_DiemDanh.getValueAt(i, 3).toString().equals(caLam) && tbl_DanhSachDiemDanh_DiemDanh.getValueAt(i, 4).toString().equals(ngayLamViec)) {
                return false;
            }
        }
        return true;
    }

    private void fillTable_DiemDanh() {
        model_DiemDanh = (DefaultTableModel) tbl_DanhSachDiemDanh_DiemDanh.getModel();
        model_DiemDanh.setRowCount(0);
        try {
            List<DiemDanh> list = ddDao.selectAll();
            for (DiemDanh dd : list) {
                String ngayLamViec = XDate.toString(dd.getNgayLamViec(), "dd/MM/yyyy");
                Object[] row = {dd.getMaDiemDanh(), dd.getMaNhanVien(), dd.getTenNhanVien(),
                    dd.getCaLamViec(), ngayLamViec, dd.getGhiChu()
                };
                model_DiemDanh.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    private DiemDanh getForm_DiemDanh() {
        DiemDanh dd = new DiemDanh();
        dd.setMaNhanVien(lbl_MaNhanVien_DiemDanh.getText());
        dd.setTenNhanVien(lbl_TenNhanVien_DiemDanh.getText());
        dd.setCaLamViec(lbl_CaLam_DiemDanh.getText());
        dd.setNgayLamViec(XDate.toDate(lbl_NgayLamViec_DiemDanh.getText(), "dd/MM/yyyy"));
        dd.setGhiChu(txa_GhiChu_DiemDanh.getText());
        return dd;
    }

    private void insert_DiemDanh() {
        DiemDanh dd = getForm_DiemDanh();
        try {
            ddDao.insert(dd);
            this.fillTable_DiemDanh();
            MsgBox.alert(this, "Điểm danh thành công!");
        } catch (Exception e) {
            MsgBox.alert(this, "Điểm danh thất bại!");
            e.printStackTrace();
        }
    }

    private void fillLable_DiemDanh() {
        if (Auth.isLogin()) {
            lbl_MaNhanVien_DiemDanh.setText(Auth.user.getMaNhanVien());
            lbl_TenNhanVien_DiemDanh.setText(ddDao.selectByTenNhanVien(Auth.user.getMaNhanVien()));
        }

        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm aa");
        String dateSt = sdf.format(now);

        String hSt = dateSt.substring(0, 2);
        String mSt = dateSt.substring(3, 5);

        int hInt = Integer.parseInt(hSt);
        int mInt = Integer.parseInt(mSt);

        boolean isAM = dateSt.endsWith("AM");

        if (isAM == true) {
            lbl_CaLam_DiemDanh.setText("Ca Sáng");
        }
        if (isAM == false) {
            lbl_CaLam_DiemDanh.setText("Ca Chiều");
        }
        lbl_NgayLamViec_DiemDanh.setText(XDate.toString(new Date(), "dd/MM/yyyy"));

        if ((hInt >= 7 && hInt < 8 && mInt <= 30 && isAM == true) || (hInt >= 13 && hInt < 22 && mInt <= 30 && isAM == false)) {
            btn_DiemDanh_DiemDanh.setEnabled(true);
        } else {
            btn_DiemDanh_DiemDanh.setEnabled(false);
        }
    }

//                         END_CARD_DIEMDANH
//                         START_CARD_KHUYENMAI
    KhuyenMaiDAO kmDAO = new KhuyenMaiDAO();
    int row_tbl_KhuyenMai = -1;
    DefaultTableModel tableModelSanPham_KhuyenMai;

    private void init_KhuyenMai() {
        fillTable_SanPham_KhuyenMai();
        fillTable_KhuyenMai();

        TableEdit.centerRendererTable(tbl_DanhSachKhuyenMaiCoSanPham_KhuyenMai);
        TableEdit.centerRendererTable(tbl_DanhSachSanPham_KhuyenMai);
        TableEdit.centerRendererTable(tbl_DanhSachKhuyenMai_KhuyenMai);
    }

    DefaultTableModel model_KhuyenMai;

    private void fillTable_KhuyenMai() {
        model_KhuyenMai = (DefaultTableModel) tbl_DanhSachKhuyenMaiCoSanPham_KhuyenMai.getModel();
        model_KhuyenMai.setRowCount(0);
        try {
            List<KhuyenMai> list = kmDAO.selectKhuyenMai();
            for (KhuyenMai km : list) {
                String ngayBatDau = XDate.toString(km.getNgayBatDau(), "dd/MM/yyyy");
                String ngayKetThuc = XDate.toString(km.getNgayKetThuc(), "dd/MM/yyyy");
                Object[] row = {km.getTenKhuyenMai(), km.getGiaGiam(), ngayBatDau,
                    ngayKetThuc, km.getTrangThai() ? "Đang hoạt động" : "Ngừng hoạt động", km.getGhiChu()
                };
                model_KhuyenMai.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    private void setForm_KhuyenMai() {
        row_tbl_KhuyenMai = tbl_DanhSachKhuyenMaiCoSanPham_KhuyenMai.getSelectedRow();
        String tenKM = (String) tbl_DanhSachKhuyenMaiCoSanPham_KhuyenMai.getValueAt(row_tbl_KhuyenMai, 0);
        String ngayBatDau = (String) tbl_DanhSachKhuyenMaiCoSanPham_KhuyenMai.getValueAt(row_tbl_KhuyenMai, 2);
        String ngayKetThuc = (String) tbl_DanhSachKhuyenMaiCoSanPham_KhuyenMai.getValueAt(row_tbl_KhuyenMai, 3);
        float giaGiam = (float) tbl_DanhSachKhuyenMaiCoSanPham_KhuyenMai.getValueAt(row_tbl_KhuyenMai, 1);
        String trangThai = (String) tbl_DanhSachKhuyenMaiCoSanPham_KhuyenMai.getValueAt(row_tbl_KhuyenMai, 4);
        String ghiChu = (String) tbl_DanhSachKhuyenMaiCoSanPham_KhuyenMai.getValueAt(row_tbl_KhuyenMai, 5);
        txt_TenKhuyenMai_KhuyenMai.setText(tenKM);
        txt_GiamGia_KhuyenMai.setText(giaGiam + "");
        dcs_NgayBatDau_KhuyenMai.setDate(XDate.toDate(ngayBatDau, "dd/MM/yyyy"));
        dcs_NgayKetThuc_KhuyenMai.setDate(XDate.toDate(ngayKetThuc, "dd/MM/yyyy"));
        if (trangThai.equals("Đang hoạt động")) {
            rdo_DangHoatDong_KhuyenMai.setSelected(true);
        } else {
            rdo_NgungHoatDong.setSelected(true);
        }
        txa_GhiChu_KhuyenMai.setText(ghiChu);
    }

    public static KhuyenMai km = new KhuyenMai();

    private KhuyenMai getForm_KhuyenMai() {
        km.setTenKhuyenMai(txt_TenKhuyenMai_KhuyenMai.getText());
        km.setMaSanPham("");
        km.setNgayBatDau(dcs_NgayBatDau_KhuyenMai.getDate());
        km.setNgayKetThuc(dcs_NgayKetThuc_KhuyenMai.getDate());
        km.setGiaGiam((Float.parseFloat(txt_GiamGia_KhuyenMai.getText())));
        if (rdo_DangHoatDong_KhuyenMai.isSelected() == true) {
            km.setTrangThai(true);
        } else {
            km.setTrangThai(false);
        }
        km.setGhiChu(txa_GhiChu_KhuyenMai.getText());
        return km;
    }

    private void edit_KhuyenMai() {
        String masp = (String) tbl_DanhSachKhuyenMaiCoSanPham_KhuyenMai.getValueAt(this.row_tbl_KhuyenMai, 0);
        KhuyenMai km = kmDAO.selectByid(masp);
        if (km != null) {
            this.setForm_KhuyenMai();
            this.updateStatus_DS_KhuyenMai_KhuyenMai();
        }
    }

    private void clearForm_KhuyenMai() {
        txt_TenKhuyenMai_KhuyenMai.setText("");
        txt_GiamGia_KhuyenMai.setText("");
        dcs_NgayBatDau_KhuyenMai.setCalendar(null);
        dcs_NgayKetThuc_KhuyenMai.setCalendar(null);
        rdo_DangHoatDong_KhuyenMai.setSelected(true);
        txa_GhiChu_KhuyenMai.setText("");
        this.row_tbl_KhuyenMai = -1;
        this.updateStatus_DS_KhuyenMai_KhuyenMai();
        txt_TenKhuyenMai_KhuyenMai.setEnabled(true);
        txt_TenKhuyenMai_KhuyenMai.requestFocus();
    }

    public void updateStatus_DS_KhuyenMai_KhuyenMai() {
        boolean edit = (this.row_tbl_KhuyenMai >= 0);
//        Trạng thái form
        btn_Them_KhuyenMai.setEnabled(!edit);
        btn_CapNhat_KhuyenMai.setEnabled(edit);
        btn_Xoa_KhuyenMai.setEnabled(edit);
    }

    private void insert_KhuyenMai() {
        KhuyenMai km = getForm_KhuyenMai();
        new ChonSanPhamKhuyenMai(this, true).setVisible(true);
        try {
            kmDAO.insert(km);
            this.fillTable_KhuyenMai();
            MsgBox.alert(this, "Thêm mới Khuyến mãi thành công!");
            this.clearForm_KhuyenMai();
        } catch (Exception e) {
            MsgBox.alert(this, "Bạn chưa chọn Sản Phẩm !!!");
            //e.printStackTrace();
        }
    }

    private void update_KhuyenMai() {
        KhuyenMai km = getForm_KhuyenMai();
        try {
            kmDAO.update(km);
            this.fillTable_KhuyenMai();
            this.clearForm_KhuyenMai();
            MsgBox.alert(this, "Cập nhật Sản phẩm thành công!");
        } catch (Exception e) {
            MsgBox.alert(this, "Bạn chưa chọn Sản Phẩm!");
        }
    }

    private void delete_KhuyenMai() {
        String tenKM = txt_TenKhuyenMai_KhuyenMai.getText();
        if (MsgBox.confirm(this, "Bạn thực sự muốn xóa Khuyến Mãi này?")) {
            try {
                kmDAO.delete(tenKM);
                this.fillTable_KhuyenMai();
                this.clearForm_KhuyenMai();
                MsgBox.alert(this, "Xóa Khuyến Mãi thành công!");
            } catch (Exception e) {
                MsgBox.alert(this, "Xóa Khuyến Mãi thất bại!");
            }
        }
    }

    private void fillTable_SanPham_KhuyenMai() {
        tableModelSanPham_KhuyenMai = (DefaultTableModel) tbl_DanhSachSanPham_KhuyenMai.getModel();
        tableModelSanPham_KhuyenMai.setRowCount(0);
        try {
            List<SanPham> list = spDAO.selectAll();
            for (SanPham sp : list) {
                Object[] row = {sp.getMaSanPham(), sp.getTenSanPham(), sp.getDonGia(), sp.getSoLuong(), sp.getHangSanXuat()};
                if (sp.isTrangThai() == true) {
                    tableModelSanPham_KhuyenMai.addRow(row);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    private void fillTableSanPhamCo_KhuyenMai(int i) {
        String maSP = tbl_DanhSachSanPham_KhuyenMai.getValueAt(i, 0).toString();
        DefaultTableModel model = (DefaultTableModel) tbl_DanhSachKhuyenMai_KhuyenMai.getModel();
        model.setRowCount(0);
        try {
            List<KhuyenMai> list = kmDAO.selectKhuyenMaiByMaSP(maSP);
            for (KhuyenMai km : list) {
                String ngayBatDau = XDate.toString(km.getNgayBatDau(), "dd/MM/yyyy");
                String ngayKetThuc = XDate.toString(km.getNgayKetThuc(), "dd/MM/yyyy");
                Object[] row = {km.getTenKhuyenMai(), km.getGiaGiam(), ngayBatDau,
                    ngayKetThuc, km.getTrangThai() ? "Đang hoạt động" : "Ngừng hoạt động", km.getGhiChu()
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    public boolean kiemTraKhuyenMai() {
        if (dcs_NgayBatDau_KhuyenMai.getCalendar() == null) {
            MsgBox.alert(this, "Bạn chưa chọn Ngày bắt đầu !!!");
            return false;
        } else if (dcs_NgayKetThuc_KhuyenMai.getCalendar() == null) {
            MsgBox.alert(this, "Bạn chưa chọn Ngày kết thúc !!!");
            return false;
        }

        try {
            if (Float.parseFloat(txt_GiamGia_KhuyenMai.getText()) < 0) {
                MsgBox.alert(this, "Giá giảm phải lớn hơn 0 !!!");
                return false;
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Giá giảm phải là giá trị số !!!");
            return false;
        }

        Date dateNgayBatDau, dateNgayKetThuc;
        dateNgayBatDau = dcs_NgayBatDau_KhuyenMai.getDate();
        dateNgayKetThuc = dcs_NgayKetThuc_KhuyenMai.getDate();

        if (dateNgayBatDau.getYear() > dateNgayKetThuc.getYear()) {
            MsgBox.alert(this, "Ngày kết thúc không thể trước ngày ngày bắt đầu !!!");
            return false;
        } else if (dateNgayBatDau.getYear() == dateNgayKetThuc.getYear() && dateNgayBatDau.getMonth() > dateNgayKetThuc.getMonth()) {
            MsgBox.alert(this, "Ngày kết thúc không thể trước ngày ngày bắt đầu !!!");
            return false;
        } else if (dateNgayBatDau.getYear() == dateNgayKetThuc.getYear() && dateNgayBatDau.getMonth() == dateNgayKetThuc.getMonth() && dateNgayBatDau.getDay() > dateNgayKetThuc.getDay()) {
            MsgBox.alert(this, "Ngày kết thúc không thể trước ngày ngày bắt đầu!!!");
            return false;
        }
        return true;
    }

//                       END_CARD_KHUYENMAI
//   ********************** TRONG HIEN**********************
//                          START_CARD_TAIKHOAN
    int row_tbl_TaiKhoan = -1;
    DefaultTableModel tbModel_DSDaCoTaiKhoan;

    private void init_TaiKhoan() {
//        FILL DATA TABLE
        this.FillTable_tbl_DSCoTaiKhoan_TaiKhoan();
        this.FillTable_tbl_DSChuaTaiKhoan_TaiKhoan();

//        Table format
        TableEdit.centerRendererTable(tbl_DSChuaTaiKhoan_TaiKhoan);
        TableEdit.centerRendererTable(tbl_DSDaCoTaiKhoan_TaiKhoan);

        if (Auth.isLogin()) {
            this.checkManager_action();
        }
    }

    private void checkManager_action() {
        if (txt_MaNV_TaiKhoan.getText().equals(Auth.user.getMaNhanVien()) && Auth.isManager()) {
            rdo_NhanVien_TaiKhoan.setEnabled(false);
            btn_Xoa_TaiKhoan.setEnabled(false);

            txt_TenDangNhap_TaiKhoan.setEditable(true);
            txt_MatKhau_TaiKhoan.setEditable(true);
            txt_Confirm_TaiKhoan.setEditable(true);

        } else {
            rdo_NhanVien_TaiKhoan.setEnabled(true);
            txt_TenDangNhap_TaiKhoan.setEditable(false);
            txt_MatKhau_TaiKhoan.setEditable(false);
            txt_Confirm_TaiKhoan.setEditable(false);
        }
    }

    private void defaultData(boolean isEnabled) {
        txt_TenDangNhap_TaiKhoan.setEditable(isEnabled);
        txt_MatKhau_TaiKhoan.setEditable(isEnabled);
        txt_Confirm_TaiKhoan.setEditable(isEnabled);
        rdo_NhanVien_TaiKhoan.setSelected(isEnabled);
    }

    private boolean isNullOnTextFiled(JTextField... args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].getText().equals("")) {
                MsgBox.alert(this, "Vui lòng nhập vào " + args[i].getToolTipText());
                args[i].requestFocus();
                return false;
            }
        }
        return true;
    }

    private void FillTable_tbl_DSCoTaiKhoan_TaiKhoan() {
        DefaultTableModel model = (DefaultTableModel) tbl_DSDaCoTaiKhoan_TaiKhoan.getModel();
        model.setRowCount(0);
        try {
            List<TaiKhoan> list = tkDAO.selectAll();
            for (TaiKhoan tk : list) {
                Object[] row = {
                    tk.getMaNhanVien(), tk.getTenDangNhap(), tk.getQuyen() ? "Quản lí" : "Nhân Viên"
                };
                model.addRow(row);
            }

        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
            e.printStackTrace();
        }
    }

    private void FillTable_tbl_DSChuaTaiKhoan_TaiKhoan() {
        DefaultTableModel model = (DefaultTableModel) tbl_DSChuaTaiKhoan_TaiKhoan.getModel();
        model.setRowCount(0);
        try {
            List<NhanVien> list = nvDAO.SelectNotInTaiKhoan();
            for (NhanVien nv : list) {
                if (nv.getTrangThai()) {
                    Object[] row = {
                        nv.getMaNhanVien(), nv.getTenNhanVien(), nv.getNgaySinh(), nv.getGioiTinh() ? "Nam" : "Nữ", nv.getSDT(), nv.getEmail(), nv.getDiaChi(),
                        nv.getCCCD(), nv.getTrangThai() ? "Đang làm" : "Nghĩ việc", nv.getHinhAnh(), nv.getGhiChu()
                    };
                    model.addRow(row);
                }
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
            e.printStackTrace();
        }
    }

    private void setForm_ChuaCoTaiKhoan_TaiKhoan(NhanVien nv) {
        txt_MaNV_TaiKhoan.setText(nv.getMaNhanVien());
        txt_TenDangNhap_TaiKhoan.setText("");
        txt_MatKhau_TaiKhoan.setText("");
        txt_Confirm_TaiKhoan.setText("");
        rdo_NhanVien_TaiKhoan.setSelected(true);
        rdo_QuanLi_TaiKhoan.setSelected(false);
    }

//    private void getForm_DaCoTaiKhoan_TaiKhoan() {
//  
//    }
    private void setForm_DaCoTaiKhoan_TaiKhoan(TaiKhoan tk) {
//        get data from TaiKhoan.Entity
        txt_MaNV_TaiKhoan.setText(tk.getMaNhanVien());
        txt_TenDangNhap_TaiKhoan.setText(tk.getTenDangNhap());
        txt_MatKhau_TaiKhoan.setText(tk.getMatKhau());
        txt_Confirm_TaiKhoan.setText(tk.getMatKhau());
        rdo_QuanLi_TaiKhoan.setSelected(tk.getQuyen());
        rdo_NhanVien_TaiKhoan.setSelected(!tk.getQuyen());
    }

    private TaiKhoan getForm_ChuaCoTaiKhoan_TaiKhoan() {
        TaiKhoan tk = new TaiKhoan();

        String MaNV = txt_MaNV_TaiKhoan.getText();
        String TenDangNhap = txt_TenDangNhap_TaiKhoan.getText();
        String MatKhau = String.valueOf(txt_MatKhau_TaiKhoan.getPassword());

        boolean isQuanLi = rdo_QuanLi_TaiKhoan.isSelected();

        tk.setMaNhanVien(MaNV);
        tk.setTenDangNhap(TenDangNhap);
        tk.setMatKhau(MatKhau);
        tk.setQuyen(isQuanLi);

        return tk;
    }

    private boolean checkForm_TaiKhoan() {
        if (tbl_DSChuaTaiKhoan_TaiKhoan.getSelectedRow() == -1) {
            MsgBox.alert(this, "Vui lòng chọn một nhân viên trên bảng chưa có tài khoản");
            return false;
        }

        if (!txt_Confirm_TaiKhoan.getText().equals(txt_MatKhau_TaiKhoan.getText())) {
            MsgBox.alert(this, "Mật khẩu xác nhận không đúng");
            return false;
        }

        return true;
    }

    private void insert_tbl_DaCoTaiKhoan_TaiKhoan() {
        if (checkForm_TaiKhoan()) {
            TaiKhoan tk = getForm_ChuaCoTaiKhoan_TaiKhoan();

            try {
                tkDAO.insert(tk);

                this.FillTable_tbl_DSCoTaiKhoan_TaiKhoan();
                this.FillTable_tbl_DSChuaTaiKhoan_TaiKhoan();
                this.clearForm_TaiKhoan();

                MsgBox.alert(this, "Thêm mới thành công");
            } catch (Exception e) {
                MsgBox.alert(this, "Thêm mới thất bại");
                e.printStackTrace();
            }
        }
    }

    private void update_tbl_DaCoTaiKhoan_TaiKhoan() {
        TaiKhoan tk = getForm_ChuaCoTaiKhoan_TaiKhoan();
        try {

            tkDAO.update(tk);

            this.FillTable_tbl_DSCoTaiKhoan_TaiKhoan();
            this.FillTable_tbl_DSChuaTaiKhoan_TaiKhoan();

            MsgBox.alert(this, "Cập nhật thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Cập nhật thất bại");
            e.printStackTrace();
        }

    }

    private void delete_tbl_DaCoTaiKhoan_TaiKhoan() {
        try {

            if (tbl_DSDaCoTaiKhoan_TaiKhoan.getSelectedRow() == -1) {
                MsgBox.alert(this, "Vui lòng chọn một dòng trên bảng");
                return;
            }

            if (MsgBox.confirm(this, "Bạn thực sự muốn xóa tài khoản này")) {
                String MaNV = txt_MaNV_TaiKhoan.getText();
                try {
                    tkDAO.delete(MaNV);

                    this.FillTable_tbl_DSCoTaiKhoan_TaiKhoan();
                    this.FillTable_tbl_DSChuaTaiKhoan_TaiKhoan();
                    this.clearForm_TaiKhoan();

                    MsgBox.alert(this, "Xóa thành công");
                } catch (Exception e) {
                    MsgBox.alert(this, "Xóa thất bại");
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            MsgBox.alert(this, e.getMessage());
        }

    }

    public void updateStatus_DS_DaCoTaiKhoan_TaiKhoan() {
        boolean edit = (this.row_tbl_TaiKhoan >= 0);
        boolean first = (this.row_DS_HoaDon_banhang == 0);
        boolean last = (this.row_DS_HoaDon_banhang == tbl_DSHoaDon_BanHang.getRowCount() - 1);
//        Trạng thái form
        btn_Them_TaiKhoan.setEnabled(!edit);
        btn_CapNhat_TaiKhoan.setEnabled(edit);
        btn_Xoa_TaiKhoan.setEnabled(edit);

        // Trạng thái điều hướng
        btn_First_DSChuaTaiKhoan_taikhoan.setEnabled(edit && !first);
        btn_prev_DSChuaTaiKhoan_taikhoan.setEnabled(edit && !first);
        btn_next_DSChuaTaiKhoan_taikhoan.setEnabled(edit && !last);
        btn_last_DSChuaTaiKhoan_taikhoan.setEnabled(edit && !last);
    }

    private void edit_DS_DaCoTaiKhoan_TaiKhoan() {
        String MaNV = (String) tbl_DSDaCoTaiKhoan_TaiKhoan.getValueAt(this.row_tbl_TaiKhoan, 0);
        TaiKhoan tk = tkDAO.selectByid(MaNV);
        this.setForm_DaCoTaiKhoan_TaiKhoan(tk);
        this.updateStatus_DS_DaCoTaiKhoan_TaiKhoan();
    }

    private void edit_DS_ChuaCoTaiKhoan_TaiKhoan() {
        String MaNV = (String) tbl_DSChuaTaiKhoan_TaiKhoan.getValueAt(this.row_tbl_TaiKhoan, 0);
        NhanVien nv = nvDAO.selectByid(MaNV);
        this.setForm_ChuaCoTaiKhoan_TaiKhoan(nv);
    }

    private void clearForm_TaiKhoan() {
        txt_MaNV_TaiKhoan.setText("");
        txt_TenDangNhap_TaiKhoan.setText("");
        txt_MatKhau_TaiKhoan.setText("");
        txt_Confirm_TaiKhoan.setText("");
        rdo_NhanVien_TaiKhoan.setSelected(true);
        rdo_QuanLi_TaiKhoan.setSelected(false);

        this.row_tbl_TaiKhoan = -1;
        this.updateStatus_DS_DaCoTaiKhoan_TaiKhoan();
    }
//                          END_CARD_TAIKHOAN  
//                          START_CARD_HETHONG 

    private void init_HeThong() {
        if (Auth.isLogin()) {
            txt_TenDangNhap_HeThong.setText(Auth.user.getTenDangNhap());
        }
    }

    private boolean checkForm_HeThong() {
        if (txt_MatKhau_HeThong.getText().equals("")) {
            txt_MatKhau_HeThong.requestFocus();
            MsgBox.alert(this, "Không để trống mật khẩu");
            return false;
        }

        if (!Auth.getMatKhau_TaiKhoan().equalsIgnoreCase(txt_MatKhau_HeThong.getText())) {
            MsgBox.alert(this, "Sai mật khẩu!");
            txt_MatKhau_HeThong.requestFocus();
            return false;
        }

        if (txt_MatKhauMoi_HeThong.getText().equals("")) {
            MsgBox.alert(this, "Chưa nhập vào mật khẩu mới");
            txt_MatKhauMoi_HeThong.requestFocus();
            return false;
        } else if (txt_MatKhauMoi_HeThong.getText().equals(txt_MatKhau_HeThong.getText())) {
            MsgBox.alert(this, "Mật khẩu mới không được trùng với mật khẩu cũ");
            txt_MatKhauMoi_HeThong.requestFocus();
            return false;
        }

        if (txt_Confirm_HeThong.getText().equals("")) {
            MsgBox.alert(this, "Chưa nhập vào xác nhận mật khẩu mới");
            txt_Confirm_HeThong.requestFocus();
            return false;
        }

        if (!txt_Confirm_HeThong.getText().equals(txt_MatKhauMoi_HeThong.getText())) {
            MsgBox.alert(this, "Mật khẩu xác nhận không đúng");
            txt_Confirm_HeThong.requestFocus();
            return false;
        }

        return true;
    }

    private TaiKhoan getForm_CapNhatMatKhau_HeThong() {
        TaiKhoan tk = new TaiKhoan();

        String tenDN = txt_TenDangNhap_HeThong.getText();
        String matKhau = txt_Confirm_HeThong.getText();

        tk.setTenDangNhap(tenDN);
        tk.setMatKhau(matKhau);

        return tk;
    }

    private void CapNhatMatKhau_HeThong() {
        TaiKhoan tk = getForm_CapNhatMatKhau_HeThong();
        try {
            if (checkForm_HeThong()) {

                tkDAO.update_Password_Only(tk);

                MsgBox.alert(this, "Cập nhật mật khẩu thành công");
                new Login_Frame().setVisible(true);
                this.dispose();
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Cập nhật thất bại");
            e.printStackTrace();
        }
    }

//                          END_CARD_HETHONG  
    //                          START_CARD_BanHang  
    int row_DS_HoaDon_banhang = -1;

    DefaultTableModel model_tbl_HoaDon;
    DefaultTableModel model_tbl_DSHoaDon;
    DefaultTableModel model_tbl_DSHoaDonChiTiet;
    DefaultTableModel model_tbl_DS_SanPham_BanHang;

    int SoLuong_tbl_HoaDon_BanHang;
    double ThanhTien_tbl_HoaDon_BanHang;
    double donGia_HoaDon_BanHang;
    int soLuong_HoaDon_Banhang;
    double thanhTien;
    double tienThanhToan;
    double tienKhachDua;
    double tienConLai;
    double tongGiaGiam;
    DecimalFormat format = new DecimalFormat("###,###,###");

    private void init_BanHang() {
//        Position row
        row_DS_HoaDon_banhang = -1;

//        Format Table
        TableEdit.centerRendererTable(tbl_HoaDon_BanHang);
        TableEdit.centerRendererTable(tbl_DSHoaDon_BanHang);
        TableEdit.centerRendererTable(tbl_DSHoaDonChiTiet_BanHang);
        TableEdit.centerRendererTable(tbl_DS_SanPham_BanHang);

//        Default value form BanHang
        this.defaultFormData();

//        Fill Ban Hang
        this.FillTable_DS_SanPham_BanHang();

//        Fill DS hoaDon
        this.fillToTableDSHoaDon_BangHang();

//        Format Form Ban Hang
        txt_TienTraLai_Banhang.setEditable(false);
    }

    private void defaultFormData() {
        lbl_MaHoaDon_BanHang.setText("");
        lbl_NgayTao_BanHang.setText("");
        lbl_NguoiTao_BanHang.setText("");
        lbl_TenKhachHang_BanHang.setText("");
    }

    private void ThanhTien_HoaDon_BanHang() {
        try {

            int x = tbl_HoaDon_BanHang.getSelectedRow();
            int y = 6;
            try {
                donGia_HoaDon_BanHang = Double.parseDouble(tbl_HoaDon_BanHang.getValueAt(x, 4).toString());
                soLuong_HoaDon_Banhang = Integer.parseInt(tbl_HoaDon_BanHang.getValueAt(x, 5).toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

//          Cal ThanTien tbl HoaDon
            ThanhTien_tbl_HoaDon_BanHang = (donGia_HoaDon_BanHang * soLuong_HoaDon_Banhang);
            tbl_HoaDon_BanHang.setValueAt(String.valueOf(ThanhTien_tbl_HoaDon_BanHang), x, y);

            this.getSum_ThanhTien_tbl_HoaDon(thanhTien);

        } catch (NumberFormatException e) {
            MsgBox.alert(this, "Lỗi tính thành tiền");
            e.printStackTrace();
        }
    }

    private void getSum_ThanhTien_tbl_HoaDon(double thanhTien) {
        try {
            for (int i = 0; i < tbl_HoaDon_BanHang.getRowCount(); i++) {
                thanhTien += (Double.parseDouble(tbl_HoaDon_BanHang.getValueAt(i, 6).toString()));
                if (txt_giaGiam_BanHang.getText().equals("0")) {
                    thanhTien += (Double.parseDouble(tbl_HoaDon_BanHang.getValueAt(i, 6).toString()));
                }
            }
            thanhTien -= Double.parseDouble(txt_giaGiam_BanHang.getText());

            lbl_TongTienThanhToan_BanHang.setText(String.valueOf(thanhTien));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    String TienKhachDua_Str = "";
    double TienKhachDua_dou = 0.0;
    String TongThanhTien_Str = "";
    double TongThanhTien_dou = 0.0;
    double TienTraLai = 0.0;

    private void tinhTienTraLai_HoaDon_BanHang() {
        try {
            if (txt_TienKhachDua_BanHang.getText().length() == 0) {
                return;
            }

            TienKhachDua_Str = txt_TienKhachDua_BanHang.getText();
            TienKhachDua_dou = Double.parseDouble(TienKhachDua_Str);

            TongThanhTien_Str = lbl_TongTienThanhToan_BanHang.getText();
            TongThanhTien_dou = Double.parseDouble(TongThanhTien_Str);

            TienTraLai = (TienKhachDua_dou - TongThanhTien_dou);

            if (TienTraLai >= 0 && tbl_HoaDon_BanHang.getRowCount() > 0) {
                txt_TienTraLai_Banhang.setText(String.valueOf(TienTraLai));
                btn_ThanhToan_BanHang.setEnabled(true);
            } else {
                this.btn_ThanhToan_BanHang.setEnabled(false);
            }

            if (TienKhachDua_dou < TongThanhTien_dou) {
                txt_TienTraLai_Banhang.setText("0");
                btn_ThanhToan_BanHang.setEnabled(false);
                return;
            }
//
//            if (TienTraLai <= 0) {
//                txt_TienTraLai_Banhang.setText("0");
//                return;
//            }         

            txt_TienTraLai_Banhang.setText(String.valueOf(TienTraLai));
        } catch (NumberFormatException e) {
            return;
        }
    }

    List<Object[]> listGiaGiam;
    List<Double> listTongGiaGiam = new ArrayList<Double>();

    private void getSumGiaGiam_BanHang() {
        double giaGiam = 0;

        for (Object[] o : listGiaGiam) {
            if (o[0] == null) {
                giaGiam = 0;
                break;
            }
            giaGiam += Double.parseDouble(o[0].toString());
        }
        listTongGiaGiam.add(giaGiam);

        double tongGiaGiam = 0.0;
        for (Double o : listTongGiaGiam) {
            tongGiaGiam += o;
        }
        txt_giaGiam_BanHang.setText(String.valueOf(tongGiaGiam));
    }

    String tenSp_tbl_BanHang;
    int soLuong_now;

    private void updateSoLuong_tbl_SP_HoaDon(boolean isPlus) {
        int index_selected = tbl_HoaDon_BanHang.getSelectedRow();
        tenSp_tbl_BanHang = tbl_HoaDon_BanHang.getValueAt(index_selected, 1).toString();
        String tenSp_tbl_SanPham = "";
        int index = 0;

        for (int i = 0; i < tbl_DS_SanPham_BanHang.getRowCount(); i++) {
            tenSp_tbl_SanPham = tbl_DS_SanPham_BanHang.getValueAt(i, 0).toString();
            if (tenSp_tbl_SanPham.equals(tenSp_tbl_BanHang)) {
                index = i;
            }
        }

        soLuong_now = Integer.parseInt(tbl_DS_SanPham_BanHang.getValueAt(index, 2).toString());

        if (isPlus) {
            soLuong_now++;
            tbl_DS_SanPham_BanHang.setValueAt(soLuong_now, index, 2);
        } else {
            soLuong_now--;
            tbl_DS_SanPham_BanHang.setValueAt(soLuong_now, index, 2);
        }
    }

    List<SanPham> list_allSP = new ArrayList<>();

    private void FillTable_DS_SanPham_BanHang() {
        model_tbl_DS_SanPham_BanHang = (DefaultTableModel) tbl_DS_SanPham_BanHang.getModel();
        model_tbl_DS_SanPham_BanHang.setRowCount(0);
        try {
            list_allSP = spDAO.selectAll();
            for (SanPham sp : list_allSP) {
                if (sp.isTrangThai()) {
                    Object[] row = {
                        sp.getTenSanPham(), sp.getMauSac(), sp.getSoLuong()};
                    model_tbl_DS_SanPham_BanHang.addRow(row);
                }
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
            e.printStackTrace();
        }
    }

    private boolean check_table_HoaDon_BanHang() {
        return true;
    }

    HashSet listTenSP = new HashSet();
    List<SanPham> list_SP_TheoTen = null;
    boolean isClear = false;
    List<String> list_tenSP_hetHang = new ArrayList<>();

    private void FillTable_HoaDon_BanHang() {
        model_tbl_HoaDon = (DefaultTableModel) tbl_HoaDon_BanHang.getModel();

        int index_selected = tbl_DS_SanPham_BanHang.getSelectedRow();
        int soLuong_temp = Integer.parseInt(tbl_DS_SanPham_BanHang.getValueAt(index_selected, 2).toString());

        try {

            String tenSP = (String) tbl_DS_SanPham_BanHang.getValueAt(tbl_DS_SanPham_BanHang.getSelectedRow(), 0);
            list_SP_TheoTen = spDAO.select_All_TheoTenSP(tenSP);

            listGiaGiam = hdDAO.getGiaGiam(tenSP);
//            Proc add giaGiam Ban Hang
            this.getSumGiaGiam_BanHang();

//          Fill data to Table
            int index = 0;
            double thanhtien_tam = 0.0;

            for (int i = 0; i < list_SP_TheoTen.size(); i++) {
                if (list_SP_TheoTen.get(i).getTenSanPham().equals(tenSP)) {
                    thanhtien_tam = list_SP_TheoTen.get(i).getDonGia();
                    index = i;
                    break;
                }
            }

            if (!isRemove) {
                if (listTenSP.add(tenSP)) {
                    soLuong_temp--;
                    tbl_DS_SanPham_BanHang.setValueAt(soLuong_temp, index_selected, 2);

                    Object[] row = {
                        list_SP_TheoTen.get(index).getMaSanPham(), list_SP_TheoTen.get(index).getTenSanPham(), list_SP_TheoTen.get(index).getHangSanXuat(),
                        list_SP_TheoTen.get(index).getMauSac(), list_SP_TheoTen.get(index).getDonGia(), "1",
                        thanhtien_tam, "+", "-", "X"
                    };

                    model_tbl_HoaDon.addRow(row);
                    listTenSP.add(tenSP);

                    tbl_HoaDon_BanHang.changeSelection(0, 0, false, false);
                    this.getSum_ThanhTien_tbl_HoaDon(thanhTien);
                    this.ThanhTien_HoaDon_BanHang();
                    this.tinhTienTraLai_HoaDon_BanHang();
                    txt_TienTraLai_Banhang.setText("0");

                } else if (!listTenSP.add(tenSP)) {
//                  if sp tồn tại thì + soLuong
                    String tenSp_tbl_banhang = "";
                    int index_tbl_banhang = 0;

                    for (int i = 0; i <= tbl_HoaDon_BanHang.getRowCount(); i++) {
                        tenSp_tbl_banhang = (String) tbl_HoaDon_BanHang.getValueAt(i, 1);
                        if (tenSP.equals(tenSp_tbl_banhang)) {
                            index_tbl_banhang = i;
                            break;
                        }
                    }

//                    bat loi khong tang duoc thanh tien
                    if (tbl_HoaDon_BanHang.getRowCount() > 0) {
                        tbl_HoaDon_BanHang.setRowSelectionInterval(index_tbl_banhang, index_tbl_banhang);
                        this.TangSoLuong_tblHoaDon_BanHang();
                        this.ThanhTien_HoaDon_BanHang();
                    }

//                    Bat loi so luong sp = 0
                    try {
                        if (soLuong_now <= 0) {
                            MsgBox.alert(this, "Sản phẩm đã hết, vui lòng nhập hàng hoặc xóa sản phẩm");
//                    Remove rows one by one from the end of the table
                            DefaultTableModel model = (DefaultTableModel) this.tbl_DS_SanPham_BanHang.getModel();
                            int[] rows = tbl_DS_SanPham_BanHang.getSelectedRows();
                            for (int i = 0; i < rows.length; i++) {
                                list_tenSP_hetHang.add(tbl_DS_SanPham_BanHang.getValueAt(i, 0).toString());
                                model.removeRow(rows[i] - i);
                            }
                        }
                    } catch (Exception e) {
                        if (tbl_DS_SanPham_BanHang.getSelectedRow() == -1) {
                            return;
                        }
                    }

                }

            }
            isRemove = false;

        } catch (NumberFormatException e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    private void TangSoLuong_tblHoaDon_BanHang() {
        int x = tbl_HoaDon_BanHang.getSelectedRow();
        int y = 5;

        int SoLuong_tbl_HoaDon_BanHang
                = Integer.parseInt(tbl_HoaDon_BanHang.getValueAt(x, y).toString());
        SoLuong_tbl_HoaDon_BanHang++;

        tbl_HoaDon_BanHang.setValueAt(SoLuong_tbl_HoaDon_BanHang, x, y);

        this.updateSoLuong_tbl_SP_HoaDon(false);

        //                    Bat loi so luong sp = 0
        if (soLuong_now <= 0) {
            MsgBox.alert(this, "Sản phẩm đã hết, vui lòng nhập hàng hoặc xóa sản phẩm");
            tbl_DS_SanPham_BanHang.setValueAt(0, tbl_DS_SanPham_BanHang.getSelectedRow(), 2);

//                    Remove rows one by one from the end of the table
            DefaultTableModel model = (DefaultTableModel) this.tbl_DS_SanPham_BanHang.getModel();
            int[] rows = tbl_DS_SanPham_BanHang.getSelectedRows();
            for (int i = 0; i < rows.length; i++) {
                list_tenSP_hetHang.add(tbl_DS_SanPham_BanHang.getValueAt(i, 0).toString());
                model.removeRow(rows[i] - i);
            }
        }
    }

    private void GiamSoLuong_tblHoaDon_BanHang() {
        model_tbl_HoaDon = (DefaultTableModel) tbl_HoaDon_BanHang.getModel();

        int x = tbl_HoaDon_BanHang.getSelectedRow();
        int y = 5;

        int SoLuong_tbl_HoaDon_BanHang
                = Integer.parseInt(tbl_HoaDon_BanHang.getValueAt(x, y).toString());
        SoLuong_tbl_HoaDon_BanHang--;

        if (soLuong_HoaDon_Banhang == 1) {
            return;
        }

        tbl_HoaDon_BanHang.setValueAt(SoLuong_tbl_HoaDon_BanHang, x, y);

        this.updateSoLuong_tbl_SP_HoaDon(true);

        //                    Bat loi so luong sp = 0
        if (soLuong_now <= 0) {
            MsgBox.alert(this, "Sản phẩm đã hết, vui lòng nhập hàng hoặc xóa sản phẩm");
            tbl_DS_SanPham_BanHang.setValueAt(0, tbl_DS_SanPham_BanHang.getSelectedRow(), 2);

//                    Remove rows one by one from the end of the table
            DefaultTableModel model = (DefaultTableModel) this.tbl_DS_SanPham_BanHang.getModel();
            int[] rows = tbl_DS_SanPham_BanHang.getSelectedRows();
            for (int i = 0; i < rows.length; i++) {
                list_tenSP_hetHang.add(tbl_DS_SanPham_BanHang.getValueAt(i, 0).toString());
                model.removeRow(rows[i] - i);
            }
        }
    }

    private void XoaSP_tblHoaDon_BanHang() {
        model_tbl_HoaDon = (DefaultTableModel) tbl_HoaDon_BanHang.getModel();
        int x = tbl_HoaDon_BanHang.getSelectedRow();

        listTenSP.remove(tbl_HoaDon_BanHang.getValueAt(x, 1));
        model_tbl_HoaDon.removeRow(x);

        this.getSum_ThanhTien_tbl_HoaDon(thanhTien);
    }

    private void HuyGioHang_BanHang() {
        DefaultTableModel model = (DefaultTableModel) tbl_HoaDon_BanHang.getModel();
        model.setRowCount(0);

        lbl_TenKhachHang_BanHang.setText("");
        txt_TienKhachDua_BanHang.setText("");
        txt_TienTraLai_Banhang.setText("");
        lbl_TongTienThanhToan_BanHang.setText("");
        txt_giaGiam_BanHang.setText("");

        listTenSP.clear();
        listGiaGiam.clear();
        listTongGiaGiam.clear();

        this.btn_ThanhToan_BanHang.setEnabled(false);
        this.FillTable_DS_SanPham_BanHang();
    }

    private void clearForm_BanHang() {
        txt_TienKhachDua_BanHang.setText("");
        txt_TienTraLai_Banhang.setText("");

        btn_ThanhToan_BanHang.setEnabled(false);
    }

    private HoaDon getForm_HD_BanHang() {
        int selectedRow_tbl_DSKhachHang = tbl_DSKhachHang_KhachHang.getSelectedRow();

        HoaDon hd = new HoaDon();
        String MaHD = lbl_MaHoaDon_BanHang.getText();
        String maKH = tbl_DSKhachHang_KhachHang.getValueAt(selectedRow_tbl_DSKhachHang, 0).toString();
        String MaNV = Auth.getIDNhanVien();
        String NgayTao = lbl_NgayTao_BanHang.getText();
        String ghiChu = txt_GhiChu_BanHang.getText();
        String tongTien = lbl_TongTienThanhToan_BanHang.getText();

        hd.setMaHoaDon(MaHD);
        hd.setMaKhachHang(maKH);
        hd.setMaNhanVien(MaNV);
        hd.setNgayTao(XDate.toDate(NgayTao, "dd/MM/yyyy"));
        hd.setGhiChu(ghiChu);
        hd.setTongTien(Double.parseDouble(tongTien));

        return hd;
    }

    public void selectTab(int index) {
        tabs_HoaDon.setSelectedIndex(index);
    }

    Map<String, Integer> list_soLuong_SP_update = new HashMap<>();
    String tenSP_update = "";
    int soLuong_update = 0;

    private void update_SoluongSP_DanhSachSP_BanHang() {
        for (int i = 0; i < tbl_DS_SanPham_BanHang.getRowCount(); i++) {
            tenSP_update = tbl_DS_SanPham_BanHang.getValueAt(i, 0).toString();
            list_soLuong_SP_update.put(tenSP_update, 0);
        }

        for (int i = 0; i < tbl_DS_SanPham_BanHang.getRowCount(); i++) {
            soLuong_update = Integer.parseInt(tbl_DS_SanPham_BanHang.getValueAt(i, 2).toString());
            list_soLuong_SP_update.put(list_allSP.get(i).getTenSanPham(), soLuong_update);
        }

        list_soLuong_SP_update.forEach(
                (key, value)
                -> {
//            System.out.println("Key:" + key);
//            System.out.println("Value:" + value);

            spDAO.update_SoLuong(key, value);
        });
    }

    private void ThanhToan_BanHang_button_click() {
        if (lbl_TenKhachHang_BanHang.getText().equals("")) {
            MsgBox.alert(this, "Chưa chọn khách hàng để thêm hóa đơn");
            onClick(pnl_KhachHang);
            onLeaveClick(pnl_ThongKe);
            onLeaveClick(pnl_BanHang);
            onLeaveClick(pnl_SanPham);
            onLeaveClick(pnl_HeThong);
            onLeaveClick(pnl_DiemDanh);
            onLeaveClick(pnl_TaiKhoan);
            onLeaveClick(pnl_NhanVien);
            onLeaveClick(pnl_Luong);
            onLeaveClick(pnl_KhuyenMai);

            onClickLabel(lbl_KhachHang);
            onLeaveClickLabel(lbl_ThongKe);
            onLeaveClickLabel(lbl_BanHang);
            onLeaveClickLabel(lbl_SanPham);
            onLeaveClickLabel(lbl_HeThong);
            onLeaveClickLabel(lbl_DiemDanh);
            onLeaveClickLabel(lbl_TaiKhoan);
            onLeaveClickLabel(lbl_NhanVien);
            onLeaveClickLabel(lbl_Luong);
            onLeaveClickLabel(lbl_KhuyenMai);

            //        indicators
            indicator1.setOpaque(false);
            indicator2.setOpaque(true);
            indicator3.setOpaque(false);
            indicator4.setOpaque(false);
            indicator6.setOpaque(false);
            indicator7.setOpaque(false);
            indicator8.setOpaque(false);
            indicator9.setOpaque(false);
            indicator10.setOpaque(false);
            indicator11.setOpaque(false);

            //        Card playout
            CardLayout playout = (CardLayout) pnl_MainDisplayCard.getLayout();
            playout.show(pnl_MainDisplayCard, "card_KhachHang");

            txt_SoDienThoaiKhachHang_KhachHang.requestFocus();
            return;
        }

        HoaDon hd = getForm_HD_BanHang();

        String maHD = lbl_MaHoaDon_BanHang.getText();
        String ghiChu = txt_GhiChu_BanHang.getText();

        if(Double.parseDouble(txt_TienKhachDua_BanHang.getText()) < Double.parseDouble(lbl_TongTienThanhToan_BanHang.getText()) ){
            MsgBox.alert(this, "Tiền khách đưa nhỏ hơn tiền thanh toán");
            txt_TienKhachDua_BanHang.requestFocus();
            btn_ThanhToan_BanHang.setEnabled(false);
            return;
        }
        
        try {
            hdDAO.insert(hd);

            List<HoaDonChiTiet> listHDCT = new ArrayList<>();

            for (int j = 0; j < listTenSP.size(); j++) {
                int soLuong = Integer.parseInt(tbl_HoaDon_BanHang.getValueAt(j, 5).toString());
                String maSP = (tbl_HoaDon_BanHang.getValueAt(j, 0).toString());
                HoaDonChiTiet hdct = new HoaDonChiTiet(maHD, maSP, soLuong, ghiChu);
                listHDCT.add(hdct);
            }

//            add vao db
            for (HoaDonChiTiet hdct : listHDCT) {
                hdctDAO.insert(hdct);
            }

//            Update lai so luong trong dataabase
            this.update_SoluongSP_DanhSachSP_BanHang();

//            Fill lai du lieu
            this.FillTable_DS_SanPham_BanHang();
            this.fillToTableDSHoaDon_BangHang();

            tbl_DSHoaDon_BanHang.setRowSelectionInterval(tbl_DSHoaDon_BanHang.getRowCount() - 1,
                    tbl_DSHoaDon_BanHang.getRowCount() - 1);
            this.fillToTableDSHoaDonChiTiet_BanHang();
            tbl_DSHoaDonChiTiet_BanHang.setRowSelectionInterval(0, 0);

            MsgBox.alert(this, "Thêm hóa đơn thành công");
            int select_tab = 1;
            this.selectTab(select_tab);

//          set lai trang thai san pham da het hang
            if (!list_tenSP_hetHang.isEmpty()) {
                for (String str : list_tenSP_hetHang) {
                    spDAO.update_trangThai(str);
                }
            }

            this.FillTable_DS_SanPham_BanHang();
            this.fillTable_SanPham();
            this.fillTable_SanPham_DaXoa();

//            String maXuatHD = lbl_MaHoaDon_BanHang.getText();
            //            this.XuatHoaDon(maXuatHD);
            this.generateInvoice();

            this.HuyGioHang_BanHang();
//            In hoa don
            this.InHoaDon();
        } catch (Exception e) {
            MsgBox.alert(this, "Thêm thất bại");
            e.printStackTrace();
        }
    }

    private void InHoaDon() {
        String otps[] = {"Có, in hóa đơn", "Không, để sau"};
        try {
            if (MsgBox.options(this, "Bạn có muốn in hóa đơn?", otps) == 0) {
                this.printInvoice();
            }
        } catch (Exception e) {
            MsgBox.alert(this, "In thất bại");
            e.printStackTrace();
        }
    }

    double bHeight = 0.0;

    //    In hoa don variable
    String invoice_ID = "";
    String invoice_Emp = "";
    String invoice_CosName = "";
    String invoice_total = "";

    String product_name = "";
    String quantity = "";
    String price = "";

    List<String> product_invoice = new ArrayList<>();
    List<String> product_quantity = new ArrayList<>();
    List<String> product_price = new ArrayList<>();

    private void generateInvoice() {

        invoice_ID = (String) tbl_DSHoaDon_BanHang.getModel().getValueAt(tbl_DSHoaDon_BanHang.getSelectedRow(), 0);
        invoice_Emp = Auth.getNameNhanVien();
        invoice_CosName = (String) tbl_DSHoaDon_BanHang.getModel().getValueAt(tbl_DSHoaDon_BanHang.getSelectedRow(), 2);
        invoice_total = (String) tbl_DSHoaDon_BanHang.getModel().getValueAt(tbl_DSHoaDon_BanHang.getSelectedRow(), 4);

        int index = tbl_DSHoaDonChiTiet_BanHang.getSelectedRow();

        product_name = (String) tbl_DSHoaDonChiTiet_BanHang.getModel().getValueAt(index, 1);
        quantity = String.valueOf(tbl_DSHoaDonChiTiet_BanHang.getModel().getValueAt(index, 3));
        price = String.valueOf(tbl_DSHoaDonChiTiet_BanHang.getModel().getValueAt(index, 4));

        for (int i = 0; i < tbl_DSHoaDonChiTiet_BanHang.getModel().getRowCount(); i++) {
            product_invoice.add(tbl_DSHoaDonChiTiet_BanHang.getModel().getValueAt(i, 1).toString());
        }

        for (int i = 0; i < tbl_DSHoaDonChiTiet_BanHang.getModel().getRowCount(); i++) {
            product_quantity.add(tbl_DSHoaDonChiTiet_BanHang.getModel().getValueAt(i, 3).toString());
        }

        for (int i = 0; i < tbl_DSHoaDonChiTiet_BanHang.getModel().getRowCount(); i++) {
            product_price.add(tbl_DSHoaDonChiTiet_BanHang.getModel().getValueAt(i, 4).toString());
        }
    }

    private void printInvoice() {
        bHeight = Double.valueOf(product_invoice.size());
        //JOptionPane.showMessageDialog(rootPane, bHeight);

        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setPrintable(new BillPrintable(), getPageFormat(pj));
        try {
            pj.print();

            MsgBox.alert(this, "In hóa đơn thành công");
        } catch (PrinterException ex) {
            MsgBox.alert(this, "Chưa chọn đường dẫn lưu file");
            ex.printStackTrace();
        }
    }

    public PageFormat getPageFormat(PrinterJob pj) {

        PageFormat pf = pj.defaultPage();
        Paper paper = pf.getPaper();

        double bodyHeight = bHeight;
        double headerHeight = 5.0;
        double footerHeight = 5.0;
        double width = cm_to_pp(8);
        double height = cm_to_pp(headerHeight + bodyHeight + footerHeight);
        paper.setSize(width, height);
        paper.setImageableArea(0, 10, width, height - cm_to_pp(1));

        pf.setOrientation(PageFormat.PORTRAIT);
        pf.setPaper(paper);

        return pf;
    }

    protected static double cm_to_pp(double cm) {
        return toPPI(cm * 0.393600787);
    }

    protected static double toPPI(double inch) {
        return inch * 72d;
    }

    public class BillPrintable implements Printable {

        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
                throws PrinterException {

            int r = product_invoice.size();
            ImageIcon icon = new ImageIcon("");
            int result = NO_SUCH_PAGE;
            if (pageIndex == 0) {

                Graphics2D g2d = (Graphics2D) graphics;
                double width = pageFormat.getImageableWidth();
                g2d.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());

                //  FontMetrics metrics=g2d.getFontMetrics(new Font("Arial",Font.BOLD,7));
                try {
                    int y = 20;
                    int yShift = 10;
                    int headerRectHeight = 20;
                    // int headerRectHeighta=40;

                    g2d.setFont(new Font("Segui UI", Font.PLAIN, 7));
                    g2d.drawImage(icon.getImage(), 80, 20, 90, 65, rootPane);
                    y += yShift;
                    g2d.drawString("------------------------------------------------------------------", 12, y);
                    y += yShift;
                    g2d.drawString("              Cửa hàng điện thoại Vũ Trụ ", 12, y);
                    y += yShift;
                    g2d.drawString("               FPT Polytechnic Cần Thơ ", 12, y);
                    y += yShift;
                    g2d.drawString("          Nguyễn Văn Linh Ninh Kiều, Cần Thơ ", 12, y);
                    y += yShift + 7;
                    g2d.drawString("                   HÓA ĐƠN THANH TOÁN      ", 12, y);
                    y += yShift + 10;
                    g2d.drawString(" Mã hóa đơn:                          " + invoice_ID, 12, y);
                    y += yShift;
                    g2d.drawString(" Tên nhân viên:                      " + invoice_Emp, 12, y);
                    y += yShift;
                    g2d.drawString(" Tên khách hàng:                   " + invoice_CosName, 12, y);
                    y += yShift;
                    g2d.drawString("------------------------------------------------------------------", 12, y);
                    y += headerRectHeight;

                    g2d.drawString(" SL       |       Giá Thành       |       Tên sản phẩm", 10, y);
                    y += yShift;
                    g2d.drawString("-------------------------------------------------------------------", 10, y);
                    y += headerRectHeight;

                    for (int s = 0; s < r; s++) {
                        g2d.drawString("   " + product_quantity.get(s) + "                " + product_price.get(s) + "                 " + product_invoice.get(s), 7, y);
                        y += yShift;
                    }

                    g2d.drawString("------------------------------------------------------------------", 10, y);
                    y += yShift;
                    g2d.drawString(" Tổng tiền:                               " + format.format(Double.parseDouble(invoice_total)) + " VNĐ", 10, y);
                    y += yShift;
                    g2d.drawString("------------------------------------------------------------------", 10, y);
                    y += yShift;

                    g2d.drawString("*******************************************************", 10, y);
                    y += yShift;
                    g2d.drawString("                THANK YOU ,COME AGAIN                 ", 10, y);
                    y += yShift;
                    g2d.drawString("*******************************************************", 10, y);
                    y += yShift;
                    g2d.drawString("               DESIGN & DEV BY GROUP 01                ", 10, y);
                    y += yShift;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                result = PAGE_EXISTS;
            }
            return result;
        }
    }

    private void fillToTableDSHoaDon_BangHang() {
        try {
            model_tbl_DSHoaDon = (DefaultTableModel) tbl_DSHoaDon_BanHang.getModel();
            model_tbl_DSHoaDon.setRowCount(0);

            List<Object[]> list = hdDAO.getHoaDon();
            for (Object[] row : list) {
                Date date = (Date) row[3];
                model_tbl_DSHoaDon.addRow(new Object[]{
                    row[0], row[1], row[2], XDate.toString(date, "dd/MM/yyyy"), String.format("%.1f", row[4])
                });
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi fill Danh sách hóa đơn");
            e.printStackTrace();
        }
    }

    private void fillToTableDSHoaDonChiTiet_BanHang() {
        model_tbl_DSHoaDonChiTiet = (DefaultTableModel) tbl_DSHoaDonChiTiet_BanHang.getModel();
        model_tbl_DSHoaDonChiTiet.setRowCount(0);

        try {
            String mahd = (String) tbl_DSHoaDon_BanHang.getValueAt(tbl_DSHoaDon_BanHang.getSelectedRow(), 0);
            List<Object[]> list = hdctDAO.getHoaDonChiTiet(mahd);

            for (Object[] row : list) {
                model_tbl_DSHoaDonChiTiet.addRow(new Object[]{
                    row[0], row[1], row[2], row[3], String.format("%.1f", row[4])
                });
            }

            tbl_DSHoaDonChiTiet_BanHang.setRowSelectionInterval(0, 0);
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi fill Danh sách hóa đơn chi tiết");
            e.printStackTrace();
        }
    }

//    Điều hướng hệ thống
    public void first_DSHoaDon_BanHang() {
        this.row_DS_HoaDon_banhang = 0;
        this.updateStatus_DSHoaDon_BanHang();
    }

    public void prev_DSHoaDon_BanHang() {
        if (this.row_DS_HoaDon_banhang > 0) {
            this.row_DS_HoaDon_banhang--;
            this.updateStatus_DSHoaDon_BanHang();
        }
    }

    public void next_DSHoaDon_BanHang() {
        if (this.row_DS_HoaDon_banhang < tbl_DSHoaDon_BanHang.getRowCount() - 1) {
            this.row_DS_HoaDon_banhang++;
            this.updateStatus_DSHoaDon_BanHang();
        }
    }

    public void last_DSHoaDon_BanHang() {
        this.row_DS_HoaDon_banhang = tbl_DSHoaDon_BanHang.getRowCount() - 1;
        this.updateStatus_DSHoaDon_BanHang();
    }

    public void updateStatus_DSHoaDon_BanHang() {
        boolean edit = (this.row_DS_HoaDon_banhang >= 0);
        boolean first = (this.row_DS_HoaDon_banhang == 0);
        boolean last = (this.row_DS_HoaDon_banhang == tbl_DSHoaDon_BanHang.getRowCount() - 1);

// Trạng thái điều hướng
        btn_First_DSHoaDon_BanHang.setEnabled(edit && !first);
        btn_prev_DSHoaDon_BanHang.setEnabled(edit && !first);
        btn_next_DSHoaDon_BanHang.setEnabled(edit && !last);
        btn_last_DSHoaDon_BanHang.setEnabled(edit && !last);
    }

//    DS hóa đơn 
    private void delete_DSHoaDon_BanHang() {
        try {

            String maHD = (String) tbl_DSHoaDon_BanHang.getValueAt(tbl_DSHoaDon_BanHang.getSelectedRow(), 0);
            hdDAO.delete(maHD);

            this.fillToTableDSHoaDon_BangHang();
            model_tbl_DSHoaDonChiTiet.setRowCount(0);
            MsgBox.alert(this, "Xóa thành công");
        } catch (Exception e) {
            System.out.println("Xóa thất bại");
            e.printStackTrace();
        }
    }

//                        END_CARD_BANHANG
//                        END_CARD_BANHANG
//                        START_CARD_HETHONG
//                        END_CARD_HETHONG
//   ********************** HOAI NAM**********************
    //START_CARD_NHANVIEN
    JFileChooser filenChooser = new JFileChooser();
    NhanVienDAO nhanVienDAO = new NhanVienDAO();
    DefaultTableModel tableModelNhanVien;
    boolean isNew_NhanVien = false;

    private void initCardNhanVien() {
        fillToTable_NhanVien_NhanVien();

        TableEdit.centerRendererTable(tbl_DSNhanVien_NhanVien);
    }

    private void fillToTable_NhanVien_NhanVien() {
        tableModelNhanVien = (DefaultTableModel) tbl_DSNhanVien_NhanVien.getModel();
        tableModelNhanVien.setRowCount(0);

        List<NhanVien> listNhanVien = nhanVienDAO.selectAll();

        listNhanVien.forEach((nhanVien) -> {
            String ngaySinh = XDate.toString(nhanVien.getNgaySinh(), "dd/MM/yyyy");

            String gioiTinh = "Nam";
            if (!nhanVien.getGioiTinh()) {
                gioiTinh = "Nữ";
            }
            String trangThai = "Đang làm";

            if (!nhanVien.getTrangThai()) {
                trangThai = "Nghỉ việc";
            }
            Object[] obj = {nhanVien.getMaNhanVien(), nhanVien.getTenNhanVien(), ngaySinh, gioiTinh, nhanVien.getSDT(),
                nhanVien.getEmail(), nhanVien.getDiaChi(), nhanVien.getCCCD(), nhanVien.getHinhAnh(), trangThai, nhanVien.getGhiChu()};

            tableModelNhanVien.addRow(obj);

        });

    }

    private void setFormNhanVien() {
        int selectedRow_tbl_DSNhanVien_NhanVien = tbl_DSNhanVien_NhanVien.getSelectedRow();
        if (selectedRow_tbl_DSNhanVien_NhanVien == -1) {
            return;
        }
        String maNhanVien = (String) tbl_DSNhanVien_NhanVien.getValueAt(selectedRow_tbl_DSNhanVien_NhanVien, 0);
        String tenNhanVien = (String) tbl_DSNhanVien_NhanVien.getValueAt(selectedRow_tbl_DSNhanVien_NhanVien, 1);
        String ngaySinh = (String) tbl_DSNhanVien_NhanVien.getValueAt(selectedRow_tbl_DSNhanVien_NhanVien, 2);
        String gioiTinh = (String) tbl_DSNhanVien_NhanVien.getValueAt(selectedRow_tbl_DSNhanVien_NhanVien, 3);
        String soDienThoai = (String) tbl_DSNhanVien_NhanVien.getValueAt(selectedRow_tbl_DSNhanVien_NhanVien, 4);
        String email = (String) tbl_DSNhanVien_NhanVien.getValueAt(selectedRow_tbl_DSNhanVien_NhanVien, 5);
        String diaChi = (String) tbl_DSNhanVien_NhanVien.getValueAt(selectedRow_tbl_DSNhanVien_NhanVien, 6);
        String CCCD = (String) tbl_DSNhanVien_NhanVien.getValueAt(selectedRow_tbl_DSNhanVien_NhanVien, 7);
        String hinhAnh = (String) tbl_DSNhanVien_NhanVien.getValueAt(selectedRow_tbl_DSNhanVien_NhanVien, 8);
        String trangThai = (String) tbl_DSNhanVien_NhanVien.getValueAt(selectedRow_tbl_DSNhanVien_NhanVien, 9);
        String ghiChu = (String) tbl_DSNhanVien_NhanVien.getValueAt(selectedRow_tbl_DSNhanVien_NhanVien, 10);

//        set du lieu len form
        txt_MaNhanVien_NhanVien.setText(maNhanVien);
        txt_TenNhanVien_NhanVien.setText(tenNhanVien);
        dc_NgaySinh_NhanVien.setDate(XDate.toDate(ngaySinh, "dd/MM/yyyy"));
        if (gioiTinh.equals("Nam")) {
            rdo_Nam_NhanVien.setSelected(true);
        } else {
            rdo_Nu_NhanVien.setSelected(true);
        }
        txt_SoDienThoai_NhanVien.setText(soDienThoai);
        txt_Email_NhanVien.setText(email);
        txt_DiaChi_NhanVien.setText(diaChi);
        txt_CCCD_NhanVien.setText(CCCD);
//        hinhAnh
        this.docAnh(hinhAnh, lbl_Anh_NhanVien);
        lbl_Anh_NhanVien.setToolTipText(hinhAnh);

        if (trangThai.equals("Đang làm")) {
            rdo_DangLam_NhanVien.setSelected(true);
        } else {
            rdo_NghiViec_NhanVien.setSelected(true);
        }

        txt_GhiChu_NhanVien.setText(ghiChu);
    }

    private void chonAnh(JLabel lbl) {
        if (filenChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = filenChooser.getSelectedFile();
            XImage.save(file);

            ImageIcon iconTam = new ImageIcon(file.getAbsolutePath());
            Image img = iconTam.getImage();
            ImageIcon icon = new ImageIcon(img.getScaledInstance(lbl.getWidth(), lbl.getHeight(), Image.SCALE_SMOOTH));

            lbl.setIcon(icon);
            lbl.setToolTipText(file.getName());
        }
    }

    void docAnh(String path, JLabel lbl) {
        ImageIcon imageIcon = XImage.read(path);
        Image image = imageIcon.getImage();
        ImageIcon icon1 = new ImageIcon(image.getScaledInstance(lbl.getWidth(), lbl.getHeight(), Image.SCALE_SMOOTH));
        lbl.setIcon(icon1);
        lbl.setToolTipText(path);
    }

    private NhanVien getFormNhanVien() {
        NhanVien nhanVien = new NhanVien();

        String maNhanVien = txt_MaNhanVien_NhanVien.getText();
        String tenNhanVien = txt_TenNhanVien_NhanVien.getText();
        Date ngaySinh = dc_NgaySinh_NhanVien.getDate();
        String soDienThoai = txt_SoDienThoai_NhanVien.getText();
        String email = txt_Email_NhanVien.getText();
        String diaChi = txt_DiaChi_NhanVien.getText();
        String CCCD = txt_CCCD_NhanVien.getText();
        String hinhAnh = lbl_Anh_NhanVien.getToolTipText();
        String ghiChu = txt_GhiChu_NhanVien.getText();

        nhanVien.setMaNhanVien(maNhanVien);
        nhanVien.setTenNhanVien(tenNhanVien);
        nhanVien.setGioiTinh(rdo_Nam_NhanVien.isSelected());
        nhanVien.setNgaySinh(ngaySinh);
        nhanVien.setSDT(soDienThoai);
        nhanVien.setEmail(email);
        nhanVien.setDiaChi(diaChi);
        nhanVien.setCCCD(CCCD);
        nhanVien.setHinhAnh(hinhAnh);
        nhanVien.setTrangThai(rdo_DangLam_NhanVien.isSelected());
        nhanVien.setGhiChu(ghiChu);
        return nhanVien;
    }

    private void addNhanVien() {
        NhanVien nv = this.getFormNhanVien();
//        if (isNotNullInTextfiled(txt_TenNhanVien_NhanVien, txt_SoDienThoai_NhanVien, txt_Email_NhanVien, txt_CCCD_NhanVien)) {
        this.isNullAddress(txt_DiaChi_NhanVien.getText());
        if (!this.isTrueNumPhone(txt_SoDienThoai_NhanVien.getText()) || this.isNullAddress(txt_DiaChi_NhanVien.getText()) || this.isTrueNameNhanVien(txt_TenNhanVien_NhanVien.getText())) {
            addNhanVien();
//            }
        }

        nhanVienDAO.insert(nv);
        this.fillToTable_NhanVien_NhanVien();
        this.FillTable_tbl_DSChuaTaiKhoan_TaiKhoan();
        String[] opts = {"Có, tiếp tục tạo Tài Khoản", "Không, để sau"};
        int option = MsgBox.options(this, "Thêm thông tim Nhân Viên thành công !", opts);
        if (option == 0) {
            onClick(pnl_TaiKhoan);
            onLeaveClick(pnl_KhachHang);
            onLeaveClick(pnl_ThongKe);
            onLeaveClick(pnl_SanPham);
            onLeaveClick(pnl_BanHang);
            onLeaveClick(pnl_DiemDanh);
            onLeaveClick(pnl_HeThong);
            onLeaveClick(pnl_NhanVien);
            onLeaveClick(pnl_Luong);
            onLeaveClick(pnl_KhuyenMai);

            onClickLabel(lbl_TaiKhoan);
            onLeaveClickLabel(lbl_KhachHang);
            onLeaveClickLabel(lbl_ThongKe);
            onLeaveClickLabel(lbl_SanPham);
            onLeaveClickLabel(lbl_BanHang);
            onLeaveClickLabel(lbl_DiemDanh);
            onLeaveClickLabel(lbl_HeThong);
            onLeaveClickLabel(lbl_NhanVien);
            onLeaveClickLabel(lbl_Luong);
            onLeaveClickLabel(lbl_KhuyenMai);

            //        indicators
            indicator1.setOpaque(false);
            indicator2.setOpaque(false);
            indicator3.setOpaque(false);
            indicator4.setOpaque(false);
            indicator6.setOpaque(false);
            indicator7.setOpaque(false);
            indicator8.setOpaque(true);
            indicator9.setOpaque(false);
            indicator10.setOpaque(false);
            indicator11.setOpaque(false);

            //        Card playout
            CardLayout playout = (CardLayout) pnl_MainDisplayCard.getLayout();
            playout.show(pnl_MainDisplayCard, "card_TaiKhoan");

            this.FillTable_tbl_DSChuaTaiKhoan_TaiKhoan();
            txt_MaNV_TaiKhoan.setText(txt_MaNhanVien_NhanVien.getText());
            txt_TenDangNhap_TaiKhoan.requestFocus();
        }
    }

    private void updateNhanVien() {
        NhanVien nv = this.getFormNhanVien();
        if (nvDAO.isNumPhoneDup(nv.getSDT())) {
            MsgBox.alert(this, "Số điện thoại đã tồn tại !");
            txt_SoDienThoai_NhanVien.requestFocus();
            return;
        }
        try {
            nhanVienDAO.update(nv);
            MsgBox.alert(this, "Cập nhật thông tin Nhân Viên thành công !");
            this.fillToTable_NhanVien_NhanVien();

        } catch (Exception e) {
            MsgBox.alert(this, "Cập nhật thông tin Nhân Viên thất bại !");
            e.printStackTrace();
        }

    }

    private void clearFormNhanVien() {
        txt_MaNhanVien_NhanVien.setText(GenerateID.genareteID(nhanVienDAO.getID_NhanVien()));
        txt_TenNhanVien_NhanVien.setText("");
        txt_TenNhanVien_NhanVien.requestFocus();
        Date tuoiDiLam;
        dc_NgaySinh_NhanVien.setDate(XDate.now());
        rdo_Nam_NhanVien.setSelected(true);
        txt_SoDienThoai_NhanVien.setText("");
        txt_Email_NhanVien.setText("");
        txt_DiaChi_NhanVien.setText("");
        txt_CCCD_NhanVien.setText("");
//        hinhAnh
        lbl_Anh_NhanVien.setText("Click vào để chọn ảnh");
        lbl_Anh_NhanVien.setToolTipText("Không có ảnh");
        rdo_DangLam_NhanVien.setSelected(true);
        txt_GhiChu_NhanVien.setText("");
        isNew_NhanVien = true;

    }

    public void filterOnTextfield(DefaultTableModel model, JTable tbl, String txt, int index) {

        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
        tbl.setRowSorter(sorter);
        if (txt.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter(txt, index));
        }
    }

    //NAVIGATION 
    int row_NhanVien = -1;

    private void next_NhanVien_NhanVien(int index) {
        if (tbl_DSNhanVien_NhanVien.getSelectedRow() != -1) {
            index = tbl_DSNhanVien_NhanVien.getSelectedRow();
            index++;
        }
        if (index == -1 || index == tbl_DSNhanVien_NhanVien.getRowCount()) {
            index = 0;
        }
        tbl_DSNhanVien_NhanVien.setRowSelectionInterval(index, index);
        this.setFormNhanVien();
    }

    private void prev_NhanVien_NhanVien(int index) {
        if (tbl_DSNhanVien_NhanVien.getSelectedRow() != -1) {
            index = tbl_DSNhanVien_NhanVien.getSelectedRow();
            index--;
        }
        if (index == tbl_DSNhanVien_NhanVien.getRowCount()) {
            index = 0;
        }
        if (index < 0) {
            index = tbl_DSNhanVien_NhanVien.getRowCount() - 1;
        }
        tbl_DSNhanVien_NhanVien.setRowSelectionInterval(index, index);
        this.setFormNhanVien();
    }

    private void first_NhanVien_NhanVien() {
        row_NhanVien = 0;
        this.next_NhanVien_NhanVien(row_NhanVien);
    }

    private void last_NhanVien_NhanVien() {
        row_NhanVien = tbl_DSNhanVien_NhanVien.getRowCount() - 1;
        tbl_DSNhanVien_NhanVien.setRowSelectionInterval(row_NhanVien, row_NhanVien);
        this.setFormNhanVien();
    }

    // CHECK PHONE NUMBER
    private boolean isTrueNumPhone(String sdt) {

        if (nvDAO.isNumPhoneDup(sdt)) {
            MsgBox.alert(this, "Số điện thoại đã tồn tại !");
            txt_SoDienThoai_NhanVien.requestFocus();
            return false;
        }

        if (!sdt.matches("^\\d{10}$") || !sdt.substring(0, 1).equals("0") || sdt.equals("")) {
            MsgBox.alert(this, "Số điện thoại không hợp lệ !");
            txt_SoDienThoai_NhanVien.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isNullAddress(String diachi) {
        if (diachi.equals("")) {
            MsgBox.alert(this, "Vui lòng nhập địa chỉ của nhân viên");
            return true;
        }
        return false;
    }

    private boolean checkEmail(String email) {

        if (!email.matches("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$") || email.equals("")) {
            MsgBox.alert(this, "Email không hợp lệ !");
            return false;
        }
        return true;
    }

    private boolean checkCCCD(String cccd) {
        if (cccd.length() != 12) {
            MsgBox.alert(this, "Số CCCD không hợp lệ ! ");
            return false;
        }
        return true;

    }

    private boolean isTrueNameNhanVien(String tenNhanVien) {
        if (tenNhanVien.equals("")) {
            MsgBox.alert(this, "Vui lòng nhập tên nhân viên !");
            return false;
        }

        return true;
    }
    //END_CARD_NHANVIEN
    //START_CARD_KHACHHANG
    DefaultTableModel tableModelKhachHang_KhachHang, tableModelKhachHang_KhachHang_DaXoa;
    KhachHangDAO khachHangDAO = new KhachHangDAO();
    KhachHang kh = new KhachHang();

//    private void initCardKhachHang() {    KhachHangDAO khachHangDAO = new KhachHangDAO();
//    KhachHang kh = new KhachHang();
//    }
    private void initCardKhachHang() {
        fillToTableKhachHang_KhachHang();

        TableEdit.centerRendererTable(tbl_DSKhachHang_KhachHang);
        TableEdit.centerRendererTable(tbl_DSKhachHangDaXoa_KhachHang);

    }

    private void fillToTableKhachHang_KhachHang() {
        tableModelKhachHang_KhachHang = (DefaultTableModel) tbl_DSKhachHang_KhachHang.getModel();
        tableModelKhachHang_KhachHang_DaXoa = (DefaultTableModel) tbl_DSKhachHangDaXoa_KhachHang.getModel();
        tableModelKhachHang_KhachHang.setRowCount(0);
        tableModelKhachHang_KhachHang_DaXoa.setRowCount(0);
        List<KhachHang> listKH = khachHangDAO.selectAll();

        listKH.forEach((o) -> {
            boolean gioiTinh = false;
            if (o.isGioiTinh()) {
                gioiTinh = true;
            }
            if (o.isTrangThai()) {
                Object[] rows = {o.getMaKhachHang(), o.getTenKhachHang(), gioiTinh == true ? "Nam" : "Nữ", o.getSDT(), o.getGhiChu()};
                tableModelKhachHang_KhachHang.addRow(rows);
            } else {
                Object[] rows = {o.getMaKhachHang(), o.getTenKhachHang(), gioiTinh == true ? "Nam" : "Nữ", o.getSDT(), o.getGhiChu()};
                tableModelKhachHang_KhachHang_DaXoa.addRow(rows);
            }

        });

    }

    private void setFormKhachHang() {
        int selectedRow_tbl_DSKhachHang = tbl_DSKhachHang_KhachHang.getSelectedRow();
        if (selectedRow_tbl_DSKhachHang == -1) {
            return;
        }
        String maKH = tbl_DSKhachHang_KhachHang.getValueAt(selectedRow_tbl_DSKhachHang, 0).toString();
        String tenKH = tbl_DSKhachHang_KhachHang.getValueAt(selectedRow_tbl_DSKhachHang, 1).toString();
        String gioiTinh = tbl_DSKhachHang_KhachHang.getValueAt(selectedRow_tbl_DSKhachHang, 2).toString();
        String soDienThoai = tbl_DSKhachHang_KhachHang.getValueAt(selectedRow_tbl_DSKhachHang, 3).toString();
        String ghiChu = tbl_DSKhachHang_KhachHang.getValueAt(selectedRow_tbl_DSKhachHang, 4).toString();

        txt_MaKhachHang_KhachHang.setText(maKH);
        txt_TenKhachHang_KhachHang.setText(tenKH);
        rdo_Nam_KhachHang.setSelected(gioiTinh.equals("Nam"));
        rdo_Nu_KhachHang.setSelected(gioiTinh.equals("Nữ"));
        txt_SoDienThoaiKhachHang_KhachHang.setText(soDienThoai);
        txt_GhiChuKhachHang_KhachHang.setText(ghiChu);

    }

    private KhachHang getFormKhachHang() {
        KhachHang kh = new KhachHang();

        String maKH = txt_MaKhachHang_KhachHang.getText();
        String tenKH = txt_TenKhachHang_KhachHang.getText();
        boolean gioiTinh = rdo_Nam_KhachHang.isSelected();
        String soDienThoai = txt_SoDienThoaiKhachHang_KhachHang.getText();
        String ghiChu = txt_GhiChuKhachHang_KhachHang.getText();

        kh.setMaKhachHang(maKH);
        kh.setTenKhachHang(tenKH);
        kh.setGioiTinh(gioiTinh);
        kh.setSDT(soDienThoai);
        kh.setTrangThai(true);
        kh.setGhiChu(ghiChu);

        return kh;
    }

    private void themKhachHang_KhachHang() {
        KhachHang kh = this.getFormKhachHang();
        khachHangDAO.insert(kh);
        MsgBox.alert(this, "Thêm thông tin Khách Hàng thành công !");
        this.fillToTableKhachHang_KhachHang();
        this.lamMoiFormKhachHang();
    }

    private void xoaKhachHang_KhachHang() {
        KhachHang kh = this.getFormKhachHang();
        kh.setTrangThai(false);
        khachHangDAO.update(kh);
        MsgBox.alert(this, "Xóa Khach Hàng thanh công !");
        tbp_KhachHang.setSelectedIndex(1);
        this.fillToTableKhachHang_KhachHang();
    }

    private void khoiPhucKhachHang_KhachHang() {
        int indexSelectedRow = tbl_DSKhachHangDaXoa_KhachHang.getSelectedRow();
        String maKhachHang = tbl_DSKhachHangDaXoa_KhachHang.getValueAt(indexSelectedRow, 0).toString();
        khachHangDAO.restore(maKhachHang);
        MsgBox.alert(this, "Khôi phục Khách Hàng thành công !");
        tbp_KhachHang.setSelectedIndex(0);
        this.fillToTableKhachHang_KhachHang();
    }

    private void capNhatKhachHang() {
        KhachHang kh = this.getFormKhachHang();
        khachHangDAO.update(kh);
        MsgBox.alert(this, "Cập nhật thông tin Khách Hàng thành công !");
        this.fillToTableKhachHang_KhachHang();
    }

    private void lamMoiFormKhachHang() {
        txt_MaKhachHang_KhachHang.setText(GenerateID.genareteID(khachHangDAO.getID_KhachHang()));
        txt_TenKhachHang_KhachHang.setText("");
        rdo_Nam_KhachHang.setSelected(true);
        txt_SoDienThoaiKhachHang_KhachHang.setText("");
        txt_GhiChuKhachHang_KhachHang.setText("");

    }

    private void timSoDienThoaiKhachHang(KeyEvent evt) {
        String txt = txt_TimSoDienThoaiKhachHang_KhachHang.getText();

        if (txt.length() > 10) {
            txt_TimSoDienThoaiKhachHang_KhachHang.setText(txt.substring(0, 10));
            return;
        }

        if (txt.length() == 10) {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                if (tbl_DSKhachHang_KhachHang.getRowCount() == 0) {
                    txt_SoDienThoaiKhachHang_KhachHang.setText(txt_TimSoDienThoaiKhachHang_KhachHang.getText());
                    txt_TenKhachHang_KhachHang.setText("");
                    txt_TenKhachHang_KhachHang.requestFocus();
                    txt_MaKhachHang_KhachHang.setText(GenerateID.genareteID(khachHangDAO.getID_KhachHang()));
                    rdo_Nam_KhachHang.setSelected(true);
                    txt_TimSoDienThoaiKhachHang_KhachHang.setText("");
                }
                return;
            }
        }
        filterOnTextfield(tableModelKhachHang_KhachHang, tbl_DSKhachHang_KhachHang, txt, 3);
    }

    private void fillFormHoaDon() {
        tenKH_KhachHang = tbl_DSKhachHang_KhachHang.getValueAt(tbl_DSKhachHang_KhachHang.getSelectedRow(), 1).toString();
        lbl_TenKhachHang_BanHang.setText(tenKH_KhachHang);
        lbl_NguoiTao_BanHang.setText(lbl_TenNhanVien.getText());
        lbl_MaHoaDon_BanHang.setText(GenerateID.genareteID(hdDAO.getID_HoaDon()));
        lbl_NgayTao_BanHang.setText(XDate.toString(XDate.now(), "dd/MM/yyyy"));

        onClick(pnl_BanHang);
        onLeaveClick(pnl_KhachHang);
        onLeaveClick(pnl_ThongKe);
        onLeaveClick(pnl_SanPham);
        onLeaveClick(pnl_HeThong);
        onLeaveClick(pnl_DiemDanh);
        onLeaveClick(pnl_TaiKhoan);
        onLeaveClick(pnl_NhanVien);
        onLeaveClick(pnl_Luong);
        onLeaveClick(pnl_KhuyenMai);

        onClickLabel(lbl_BanHang);
        onLeaveClickLabel(lbl_KhachHang);
        onLeaveClickLabel(lbl_ThongKe);
        onLeaveClickLabel(lbl_SanPham);
        onLeaveClickLabel(lbl_HeThong);
        onLeaveClickLabel(lbl_DiemDanh);
        onLeaveClickLabel(lbl_TaiKhoan);
        onLeaveClickLabel(lbl_NhanVien);
        onLeaveClickLabel(lbl_Luong);
        onLeaveClickLabel(lbl_KhuyenMai);

        //        indicators
        indicator1.setOpaque(false);
        indicator2.setOpaque(false);
        indicator3.setOpaque(false);
        indicator4.setOpaque(true);
        indicator6.setOpaque(false);
        indicator7.setOpaque(false);
        indicator8.setOpaque(false);
        indicator9.setOpaque(false);
        indicator10.setOpaque(false);
        indicator11.setOpaque(false);

        //        Card playout
        CardLayout playout = (CardLayout) pnl_MainDisplayCard.getLayout();
        playout.show(pnl_MainDisplayCard, "card_BanHang");
    }

    private boolean isNotNullInTextfiled(JTextField... agrs) {
        for (int i = 0; i < agrs.length; i++) {
            if (agrs[i].getText().equals("")) {
                agrs[i].setBorder(new LineBorder(Color.RED));
                agrs[i].requestFocus();
                return false;
            } else if (!agrs[i].getText().equals("")) {
                agrs[i].setBorder(new LineBorder(Color.BLACK));
            }
        }
        return true;
    }
    int row_KhachHang = -1;

    private void next_KhachHang_KhachHang(int index) {
        if (tbl_DSNhanVien_NhanVien.getSelectedRow() != -1) {
            index = tbl_DSNhanVien_NhanVien.getSelectedRow();
            index++;
        }
        if (index == -1 || index == tbl_DSNhanVien_NhanVien.getRowCount()) {
            index = 0;
        }
        tbl_DSNhanVien_NhanVien.setRowSelectionInterval(index, index);
        this.setFormKhachHang();
    }

    private void prev_KhachHang_KhachHang(int index) {
        if (tbl_DSNhanVien_NhanVien.getSelectedRow() != -1) {
            index = tbl_DSNhanVien_NhanVien.getSelectedRow();
            index--;
        }
        if (index == tbl_DSNhanVien_NhanVien.getRowCount()) {
            index = 0;
        }
        if (index < 0) {
            index = tbl_DSNhanVien_NhanVien.getRowCount() - 1;
        }
        tbl_DSNhanVien_NhanVien.setRowSelectionInterval(index, index);
        this.setFormKhachHang();
    }

    private void first_KhachHang_KhachHang() {
        row_NhanVien = 0;
        this.next_KhachHang_KhachHang(row_NhanVien);
    }

    private void last_KhachHang_KhachHang() {
        row_NhanVien = tbl_DSNhanVien_NhanVien.getRowCount() - 1;
        tbl_DSNhanVien_NhanVien.setRowSelectionInterval(row_NhanVien, row_NhanVien);
        this.setFormKhachHang();
    }
    //END_CARD_KHACHHANG
    //START_CARD_LUONG     
    private DefaultTableModel tableModelLuong;

    private void initCardLuong() {
        this.fillCombobox_MaNhanVien_Luong();
        this.fillTableLuong_Luong();

        TableEdit.centerRendererTable(tbl_DSLuong_Luong);
    }

    private void fillCombobox_MaNhanVien_Luong() {
        DefaultComboBoxModel cbxModel_MaNhanVien_Lương = (DefaultComboBoxModel) cbo_MaNhanVien_Luong.getModel();
        cbxModel_MaNhanVien_Lương.removeAllElements();
        cbxModel_MaNhanVien_Lương.addElement("Chọn mã nhân viên");
        List<String> listMaNhanVien = ddDao.getMaNhanVien();
        for (String maNhanVien : listMaNhanVien) {
            cbxModel_MaNhanVien_Lương.addElement(maNhanVien);
        }
        cbxModel_MaNhanVien_Lương.setSelectedItem("Chọn mã nhân viên");
    }

    private void fillTableLuong_Luong() {
        tableModelLuong = (DefaultTableModel) tbl_DSLuong_Luong.getModel();

        tableModelLuong.setRowCount(0);
        List<Object[]> list = luongDAO.getAllLuong();

        for (Object[] o : list) {
            Date ngayNhanDate = (Date) o[7];
            String ngayNhanSt = "";
            if (ngayNhanDate != null) {
                ngayNhanSt = XDate.toString(ngayNhanDate, "dd/MM/yyyy");
            }

            tableModelLuong.addRow(new Object[]{o[0], o[1], o[2], o[3], o[4], o[5], o[6], ngayNhanSt, o[8], o[9]});
        }
    }

    private void fillToFormLuongByID(String id) {
        String tenNhanVien = "";
        boolean chucVu = false;
        List<Object[]> listLuongTheoNhanVien = null;
        try {
            listLuongTheoNhanVien = luongDAO.getLuong(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Object[] o : listLuongTheoNhanVien) {
            tenNhanVien = (String) o[0];
            chucVu = (boolean) o[1];
            tongCa = (int) o[2];
        }

        lbl_TenNhanVien_Luong.setText(tenNhanVien);
        lbl_ChucVu_Luong.setText((chucVu == true) ? "Quản lý" : "Nhân viên");
        lbl_TongCaLam_Luong.setText(String.valueOf(tongCa));
        txt_Luong_Luong.setText((chucVu == true) ? "200000" : "100000");
        txt_TienThuong_Luong.setText("0");
        luongTrenCa = Double.parseDouble(txt_Luong_Luong.getText());
        tienThuong = Double.parseDouble(txt_TienThuong_Luong.getText());
        lbl_NgayNhan_Luong.setText("Chưa nhận");

        tinhLuongOnTextfiled(txt_Luong_Luong, tongCa, txt_TienThuong_Luong);
    }

    private void tinhLuongOnTextfiled(JTextField txt_Luong_Luong, int tongCa, JTextField txt_TienThuong_Luong) {
        if (txt_Luong_Luong.getText().length() == 0 || txt_TienThuong_Luong.getText().length() == 0) {
            return;
        }
//            if (!txt_Luong_Luong.getText().matches("[0-9]{0,10}") || !txt_TienThuong_Luong.getText().matches("[0-9]{0,10}")) {
        if (txt_Luong_Luong.getText().matches("\\D{0,20}") || txt_TienThuong_Luong.getText().matches("\\D{0,10}")) {
            return;
        }

        if (txt_Luong_Luong.getText().contains("-") || txt_TienThuong_Luong.getText().contains("-")) {
            return;

        }
        try {

            luongTrenCa = Double.parseDouble(txt_Luong_Luong.getText());
            tienThuong = Double.parseDouble(txt_TienThuong_Luong.getText());
        } catch (Exception e) {
            MsgBox.alert(this, "Vui lòng nhập đúng định dạng");
            e.printStackTrace();
            return;
        }
        tongLuong = (luongTrenCa * tongCa) + tienThuong;
        if (tongLuong >= 0) {
            lbl_TongLuong_Luong.setText(String.valueOf(tongLuong));
        }
    }

    private void tinhLuong_Luong() {
        Luong luong = this.getFormLuong();
        try {
            luongDAO.insert(luong);
            MsgBox.alert(this, "Tính lương thành công ! ");
            this.fillTableLuong_Luong();
        } catch (Exception e) {
            MsgBox.alert(this, "Tính lương thất bại ! ");
            e.printStackTrace();
        }

    }

    private void capNhatLuong_Luong() {
        Luong luong = this.getFormLuong();
        if (isNhanLuong) {
            luong.setNgayNhan(XDate.now());
        }
        luong.setTrangThai("Đã nhận");
        String maLuong = tbl_DSLuong_Luong.getValueAt(tbl_DSLuong_Luong.getSelectedRow(), 0).toString();
        luong.setMaLuong(maLuong);
        try {
            luongDAO.update(luong);
            if (isNhanLuong) {
                MsgBox.alert(this, "Nhận lương thành công ! ");
            } else {
                MsgBox.alert(this, "Cập nhật lương thành công ! ");
            }
            this.fillTableLuong_Luong();
        } catch (Exception e) {
            if (isNhanLuong) {
                MsgBox.alert(this, "Nhận lương thất bại ! ");
            } else {
                MsgBox.alert(this, "Cập nhật lương thất bại ! ");
            }
            e.printStackTrace();
        }

    }

    private Luong getFormLuong() {
        Luong luong = new Luong();
        String ngayNhan = lbl_NgayNhan_Luong.getText();
        if (ngayNhan.equalsIgnoreCase("Chưa nhận")) {
            ngayNhan = "";
        }
        luong.setMaNhanVien(cbo_MaNhanVien_Luong.getSelectedItem().toString());
        luong.setLuongTrenCa(Float.parseFloat(txt_Luong_Luong.getText()));
        luong.setTienThuong(Float.parseFloat(txt_TienThuong_Luong.getText()));
        luong.setTongCaLam(Float.parseFloat(lbl_TongCaLam_Luong.getText()));
        luong.setNgayNhan(null);
        luong.setTrangThai("Chưa nhận");
        luong.setGhiChu(txt_GhiChu_Luong.getText());

        return luong;
    }

    private void setFormLuong() {
        int index = tbl_DSLuong_Luong.getSelectedRow();

        String maNhanVien = tbl_DSLuong_Luong.getValueAt(index, 1).toString();
        String tenNhanVien = tbl_DSLuong_Luong.getValueAt(index, 2).toString();
        String tongCaLam = tbl_DSLuong_Luong.getValueAt(index, 3).toString();
        String luongTrenCa = tbl_DSLuong_Luong.getValueAt(index, 4).toString();
        String tienThuong = tbl_DSLuong_Luong.getValueAt(index, 5).toString();
        String tongLuong = tbl_DSLuong_Luong.getValueAt(index, 6).toString();
        String ngayNhan = tbl_DSLuong_Luong.getValueAt(index, 7).toString();
        String trangThai = tbl_DSLuong_Luong.getValueAt(index, 8).toString();
        String ghiChu = tbl_DSLuong_Luong.getValueAt(index, 9).toString();
        boolean chucVu = tkDAO.selectByid(maNhanVien).getQuyen();

        cbo_MaNhanVien_Luong.setSelectedItem(maNhanVien);
        lbl_TenNhanVien_Luong.setText(tenNhanVien);
        lbl_ChucVu_Luong.setText(chucVu == true ? "Quản lý" : "Nhân viên");
        lbl_TongCaLam_Luong.setText(tongCaLam);
        txt_Luong_Luong.setText(luongTrenCa);
        txt_TienThuong_Luong.setText(tienThuong);
        lbl_TongLuong_Luong.setText(tongLuong);
        lbl_NgayNhan_Luong.setText(ngayNhan.equals("") || ngayNhan.equals(null) ? "Chưa nhận" : ngayNhan);

        txt_GhiChu_Luong.setText(ghiChu);

    }

    private void locNgayNhanLuong(PropertyChangeEvent evt) {
        if ("calendar".equals(evt.getPropertyName())
                || "date".equals(evt.getPropertyName())) {
            int index_NgayNhan = 7;
            String date = XDate.toString(dcs_LocTheoNgayNhan_Luong.getDate(), "dd/MM/yyyy");
            this.filterOnTextfield(tableModelLuong, tbl_DSLuong_Luong, date, index_NgayNhan);
        }
    }
    //END_CARD_LUONG     

    //START_CARD_THONGKE
    // DECLARE VARIABLE 
    ThongKeDAO thongKeDAO = new ThongKeDAO();
    DefaultTableModel tableModelThongKe;

    private void initThongKe() {
        this.fillLable_DoanhThu_Ngay();
        this.fillLable_DoanhThu_Thang();
        this.fillLable_DoanhThu_Nam();
        this.fillChart_TheoNgay();
//        this.fillChart_Tong();
        this.fillTableThongKe();
        this.fillComBoBox_HangSanXuat_ThongKe();
    }

    private void fillLable_DoanhThu_Ngay() {
        int soLuong = 0;
        float tongTien = 0;
        String ngayHIenTai = XDate.toString(XDate.now(), "dd/MM/yyyy").substring(0, 2);
        lbl_Ngay_DoanhThu.setText("Ngày: " + ngayHIenTai);
        List<Object[]> list = thongKeDAO.getDoanhThuTheoNgayHienTai(XDate.now());
        for (Object[] o : list) {
            if (o[0] != null) {
                tongTien = Float.parseFloat(o[0].toString());
            }
            if (o[1] != null) {
                soLuong = Integer.parseInt(o[1].toString());
            }
            lbl_TongTien_NgayHienTai_DoanhThu.setText("Tổng tiền : " + tongTien);
            lbl_SoLuong_NgayHienTai_DoanhThu.setText("Số lượng sản phẩm bán được: " + soLuong);

        }
    }

    private void fillLable_DoanhThu_Thang() {
        int soLuong = 0;
        float tongTien = 0;
        String thangHIenTai = XDate.toString(XDate.now(), "dd/MM/yyyy").substring(3, 5);
        lbl_Thang_DoanhThu.setText("Tháng: " + thangHIenTai);
        List<Object[]> list = thongKeDAO.getDoanhThuTheoThangHienTai(XDate.now());
        for (Object[] o : list) {
            if (o[0] != null) {
                tongTien = Float.parseFloat(o[0].toString());
            }
            if (o[1] != null) {
                soLuong = Integer.parseInt(o[1].toString());
            }
            lbl_TongTien_ThangHienTai_DoanhThu.setText("Tổng tiền : " + tongTien);
            lbl_SoLuong_ThangHienTai_DoanhThu.setText("Số lượng sản phẩm bán được: " + soLuong);

        }
    }

    private void fillLable_DoanhThu_Nam() {
        int soLuong = 0;
        float tongTien = 0;
        String namHienTai = XDate.toString(XDate.now(), "dd/MM/yyyy").substring(6, XDate.toString(XDate.now(), "dd/MM/yyyy").length());
        lbl_Nam_DoanhThu.setText("Năm: " + namHienTai);
        List<Object[]> list = thongKeDAO.getDoanhThuTheoNamHienTai(XDate.now());
        for (Object[] o : list) {
            if (o[0] != null) {
                tongTien = Float.parseFloat(o[0].toString());
            }
            if (o[1] != null) {
                soLuong = Integer.parseInt(o[1].toString());
            }
            lbl_TongTien_NamHienTai_DoanhThu.setText("Tổng tiền : " + tongTien);
            lbl_SoLuong_NamHienTai_DoanhThu.setText("Số lượng sản phẩm bán được: " + soLuong);
        }
    }

    private void fillChart_TheoNgay() {
        pieChart_TheoNgay.clearData();
        List<Object[]> list = thongKeDAO.getTongDoanhThuTheoNgay(dcs_NgayBatDau_ThongKe_DoanhThu.getDate(), dcs_NgayKetThuc_ThongKe_DoanhThu.getDate());
        for (Object[] o : list) {
            if (o[0].equals("null") && o[1].equals("null")) {
                MsgBox.alert(this, "Không tìm thấy thông tin thống kê");
                return;
            }
            pieChart_TheoNgay.addData(new ModelPieChart(o[0].toString(), Double.parseDouble(o[1].toString()), getRandomColor()));
        }
    }

    private void fillChart_Tong() {
        pieChart_Tong.clearData();
        List<Object[]> list = thongKeDAO.getTongDoanhThu();
        for (Object[] o : list) {
            if (o[0].equals("null") && o[1].equals("null")) {
                return;
            }
            pieChart_Tong.addData(new ModelPieChart(o[0].toString(), Double.parseDouble(o[1].toString()), getRandomColor()));
        }
    }

    private Color getRandomColor() {
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        Color randomColor = new Color(r, g, b);
        return randomColor;

    }

//    thong ke full 
    private void fillTableThongKe() {
        tableModelThongKe = (DefaultTableModel) tbl_ThongKe_ThongKe.getModel();
        tableModelThongKe.setRowCount(0);
        List<Object[]> list = thongKeDAO.getThongKeFull();
        for (Object[] o : list) {
            tableModelThongKe.addRow(new Object[]{o[0], o[1], o[2], o[3], o[4], o[5], o[6]});
        }

    }

    private void fillComBoBox_HangSanXuat_ThongKe() {
        SanPhamDAO spDAO = new SanPhamDAO();
        List<Object> listHang = spDAO.selectByHang();
        DefaultComboBoxModel model = (DefaultComboBoxModel) this.cbo_ChonHang_ThongKe.getModel();
        model.removeAllElements();
        for (Object sp : listHang) {
            model.addElement(sp);
        }
    }

    private void fillTableThongKe_Top5BanChay() {
        tableModelThongKe = (DefaultTableModel) tbl_ThongKe_ThongKe.getModel();
        tableModelThongKe.setRowCount(0);
        try {
            List<Object[]> list = thongKeDAO.getThongKeTop5(dcs_NgayBatDau_ThongKe.getDate(), dcs_NgayKetThuc_ThongKe.getDate());
            if (list.isEmpty()) {
                MsgBox.alert(this, "Không có thông tin thống kê theo ngày bạn chọn !!");
                return;
            }
            for (Object[] o : list) {
                tableModelThongKe.addRow(new Object[]{o[0], o[1], o[2], o[3], o[4], o[5], o[6]});
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Không có thông tin thống kê theo ngày bạn chọn !!");

            e.printStackTrace();
        }

    }

    private void fillTable_TheoHang_ThongKe() {
        if (!cbo_ChonHang_ThongKe.isValid()) {
            return;
        }
        if (dcs_NgayBatDau_ThongKe.getDate() == null || dcs_NgayKetThuc_ThongKe.getDate().equals(null)) {
            MsgBox.alert(this, "Vui lòng chọn ngày bắt đầu vào kết thúc để thống kê");
            return;
        } else {
            String hang = cbo_ChonHang_ThongKe.getSelectedItem().toString();

            tableModelThongKe = (DefaultTableModel) tbl_ThongKe_ThongKe.getModel();
            tableModelThongKe.setRowCount(0);
            List<Object[]> list = thongKeDAO.getThongKeTheoHangSanXuat(dcs_NgayBatDau_ThongKe.getDate(), dcs_NgayKetThuc_ThongKe.getDate(), hang);
            if (list.isEmpty()) {
                MsgBox.alert(this, "Không có thông tin thống kê theo ngày bạn chọn !!");
                return;
            }
            for (Object[] o : list) {
                tableModelThongKe.addRow(new Object[]{o[0], o[1], o[2], o[3], o[4], o[5], o[6]});
            }
        }
    }
    int row_ThongKe = -1;

    private void next_ThongKe(int index) {
        if (tbl_ThongKe_ThongKe.getSelectedRow() != -1) {
            index = tbl_ThongKe_ThongKe.getSelectedRow();
            index++;
        }
        if (index == -1 || index == tbl_ThongKe_ThongKe.getRowCount()) {
            index = 0;
        }
        tbl_ThongKe_ThongKe.setRowSelectionInterval(index, index);
        this.fillTableThongKe();
    }

    private void prev_ThongKe(int index) {
        if (tbl_ThongKe_ThongKe.getSelectedRow() != -1) {
            index = tbl_ThongKe_ThongKe.getSelectedRow();
            index--;
        }
        if (index == tbl_ThongKe_ThongKe.getRowCount()) {
            index = 0;
        }
        if (index < 0) {
            index = tbl_ThongKe_ThongKe.getRowCount() - 1;
        }
        tbl_ThongKe_ThongKe.setRowSelectionInterval(index, index);
        this.fillTableThongKe();
    }

    private void first_ThongKe() {
        row_ThongKe = 0;
        this.next_ThongKe(row_ThongKe);
    }

    private void last_ThongKe() {
        row_ThongKe = tbl_ThongKe_ThongKe.getRowCount() - 1;
        tbl_ThongKe_ThongKe.setRowSelectionInterval(row_NhanVien, row_NhanVien);
        this.fillTableThongKe();
    }
    //END_CAAARD_THONGKE

}
