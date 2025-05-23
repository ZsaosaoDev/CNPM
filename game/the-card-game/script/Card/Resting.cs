using Godot;

/// <summary>
/// Thẻ bài nghỉ ngơi.
/// </summary>
public class Resting : ICard
{
    public void doAction(CardManager.CardInputType option)
    {
        if (option == CardManager.CardInputType.LEFT)
        {
            StatsManager.UpdateStats(5, -1, 0, 0);
            CardDatabase.Instance.AddSequenceCard(new RestingResult());
        }
        else if (option == CardManager.CardInputType.RIGHT)
        {
            StatsManager.UpdateStats(-5, 1, 0, 0);
        }
    }

    public string GetDescription()
    {
        return "Cậu làm việc chăm chỉ quá rồi, hãy nghỉ ngơi một chút đi!";
    }

    public Texture2D GetTexture()
    {
        return ResourceLoader.Load<Texture2D>("res://assets/img/aya/worry.png");
    }
}

/// <summary>
/// Thẻ bài kết quả của hành động nghỉ ngơi.
/// </summary>
public class RestingResult : ICard
{
    public void doAction(CardManager.CardInputType option)
    {
        return; 
    }

    public string GetDescription()
    {
        return "Thời gian nghỉ ngơi";
    }

    public Texture2D GetTexture()
    {
        return ResourceLoader.Load<Texture2D>("res://assets/img/card/resting.png");
    }
}