package com.example.Ecommerce.dto.request;

import com.example.Ecommerce.model.Card;
import com.example.Ecommerce.model.Cart;
import com.example.Ecommerce.model.Ordered;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CustomerRequestDto {
    String name;
    String emailId;
    Integer age;
    String mobNo;
    String address;
}
