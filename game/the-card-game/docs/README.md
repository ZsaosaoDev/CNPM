# Hướng dẫn cài đặt phát triển
Đây là hướng dẫn ngắn gọn về việc cài đặt môi trường riêng cho trò chơi thẻ bài.
# Cài đặt môi trường
## Bước 1: Cài đặt Godot 4 .NET.
- Truy cập trang chủ của godot tại https://godotengine.org
- Nhấn vào Download.
- Chọn phiên bản .NET.
- Làm theo hướng dẫn trên trang web.
## Bước 2: Khởi động dự án.
- Đầu tiên, bạn hãy mở godot lên.
- Sau đó chọn import dự án.
- Vị trí dự án nằm ở `game/the-card-game`
- Nhấn xác nhận và dự án đã sẵn sàng.
# Phát triển
Hướng dẫn này giả định bạn đã biết lập trình bằng godot và hiểu các khái niệm liên quan, nếu chưa có thể tham quan tài liệu godot https://docs.godotengine.org/en/stable/
## 1. Tạo thẻ mới
### Chuẩn bị:
- Hình ảnh của thẻ bài.
- Mô tả về thẻ bài.

### Implement ICard:
Bạn cần phải Implement interface ICard, bạn sẽ phải tự triển khai 3 hàm sau:

+ GetDescription(): Là mô tả về thẻ bài.
+ doAction(CardManager.CardInputType option): là sụ kiện sẽ diễn ra nếu người chơi chọn yes/no.
+ GetTexture() : Là hình ảnh của thẻ bài đó

Ví dụ:
```cs
using System;
using Godot;
public class NewRecipe : ICard
{
    public void doAction(CardManager.CardInputType option)
    {
        Random random = new Random();
        int randomValue = random.Next(0, 2);
        if (option == CardManager.CardInputType.LEFT)
        {
            StatsManager.UpdateStats(0, 0, 0, 10 * randomValue);
        }
        else if (option == CardManager.CardInputType.RIGHT)
        {
            StatsManager.UpdateStats(0, 0, 0, -10 * randomValue);
        }
    }

    public string GetDescription()
    {
        return "Cậu đã có một công thức mới, hãy thử nghiệm với nó nhé!";
    }

    public Texture2D GetTexture()
    {
        return ResourceLoader.Load<Texture2D>("res://assets/img/card/new_recipe.png");
    }
}
```

## 2. Thêm thẻ mới vào trò chơi.
Để thêm thẻ, bạn cần thêm nó vào trong `CardDatabase`.
```cs
CardDatabase.Instance.AddCard(new UpdateShopCard());
```

## Nâng cao: Card Sequence
Card sequence là giải pháp cho phép bạn thể hiện một kế quả liên tiếp của một sụ kiện thẻ nào đó, ví dụ sự kiện `công thức mới` sẽ có 2 luồng thẻ là công thức đó được `mọi người thích` hoặc `không thích`, lúc này ta sử dụng Card Sequence để tạo ra các sự kiện liên tiếp của sự kiện `công thức mới`.

Ví dụ:
```cs
using Godot;

public class Resting : ICard
{
    public void doAction(CardManager.CardInputType option)
    {
        if (option == CardManager.CardInputType.LEFT)
        {
            StatsManager.UpdateStats(5, -1, 0, 0);
            // Thêm diễn biến.
            CardDatabase.Instance.AddSequenceCard(new RestingResult());
        }
        else if (option == CardManager.CardInputType.RIGHT)
        {
            StatsManager.UpdateStats(-5, 1, 0, 0);
        }
    }

    public string GetDescription()
    {
        return "Cậu làm việc chăm chỉ quá rồi, hãy nghỉ ngơi một chút đi!";
    }

    public Texture2D GetTexture()
    {
        return ResourceLoader.Load<Texture2D>("res://assets/img/aya/worry.png");
    }
}

// Kết quả của sự kiện nghỉ ngơi
public class RestingResult : ICard
{
    public void doAction(CardManager.CardInputType option)
    {
        return; 
    }

    public string GetDescription()
    {
        return "Thời gian nghỉ ngơi";
    }

    public Texture2D GetTexture()
    {
        return ResourceLoader.Load<Texture2D>("res://assets/img/card/resting.png");
    }
}
```