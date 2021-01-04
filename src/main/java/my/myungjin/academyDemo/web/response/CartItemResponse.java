package my.myungjin.academyDemo.web.response;

import lombok.Getter;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.order.CartItem;

@Getter
public class CartItemResponse {

    private String cartItemId;

    private int count;

    private int originalPrice;

    private String itemName;

    private int itemPrice;

    private String itemOptionId;

    private String optionSize;

    private String optionColor;

    public CartItemResponse of(CartItem entity){
        this.cartItemId = entity.getId();
        this.count = entity.getCount();
        ItemDisplay display = entity.getItemOption().getItemDisplay();
        this.itemName = display.getItemDisplayName();
        this.originalPrice = display.getItemMaster().getPrice();
        this.itemPrice = display.getSalePrice();
        ItemDisplay.ItemDisplayOption option = entity.getItemOption();
        this.itemOptionId =  option.getId();
        this.optionColor = option.getColor();
        this.optionSize = option.getSize();
        return this;
    }


}
