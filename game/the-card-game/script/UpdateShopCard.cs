public class UpdateShopCard : ICard
{
    public void doAction(CardManager.CardInputType option)
    {
        // Implement the action logic here
        if (option == CardManager.CardInputType.LEFT)
        {
            // 4.5 Cập nhật chỉ số
            StatsManager.UpdateStats(0, -10, 0, 10);
        }
        else if (option == CardManager.CardInputType.RIGHT)
        {
            return; // Do nothing
        }
    }


    public string GetDescription()
    {
        return "Quán của chúng ta khá cũ, bạn có muốn nâng cấp cơ sở vật chất không?";
    }
}
