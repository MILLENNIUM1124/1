# LEICA LUX · 徕卡色卡与 iPhone 2D 调色盘安卓工程 (`LeicaLuxCamera`)

本工程为你量身定制的 **徕卡色卡 (Leica Looks) + iPhone 16 Pro 风格 2D 触控调色盘** 安卓相机应用完整源码包。

## 包含核心特性
1. **8款经典徕卡色彩科学色卡 (`LeicaColorEngine.kt`)**：
   - `Leica Classic` (经典传奇·M11)、`Leica Contemporary` (现代纪实·Q3)、`Leica Eternal` (柯达64传奇胶片)、`Leica Vivid` (鲜明浓郁)、`Leica Natural` (自然柔光人像)、`Leica Brass` (复古黑金露铜)、`Leica Monochrome` (经典银盐黑白)、`Leica Monochrome HC` (森山大道高反差黑白)。
2. **iPhone 相机风格 2D 触控调色盘 (`MainActivity.kt` & 内置 Studio 引擎)**：
   - X轴实时控制色温与色彩浓郁度 (`Color -100 ~ +100`)，Y轴实时控制影调反差与暗部提亮 (`Tone -100 ~ +100`)，带动态点阵感应缩放与强度滑块。
3. **徕卡五色色卡展览级水印相框直出**：
   - 支持一键导出带有徕卡红标、机身镜头 EXIF 参数以及当前五色色卡条的高清 PNG 成片与 `.cube` 3D LUT 文件。

---

## 如何获取可安装的 `.apk` 文件（3种简单方式）

### 方式 1：免编译！在安卓手机直接安装为全屏桌面应用（最快，30秒完成）
工程内的 `app/src/main/assets/leica_camera_studio.html`（以及聊天窗口直接提供的同名文件）已内置完整移动 WebApp 清单：
1. 将 `leica_camera_studio.html` 发送到你的安卓手机并用 **Chrome** 或 **Edge** 浏览器打开；
2. 点击浏览器右上角菜单 `⋮` → 选择 **「添加到主屏幕 (Add to Home screen)」**；
3. 桌面即刻生成 **LEICA LUX** 相机图标，点开即可全屏使用所有徕卡色卡、2D调色盘、调用手机前后摄像头拍照并导出徕卡相框照片！

### 方式 2：使用 GitHub Actions 免安装软件云端自动打包 `.apk`（推荐无本地环境用户）
本工程已内置 `.github/workflows/build-apk.yml` 自动打包脚本：
1. 在 GitHub 新建一个仓库，将本压缩包解压后的所有文件上传上去；
2. 点击仓库顶部的 **Actions** 标签页，等待约 2 分钟编译完成；
3. 在构建结果底部 **Artifacts** 区域点击下载 `LeicaLuxCamera-debug-apk`，解压即可得到 `app-debug.apk` 安装到手机。

### 方式 3：使用 Android Studio 本地一键生成 `.apk`
1. 解压本压缩包，使用 **Android Studio (Iguana 或更新版本)** 打开 `LeicaColorCamera_AndroidProject` 文件夹；
2. 等待 Gradle 同步完成后，点击顶部菜单 **Build → Build Bundle(s) / APK(s) → Build APK(s)**；
3. 在 `app/build/outputs/apk/debug/app-debug.apk` 即可提取安装包。
