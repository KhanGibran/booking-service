package com.imaginea.bookingservice.service;

import com.imaginea.bookingservice.domain.BookingDetail;
import com.imaginea.bookingservice.domain.BookingStatus;
import com.imaginea.bookingservice.domain.Seat;
import com.imaginea.bookingservice.dto.PaymentDTO;
import com.imaginea.bookingservice.dto.ScreenDTO;
import com.imaginea.bookingservice.exception.BookMyShowException;

import java.util.List;
import java.util.Map;

public interface BookingService {
	List<ScreenDTO> getScreens();

	Map<Seat, BookingDetail> getAvailableSeats(Long screenId) throws BookMyShowException;

	Map<Seat,BookingDetail> bookingInitialization(Long screenId, Long seatId) throws BookMyShowException;

	String bookSeats(Long screenId, String userName) throws BookMyShowException;

	Object getAllPaymentDetails();
}
