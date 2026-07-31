# NUSV LITE

NUSV 的官方安卓客户端，将 NUSV 生态装进口袋。

## 功能

- 浏览 NUSV 各站点的最新内容，分类整理，快速直达
- 内置丰富的小工具和游戏：经典游戏、计算与换算、开发工具、生成式创意工具等应有尽有
- 每日签到 & 积分系统，积分可解锁主题商店配色
- **Orca 高级主题**：通过长期签到解锁的专属高级主题
  - 纯黑背景 + 纯白界面，卡片透明 + 白边框
  - 所有游戏和签到积分翻倍（×2）
  - 解锁仅 Orca 用户可见的专属隐藏工具
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

## 文档

- [更新日志](CHANGELOG.md)（v1.1.0 至今完整版本记录）
- 应用内「文档」页同步维护
