using Godot;
using System;

/// <summary>
/// Interface cho các thẻ bài.
/// </summary>
public interface ICard
{
    /// <summary>
    /// Lấy mô tả của thẻ bài.
    /// </summary>
    /// <returns>Mô tả của thẻ bài.</returns>
    public string GetDescription();
    /// <summary>
    /// Thực hiện hành động của thẻ bài.
    /// </summary>
    /// <param name="option">Lựa chọn của người chơi.</param>
    public void doAction(CardManager.CardInputType option);
    /// <summary>
    /// Lấy hình ảnh của thẻ bài.
    /// </summary>
    /// <returns>Hình ảnh của thẻ bài.</returns>
    public Texture2D GetTexture();
}
