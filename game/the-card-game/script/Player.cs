using Godot;

public partial class Player : Node
{
    [Export]
    private CardManager cardManager;

   

    public override void _Process(double delta)
    {
        // 4.1.chọn trái hoặc phải
        if (Input.IsActionJustPressed("ui_left"))
        {
            // 4.2. lấy thẻ tiếp theo
            cardManager.NextCard(CardManager.CardInputType.LEFT);
        }
        else if (Input.IsActionJustPressed("ui_right"))
        {
            cardManager.NextCard(CardManager.CardInputType.RIGHT);
        }
    }
}