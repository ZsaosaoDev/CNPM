using Godot;

public class UpdateShopCard : ICard
{
    public void doAction(CardManager.CardInputType option)
    {
        if (option == CardManager.CardInputType.LEFT)
        {
            StatsManager.UpdateStats(-10, -5, 0, 10);
        }
        else if (option == CardManager.CardInputType.RIGHT)
        {
            return;
        }
    }

    public Texture2D GetTexture()
    {
        return ResourceLoader.Load<Texture2D>("res://assets/img/card/update_shop.png");
    }


    public string GetDescription()
    {
        return "Quán của chúng ta khá cũ, bạn có muốn nâng cấp cơ sở vật chất không?";
    }
}
