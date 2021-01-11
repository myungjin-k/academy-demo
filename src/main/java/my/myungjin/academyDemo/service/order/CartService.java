package my.myungjin.academyDemo.service.order;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.item.ItemDisplayOptionRepository;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.member.MemberRepository;
import my.myungjin.academyDemo.domain.order.CartItem;
import my.myungjin.academyDemo.domain.order.CartRepository;
import my.myungjin.academyDemo.error.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CartService {

    private final MemberRepository memberRepository;

    private final ItemDisplayOptionRepository itemRepository;

    private final CartRepository cartRepository;

    @Transactional(readOnly = true)
    public List<CartItem> findByMember(@Valid Id<Member, String> memberId, @Valid Id<Member, String> loginUserId){
        if(!loginUserId.value().equals(memberId.value()))
            throw new IllegalArgumentException("member id must be equal to login user id(member="+ memberId+", loginUser=" +loginUserId +")");
        return cartRepository.findAllByMember(findMember(memberId));
    }

    @Transactional
    public CartItem add(@Valid Id<Member, String> memberId, @Valid Id<Member, String> loginUserId,
                        @Valid Id<ItemDisplay.ItemDisplayOption, String> itemId, @Positive int count){
        if(!loginUserId.value().equals(memberId.value()))
            throw new IllegalArgumentException("member id must be equal to login user id(member="+ memberId+", loginUser=" +loginUserId +")");

        CartItem cartItem = cartRepository.getByItemOptionId(itemId.value());
        if(cartItem != null){
            cartItem.modify(cartItem.getCount() + count);
            return save(cartItem);
        }

        cartItem = new CartItem(count);
        cartItem.setMember(findMember(memberId));
        cartItem.setItemOption(findItem(itemId));
        return save(cartItem);
    }


    @Transactional
    public CartItem modify(@Valid Id<Member, String> memberId,  @Valid Id<Member, String> loginUserId,
                           @Valid Id<CartItem, String> cartItemId, @Positive int count){
        if(!loginUserId.value().equals(memberId.value()))
            throw new IllegalArgumentException("member id must be equal to login user id(member="+ memberId+", loginUser=" +loginUserId +")");
        return cartRepository.findById(cartItemId.value())
                .map(cartItem -> {
                    cartItem.modify(count);
                    return save(cartItem);
                }).orElseThrow(() -> new NotFoundException(CartItem.class, cartItemId));
    }

    @Transactional
    public CartItem delete(@Valid Id<Member, String> memberId,  @Valid Id<Member, String> loginUserId,
                           @Valid Id<CartItem, String> cartItemId){
        if(!loginUserId.value().equals(memberId.value()))
            throw new IllegalArgumentException("member id must be equal to login user id(member="+ memberId+", loginUser=" +loginUserId +")");
       return cartRepository.findById(cartItemId.value())
                .map(cartItem -> {
                    cartRepository.delete(cartItem);
                    return cartItem;
                }).orElseThrow(() -> new NotFoundException(CartItem.class, cartItemId));
    }

    @Transactional
    public void deleteAllByMember(@Valid Id<Member, String> memberId,  @Valid Id<Member, String> loginUserId){
        if(!loginUserId.value().equals(memberId.value()))
            throw new IllegalArgumentException("member id must be equal to login user id(member="+ memberId+", loginUser=" +loginUserId +")");
        cartRepository.deleteAllByMember(findMember(memberId));
    }
    private Member findMember(Id<Member, String> memberId){
        return memberRepository.findById(memberId.value())
                .orElseThrow(() -> new NotFoundException(Member.class, memberId));
    }

    private ItemDisplay.ItemDisplayOption findItem(Id<ItemDisplay.ItemDisplayOption, String> itemId){
        return itemRepository.findById(itemId.value())
                .orElseThrow(() -> new NotFoundException(ItemDisplay.ItemDisplayOption.class, itemId));
    }

    private CartItem save(CartItem cartItem){
        return cartRepository.save(cartItem);
    }
}
