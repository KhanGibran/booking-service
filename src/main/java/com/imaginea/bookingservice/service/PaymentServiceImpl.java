package com.imaginea.bookingservice.service;

import com.imaginea.bookingservice.domain.PaymentStatus;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private RestTemplate restTemplate;

	@Override
	@CircuitBreaker(name = "cBPaymentService")
	public String doPayment(String bookingId) {
		return restTemplate.getForObject("/book/"+bookingId+"/pay", PaymentStatus.class).name();
	}

	public String cBFallMethodForPayment(String bookingId,Throwable t){
		log.error("Inside fallBackMethodForPayment, cause - {}",t.toString());
		return PaymentStatus.PAYMENT_FAILED.name();
	}
	@Override
	@Retry(name = "retryPaymentDetailsService",fallbackMethod = "retryFallBackMethodForGetPaymentDetails")
	public Object getAllPaymentDetails(){
		return restTemplate.getForObject("/details",Object.class);
	}

	public Object retryFallBackMethodForGetPaymentDetails(Throwable t){
		log.error("Inside retryFallBackMethodGetAllPaymentDetailsService, cause - {}",t.toString());
		return "Failed To Extract Payment Details";
	}
}
