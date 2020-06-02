package com.imaginea.bookingservice.dto;

import com.imaginea.bookingservice.domain.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDTO {
	private Long paymentDate;
	private PaymentStatus paymentStatus;
}
