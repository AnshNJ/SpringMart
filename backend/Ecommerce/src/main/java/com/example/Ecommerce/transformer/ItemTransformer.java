package com.example.Ecommerce.transformer;

import com.example.Ecommerce.dto.request.ItemRequestDto;
import com.example.Ecommerce.dto.response.ItemResponseDto;
import com.example.Ecommerce.model.Item;

public class ItemTransformer {
    public static Item ItemRequestDtoToItem(ItemRequestDto itemRequestDto){
        return Item.builder()
                .requiredQuantity(itemRequestDto.getRequiredQuantity())
                .build();
    }

    public static ItemResponseDto ItemToItemResponseDto(Item item){
        return ItemResponseDto.builder()
                .quantity(item.getRequiredQuantity())
                .build();
    }
}

