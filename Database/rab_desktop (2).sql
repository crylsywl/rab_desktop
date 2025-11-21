-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 21 Nov 2025 pada 11.38
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `rab_desktop`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `isirab`
--

CREATE TABLE `isirab` (
  `id_rab` varchar(255) NOT NULL,
  `id_material` varchar(50) DEFAULT NULL,
  `type` varchar(100) DEFAULT NULL,
  `category` varchar(100) DEFAULT NULL,
  `nama_material` varchar(150) DEFAULT NULL,
  `satuan` varchar(50) DEFAULT NULL,
  `jumlah` int(11) DEFAULT NULL,
  `harga_satuan` decimal(15,2) DEFAULT NULL,
  `harga_total` decimal(18,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `isirab`
--

INSERT INTO `isirab` (`id_rab`, `id_material`, `type`, `category`, `nama_material`, `satuan`, `jumlah`, `harga_satuan`, `harga_total`) VALUES
('RAB0001', 'MTR0001', 'Budget', 'Pondasi', 'Batu', '1 truk', 10, 12000.00, 120000.00),
('RAB0001', 'MTR0000', 'Budget', 'Pondasi', 'Batu', '1 Truk', 100, 8000.00, 800000.00),
('RAB0002', 'MTR0001', 'Budget', 'Pondasi', 'Batu', '1 truk', 100, 12000.00, 1200000.00),
('RAB0002', 'MTR0001', 'Budget', 'Pondasi', 'Batu', '1 truk', 12, 12000.00, 1200000.00),
('RAB0002', 'MTR0001', 'Budget', 'Pondasi 2', 'Batu', '1 truk', 25, 12000.00, 1200000.00);

-- --------------------------------------------------------

--
-- Struktur dari tabel `material`
--

CREATE TABLE `material` (
  `id_material` varchar(50) NOT NULL,
  `nama_supplier` varchar(100) DEFAULT NULL,
  `nama_material` varchar(150) DEFAULT NULL,
  `spesifikasi` text DEFAULT NULL,
  `satuan` varchar(50) DEFAULT NULL,
  `stok` int(11) DEFAULT NULL,
  `price` decimal(15,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `material`
--

INSERT INTO `material` (`id_material`, `nama_supplier`, `nama_material`, `spesifikasi`, `satuan`, `stok`, `price`) VALUES
('MTR0001', 'A', 'Batu', 'batu cincin', '1 truk', 100, 12000.00),
('MTR0002', 'Toko Jaya Baru', 'Kayu', 'Kayu Jati asli asli', '3m', 120, 80000.00);

-- --------------------------------------------------------

--
-- Struktur dari tabel `project`
--

CREATE TABLE `project` (
  `id_project` varchar(50) NOT NULL,
  `nama_project` varchar(150) DEFAULT NULL,
  `jumlah_rumah` varchar(50) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `location` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `project`
--

INSERT INTO `project` (`id_project`, `nama_project`, `jumlah_rumah`, `type`, `location`) VALUES
('PRJ0001', 'Perumahan Griya Kalideres', '12', '32', 'Jakarta'),
('PRJ0002', 'Perumahan Griya Kalideres 2', '20', '40', 'Jakarta'),
('PRJ0003', 'Perumahan Griya Kalideres 3', '25', '50', 'Jakarta');

-- --------------------------------------------------------

--
-- Struktur dari tabel `rab`
--

CREATE TABLE `rab` (
  `id_rab` varchar(50) NOT NULL,
  `id_project` varchar(50) DEFAULT NULL,
  `nama_project` varchar(150) DEFAULT NULL,
  `total` decimal(18,2) DEFAULT NULL,
  `permeter` decimal(18,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `rab`
--

INSERT INTO `rab` (`id_rab`, `id_project`, `nama_project`, `total`, `permeter`) VALUES
('RAB0001', 'PRJ0001', 'Perumahan Griya Kalideres', 120000000.00, 12000000.00),
('RAB0002', 'PRJ0002', 'Perumahan Griya Kalideres 2', 3600000.00, 90000.00),
('RAB0003', 'PRJ0003', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Struktur dari tabel `supplier`
--

CREATE TABLE `supplier` (
  `id_supplier` varchar(50) NOT NULL,
  `nama_supplier` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `no_telp` varchar(20) DEFAULT NULL,
  `alamat` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `supplier`
--

INSERT INTO `supplier` (`id_supplier`, `nama_supplier`, `email`, `no_telp`, `alamat`) VALUES
('SPLR0001', 'Toko Jaya Baru', 'jayabaru@gmail.com', '0981234567', 'Jln. raya dewi sartika'),
('SPLR0002', 'Toko Pembangunan Mandiri', 'mandiri@gmail.com', '0923423', 'jakarta');

-- --------------------------------------------------------

--
-- Struktur dari tabel `user`
--

CREATE TABLE `user` (
  `id_user` varchar(50) NOT NULL,
  `nama` varchar(100) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `user`
--

INSERT INTO `user` (`id_user`, `nama`, `username`, `password`) VALUES
('USR0001', 'cryl', 'cryl', '123'),
('USR0002', 'syawal', 'syawal', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3'),
('USR0004', 'sofyan', 'sofyan', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3'),
('USR0005', 'admin', 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `material`
--
ALTER TABLE `material`
  ADD PRIMARY KEY (`id_material`);

--
-- Indeks untuk tabel `project`
--
ALTER TABLE `project`
  ADD PRIMARY KEY (`id_project`);

--
-- Indeks untuk tabel `rab`
--
ALTER TABLE `rab`
  ADD PRIMARY KEY (`id_rab`);

--
-- Indeks untuk tabel `supplier`
--
ALTER TABLE `supplier`
  ADD PRIMARY KEY (`id_supplier`);

--
-- Indeks untuk tabel `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `username` (`username`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
