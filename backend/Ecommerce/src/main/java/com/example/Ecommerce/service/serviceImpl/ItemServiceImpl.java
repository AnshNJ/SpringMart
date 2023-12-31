package com.example.Ecommerce.service.serviceImpl;

import com.example.Ecommerce.dto.request.ItemRequestDto;
import com.example.Ecommerce.enums.ProductStatus;
import com.example.Ecommerce.exception.CustomerNotFound;
import com.example.Ecommerce.exception.ProductNotAvailable;
import com.example.Ecommerce.model.Customer;
import com.example.Ecommerce.model.Item;
import com.example.Ecommerce.model.Product;
import com.example.Ecommerce.repository.CustomerRepository;
import com.example.Ecommerce.repository.ItemRepository;
import com.example.Ecommerce.repository.ProductRepository;
import com.example.Ecommerce.service.ItemService;
import com.example.Ecommerce.transformer.ItemTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemRepository itemRepo;

    @Autowired
    ProductRepository productRepo;

    @Autowired
    CustomerRepository customerRepo;

    @Override
    public Item addItem(ItemRequestDto itemRequestDto) throws ProductNotAvailable, CustomerNotFound {

        //Customer checks
        Customer customer;
        //See if customer exists
        try{
            customer = customerRepo.findById(itemRequestDto.getCustomerId()).get();
        } catch(Exception e){
            throw new CustomerNotFound("Customer Not Found");
        }

        //Product Checks
        Product product;
        //See if product exists
        try{
            product = productRepo.findById(itemRequestDto.getProductId()).get();
        } catch (Exception e){
            throw new ProductNotAvailable("Product Not Found");
        }

        //See if the quantity is more than required quantity
        if(product.getQuantity() < itemRequestDto.getRequiredQuantity() || product.getProductStatus() != ProductStatus.AVAILABLE){
            throw new ProductNotAvailable("Product Out Of Stock");
        }

        Item item = ItemTransformer.ItemRequestDtoToItem(itemRequestDto);
        item.setCart(customer.getCart());
        item.setProduct(product);

        product.getItemList().add(item);
        productRepo.save(product);

        return itemRepo.save(item);
    }
}
