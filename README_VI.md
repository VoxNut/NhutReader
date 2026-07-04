<div align="center">

# Nhut Reader

[English](README.md)

Nhut Reader Android là ứng dụng đọc sách EPUB tiếng Nhật nhẹ cho Android, xây dựng cho immersion learning với tra cứu Yomitan, tạo thẻ Anki, nghe sách nói đi kèm Sasayaki, và tùy chọn chế độ e-ink.

<table>
  <tr>
    <td><img src="images/bookshelf.jpg" alt="Bookshelf" width="100%"></td>
    <td><img src="images/reader-lookup-popup.jpg" alt="Reader lookup popup" width="100%"></td>
    <td><img src="images/reader-dark-theme.jpg" alt="Dark reader" width="100%"></td>
    <td><img src="images/reader-eink-mode.jpg" alt="E-ink reader" width="100%"></td>
  </tr>
  <tr>
    <td><img src="images/sasayaki-audiobook.jpg" alt="Sasayaki audiobook" width="100%"></td>
    <td><img src="images/reader-statistics.jpg" alt="Reader statistics" width="100%"></td>
    <td><img src="images/reader-highlights.jpg" alt="Reader highlights" width="100%"></td>
    <td><img src="images/dictionary-recursive-lookup.jpg" alt="Dictionary recursive lookup" width="100%"></td>
  </tr>
  <tr>
    <td><img src="images/reader-appearance-settings.jpg" alt="Reader appearance settings" width="100%"></td>
    <td><img src="images/dictionary-management.jpg" alt="Dictionary management" width="100%"></td>
    <td><img src="images/anki-card-settings.jpg" alt="Anki card settings" width="100%"></td>
    <td><img src="images/sync-settings.jpg" alt="Sync settings" width="100%"></td>
  </tr>
  <tr>
    <td><img src="images/ai-translation-pop-up.jpg" alt="AI translation pop up" width="100%"></td>
    <td><img src="images/ai-translation-settings.jpg" alt="AI translation settings" width="100%"></td>
    <td><img src="images/anki-mining-history.jpg" alt="Anki mining history" width="100%"></td>
    <td><img src="images/firebase-cloud.jpg" alt="Firebase cloud integration" width="100%"></td>
  </tr>
</table>

</div>

## Tính Năng

### Tủ Sách
- Nhập tệp EPUB riêng lẻ, hàng loạt, hoặc đệ quy từ các thư mục, và hiển thị trực quan tiến trình đọc sách từ tủ sách.
- Tổ chức sách bằng các kệ sách tùy chỉnh.
- Xuất tệp EPUB hoặc tải lại sách đã đồng bộ từ xa về thư viện cục bộ.

### Trình Đọc
- Đọc sách tiếng Nhật với dạng chữ dọc hoặc ngang, cuộn liên tục hoặc phân trang.
- Tùy biến chủ đề, phông chữ, khoảng cách đoạn, và các điều khiển trình đọc, bao gồm các chủ đề trình đọc tùy chỉnh.
- Sử dụng chế độ tập trung chuyên sâu, chuyển trang bằng phím âm lượng, và các tùy chọn hiển thị e-ink.
- Mở hình ảnh trong trình đọc ở chế độ toàn màn hình để phóng to, sao chép, lưu, và chia sẻ.

### Tra Cứu
- Nhập, tải xuống, cập nhật, và quản lý các bộ từ điển Yomitan.
- Chạm vào văn bản trong trình đọc, tìm kiếm từ tab Từ điển, hoặc tra cứu văn bản được chọn từ các ứng dụng Android khác.
- Chạm vào từ chưa biết bên trong định nghĩa để tra cứu đệ quy đa tầng.
- Nhúng các mã phong cách CSS tùy chỉnh.
- Sử dụng âm thanh từ vựng trực tuyến hoặc cục bộ.

### Dịch Thuật AI
- Dịch từ vựng hoặc câu trực tiếp từ trình đọc hoặc popup tra cứu bằng các mô hình Gemini (chẳng hạn như `gemini-2.5-flash`, `gemini-2.5-pro`, hoặc `gemini-3-flash-preview`).
- Cấu hình mã khóa API Gemini cá nhân, ngôn ngữ đích dịch thuật, trình kích hoạt dịch tự động, và chọn các mô hình AI cụ thể.

### Đánh Dấu Và Thống Kê
- Thêm đánh dấu bằng năm màu sắc trong khi đọc và nhảy đến chúng bất cứ lúc nào.
- Theo dõi thống kê đọc sách, bao gồm số ký tự đã đọc, thời gian đã đọc, và tốc độ đọc, hiển thị trực quan trong khi đọc.

### Tạo Thẻ Anki
- Tạo thẻ học thông qua AnkiDroid hoặc AnkiConnect.
- Sử dụng các trường tương thích với Lapis, kiểm tra trùng lặp, và xuất dữ liệu đa phương tiện.
- Theo dõi tất cả các từ đã tạo thẻ trước đó và ngày tạo của chúng trong nhật ký lịch sử tạo thẻ.

### Sách Nói Đi Kèm
- Khớp tệp phụ đề sách nói với văn bản sách để tô sáng câu hiện tại.
- Theo dõi tô sáng với lật trang tự động.
- Điều khiển tốc độ phát, hành động nhảy dòng, và các điều khiển đa phương tiện của Android.

## Quyền Riêng Tư Và Dữ Liệu
Nhut Reader Android lưu trữ sách nhập vào, từ điển, phông chữ, dữ liệu sách nói, tiến độ đọc, đánh dấu, thống kê, và cài đặt cục bộ trong bộ nhớ ứng dụng.

Đồng bộ Google Drive sử dụng quy trình mã thiết bị Google Cloud OAuth do người dùng cấu hình. Tạo thẻ Anki giao tiếp với AnkiDroid hoặc cấu hình đầu cuối AnkiConnect. Kiểm tra cập nhật đọc siêu dữ liệu release của GitHub. Tích hợp Firebase được sử dụng cho báo cáo crash và chẩn đoán độ ổn định ứng dụng.

## Đóng Góp Tham Chiếu
Nhut Reader Android được xây dựng dựa trên hệ sinh thái này:
- [hoshidicts](https://github.com/Manhhao/hoshidicts) and [hoshidicts-kotlin-bridge](https://github.com/Manhhao/hoshidicts-kotlin-bridge) for Yomitan dictionary support.
- [Yomitan](https://github.com/yomidevs/yomitan) for dictionary format and lookup inspiration.
- [AnkiDroid](https://github.com/ankidroid/Anki-Android) for Android card creation integration.
- [Ankiconnect Android](https://github.com/KamWithK/AnkiconnectAndroid) for local audio behavior and AnkiDroid duplicate scope/checksum query references.
- [ッツ Ebook Reader](https://github.com/ttu-ttu/ebook-reader) for reader, statistics, and sync compatibility references.

## Bản Quyền
Phát hành dưới Giấy phép Công cộng GNU v3.0. Xem [LICENSE](LICENSE) để biết chi tiết.
