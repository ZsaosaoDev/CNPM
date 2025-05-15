public class SimpleCard : ICard
{
    private string name;
    private string description;

    public SimpleCard(string name, string description)
    {
        this.name = name;
        this.description = description;
    }

    public string GetDescription()
    {
        return description;
    }
}