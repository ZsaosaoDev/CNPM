public class UpdateShopCard : ICard
{
    public void doAction(CardManager.CardInputType option)
    {
        if (option == CardManager.CardInputType.LEFT)
        {
            StatsManager.UpdateStats(0, -5, 0, 10);
        }
        else if (option == CardManager.CardInputType.RIGHT)
        {
            return;
        }
    }


    public string GetDescription()
    {
        return "Quán của chúng ta khá cũ, bạn có muốn nâng cấp cơ sở vật chất không?";
    }
}
