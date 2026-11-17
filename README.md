# Mizanku Android — prototipe awal

Ini adalah proyek Android native (Kotlin), bukan aplikasi HTML.

## Isi prototipe saat ini
- Daftar produk contoh dan tambah produk
- Keranjang belanja dan hitung total
- Simpan transaksi dasar ke SQLite lokal

## Belum tersedia
Ini belum merupakan pemindahan lengkap Mizanku Sederhana 3.3. Fitur seperti scan barcode/kamera, stok lengkap, laporan, utang-piutang, kas, QRIS, printer, backup/restore, dan fitur lanjutan lainnya belum dipindahkan.

## Build APK melalui GitHub Actions
1. Ekstrak ZIP ini.
2. Unggah seluruh isi folder proyek ke root repository `shoneasus-star/Mizanku-Android`, dengan struktur folder tetap dipertahankan.
3. Pastikan file `.github/workflows/android.yml` ikut terunggah.
4. Buka tab **Actions** di repository.
5. Pilih workflow **Build Mizanku APK** dan tunggu sampai berstatus hijau (Success).
6. Buka hasil run, bagian **Artifacts**, lalu unduh `Mizanku-debug-apk`.
7. Ekstrak artifact dan pasang `app-debug.apk` di perangkat Android. Jika diminta, izinkan pemasangan aplikasi dari sumber tersebut.

## Persyaratan build
GitHub Actions menyediakan Ubuntu, Java 17, Android SDK 35, dan Gradle 8.9. Tidak perlu memasang Android SDK di HP.

## Identitas aplikasi
- Package: `id.mizanku.app`
- Minimum Android: API 23 (Android 6.0)
- Version: `0.1.0` (prototipe)
