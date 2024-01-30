package com.dna.springboot.myfirstwepapp.hello;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SayHelloController {
    // url : "say-helo" => "Hello! What are you learning today?"
    
    @RequestMapping("say-hello")
    @ResponseBody
    public String sayHello() {
        return "Hello! What are you learning today?";
    }
    
    @RequestMapping("say-hello-html")
    @ResponseBody
    public String sayHelloHtml() {
        StringBuffer sb = new StringBuffer();
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<title>My first HTML Page - Changed</title>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("My first HTML Page with body - Changed");
        sb.append("</body>");
        sb.append("</html>");
        
        return sb.toString();
    }
    
    // "say-hello-jsp" => sayHello.jsp
    // src/main/resources/META-INF/resources/WEB-INF/jsp/sayHello.jsp
    @RequestMapping("say-hello-jsp")
    public String sayHelloJsp() {
        return "sayHello";
    }
}
