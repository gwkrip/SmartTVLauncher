# 🔑 GitHub Secrets Setup Guide

This document explains how to configure GitHub Secrets for signing your SmartTV Launcher release APK.

---

## Required Secrets

Go to your GitHub repo → **Settings** → **Secrets and variables** → **Actions** → **New repository secret**

| Secret Name       | Description                              | Required |
|-------------------|------------------------------------------|----------|
| `KEYSTORE_BASE64` | Your keystore file encoded in Base64     | ✅ Yes   |
| `KEYSTORE_PATH`   | Path to keystore inside runner (default: `app/release.keystore`) | Optional |
| `STORE_PASSWORD`  | Keystore store password                  | ✅ Yes   |
| `KEY_ALIAS`       | Key alias inside keystore                | ✅ Yes   |
| `KEY_PASSWORD`    | Key password                             | ✅ Yes   |

> ⚠️ Without secrets, the release build will be signed with the **debug keystore** as fallback (not suitable for Play Store).

---

## Step 1: Generate a Release Keystore

If you don't have a keystore yet, generate one:

```bash
keytool -genkey -v \
  -keystore smarttv_release.keystore \
  -alias smarttv_key \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -dname "CN=SmartTV Launcher, OU=Dev, O=YourCompany, L=Jakarta, S=DKI, C=ID"
```

You'll be prompted to enter:
- **Keystore password** → save as `STORE_PASSWORD`
- **Key alias** → `smarttv_key` → save as `KEY_ALIAS`
- **Key password** → save as `KEY_PASSWORD`

---

## Step 2: Encode Keystore to Base64

**Linux/macOS:**
```bash
base64 -w 0 smarttv_release.keystore > keystore_base64.txt
cat keystore_base64.txt
```

**Windows (PowerShell):**
```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("smarttv_release.keystore")) | Out-File keystore_base64.txt
```

Copy the entire content of `keystore_base64.txt` as your `KEYSTORE_BASE64` secret.

---

## Step 3: Add Secrets to GitHub

1. Open your repo on GitHub
2. Go to **Settings → Secrets and variables → Actions**
3. Click **New repository secret** and add each secret:

```
KEYSTORE_BASE64  =  <paste entire base64 string>
STORE_PASSWORD   =  your_store_password
KEY_ALIAS        =  smarttv_key
KEY_PASSWORD     =  your_key_password
```

---

## Step 4: Create a Release

### Via Git Tag (recommended):

```bash
# Make sure you're on main branch
git checkout main
git pull

# Create and push a version tag
git tag v1.0.0
git push origin v1.0.0
```

The workflow will automatically:
1. Build Debug APK
2. Build Release APK (signed)
3. Build Release AAB (for Google Play)
4. Create a GitHub Release with all files attached

### Via GitHub UI (manual trigger):

1. Go to **Actions** tab in your repo
2. Select **🚀 Build & Release** workflow
3. Click **Run workflow**
4. Enter version name and select release type
5. Click **Run workflow**

---

## Workflow Files

| File | Purpose |
|------|---------|
| `.github/workflows/release.yml` | Build + create GitHub Release on tag push |
| `.github/workflows/ci.yml` | Lint + debug build check on every PR/push |

---

## Version Tagging Convention

| Tag | Meaning |
|-----|---------|
| `v1.0.0` | Stable release |
| `v1.0.0-beta.1` | Beta release (marked as pre-release) |
| `v1.0.0-alpha.1` | Alpha release (marked as pre-release) |
| `v1.0.0-rc.1` | Release candidate (marked as pre-release) |

---

## ADB Install Command

After downloading the APK from GitHub Releases:

```bash
# Install on connected TV via ADB
adb connect <TV_IP_ADDRESS>:5555
adb install SmartTVLauncher-v1.0.0-release.apk

# Or install over USB
adb install -r SmartTVLauncher-v1.0.0-release.apk
```
