# PrivateVoiceBot

PrivateVoiceBot is a Discord bot that allows users to create private voice channels in their server. The bot is easy to use and offers simple commands for control.

## Features
- Automatically creates private voice channels when users join a specified channel.
- Deletes empty private voice channels after a certain period.
- Allows users to configure the category where private channels will be created.

## Commands
### General Commands
- `/about` - Get information about the bot, including the source code.

### Voice Channel Commands
- `/voice setcreatechannel` - Set the channel where joining will create a private voice channel.
- `/voice setcreatecategory` - Set the category where private voice channels will be created.

## Build and Installation
This project is built using **Maven**, and the dependencies, including JDA, are packaged together.

### Prerequisites
- Java 21 or later
- Maven installed

### Build
Run the following command to build the project with dependencies included:
```sh
mvn clean package
```

The built JAR file can be found in the `target/` directory.

## Setup
1. Invite the bot to your server.
2. Assign the necessary permissions.
3. Use the provided commands to configure private voice channels.

---

# PrivateVoiceBot （中文版）

PrivateVoiceBot 是一款 Discord 機器人，允許用戶在伺服器中創建私人語音頻道。該機器人使用簡單的指令來控制和管理頻道。

## 功能
- 用戶加入指定頻道時，自動創建私人語音頻道。
- 自動刪除長時間無人的私人語音頻道。
- 允許用戶設置私人語音頻道的創建類別。

## 指令
### 一般指令
- `/about` - 獲取機器人資訊，包括原始碼連結。

### 語音頻道指令
- `/voice setcreatechannel` - 設置進入後會創建私人語音頻道的頻道。
- `/voice setcreatecategory` - 設置私人語音頻道會創建在哪個類別。

## 構建與安裝
該專案使用 **Maven** 構建，並會將 JDA 等依賴一起打包。

### 先決條件
- Java 21 或更高版本
- 已安裝 Maven

### 構建
執行以下命令來構建專案並包含所有依賴：
```sh
mvn clean package
```

構建完成的 JAR 檔案將位於 `target/` 目錄內。

## 設置
1. 邀請機器人進入您的伺服器。
2. 賦予必要的權限。
3. 使用指令配置私人語音頻道。

Enjoy using PrivateVoiceBot!

