package com.neuedu.schedule;

import com.neuedu.service.IOrderService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CloseOrderSchedule {

    @Value("${order.close.timeout}")
    private int orderTimeOut;

    @Autowired
    IOrderService orderService;

    @Scheduled(cron = "* */2 * * * *")
    public void closeOrder(){

        Date closeOrderTime = DateUtils.addHours(new Date(),-orderTimeOut);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(closeOrderTime);
        orderService.selectOrderByCreateTime(date);

    }

}
