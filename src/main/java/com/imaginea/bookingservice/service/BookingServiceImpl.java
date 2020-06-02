package com.imaginea.bookingservice.service;

import com.imaginea.bookingservice.config.CacheConfig;
import com.imaginea.bookingservice.domain.*;
import com.imaginea.bookingservice.dto.PaymentDTO;
import com.imaginea.bookingservice.dto.ScreenDTO;
import com.imaginea.bookingservice.exception.BookMyShowException;
import com.imaginea.bookingservice.util.MovieBookingUtils;
import io.github.resilience4j.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.imaginea.bookingservice.constants.DatabaseConstant.screens;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private TheatreService theatreService;

	@Autowired
	private CacheConfig cacheConfig;

	@Override
	public List<ScreenDTO> getScreens() {
		return (List<ScreenDTO>) Cache.decorateSupplier(cacheConfig.getCacheContext(),theatreService::getScreens).apply("screensCache");
	}

	@Override
	public Map<Seat, BookingDetail> getAvailableSeats(Long screenId) throws BookMyShowException {
		return theatreService.getAvailableSeats(screenId);
	}

	@Override
	public Map<Seat,BookingDetail> bookingInitialization(Long screenId, Long seatId) throws BookMyShowException {
		Map<Seat,BookingDetail> seat = theatreService.getSeat(screenId,seatId);
		BookingDetail bookingDetail = null;
		for(Map.Entry entry:seat.entrySet()){
			bookingDetail = (BookingDetail) entry.getValue();
			break;
		}
		assert bookingDetail != null;
		if(bookingDetail.getBookingStatus()!=BookingStatus.FREE)
			throw new BookMyShowException("Already Booked. Please choose some other seat!");
		else{
			bookingDetail.setBookingStatus(BookingStatus.LOCKED);
			bookingDetail.setBookedBy("gibran");
		}
		return seat;
	}


	@Override
	public String bookSeats(Long screenId, String userName) throws BookMyShowException {
		Map<Seat,BookingDetail> seats = theatreService.getSeats(screenId,BookingStatus.LOCKED);
		Map<Seat,BookingDetail> bookedSeats = seats.entrySet().stream()
				.filter(map->map.getValue().getBookedBy().equalsIgnoreCase(userName))
				.collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));

		if(bookedSeats.isEmpty()){
			throw new BookMyShowException("No Seat is selected. Please select a seat!");
		}
		String bookingId = MovieBookingUtils.getBookingId();
		BookingDetail bookingDetail = null;
		for(Map.Entry entry:bookedSeats.entrySet()){
			bookingDetail = (BookingDetail) entry.getValue();
			if(bookingDetail.getBookingStatus() == BookingStatus.AWAITING_PAYMENT){
				throw new BookMyShowException("Payment is already initiated!");
			}
			if(bookingDetail.getBookingStatus() == BookingStatus.BOOKED){
				throw new BookMyShowException("Seat is already booked!.Please select another seat!");
			}
			if(bookingDetail.getBookingStatus() == BookingStatus.LOCKED){
				bookingDetail.setBookingStatus(BookingStatus.AWAITING_PAYMENT);
				bookingDetail.setBookingId(bookingId);
			}
		}
		String finalResponse = null;
		String response = doPayment(bookingId);
		if(response.equalsIgnoreCase(PaymentStatus.PAYMENT_SUCCESS.name())){
			for(Map.Entry entry:bookedSeats.entrySet()){
				bookingDetail = (BookingDetail) entry.getValue();
				bookingDetail.setBookingStatus(BookingStatus.BOOKED);
			}
			finalResponse = "Booked Successfully";
		}
		if(response.equalsIgnoreCase(PaymentStatus.PAYMENT_FAILED.name())){
			Map<Seat, BookingDetail> seatsByBookingId = theatreService.getSeatsByBookingId(bookingId);
			BookingDetail myBookingDetails;
			for(Map.Entry entry:seatsByBookingId.entrySet()){
				myBookingDetails = (BookingDetail) entry.getValue();
				myBookingDetails.setBookingStatus(BookingStatus.FREE);
				myBookingDetails.setBookingId(null);
				myBookingDetails.setBookedBy(null);
			}
			finalResponse = "Payment Failed";
		}
		return finalResponse;
	}

	@Override
	public Object getAllPaymentDetails() {
		return paymentService.getAllPaymentDetails();
	}

	private String doPayment(String bookingId) {
		return paymentService.doPayment(bookingId);
	}
}
