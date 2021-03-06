package org.toolkit.communication;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.toolkit.communication.email.util.EmailUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class ToolkitCommunicationApplicationTests {

    @Test
    void contextLoads() {

        EmailUtil.sendMail("test@qq.com", "test一下", "测试发送");

        List<String> receivingAddresses = new ArrayList<>(2);
        receivingAddresses.add("test1@qq.com");
        receivingAddresses.add("test2@qq.com");
        EmailUtil.sendMail(receivingAddresses, "test一下", "测试发送");


        Map<String, String> receivingAddressesAndEmailTitle = new HashMap<>(2);
        receivingAddressesAndEmailTitle.put("test1@qq.com", "测试发送");
        receivingAddressesAndEmailTitle.put("test2@qq.com", "测试发送");
        EmailUtil.sendMail(receivingAddressesAndEmailTitle, "test一下");


        // 测试使用sendMailParam作为入参数时的单次发送情况
        EmailUtil.SendMailParam sendMailParam = EmailUtil.builderSingleSendMailParam();
        sendMailParam.setReceivingAddress("test@qq.com");
        sendMailParam.setEmailContent("test一下");
        sendMailParam.setEmailTitle("测试发送");
        EmailUtil.sendMail(sendMailParam);


        // 测试使用sendMailParam作为入参数时的批量发送情况
        List<EmailUtil.SendMailParam> sendMailParams = new ArrayList<>();
        for (int a = 0; a < 10; a++) {
            EmailUtil.SendMailParam sendMailParam1 = EmailUtil.builderSingleSendMailParam();
            if (a > 2 && a < 6) {
                sendMailParam1.setReceivingAddress("test@qq.com");
            } else {
                sendMailParam1.setReceivingAddress("test@gmail.com");
            }
            sendMailParam1.setEmailContent("test" + a + "下");
            sendMailParam1.setEmailTitle(a + "次测试发送");
            sendMailParams.add(sendMailParam1);
        }
        EmailUtil.sendMail(sendMailParams);
    }

}
