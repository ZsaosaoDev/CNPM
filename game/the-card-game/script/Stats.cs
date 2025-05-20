using Godot;

public partial class Stats : Node
{
    [Export]
    private Label healthLabel;
    [Export]
    private Label moneyLabel;
    [Export]
    private Label customer;
    [Export]
    private Label happinessLabel;

    private int health;
    private int money;
    private int customerCount;
    private int happiness;


    public override void _Ready()
    {
        health = 50;
        money = 50;
        customerCount = 10;
        happiness = 100;
    }

    public void UpdateStats(int healthChange, int moneyChange, int customerChange, int happinessChange)
    {
        health += healthChange;
        money += moneyChange;
        customerCount += customerChange;
        happiness += happinessChange;
        // 4.7. Cập nhật label
        UpdateLabels();
    }

    private void UpdateLabels()
    {
        // 4.8. set label text
        healthLabel.Text = health.ToString();
        moneyLabel.Text = money.ToString();
        customer.Text =  customerCount.ToString();
        happinessLabel.Text = happiness.ToString();
    }
}