![image](https://github.com/user-attachments/assets/98662541-4c00-48c3-aaa4-fc4ae3c15848)

# Jshop

## 簡介

本專案是一個用於處理 CSV 文件的系統。使用者可以上傳 CSV 文件，系統將這些文件的內容通過 RabbitMQ 傳遞，並使用 Spring Batch 進行非同步批量處理和儲存。

## 功能

- 使用者可以上傳 CSV 文件
- 文件內容通過 RabbitMQ 消息隊列處理
- 使用 Spring Batch 批量寫入數據到資料庫
- 支援死信隊列

## 技術架構

簡要描述技術架構圖的內容：

1. 使用者通過介面上傳 CSV 文件到伺服器。
2. 伺服器將文件內容發送到 Server 且將任務寫進 RabbitMQ 消息隊列。
3. RabbitMQ 消費者讀取消息，並使用 Spring Batch 批量處理數據。
