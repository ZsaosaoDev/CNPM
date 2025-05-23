public class TutorialCard : ICard
{
    private string description;

    public TutorialCard(string description)
    {
        this.description = description;
    }

    public string GetDescription()
    {
        return description;
    }

    public void doAction(CardManager.CardInputType option)
    {
        return; // Implement the action logic here
    }
}