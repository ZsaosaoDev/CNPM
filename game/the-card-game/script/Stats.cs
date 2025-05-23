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

        // 4.8. Cập nhật chỉ số
        
        
        
        customerCount += customerChange;
        happiness += happinessChange;
        if (health <= 100)
        {
            health += healthChange;
        }
        else
        {
            health = 100;
        }

        if (money <= 100)
        {
            money += moneyChange;
        }
        else
        {
            money = 100;
        }

        if (customerCount <= 100)
        {
            customerCount += customerChange;
        }
        else
        {
            customerCount = 100;
        }

        if (happiness <= 100)
        {
            happiness += happinessChange;
        }
        else
        {
            happiness = 100;
        }

        UpdateLabels();
    }

    private void UpdateLabels()
    {
        // 4.9. Cập nhật label
        healthLabel.Text = health.ToString();
        moneyLabel.Text = money.ToString();
        customer.Text = customerCount.ToString();
        happinessLabel.Text = happiness.ToString();
    }
}