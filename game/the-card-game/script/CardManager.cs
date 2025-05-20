using Godot;
using System;
using System.Collections.Generic;

public partial class CardManager : Node 
{
    public enum CardInputType {
        LEFT,
        RIGHT
    }
    [Export]
    private AnimationPlayer animationPlayer;
    private ICard nextCard;
    [Export]
    private Label preCardDescription;
    [Export]
    private Label postCardDescription;

    

    private List<ICard> cards = new List<ICard>();

    private bool lockInput = false;
    public void NextCard(CardInputType option)
    {
        if (lockInput)
        {
            return;
        }
        
        lockInput = true;

        // 4.3. lấy thẻ ngẫu nhiên
        ICard randomCard = RandomCard();
        nextCard = randomCard;
        preCardDescription.Text = nextCard.GetDescription();

        // 4.4 thực hiện sự kiện
        randomCard.doAction(option);

        if (option == CardInputType.LEFT)
        {
            // 4.9. Chạy animation
            animationPlayer.Play("left_drag");
        }
        else if (option == CardInputType.RIGHT)
        {
            animationPlayer.Play("right_drag");
        }
        
        

    }
    
    // 4.10. Kết thúc animation
    public void OnAnimationFinished(StringName animName)
    {
        preCardDescription.Text = "";
        postCardDescription.Text = nextCard.GetDescription();
        animationPlayer.Play("RESET");
        lockInput = false;
    }

    private ICard RandomCard() {
        Random random = new Random();
        int randomIndex = random.Next(cards.Count);

        return cards[randomIndex];
    }


    public override void _Ready() {
        // Initialize the cards list with some card instances
        cards.Add(new TutorialCard("Description of Card 1"));
        cards.Add(new TutorialCard("Description of Card 2"));
        cards.Add(new TutorialCard("Description of Card 3"));
        // Add more cards as needed
        animationPlayer.AnimationFinished += OnAnimationFinished;
        

    }

}