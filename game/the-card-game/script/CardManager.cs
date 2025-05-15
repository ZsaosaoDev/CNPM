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

    private bool lockInput = false;

    private List<ICard> cards = new List<ICard>();


    public void NextCard(CardInputType option) {
        lockInput = true;
        GD.Print("NextCard called with option: " + option);
        ICard randomCard = RandomCard();
        nextCard = randomCard;
        preCardDescription.Text = nextCard.GetDescription();

        if (option == CardInputType.LEFT) {
            animationPlayer.Play("left_drag");
        } else if (option == CardInputType.RIGHT) {
            animationPlayer.Play("right_drag");
        }

    }

    public void OnAnimationFinished(StringName animName) {
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
        cards.Add(new SimpleCard("Card 1", "Description of Card 1"));
        cards.Add(new SimpleCard("Card 2", "Description of Card 2"));
        cards.Add(new SimpleCard("Card 3", "Description of Card 3"));
        // Add more cards as needed
        animationPlayer.AnimationFinished += OnAnimationFinished;
        

    }


    public override void _Process(double delta) {
        if (lockInput) {
            return;
        }

        if (Input.IsActionJustPressed("ui_left")) {
            NextCard(CardInputType.LEFT);
        } else if (Input.IsActionJustPressed("ui_right")) {
            NextCard(CardInputType.RIGHT);
        }

    }
}