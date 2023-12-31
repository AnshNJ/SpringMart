package com.example.Ecommerce.service.serviceImpl;

import com.example.Ecommerce.dto.request.DirectOrderRequestDto;
import com.example.Ecommerce.dto.response.ItemResponseDto;
import com.example.Ecommerce.dto.response.OrderResponseDto;
import com.example.Ecommerce.enums.ProductStatus;
import com.example.Ecommerce.exception.CustomerNotFound;
import com.example.Ecommerce.exception.InvalidCard;
import com.example.Ecommerce.exception.ProductNotAvailable;
import com.example.Ecommerce.model.*;
import com.example.Ecommerce.repository.*;
import com.example.Ecommerce.service.CardService;
import com.example.Ecommerce.service.CartService;
import com.example.Ecommerce.service.OrderService;
import com.example.Ecommerce.service.ProductService;
import com.example.Ecommerce.transformer.ItemTransformer;
import com.example.Ecommerce.transformer.OrderTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    ProductServiceImpl productServiceImpl;

    @Autowired
    CustomerRepository customerRepo;

    @Autowired
    ProductRepository productRepo;

    @Autowired
    CardRepository cardRepo;

//    @Autowired
//    private JavaMailSender emailSender;


    @Override
    public Ordered placeOrder(Customer customer, Card card , Cart cart) throws Exception {

        Ordered order = new Ordered();
        order.setOrderNo(UUID.randomUUID().toString());
        order.setOrderDate(LocalDate.now());
        order.setTotalValue(cart.getCartTotal());

        //Encode card Number
        String cardNo = card.getCardNo();

        order.setCardUsed(maskCardNo(cardNo));

        order.setCustomer(customer);

        //Make sure total items quantity don't overlap total quantity
        List<Item> orderList = new ArrayList<>();
        for(Item item : cart.getItems()){
            try{
                productServiceImpl.decreaseProductQuantity(item);
                orderList.add(item);
            } catch(Exception e){
                throw new Exception(e.getMessage());
            }
        }
        order.setItemList(orderList);
        orderList.forEach(item -> item.setOrdered(order));

//        //SMTP MAIL
//        String text = "Congratulations! Cart checkout successful with id: "+ cart.getId()+" costing Rs."+cart.getCartTotal()+" only.";
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("noreply@baeldung.com");
//        message.setTo(customer.getEmailId());
//        message.setSubject("Cart checkout successful");
//        message.setText(text);
//        emailSender.send(message);
//
        return order;
    }

    @Override
    public ResponseEntity placeOrder(DirectOrderRequestDto directOrderRequestDto) throws Exception {
        //Customer checks
        Customer customer;
        //See if customer exists
        try{
            customer = customerRepo.findById(directOrderRequestDto.getCustomerId()).get();
        } catch(Exception e){
            return new ResponseEntity(new CustomerNotFound("Customer Not Found").getMessage() , HttpStatus.BAD_REQUEST);
        }

        //Product Checks
        Product product;
        //See if product exists
        try{
            product = productRepo.findById(directOrderRequestDto.getProductId()).get();
        } catch (Exception e){
            return new ResponseEntity(new ProductNotAvailable("Product Not Found").getMessage()  , HttpStatus.BAD_REQUEST);
        }

        //See if the quantity is more than required quantity
        if(product.getQuantity() < directOrderRequestDto.getRequiredQuantity() || product.getProductStatus() != ProductStatus.AVAILABLE){
            return new ResponseEntity(new ProductNotAvailable("Product Out Of Stock").getMessage()  , HttpStatus.BAD_REQUEST);
        }

        //Check if card details are correct
        Card card = cardRepo.findByCardNo(directOrderRequestDto.getCardNo());
        if(card == null){
            return new ResponseEntity(new InvalidCard("Invalid Card Number").getMessage() , HttpStatus.BAD_REQUEST);
        } else if(card.getCvv() != directOrderRequestDto.getCvv()){
            return new ResponseEntity(new InvalidCard("Invalid CVV details").getMessage() , HttpStatus.BAD_REQUEST);
        } else if(card.getExpiryDate().compareTo(LocalDate.now()) < 0){
            return new ResponseEntity(new InvalidCard("Card expired!").getMessage() , HttpStatus.BAD_REQUEST);
        }

        //Create item
        Item item = Item.builder()
                .requiredQuantity(directOrderRequestDto.getRequiredQuantity())
                .product(product)
                .build();

        //Decrease product quantity
        try{
            productServiceImpl.decreaseProductQuantity(item);
        } catch(Exception e){
            throw new Exception(e.getMessage());
        }

        //Prepare order
        Ordered order = new Ordered();
        order.setOrderNo(UUID.randomUUID().toString());
        order.setOrderDate(LocalDate.now());

        //Encode card Number
        String cardNo = card.getCardNo();
        order.setCardUsed(maskCardNo(cardNo));

        order.setCustomer(customer);
        order.setTotalValue(item.getProduct().getPrice() * item.getRequiredQuantity());
        order.getItemList().add(item);

        customer.getOrderedList().add(order);
        item.setOrdered(order);
        product.getItemList().add(item);

        Ordered savedOrder = orderRepo.save(order); //saves order and item

        //Prepare orderResponse Dto
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

//        //SMTP MAIL
//        String text = "Congratulations! Order placed with id: "+ order.getOrderNo()+" costing Rs."+order.getTotalValue()+" only.";
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("noreply@baeldung.com");
//        message.setTo(customer.getEmailId());
//        message.setSubject("Order placed");
//        message.setText(text);
//        emailSender.send(message);
//
        return new ResponseEntity(orderResponseDto , HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity getAllOrdersFromCustomer(int customerId) throws CustomerNotFound {
        Customer customer;
        try{
            customer = customerRepo.findById(customerId).get();
        } catch (Exception e){
            return new ResponseEntity(new CustomerNotFound("Customer Not Found").getMessage() , HttpStatus.BAD_REQUEST);
        }
        customerRepo.deleteById(customerId);

        List<Ordered> orderedList = customer.getOrderedList();
        List<OrderResponseDto> orderResponseDtoList = new ArrayList<>();
        for(Ordered order : orderedList){
            orderResponseDtoList.add(OrderTransformer.OrderToOrderResponseDto(order));
        }

        return new ResponseEntity(orderResponseDtoList , HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity getRecentOrders(int numberOfOrders) {
        List<Ordered> orderedList = orderRepo.findAll();

        PriorityQueue<Ordered> pq = new PriorityQueue<>((a,b) -> {
            return a.getOrderDate().compareTo(b.getOrderDate());
        });

        for(Ordered order : orderedList){
            pq.add(order);
        }

        List<OrderResponseDto> orderResponseDtoList = new ArrayList<>();

        for(int i=0;i<numberOfOrders;i++){
            if(pq.isEmpty()) break;
            Ordered currOrder = pq.remove();
            orderResponseDtoList.add(OrderTransformer.OrderToOrderResponseDto(currOrder));
        }

        return new ResponseEntity( orderResponseDtoList , HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity highestValueOrder(int customerId) throws CustomerNotFound {
        Customer customer;
        try{
            customer = customerRepo.findById(customerId).get();
        } catch (Exception e){
            return new ResponseEntity(new CustomerNotFound("Customer Not Found").getMessage() , HttpStatus.BAD_REQUEST);
        }
        customerRepo.deleteById(customerId);

        List<Ordered> orderedList = customer.getOrderedList();
        int highestVal = 0;
        for(Ordered order : orderedList){
            highestVal = Math.max(highestVal , order.getTotalValue());
        }

        return new ResponseEntity(customer.getName()+" had the highest gross order value for amount : "+highestVal , HttpStatus.FOUND);
    }

    public String maskCardNo(String cardNo){
        String maskedCardNo = "";
        for(int len=0;len<cardNo.length()-4;len++){
            maskedCardNo += "X";
        }
        maskedCardNo += cardNo.substring(cardNo.length() - 4);
        return maskedCardNo;
    }
}
