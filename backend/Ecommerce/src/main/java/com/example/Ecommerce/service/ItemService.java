package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.request.ItemRequestDto;
import com.example.Ecommerce.exception.CustomerNotFound;
import com.example.Ecommerce.exception.ProductNotAvailable;
import com.example.Ecommerce.model.Item;
import org.springframework.http.ResponseEntity;

public interface ItemService {
    public Item addItem(ItemRequestDto itemRequestDto) throws ProductNotAvailable, CustomerNotFound;
}
