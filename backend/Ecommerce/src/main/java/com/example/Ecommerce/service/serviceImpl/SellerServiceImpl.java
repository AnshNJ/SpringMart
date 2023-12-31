package com.example.Ecommerce.service.serviceImpl;

import com.example.Ecommerce.dto.request.SellerRequestDto;
import com.example.Ecommerce.dto.response.SellerResponseDto;
import com.example.Ecommerce.exception.EmailAlreadyPresent;
import com.example.Ecommerce.exception.SellerNotFound;
import com.example.Ecommerce.model.Seller;
import com.example.Ecommerce.repository.SellerRepository;
import com.example.Ecommerce.service.SellerService;
import com.example.Ecommerce.transformer.SellerTransformer;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    SellerRepository sellerRepo;

    @Override
    public ResponseEntity addSeller(SellerRequestDto sellerRequestDto) throws Exception {
//            Seller seller = new Seller();
//            seller.setName(sellerRequestDto.getName());
//            seller.setAge(sellerRequestDto.getAge());
//            seller.setEmailId(sellerRequestDto.getEmailId());
//            seller.setMobNo(sellerRequestDto.getMobNo());

            //Check if exists
            if(sellerRepo.findByEmailId(sellerRequestDto.getEmailId()) != null){
                return new ResponseEntity(new SellerNotFound("Seller Doesn't exist").getMessage(), HttpStatus.FOUND);
            }

            //Builder application
            Seller seller = SellerTransformer.SellerRequestDtoToSeller(sellerRequestDto);

            sellerRepo.save(seller);

            SellerResponseDto sellerResponseDto = SellerTransformer.SellerToSellerResponseDto(seller);

            return new ResponseEntity(sellerResponseDto , HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity getSellerByEmail(String emailId) throws SellerNotFound {
        Seller seller = sellerRepo.findByEmailId(emailId);
        if(seller == null) return new ResponseEntity(new SellerNotFound("Seller Doesn't exist").getMessage(), HttpStatus.FOUND);

        //Prepare response
        SellerResponseDto sellerResponseDto = SellerTransformer.SellerToSellerResponseDto(seller);
        return new ResponseEntity(sellerResponseDto , HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity getSellerById(Integer id) throws SellerNotFound {
        try{
            Seller seller = sellerRepo.findById(id).get();
            if(seller == null) return new ResponseEntity(new SellerNotFound("Seller Doesn't exist").getMessage(), HttpStatus.FOUND);

            //Prepare response
            SellerResponseDto sellerResponseDto = SellerTransformer.SellerToSellerResponseDto(seller);
            return new ResponseEntity(sellerResponseDto , HttpStatus.FOUND);
        } catch(Exception e){
            return new ResponseEntity(new SellerNotFound("Seller Doesn't exist").getMessage(), HttpStatus.FOUND);
        }

    }

    @Override
    public ResponseEntity getAllSellers() {
        List<Seller> sellerList = sellerRepo.findAll();
        if(sellerList.isEmpty()) return new ResponseEntity("No sellers found" , HttpStatus.NO_CONTENT);
        return new ResponseEntity(sellerList , HttpStatus.FOUND);
    }

    @Transactional
    @Override
    public ResponseEntity deleteSellerByEmail(String emailId) throws SellerNotFound {
        //Check if exists
        if(sellerRepo.findByEmailId(emailId) == null){
            return new ResponseEntity(new SellerNotFound("Seller Doesn't exist").getMessage(), HttpStatus.FOUND);
        }
        Seller currSeller = sellerRepo.findByEmailId(emailId);

        sellerRepo.deleteByEmailId(emailId);

        SellerResponseDto sellerResponseDto = SellerTransformer.SellerToSellerResponseDto(currSeller);
        return new ResponseEntity(sellerResponseDto , HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity updateSellerUsingEmail(SellerRequestDto sellerRequestDto) throws SellerNotFound {
        //Check if exists
        Seller seller;
        if(sellerRepo.findByEmailId(sellerRequestDto.getEmailId()) == null){
            return new ResponseEntity(new SellerNotFound("Seller Doesn't exist").getMessage(), HttpStatus.FOUND);
        }
        seller = sellerRepo.findByEmailId(sellerRequestDto.getEmailId());

        //Updates
        seller.setMobNo(sellerRequestDto.getMobNo());
        seller.setAge(sellerRequestDto.getAge());
        seller.setName(sellerRequestDto.getName());

        sellerRepo.save(seller);

        SellerResponseDto sellerResponseDto = SellerTransformer.SellerToSellerResponseDto(seller);
        return new ResponseEntity(sellerResponseDto , HttpStatus.CREATED);
    }


}
