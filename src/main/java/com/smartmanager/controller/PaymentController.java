package com.smartmanager.controller;
import com.razorpay.*;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class PaymentController {
    @PostMapping("/create-order")
    @ResponseBody
    public String createOrder(@RequestBody Map<String,Object> data) throws RazorpayException {
        int amt= Integer.parseInt(data.get("amount").toString());

      var client=new RazorpayClient("rzp_test_uS8ICCHpVMs0ZG","jfB5QekZRd81yKmIlb0JBkDV");
        JSONObject ob= new JSONObject();
        ob.put("amount",amt*100);
        ob.put("currency","INR");
        ob.put("receipt","txn_1234");

        //Creating new order
        Order order= client.Orders.create(ob);
     //   System.out.println(order);

        //saving order info in db

        return order.toString();
    }
}
