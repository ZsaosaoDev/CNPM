using Godot;

public partial class StatsManager : Node
{
    public static StatsManager Instance { get; private set; }

    [Export]
    private Stats stats;

    public override void _Ready()
    {
        if (Instance == null)
        {
            Instance = this;
        }
        else
        {
            QueueFree();
        }

        stats = GetNode<Stats>("Stats");
    }

    public static void UpdateStats(int healthChange, int moneyChange, int customerChange, int happinessChange)
    {
        if (Instance != null)
        {
            // 4.6. Cập nhật chỉ số
            Instance.stats.UpdateStats(healthChange, moneyChange, customerChange, happinessChange);
        }
    }
}