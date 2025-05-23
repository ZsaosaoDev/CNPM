using System;
using Godot;

public class TutorialCard : ICard
{
    public const string TALK = "talk";
    public const string LIGHT_SMILE = "light_smile";
    public const string WORRY = "worry";
    public const string HAPPY = "happy";
    public const string DOUBT = "doubt";
    private string description;
    private string face = "talk";

    public TutorialCard(string description, string face)
    {
        this.description = description;
        this.face = face;
    }

    public string GetDescription()
    {
        return description;
    }

    public void doAction(CardManager.CardInputType option)
    {
        return; // Implement the action logic here
    }

    public Texture2D GetTexture()
    {
        Texture2D texture = ResourceLoader.Load<Texture2D>("res://assets/img/aya/" + face + ".png");
        return texture;
    }
}