using Godot;
using System;

public partial class CardManager : Node 
{
    public enum CardInputType {
        LEFT,
        RIGHT
    }
    [Export]
    private AnimationPlayer animationPlayer;
    private ICard nextCard;
    private ICard postCard;
    [Export]
    private Label preCardDescription;
    [Export]
    private Label postCardDescription;

    [Export]
    private TextureRect preCardTexture;
    [Export]
    private TextureRect postCardTexture;

    private bool lockInput = false;
    public void NextCard(CardInputType option)
    {
        if (lockInput)
        {
            return;
        }
        
        lockInput = true;

        

        // 4.6 thực hiện sự kiện
        if (postCard != null)
        {
            postCard.doAction(option);
        }

        // 4.3. lấy thẻ ngẫu nhiên
        ICard randomCard = RandomCard();
        nextCard = randomCard;
        preCardDescription.Text = nextCard.GetDescription();
        preCardTexture.Texture = nextCard.GetTexture();
        

        if (option == CardInputType.LEFT)
        {
            animationPlayer.Play("left_drag");
        }
        else if (option == CardInputType.RIGHT)
        {
            animationPlayer.Play("right_drag");
        }
        
        

    }

    // 4.11. Kết thúc animation
    public void OnAnimationFinished(StringName animName)
    {
        preCardDescription.Text = "";
        postCardDescription.Text = nextCard.GetDescription();
        animationPlayer.Play("RESET");
        lockInput = false;
        postCard = nextCard;
        postCardTexture.Texture = nextCard.GetTexture();
    }

    private ICard RandomCard() {
        Random random = new Random();
        // 4.4 Đếm số lượng thẻ
        int randomIndex = random.Next(CardDatabase.Instance.GetCardCount());
        // 4.5 Lấy thẻ
        return CardDatabase.Instance.GetCard(randomIndex);
    }


    public override void _Ready() {
        // Add more cards as needed
        animationPlayer.AnimationFinished += OnAnimationFinished;
        NextCard(CardInputType.LEFT);
    }

}