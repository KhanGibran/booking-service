package com.imaginea.bookingservice.service;

import com.imaginea.bookingservice.dto.PaymentDTO;

import java.util.Map;

public interface PaymentService {
	String doPayment(String bookingId);

	Object getAllPaymentDetails();
}
