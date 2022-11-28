USE [PhoneSys]
GO
/****** Object:  StoredProcedure [dbo].[getHD]    Script Date: 11/27/2022 11:13:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

ALTER proc [dbo].[getHD] 
as
begin
select hd.MaHoaDon, nv.TenNhanVien,kh.TenKhachHang, hd.NgayTao, 
sum(sp.DonGia * ct.SoLuong) as 'thanhtien'from HoaDon hd 
join HoaDonChiTiet ct  on hd.MaHoaDon = ct.MaHoaDon 
join NhanVien nv on  nv.MaNhanVien = hd.MaNhanVien 
join SanPham sp  on sp.MaSanPham = ct.MaSanPham 
join KhachHang kh on kh.MaKhachHang = hd.MaKhachHang
group by hd.MaHoaDon, nv.TenNhanVien,kh.TenKhachHang, hd.NgayTao
end