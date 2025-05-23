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