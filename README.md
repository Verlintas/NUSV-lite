# NUSV LITE

NUSV 的官方安卓客户端，将 NUSV 生态装进口袋。

## 功能

- 浏览 NUSV 各站点的最新内容，分类整理，快速直达
- 内置 67 个小工具和游戏：贪吃蛇、Wordle、Simon Says、打地鼠、骰子、科学计算器、画板、QR 码生成器、颜文字键盘、随机语录、名字生成器、手电筒、电池信息、快速计时器等
- 每日签到 & 积分系统，积分可解锁主题商店配色
- 主题商店：10 款可解锁配色 + **Orca 高级主题**
  - 连续签到 7 天解锁购买资格（10,000 积分）
  - 纯黑背景 + 纯白界面，卡片透明 + 白边框
  - 所有游戏和签到积分翻倍（×2）
  - 解锁 11 个专属隐藏工具：Matrix Rain、Secret Vault、Clipboard History、Habit Tracker、Text Encrypt、Speed Reader、Color Picker、Password Checker、Text Diff、CSS Gradient、Quick Notes
- 中文 / English 语言切换
- 深色 / 浅色主题
- 布局切换（列表 / 两列网格 / 三列网格）
- 搜索结果高亮、触觉反馈、远程同步
- 游戏赚积分 & 历史最高分记录

## 技术栈

- Kotlin + Jetpack Compose
- Material 3 设计语言
- Room 本地数据库
- 灵感来自 Arc / Linear / Vercel

## 构建

```bash
# 需要 JDK 17
./gradlew assembleDebug
# 产物: app/build/outputs/apk/debug/app-debug.apk
```

发布版 APK 位于 `release/` 目录。

## 版本

当前版本 **v1.9.0**（工具总数 67），更新日志见应用内「文档」页。
