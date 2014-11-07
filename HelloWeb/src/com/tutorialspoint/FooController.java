package com.tutorialspoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FooController{
   
   @RequestMapping(value="/postListener")
   public Greeting postListener(HttpServletRequest request, 
	        HttpServletResponse response)
   {
	   //Hallo miteinander
	   
	   System.out.println(request.getSession().getId());
	   return new Greeting(11,"foo");
   }

}