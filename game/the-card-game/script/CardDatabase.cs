using System.Collections.Generic;
using Godot;

/// <summary>
/// Quản lý danh sách thẻ bài.
/// </summary>
public partial class CardDatabase : Node
{
    public static CardDatabase Instance { get; private set; }
    private readonly List<ICard> cards = new List<ICard>();
    private readonly Queue<ICard> cardSequence = new Queue<ICard>();
    public override void _Ready()
    {
        AddSequenceCard(new TutorialCard("Xin chào bạn, tôi là Aya", TutorialCard.LIGHT_SMILE));
        AddSequenceCard(new TutorialCard("Hủm, bạn quên tôi là ai rồi à?", TutorialCard.DOUBT));
        AddSequenceCard(new TutorialCard("Tôi là cổ đông của quán cà phê này, cũng là nhân viên của bạn.", TutorialCard.TALK));
        AddSequenceCard(new TutorialCard("Tôi sẽ giúp bạn quản lý quán cà phê này.", TutorialCard.LIGHT_SMILE));
        AddSequenceCard(new TutorialCard("Mỗi tấm thẻ đều là một lựa chọn, bạn có quyền chọn bằng cách nhấn '←' hoặc không bằng '→'.", TutorialCard.TALK));
        AddSequenceCard(new TutorialCard("Mỗi thẻ đều ảnh hưởng đến các chỉ số nhưng sức khỏe, ngân sách, lượt khách, độ hài lòng của quán.", TutorialCard.LIGHT_SMILE));
        AddSequenceCard(new TutorialCard("Hãy chọn thẻ mà bạn cảm thấy phù hợp nhất với tình huống hiện tại.", TutorialCard.TALK));
        AddSequenceCard(new TutorialCard("Đừng lo lắng, tôi sẽ giúp bạn.", TutorialCard.LIGHT_SMILE));
        AddSequenceCard(new TutorialCard("Hãy bắt đầu nào!", TutorialCard.HAPPY));
        AddCard(new UpdateShopCard());
        AddCard(new Resting());
        AddCard(new NewRecipe());
        Instance = this;
    }

    public ICard GetCard(int index)
    {
        if (cardSequence.Count > 0)
        {
            return cardSequence.Dequeue();
        }

        if (index < 0 || index >= cards.Count)
        {
            GD.PrintErr("Index out of range");
            return null;
        }

        return cards[index];
    }

    public void AddCard(ICard card)
    {
        if (card != null)
        {
            cards.Add(card);
        }
        else
        {
            GD.PrintErr("Card is null");
        }
    }

    /// <summary>
    /// Thêm thẻ quá trình vào hàng đợi.
    /// Thẻ quá trình là thẻ thuộc dạng kết quả của một hành động nào đó.
    /// Ví dụ: thẻ nghỉ ngơi, thẻ làm việc, thẻ đi chơi...
    /// Thẻ quá trình sẽ được thêm vào hàng đợi và sẽ được lấy ra khi cần thiết.
    /// Thẻ quá trình sẽ không được thêm vào danh sách thẻ chính thức.
    /// </summary>
    /// <param name="card"></param>
    public void AddSequenceCard(ICard card)
    {
        if (card != null)
        {
            cardSequence.Enqueue(card);
        }
        else
        {
            GD.PrintErr("Card is null");
        }
    }

    public int GetCardCount()
    {
        return cards.Count;
    }
}