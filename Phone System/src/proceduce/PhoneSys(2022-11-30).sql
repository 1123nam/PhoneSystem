
-- Tạo database PhoneSys
CREATE DATABASE PhoneSys;
go
USE PhoneSys;
go
-- Tạo table NhanVien
CREATE TABLE NhanVien (
	MaNhanVien varchar(10) NOT NULL PRIMARY KEY,
	TenNhanVien nvarchar(50) NOT NULL,
	NgaySinh date NOT NULL,
	GioiTinh bit NOT NULL,
	CCCD varchar(20) NOT NULL,
	SDT varchar(10) NOT NULL,
	DiaChi nvarchar(255) NOT NULL,
	Email varchar(50) NOT NULL,
	TrangThai BIT NOT NULL,
	HinhAnh varchar(100)  NULL,
	GhiChu nvarchar(50) NULL
);
go
-- Tao table DiemDanh
CREATE TABLE DiemDanh (
	 STT int IDENTITY(1,1) NOT NULL PRIMARY KEY,
	 MaNhanVien varchar(10) NOT NULL,
	 CaLamViec nvarchar(50) NOT NULL,
	 NgayLamViec date  NOT NULL,
	 GhiChu nvarchar(50) NULL
);
go
-- Tạo table TaiKhoan
CREATE TABLE TaiKhoan (
	 MaNhanVien varchar(10) NOT NULL,
	 TenDangNhap nvarchar(50) NOT NULL,
	 MatKhau nvarchar(10) NOT NULL,
	 Quyen bit NOT NULL,
	 GhiChu nvarchar(50) NULL,
	 CONSTRAINT PK_TaiKhoan PRIMARY KEY (MaNhanVien)
);
go
-- Tạo table Luong
CREATE TABLE Luong (
	 MaLuong int IDENTITY(1,1) NOT NULL PRIMARY KEY,
	 MaNhanVien varchar (10) NULL,
	 LuongTrenCa float NOT NULL,
	 TongCaLam float NOT NULL,
	 TienThuong float NOT NULL,
	 NgayNhan date NULL,
	 TrangThai nvarchar(50) NOT NULL,
	 GhiChu nvarchar(50) NULL
);
go
-- Tạo table KhachHang
CREATE TABLE KhachHang (
	 MaKhachHang varchar(10) NOT NULL PRIMARY KEY,
	 TenKhachHang nvarchar(50) NOT NULL,
	 GioiTinh bit NOT NULL,
	 SDT varchar(10) NOT NULL,
	 TrangThai bit NOT NULL,
	 GhiChu nvarchar(50) NULL
);
go
-- Tạo table SanPham (
CREATE TABLE SanPham (
	 MaSanPham varchar(10) NOT NULL PRIMARY KEY,
	 TenSanPham nvarchar(50) NOT NULL,
	 HangSanXuat nvarchar (50) NOT NULL,
	 XuatXu nvarchar (50) NOT NULL,
	 MauSac nvarchar (50) NOT NULL,
	 SoLuong int  NOT NULL,
	 DonGia float  NOT NULL,
	 HinhAnh varchar(100)  NULL,
	 TrangThai bit NOT NULL,
	 GhiChu nvarchar(50) NULL
);
go

-- Tạo table KhuyenMai
CREATE TABLE KhuyenMai (
	 TenKhuyenMai nvarchar(50) NOT NULL,
	 MaSanPham varchar(10) NOT NULL,
	 NgayBatDau date NOT NULL,
	 NgayKetThuc date NOT NULL,
	 GiaGiam float NOT NULL,
	 TrangThai bit NOT NULL,
	 GhiChu nvarchar(50) NULL,
	 CONSTRAINT PK_KhuyenMai PRIMARY KEY (TenKhuyenMai,MaSanPham)
);


-- Tạo table HoaDon
CREATE TABLE HoaDon (
	 MaHoaDon varchar (10) NOT NULL PRIMARY KEY,
	 MaKhachHang varchar (10) NOT NULL,
	 MaNhanVien varchar (10) NOT NULL,
	 NgayTao date NOT NULL,
	 TongTien float NOT NULL,
	 GhiChu nvarchar(50) NULL
);
go
-- Tạo table HoaDonChiTiet 
CREATE TABLE HoaDonChiTiet (
	 MaHoaDonChiTiet int IDENTITY(1,1) NOT NULL PRIMARY KEY,
	 MaHoaDon varchar (10) NOT NULL,
	 MaSanPham varchar (10) NOT NULL,
	 SoLuong int NOT NULL,
	 GhiChu nvarchar(50) NULL
);
go
-- Tạo khóa ngoại (MaNhanVien) cho table DiemDanh
ALTER TABLE DiemDanh
ADD CONSTRAINT FK_MaNhanVien_DiemDanh
FOREIGN KEY(MaNhanVien)
REFERENCES NhanVien(MaNhanVien);
go
-- Tạo khóa ngoại (MaNhanVien) cho table TaiKhoan
ALTER TABLE TaiKhoan
ADD CONSTRAINT FK_MaNhanVien_TaiKhoan
FOREIGN KEY(MaNhanVien)
REFERENCES NhanVien(MaNhanVien);
go
-- Tạo khóa ngoại (MaNhanVien) cho table Luong
ALTER TABLE Luong
ADD CONSTRAINT FK_MaNhanVien_Luong
FOREIGN KEY(MaNhanVien)
REFERENCES NhanVien(MaNhanVien);
go
-- Tạo khóa ngoại (MaSanPham) cho table KhuyenMai
ALTER TABLE KhuyenMai
ADD CONSTRAINT FK_MaSanPham_KhuyenMai
FOREIGN KEY(MaSanPham)
REFERENCES SanPham(MaSanPham);
go
-- Tạo khóa ngoại (MaKhachHang) cho table HoaDon
ALTER TABLE HoaDon
ADD CONSTRAINT FK_MaKhachHang_HoaDon
FOREIGN KEY(MaKhachHang)
REFERENCES KhachHang(MaKhachHang);
go

-- Tạo khóa ngoại (MaNhanVien) cho table HoaDon
ALTER TABLE HoaDon
ADD CONSTRAINT FK_MaNhanVien_HoaDon
FOREIGN KEY(MaNhanVien)
REFERENCES NhanVien(MaNhanVien);
go
-- Tạo khóa ngoại (MaSanPham) cho table HoaDonChiTiet
ALTER TABLE HoaDonChiTiet
ADD CONSTRAINT FK_MaSanPham_HoaDonChiTiet
FOREIGN KEY(MaSanPham)
REFERENCES SanPham(MaSanPham);
go
-- Tạo khóa ngoại (MaHoaDon) cho table HoaDonChiTiet
ALTER TABLE HoaDonChiTiet
ADD CONSTRAINT FK_MaHoaDon_HoaDonChiTiet
FOREIGN KEY(MaHoaDon)
REFERENCES HoaDon(MaHoaDon)
ON DELETE CASCADE
go
--ok
-- INSERT DỮ LIỆU VÀO TABLE

--Insert dữ liệu vào table NhanVien
INSERT INTO NhanVien
VALUES
('NV01',N'Trần Trọng Hiến','2003-11-27','True','687543268','0723855478',N'Bạc Liêu','hientt@2003','true','',''),
('NV02',N'Nguyễn Hoài Nam','2003-09-15','True','687073268','0773455478',N'Sóc Trăng','namnh@2003','true','',''),
('NV03',N'Lê Minh Dương','2003-03-19','False','687545468','0790765478',N'Hậu Giang','duonglm@2003','true','',''),
('NV04',N'Phạm Hoàng Long','2003-07-20','False','546786468','0778970478',N'Cần Thơ','longph@2003','true','',''),
('NV05',N'Nguyễn Huỳnh Như','2001-03-09','False','546635468','0723555478',N'Hậu Giang','nhunh@2003','true','','');;
go
-- Insert dữ liệu vào table DiemDanh
INSERT INTO DiemDanh
VALUES
('NV01',N'Ca Sáng','2022-10-10',''),
('NV01',N'Ca Chiều','2022-10-10',''),
('NV02',N'Ca Sáng','2022-10-10',''),
('NV02',N'Ca Chiều','2022-10-10',''),
('NV03',N'Ca Sáng','2022-10-10',''),
('NV03',N'Ca Chiều','2022-10-10',''),
('NV04',N'Ca Sáng','2022-10-10',''),
('NV04',N'Ca Chiều','2022-10-10',''),
('NV05',N'Ca Sáng','2022-10-10',''),
('NV05',N'Ca Chiều','2022-10-10',''),
('NV01',N'Ca Sáng','2022-10-11',''),
('NV01',N'Ca Chiều','2022-10-11',''),
('NV02',N'Ca Sáng','2022-10-11',''),
('NV02',N'Ca Chiều','2022-10-11',''),
('NV03',N'Ca Sáng','2022-10-11',''),
('NV03',N'Ca Chiều','2022-10-11',''),
('NV04',N'Ca Sáng','2022-10-11',''),
('NV04',N'Ca Chiều','2022-10-11',''),
('NV05',N'Ca Sáng','2022-10-11',''),
('NV05',N'Ca Chiều','2022-10-11',''),
('NV01',N'Ca Sáng','2022-10-12',''),
('NV01',N'Ca Chiều','2022-10-12',''),
('NV02',N'Ca Sáng','2022-10-12',''),
('NV02',N'Ca Chiều','2022-10-12',''),
('NV03',N'Ca Sáng','2022-10-12',''),
('NV03',N'Ca Chiều','2022-10-12',''),
('NV04',N'Ca Sáng','2022-10-12',''),
('NV04',N'Ca Chiều','2022-10-12',''),
('NV05',N'Ca Sáng','2022-10-12',''),
('NV05',N'Ca Chiều','2022-10-12','');
go
-- Insert dữ liệu vào table TaiKhoan
INSERT INTO TaiKhoan
VALUES
('NV01',N'hientrantrong',N'hien123','True',''),
('NV02',N'namnguyenhoai',N'nam123','True',''),
('NV03',N'duongminhle',N'duong123','False','');
go
-- Insert dữ liệu vào table Luong
INSERT INTO Luong
VALUES
('NV01',200000,3,0,'2022-11-27',N'Đã nhận',''),
('NV02',200000,4,0,'2022-11-27',N'Đã nhận',''),
('NV03',100000,2,0,null,N'Chưa nhận','');
go
-- Insert dữ liệu vào table KhachHang
INSERT INTO KhachHang
VALUES
('KH01',N'Trần Khánh Dư','True','0987654865','True',''),
('KH02',N'Nguyễn Văn Hiệp','True','0988234865','True',''),
('KH03',N'Trần Mỹ Nhân','False','0987601265','False','');
go

-- Insert dữ liệu vào table SanPham
INSERT INTO SanPham
VALUES
('SP01',N'Apple A51',N'Apple',N'Trung Quốc',N'Xanh',100,1500000,'','True',''),
('SP02',N'SamSung J4',N'SamSung',N'Hàn Quốc',N'Vàng',110,4500000,'','True',''),
('SP03',N'Iphone 12',N'Iphone',N'Trung Quốc',N'Tím',87,2500000,'','True',''),
('SP04',N'SamSung Galaxy Fold',N'SamSung',N'Hàn Quốc',N'Hồng',90,4500000,'','True',''),
('SP05',N'Iphone 14',N'Iphone',N'Mỹ','Tím',200,15500000,'','True','');
go
-- Insert dữ liệu vào table KhuyenMai
INSERT INTO KhuyenMai
VALUES
(N'Noel an lành','SP01','2022-10-10','2022-11-11',250000,'True',''),
(N'Tết vui vẻ','SP02','2022-12-01','2022-12-30',500000,'True',''),
(N'Mùa đông không lạnh','SP03','2022-09-10','2022-10-10',200000,'True','');
go
-- Insert dữ liệu vào table HoaDon
INSERT INTO HoaDon
VALUES
('HD01','KH01','NV01','2022-12-07',25000000,null);

go
-- Insert dữ liệu vào table HoaDonChiTiet
INSERT INTO HoaDonChiTiet
VALUES
('HD01','SP01',2,null);
go



GO;
--Start PROC 
 -- GET MA GIAM GIA
create proc getGiaGiam @tenSP NVARCHAR(MAX)
as
begin
select sum(km.giagiam) as'GiaGiam' from KhuyenMai km 
JOIN sanpham sp on km.MaSanPham = sp.MaSanPham
where tenSanPham = @tenSP;
END

 -- execute getGiaGiam 'Iphone x'
GO;
-- GET HOA DON
create proc getHD
as
begin
	select hd.MaHoaDon, nv.TenNhanVien,kh.TenKhachHang, hd.NgayTao, hd.TongTien
	from HoaDon hd 
	join HoaDonChiTiet ct  on hd.MaHoaDon = ct.MaHoaDon 
	join NhanVien nv on  nv.MaNhanVien = hd.MaNhanVien 
	join SanPham sp  on sp.MaSanPham = ct.MaSanPham 
	join KhachHang kh on kh.MaKhachHang = hd.MaKhachHang
	group by hd.MaHoaDon, nv.TenNhanVien,kh.TenKhachHang, hd.NgayTao, hd.TongTien
end
GO;
--GET HOA DON CHI TIET
select * from HoaDon
create proc getHDCT(@maHoaDon nvarchar(max))
as 
begin
	select hd.MaHoaDon,sp.TenSanPham,sp.DonGia,hdct.SoLuong, sp.DonGia*hdct.SoLuong as N'Tong tien'
	from HoaDon hd 
	join HoaDonChiTiet hdct on hd.MaHoaDon = hdct.MaHoaDon
	join SanPham sp on hdct.MaSanPham = sp.MaSanPham
	where @maHoaDon = hd.MaHoaDon
	group by hd.MaHoaDon,sp.TenSanPham,sp.DonGia,hdct.SoLuong
end

-- GET LUONG 
GO;
create procedure getLuongByMaNhanVien @MaNhanVien varchar(10)
as 
begin 
select nv.TenNhanVien, tk.Quyen, count(tk.MaNhanVien)  as N'Tổng ca' from 
NhanVien nv  join TaiKhoan tk  on nv.MaNhanVien = tk.MaNhanVien join Luong lg 
on lg.MaNhanVien = nv.MaNhanVien join DiemDanh dd on dd.MaNhanVien = nv.MaNhanVien 
where  tk.MaNhanVien = @MaNhanVien
group by nv.TenNhanVien, tk.Quyen
end 

GO;
-- execute getLuong 'NV01'
create procedure getAllLuong 
as 
begin
	select lg.MaLuong, lg.MaNhanVien, nv.TenNhanVien, lg.TongCaLam, lg.LuongTrenCa, lg.TienThuong,(lg.LuongTrenCa * lg.TongCaLam) + lg.TienThuong as N'Tong luong' ,
	lg.NgayNhan, lg.TrangThai, lg.GhiChu from  Luong lg join NhanVien nv on 
	lg.MaNhanVien = nv.MaNhanVien
end
--execute getAllLuong
GO;
-- top 5 sp 
CREATE PROCEDURE Top5BanChay @ngaybatdau  date, @ngayketthuc date
AS
begin
	SELECT top 5  hdct.MaSanPham,sp.TenSanPham, sp.HangSanXuat,sum(hdct.SoLuong) as N'Số lượng bán',sp.DonGia, (sum(hdct.SoLuong) * sp.DonGia) + km.GiaGiam as N'Tổng tiền',hd.NgayTao FROM HoaDonChiTiet hdct join SanPham sp on  hdct.MaSanPham = sp.MaSanPham 
	join HoaDon hd on hdct.MaHoaDon = hd.MaHoaDon join KhuyenMai km on sp.MaSanPham = km.MaSanPham
	where hd.NgayTao between @ngaybatdau and @ngayketthuc
	group by hdct.MaSanPham,sp.TenSanPham, sp.HangSanXuat,hd.NgayTao, sp.DonGia, km.GiaGiam
	ORDER BY sum(hdct.SoLuong) DESC 
end
GO;

EXEC  Top5BanChay @ngaybatdau  = '2022-12-03', @ngayketthuc ='2022-12-03'



-- Thống kê full

create proc thongKeFull
as
begin
select hdct.MaSanPham ,sp.TenSanPham,sp.HangSanXuat,sp.DonGia
, sum(hdct.SoLuong) AS 'Tong so luong'
, (sum(hdct.SoLuong) * sp.DonGia) as N'Tổng tiền',hd.NgayTao
FROM HoaDonChiTiet hdct join SanPham sp 
on  hdct.MaSanPham = sp.MaSanPham join KhuyenMai km 
on sp.MaSanPham = km.MaSanPham  join HoaDon hd on hd.MaHoaDon = hdct.MaHoaDon
where hdct.MaSanPham in (select MaSanPham from HoaDonChiTiet)
group by hdct.MaSanPham, sp.TenSanPham, sp.DonGia,sp.HangSanXuat, hdct.SoLuong, hd.NgayTao;
end

execute thongKeFull



--thong ke ver 2
select hdct.MaSanPham , sp.TenSanPham, sum(hdct.SoLuong) as N'Số lượng bán', sp.HangSanXuat, sum(hdct.SoLuong * sp.DonGia) as N'Tổng tiền', hd.NgayTao
FROM HoaDonChiTiet hdct join SanPham sp on  hdct.MaSanPham = sp.MaSanPham join KhuyenMai km on sp.MaSanPham = km.MaSanPham join HoaDon hd on hd.MaHoaDon = hdct.MaHoaDon 
where hdct.MaSanPham in (select MaSanPham from HoaDonChiTiet) and hd.NgayTao between '2022-11-27' and '2022-11-30'
group by hdct.MaSanPham, sp.TenSanPham, sp.HangSanXuat, km.GiaGiam, hdct.SoLuong, hd.NgayTao
order by hd.NgayTao;


---- Thống kê  danh sách sản phẩm bán được theo hãng và ngày/tháng/năm
go
create proc thongKeTheoHang @ngayBatDau date , @ngayKetThuc date, @hang nvarchar(20)
as 
begin 
	select hdct.MaSanPham ,sp.TenSanPham,sp.HangSanXuat,sp.DonGia
, sum(hdct.SoLuong) AS 'Tong so luong'
, (sum(hdct.SoLuong) * sp.DonGia) as N'Tổng tiền',hd.NgayTao
FROM HoaDonChiTiet hdct join SanPham sp 
on  hdct.MaSanPham = sp.MaSanPham join KhuyenMai km 
on sp.MaSanPham = km.MaSanPham  join HoaDon hd on hd.MaHoaDon = hdct.MaHoaDon
where hdct.MaSanPham in (select MaSanPham from HoaDonChiTiet) and hd.NgayTao between  @ngayBatDau and @ngayKetThuc and sp.HangSanXuat = @hang
group by hdct.MaSanPham, sp.TenSanPham, sp.DonGia,sp.HangSanXuat, hdct.SoLuong, hd.NgayTao;
end

--thong ke so luong sp ban duoc theo ngay
create proc thongKeTheoNgayHienTai @ngay date
as 
begin
SELECT sum(hd.TongTien) as 'Tong Tien', sum(hdct.SoLuong)  as 'Tong SL'
FROM HoaDonChiTiet hdct join HoaDon hd on hd.MaHoaDon = hdct.MaHoaDon  
where hd.NgayTao = @ngay
end;

execute thongKeTheoNgayHienTai '2022-12-01'

select * from HoaDon
--thong ke so luong sp ban duoc theo thang
create proc thongKeTheoThangHienTai @thang date
as 
begin
	SELECT sum(hd.TongTien) as 'Tong Tien', sum(hdct.SoLuong)  as 'Tong SL'
	FROM HoaDonChiTiet hdct join HoaDon hd on hd.MaHoaDon = hdct.MaHoaDon  
	where MONTH(hd.NgayTao)  = MONTH(@thang) and year(hd.NgayTao) = year(@thang)
end;
execute thongKeTheoThangHienTai '2022-12-01'

--thong ke so luong sp ban duoc theo nam
create proc thongKeTheoNamHienTai @nam date
as 
begin
	SELECT sum(hd.TongTien) as 'Tong Tien', sum(hdct.SoLuong)  as 'Tong SL'
	FROM HoaDonChiTiet hdct join HoaDon hd on hd.MaHoaDon = hdct.MaHoaDon  
	where year(hd.NgayTao) = year(@nam)
end;

create procedure getTongDoanhThuTheoNgay @ngayBatDau date , @ngayKetThuc date
as 
begin
Select sp.TenSanPham, sum(ct.SoLuong * sp.DonGia) as 'TongDoanhThu' from HoaDon hd join HoaDonChiTiet ct 
on hd.MaHoaDon = ct.MaHoaDon join SanPham sp 
on ct.MaSanPham = sp.MaSanPham 
where sp.MaSanPham in (
select MaSanPham from HoaDonChiTiet
) and hd.NgayTao between @ngayBatDau and @ngayKetThuc
group by sp.TenSanPham
end

execute getTongDoanhThuTheoNgay '2022-12-03' , '2022-12-03'
select * from TaiKhoan


execute thongKeTheoNamHienTai '2022-12-03'




--- thống kê toàn bộ doanh thu 
create procedure	getTongDoanhThu
as 
begin
Select sp.TenSanPham, sum(ct.SoLuong * sp.DonGia) as 'TongDoanhThu' from HoaDon hd join HoaDonChiTiet ct 
on hd.MaHoaDon = ct.MaHoaDon join SanPham sp 
on ct.MaSanPham = sp.MaSanPham 
where sp.MaSanPham in (
select MaSanPham from HoaDonChiTiet
) 
group by sp.TenSanPham
end