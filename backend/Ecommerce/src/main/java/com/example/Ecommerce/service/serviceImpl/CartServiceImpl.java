package com.example.Ecommerce.service.serviceImpl;

import com.example.Ecommerce.dto.request.ItemRequestDto;
import com.example.Ecommerce.dto.request.OrderRequestDto;
import com.example.Ecommerce.dto.response.CartResponseDto;
import com.example.Ecommerce.dto.response.ItemResponseDto;
import com.example.Ecommerce.dto.response.OrderResponseDto;
import com.example.Ecommerce.enums.CardType;
import com.example.Ecommerce.enums.ProductStatus;
import com.example.Ecommerce.exception.CartException;
import com.example.Ecommerce.exception.InvalidCard;
import com.example.Ecommerce.exception.CustomerNotFound;
import com.example.Ecommerce.model.*;
import com.example.Ecommerce.repository.*;
import com.example.Ecommerce.service.CartService;
import com.example.Ecommerce.service.ItemService;
import com.example.Ecommerce.service.OrderService;
import com.example.Ecommerce.transformer.CartTransformer;
import com.example.Ecommerce.transformer.ItemTransformer;
import com.example.Ecommerce.transformer.OrderTransformer;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    ItemService itemService;

    @Autowired
    CartRepository cartRepo;

    @Autowired
    CustomerRepository customerRepo;

    @Autowired
    CardRepository cardRepo;

    @Autowired
    OrderService orderService;

    @Autowired
    ItemRepository itemRepo;

    @Autowired
    OrderRepository orderRepo;

    @Override
    public ResponseEntity addCart(ItemRequestDto itemRequestDto) {
        Item savedItem;
        //First we need to create the item and make sure it passes all checks for availability
        try{
            savedItem = itemService.addItem(itemRequestDto);
        } catch( Exception e){
            return new ResponseEntity( e.getMessage() , HttpStatus.BAD_REQUEST);
        }

        Customer customer = customerRepo.findById(itemRequestDto.getCustomerId()).get();

        Cart cart = customer.getCart();
        //update contents in cart
        int newTotal = cart.getCartTotal() + savedItem.getRequiredQuantity()*savedItem.getProduct().getPrice(); //total price increase
        cart.setCartTotal(newTotal);
        cart.getItems().add(savedItem); //new item added
        cart.setNumberOfItems(cart.getItems().size()); //size updated
        savedItem.setCart(cart);

        cartRepo.save(cart);

        CartResponseDto cartResponseDto = CartTransformer.CartToCartResponseDto(cart);
        cartResponseDto.setCustomerName(customer.getName());

        List<ItemResponseDto> itemResponseDtoList = new ArrayList<>();

        for(Item itemEntity : cart.getItems()){
            ItemResponseDto itemResponseDto = ItemTransformer.ItemToItemResponseDto(itemEntity);
            itemResponseDto.setProductName(itemEntity.getProduct().getProductName());
            itemResponseDto.setPriceOfOneItem(itemEntity.getProduct().getPrice());
            itemResponseDto.setTotalPrice(itemEntity.getRequiredQuantity() * itemEntity.getProduct().getPrice());

            itemResponseDtoList.add(itemResponseDto);
        }

        cartResponseDto.setItemList(itemResponseDtoList);

        return new ResponseEntity(cartResponseDto , HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity checkoutCart(OrderRequestDto orderRequestDto) throws Exception {
        //Check if customer Id is correct
        Customer customer;
        try{
            customer = customerRepo.findById(orderRequestDto.getCustomerId()).get();
        } catch(Exception e){
            return new ResponseEntity(new CustomerNotFound("Customer not found").getMessage(), HttpStatus.BAD_REQUEST);
        }

        //Check if card details are correct
        Card card = cardRepo.findByCardNo(orderRequestDto.getCardNo());
        if(card == null){
            return new ResponseEntity(new InvalidCard("Invalid Card Number").getMessage(), HttpStatus.BAD_REQUEST);
        } else if(card.getCvv() != orderRequestDto.getCvv()){
            return new ResponseEntity(new InvalidCard("Invalid CVV details").getMessage(), HttpStatus.BAD_REQUEST);
        } else if(card.getExpiryDate().compareTo(LocalDate.now()) < 0){
            return new ResponseEntity(new InvalidCard("Card expired!").getMessage(), HttpStatus.BAD_REQUEST);
        }

        //Check if cart is empty
        Cart cart = cartRepo.findById(customer.getCart().getId()).get(); //To solve cart resetting issue

        if(cart.getNumberOfItems() == 0){
            return new ResponseEntity(new CartException("No items in cart!").getMessage(), HttpStatus.BAD_REQUEST);
        }

        Ordered order;

        try{
            order = orderService.placeOrder(customer , card , cart);
            //Update customer's order list
            customer.getOrderedList().add(order);
            //reset cart
            resetCart(cart);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        //save order
        Ordered savedOrder = orderRepo.save(order);

        OrderResponseDto orderResponseDto = OrderTransformer.OrderToOrderResponseDto(savedOrder);

        List<ItemResponseDto> itemResponseDtoList = new ArrayList<>();

        for(Item itemEntity : savedOrder.getItemList()){
            ItemResponseDto itemResponseDto = ItemTransformer.ItemToItemResponseDto(itemEntity);
            itemResponseDto.setProductName(itemEntity.getProduct().getProductName());
            itemResponseDto.setPriceOfOneItem(itemEntity.getProduct().getPrice());
            itemResponseDto.setTotalPrice(itemEntity.getRequiredQuantity() * itemEntity.getProduct().getPrice());

            itemResponseDtoList.add(itemResponseDto);
        }

        orderResponseDto.setItemList(itemResponseDtoList);
        orderResponseDto.setCustomerName(customer.getName());



        return new ResponseEntity(orderResponseDto , HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity deleteFromCart(int customerId ,int itemId) throws Exception {
        //Check if customer Id is correct
        Customer customer;
        try{
            customer = customerRepo.findById(customerId).get();
        } catch(Exception e){
            return new ResponseEntity(new CustomerNotFound("Customer not found").getMessage(), HttpStatus.BAD_REQUEST);
        }

        //Check if item exists
        Item item;
        try{
            item = itemRepo.findById(itemId).get();
        } catch(Exception e){
            return new ResponseEntity(new CartException("Item Not Found").getMessage(), HttpStatus.BAD_REQUEST);
        }

        Cart cart = customer.getCart();
        List<Item> itemList = cart.getItems();
        for(Item curritem : itemList){
            if(curritem.getId() == itemId){
                curritem.setCart(null);
            }
        }
        itemList.remove(item);
        cart.setCartTotal(cart.getCartTotal() - (item.getProduct().getPrice() * item.getRequiredQuantity()));
        cart.setNumberOfItems(cart.getNumberOfItems() - 1);

        return new ResponseEntity(ItemTransformer.ItemToItemResponseDto(item) , HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity viewEntireCart(int customerId) {
        //Check if customer Id is correct
        Customer customer;
        try{
            customer = customerRepo.findById(customerId).get();
        } catch(Exception e){
            return new ResponseEntity(new CustomerNotFound("Customer not found").getMessage(), HttpStatus.BAD_REQUEST);
        }

        Cart cart = customer.getCart();

        List<ItemResponseDto> itemResponseDtoList = new ArrayList<>();
        for(Item item : cart.getItems()){
            itemResponseDtoList.add(ItemTransformer.ItemToItemResponseDto(item));
        }

        return new ResponseEntity(itemResponseDtoList , HttpStatus.FOUND);
    }

    public void resetCart(Cart cart){

        cart.setCartTotal(0);
        for(Item item: cart.getItems()){
            item.setCart(null);
        }
        cart.setNumberOfItems(0);
        cart.getItems().clear();

    }
}
