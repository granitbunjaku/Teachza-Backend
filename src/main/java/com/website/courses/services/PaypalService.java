package com.website.courses.services;

import com.google.gson.Gson;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.website.courses.models.Course;
import com.website.courses.models.CourseRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.Principal;
import java.util.*;

@Component
@RequiredArgsConstructor
public class PaypalService implements Serializable{

    String clientId = "AbYcsduee0PtdrHvGGqPJpZqgmXNE6FTeAU9tx6zPfzDtjdb4PZfOExGeNHmD_9yjc633TLR5e2tHWkr";
    String clientSecret = "EDPi8vnUbC8cgRyxnoCV-lXQ4duChM0zsc-ZTMkCZjV-JFhWijnLoHP7en9aOXARipNL9Lvq38Z5plX8";

    private final CourseService courseService;
    @PreAuthorize("hasRole('STUDENT')")
    public Map<String, Object> createPayment(String sum)
    {
        Map<String, Object> response = new HashMap<>();

        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(sum);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:5173/cancel");
        redirectUrls.setReturnUrl("http://localhost:5173/");
        payment.setRedirectUrls(redirectUrls);

        Payment createdPayment;

        try {
            String redirectUrl = "";
            APIContext apiContext = new APIContext(clientId, clientSecret, "sandbox");
            createdPayment = payment.create(apiContext);
            if(createdPayment != null)
            {
                List<Links> links = createdPayment.getLinks();

                for(Links link : links)
                {
                    if(link.getRel().equals("approval_url"))
                    {
                        redirectUrl = link.getHref();
                        break;
                    }
                }
                response.put("status", "success");
                response.put("redirect_url", redirectUrl);
            }

        } catch (PayPalRESTException e) {
            System.out.println("Error happened during payment creation!");
        }

        return response;

    }
    @PreAuthorize("hasRole('STUDENT')")
    public Map<String, Object> completePayment(HttpServletRequest request, Course course, Principal principal)
    {
        Map<String, Object> response = new HashMap<>();
        Payment payment = new Payment();
        payment.setId(request.getParameter("paymentId"));

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(request.getParameter("PayerID"));

        try {
            APIContext apiContext = new APIContext(clientId, clientSecret, "sandbox");
            Payment createdPayment = payment.execute(apiContext, paymentExecution);
            if(createdPayment != null)
            {
                courseService.joinCourse(course.getId(), principal);
                response.put("status", "success");
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }

        return response;
    }

}
