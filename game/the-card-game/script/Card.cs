using Godot;
using System;

public interface ICard
{
    public string GetDescription();
    public void doAction(CardManager.CardInputType option);
}
