# PrivateVoiceBot

PrivateVoiceBot is a Discord bot that allows users to create private voice channels in their server. The bot is easy to use and offers simple commands for control.

## Features
- Automatically creates private voice channels when users join a specified channel.
- Deletes empty private voice channels after a certain period.
- Allows users to configure the category where private channels will be created.

## Commands
### General Commands
- `/about` - Get information about the bot, including the source code.
- `/setup` - Get instructions on setting up the bot.

### Voice Channel Commands (Admin Only)
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

## Configuration
```
token: <your-bot-token>
# Your bot token. Do not share this token
removeExceptionChannels: false
# Whether to remove all channels in the create category after bot startup. Use with caution
commandMessageEphemeral: false
# Whether command replies are ephemeral. If true, only the command sender can see the reply
defaultChannelVisibility: true
# Whether private voice channels are visible to everyone by default
privateVoiceCategoryId:
# Map of create category IDs. Do not manually modify this setting
privateVoiceChannelId:
# Map of create channel IDs. Do not manually modify this setting
privateVoiceChannelName: "點我建立私人頻道"
noPermissionTitle: "您的權限不足！"
noPermissionDescription: "您沒有權限執行此指令！"
noPermissionColor: "#FF7A7A"
noVoiceChannelColor: "#FF7A7A"
noVoiceChannelTitle: "您需要先有一個私人語音頻道！"
noVoiceChannelDescription: "請先建立一個私人語音頻道再執行此指令！"
successColor: "#CFFFC0"
errorColor: "#FF7A7A"
successTitle: "成功！"
errorTitle: "錯誤！"
infoColor: "#7AD3FF"
commandToBotError: "您無法對機器人執行該指令！"
commandSelfError: "您無法對自己執行指令！"
commandReloadSuccess: "成功重新載入設定！"
commandSetCreateChannelSuccess: "成功設定私人語音建立頻道！"
commandSetCreateChannelError: "設定私人語音建立頻道時發生錯誤，請確認您指定頻道是否為語音頻道！"
commandSetCreateCategorySuccess: "成功設定私人語音建立分類！"
commandSetCreateCategoryError: "設定私人語音建立分類時發生錯誤，請確認您指定類型是否為分類！"
commandInviteSuccess: "已成功邀請使用者！"
commandKickPublicError: "您只能在私人語音頻道踢出其他使用者！"
commandKickNotInvitedError: "對方不在此私人語音頻道的邀請列表！"
commandInviteAlreadyInvitedError: "對方已經在此私人語音頻道的邀請列表！"
commandKickSuccess: "已成功踢出使用者！"
commandRenameSuccess: "已成功更改私人語音頻道名稱！"
commandTransferSelfError: "您已經是此私人語音頻道的擁有者！"
commandTransferSuccess: "已成功轉移私人語音頻道之擁有權！"
commandTransferTargetError: "對方已經擁有一個私人語音頻道！"
commandToggleVisibilitySuccess: "已成功切換頻道私人語音可見性！"
commandLimitSuccess: "已成功更改私人語音頻道人數限制！"
commandCloseSuccess: "已成功關閉頻道！"
footer: "Made by Nontage"
footerIcon: "https://cdn.discordapp.com/avatars/810170073239126066/98c4e35237d5ab7ff452e7dcd71e4a75.png?size=2048&quality=lossless"
privateVoiceChannelPrefix: ""
privateVoiceChannelSuffix: "的私人頻道"
privateVoiceChannelTimeout: 300
# Timeout for private voice channels in seconds
```

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
- `/setup` - 獲取機器人設定教學。

### 語音頻道指令 (僅限管理員)
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

## 配置
```
token: <your-bot-token>
# 您的機器人令牌。請勿分享此令牌
removeExceptionChannels: false
# 是否在機器人啟動後刪除所有在建立類別的頻道。請謹慎使用此選項
commandMessageEphemeral: false 
# 指令回覆是否為僅個人可見。若為 true，則僅指令發送者可見
defaultChannelVisibility: true
# 私人語音頻道是否默認對所有人可見
privateVoiceCategoryId:
# 建立類別映射表。請勿手動修改此設定
privateVoiceChannelId:
# 建立頻道映射表。請勿手動修改此設定
privateVoiceChannelName: "點我建立私人頻道"
noPermissionTitle: "您的權限不足！"
noPermissionDescription: "您沒有權限執行此指令！"
noPermissionColor: "#FF7A7A"
noVoiceChannelColor: "#FF7A7A"
noVoiceChannelTitle: "您需要先有一個私人語音頻道！"
noVoiceChannelDescription: "請先建立一個私人語音頻道再執行此指令！"
successColor: "#CFFFC0"
errorColor: "#FF7A7A"
successTitle: "成功！"
errorTitle: "錯誤！"
infoColor: "#7AD3FF"
commandToBotError: "您無法對機器人執行該指令！"
commandSelfError: "您無法對自己執行指令！"
commandReloadSuccess: "成功重新載入設定！"
commandSetCreateChannelSuccess: "成功設定私人語音建立頻道！"
commandSetCreateChannelError: "設定私人語音建立頻道時發生錯誤，請確認您指定頻道是否為語音頻道！"
commandSetCreateCategorySuccess: "成功設定私人語音建立分類！"
commandSetCreateCategoryError: "設定私人語音建立分類時發生錯誤，請確認您指定類型是否為分類！"
commandInviteSuccess: "已成功邀請使用者！"
commandKickPublicError: "您只能在私人語音頻道踢出其他使用者！"
commandKickNotInvitedError: "對方不在此私人語音頻道的邀請列表！"
commandInviteAlreadyInvitedError: "對方已經在此私人語音頻道的邀請列表！"
commandKickSuccess: "已成功踢出使用者！"
commandRenameSuccess: "已成功更改私人語音頻道名稱！"
commandTransferSelfError: "您已經是此私人語音頻道的擁有者！"
commandTransferSuccess: "已成功轉移私人語音頻道之擁有權！"
commandTransferTargetError: "對方已經擁有一個私人語音頻道！"
commandToggleVisibilitySuccess: "已成功切換頻道私人語音可見性！"
commandLimitSuccess: "已成功更改私人語音頻道人數限制！"
commandCloseSuccess: "已成功關閉頻道！"
footer: "Made by Nontage"
footerIcon: "https://cdn.discordapp.com/avatars/810170073239126066/98c4e35237d5ab7ff452e7dcd71e4a75.png?size=2048&quality=lossless"
privateVoiceChannelPrefix: ""
privateVoiceChannelSuffix: "的私人頻道"
privateVoiceChannelTimeout: 300
# 私人語音頻道空閒超時時間（秒）
```

Enjoy using PrivateVoiceBot!

