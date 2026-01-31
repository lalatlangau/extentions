# Cloudstream Dutamovie Plugin

Plugin Cloudstream untuk streaming film dan series dari Dutamovie (https://hidekielectronics.com)

## 📁 Struktur Project

```
cloudstream-dutamovie/
├── .github/workflows/build.yml   # Auto build dengan GitHub Actions
├── Dutamovie.kt          # Main plugin file
├── build.gradle.kts               # Build configuration
├── settings.gradle.kts            # Gradle settings
└── repo.json                      # Repository manifest
```

## 🚀 Cara Menggunakan

### 1. Upload ke GitHub

```bash
cd cloudstream-dutamovie
git init
git add .
git commit -m "Initial commit"
git branch -M master
git remote add origin https://github.com/YOUR-USERNAME/cloudstream-dutamovie.git
git push -u origin master
```

### 2. Setup Auto Build

Buat branch `builds`:
```bash
git checkout --orphan builds
git rm -rf .
echo "# Builds" > README.md
git add README.md
git commit -m "Initial builds branch"
git push origin builds
git checkout master
```

### 3. Update repo.json

Edit file `repo.json` dan ganti `YOUR-USERNAME` dengan username GitHub Anda.

### 4. Install di Cloudstream

1. Buka Cloudstream App
2. **Settings** → **Extensions** → **Add Repository**
3. Paste URL:
   ```
   https://raw.githubusercontent.com/YOUR-USERNAME/cloudstream-dutamovie/master/repo.json
   ```

## 🎯 Fitur

- ✅ Search film/series
- ✅ Multiple genres
- ✅ TV Shows support
- ✅ Download support
- ✅ Auto recommendations

## 🔧 Customization

### Ganti Domain
Edit `Dutamovie.kt`:
```kotlin
override var mainUrl = "https://domain-baru.com"
```

### Tambah Genre
Edit bagian `mainPage`:
```kotlin
"$mainUrl/genre/GENRE/page/" to "Nama Genre"
```

## 📝 Template Info

- **Domain**: https://hidekielectronics.com
- **Template**: WordPress Movie Theme
- **Plugin Name**: Dutamovie

## 🐛 Troubleshooting

Jika plugin tidak bekerja, kemungkinan struktur website sudah berubah.
Edit selector di file `.kt` sesuai dengan struktur HTML website.

## 📄 License

Free to use and modify
