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

    private double health;
    private double money;
    private double customerCount;
    private double happiness;

    private double incommingHealth;
    private double incommingMoney;
    private double incommingCustomer;
    private double incommingHappiness;


    public override void _Ready()
    {
        health = 0;
        money = 0;
        customerCount = 0;
        happiness = 0;

        incommingHealth = 100;
        incommingMoney = 100;
        incommingCustomer = 10;
        incommingHappiness = 100;
    }

    /// <summary>
    /// Cập nhật các chỉ số của người chơi
    /// Chỉ số bao gồm: sức khỏe, ngân sách, lượt khách, độ hài lòng
    /// Các chỉ số này sẽ được cập nhật khi người chơi thực hiện một hành động nào đó
    /// Các chỉ số này sẽ được cập nhật trong các thẻ sự kiện
    /// Các chỉ số này sẽ được cập nhật trong các thẻ quá trình
    /// </summary>
    /// <param name="healthChange"></param>
    /// <param name="moneyChange"></param>
    /// <param name="customerChange"></param>
    /// <param name="happinessChange"></param>
    public void UpdateStats(int healthChange, int moneyChange, int customerChange, int happinessChange)
    {

        // 4.8. Cập nhật chỉ số
        if (incommingHealth <= 100)
        {
            incommingHealth += healthChange;
        }
        else
        {
            incommingHealth = 100;
        }

        if (incommingMoney <= 100)
        {
            incommingMoney += moneyChange;
        }
        else
        {
            incommingMoney = 100;
        }

        if (incommingCustomer <= 100)
        {
            incommingCustomer += customerChange;
        }
        else
        {
            incommingCustomer = 100;
        }

        if (incommingHappiness <= 100)
        {
            incommingHappiness += happinessChange;
        }
        else
        {
            incommingHappiness = 100;
        }
    }

    private void UpdateLabels()
    {
        healthLabel.Text = Mathf.RoundToInt(health).ToString();
        moneyLabel.Text = Mathf.RoundToInt(money).ToString();
        customer.Text = Mathf.RoundToInt(customerCount).ToString();
        happinessLabel.Text = Mathf.RoundToInt(happiness).ToString();
    }

    public override void _Process(double delta)
    {
        // 4.10. Thay đổi chỉ số một cách mượt mà
        incommingHealth = Mathf.Clamp(incommingHealth, 0, 100);
        incommingMoney= Mathf.Clamp(incommingMoney, 0, 100);
        incommingCustomer= Mathf.Clamp(incommingCustomer, 0, 100);
        incommingHappiness = Mathf.Clamp(incommingHappiness, 0, 100);

        SmoothChange(ref health, incommingHealth, delta);
        SmoothChange(ref money, incommingMoney, delta);
        SmoothChange(ref customerCount, incommingCustomer, delta);
        SmoothChange(ref happiness, incommingHappiness, delta);

        

        
        UpdateLabels();
    }

    private void SmoothChange(ref double currentValue, double targetValue, double delta, double smoothingFactor = 10)
    {
        if (currentValue == targetValue)
        {
            currentValue = targetValue; 
            return; 
        }

        double changeAmount = (targetValue - currentValue) * smoothingFactor * delta;
        currentValue += changeAmount;

    }
}