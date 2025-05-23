using System.Collections.Generic;
using Godot;

public partial class CardDatabase : Node
{
    public static CardDatabase Instance { get; private set; }
    private List<ICard> cards = new List<ICard>();
    private Queue<ICard> tutorial = new Queue<ICard>();
    public override void _Ready()
    {
        tutorial.Enqueue(new TutorialCard("Xin chào, chào mừng bạn đến với trò chơi thẻ bài, hãy nhấn mũi tên trái hoặc phải để bắt đầu."));
        cards.Add(new UpdateShopCard());
        Instance = this;
    }

    public ICard GetCard(int index)
    {
        if (tutorial.Count > 0)
        {
            return tutorial.Dequeue();
        }

        if (index < 0 || index >= cards.Count)
        {
            GD.PrintErr("Index out of range");
            return null;
        }

        return cards[index];
    }

    public int GetCardCount()
    {
        return cards.Count;
    }
}