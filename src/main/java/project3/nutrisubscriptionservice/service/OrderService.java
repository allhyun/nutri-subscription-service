package project3.nutrisubscriptionservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project3.nutrisubscriptionservice.dto.OrderDTO;
import project3.nutrisubscriptionservice.dto.ProductDTO;
import project3.nutrisubscriptionservice.dto.UserDTO;
import project3.nutrisubscriptionservice.entity.OrderEntity;
import project3.nutrisubscriptionservice.repository.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserProfileService userProfileService;

    @Autowired
    ProductService productService;

    //주문목록 조회
    @Transactional(readOnly = true)//데이터베이스의 상태를 변경하는 작업 또는 한번에 수행되어야 하는 연산들
    public List<OrderDTO> findAll(){
        List<OrderDTO> orderDTOList = orderRepository.findAll().stream()
                .map(order -> {return new OrderDTO(order);}).toList();

        for(OrderDTO orderDTO : orderDTOList){
            settingProductDTO(orderDTO);
        }
        return orderDTOList;
    }

    //주문번호로 주문 조회
    public OrderDTO findById(long order_id){
        //order_id  로 주문정보 조회
        OrderEntity orderEntity = orderRepository.findById(order_id)
                .orElseThrow(()->new IllegalArgumentException("해당 주문번호가 없습니다. order_id="+order_id));

        //orderDTO생성
        OrderDTO orderDTO = new OrderDTO(orderEntity);

        settingProductDTO(orderDTO);
        return  orderDTO;
    }

    //유저의 id로 주문목록 조회
    @Transactional(readOnly = true)
    public List<OrderDTO> findByUserId(long id){
        List<OrderDTO> orderDTOList = orderRepository.findByUserId(id).stream()
                .map(order -> {return new OrderDTO(order);}).collect(Collectors.toList());
        for(OrderDTO orderDTO : orderDTOList){
            settingProductDTO(orderDTO);
        }
        return orderDTOList;
    }

    //현재 인증된 사용자의 주문목록 조회

//    @Transactional(readOnly = true)
//    public List<OrderDTO> findByMyUser(){
//        //현재 인증된 사용자의 userId 조회
//        UserDTO userDTO = new UserDTO(userProfileService.loadUserByUsername().getUsername())
//    }





    @Transactional(readOnly = true)
    public void  settingProductDTO(OrderDTO orderDTO){
        orderDTO.setProducts(productService.getAllProducts());
    }
}
