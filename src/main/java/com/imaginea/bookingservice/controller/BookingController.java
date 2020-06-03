package com.imaginea.bookingservice.controller;

import com.imaginea.bookingservice.constants.DatabaseConstant;
import com.imaginea.bookingservice.domain.BookingStatus;
import com.imaginea.bookingservice.domain.Screen;
import com.imaginea.bookingservice.domain.BookingDetail;
import com.imaginea.bookingservice.domain.Seat;
import com.imaginea.bookingservice.dto.PaymentDTO;
import com.imaginea.bookingservice.dto.ScreenDTO;
import com.imaginea.bookingservice.exception.BookMyShowException;
import com.imaginea.bookingservice.service.BookingService;
import com.imaginea.bookingservice.service.TheatreService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class BookingController {

	@Autowired
	private BookingService bookingService;
	
	@Autowired
	private TheatreService theatreService;
	
	@GetMapping("/data")
	public List<Screen> getAllScreenData(){
		return DatabaseConstant.screens;
	}

	@GetMapping("/screens")
	public List<ScreenDTO> getScreens(){
		return bookingService.getScreens();
	}

	@GetMapping("/screen/{screenId}/availableSeats")
	public Map<Seat, BookingDetail> getAvailableSeats(@PathVariable Long screenId) throws BookMyShowException {
		return bookingService.getAvailableSeats(screenId);
	}

	/* TODO :- This rate limiter should be implemented in the service which is consuming this API
	 * API to lock the Seat
	 * @param screenId
	 * @param seatId
	 * @return
	 */
	@GetMapping("/screen/{screenId}/seat/{seatId}")
	@RateLimiter(name="rateLimiterService",fallbackMethod = "rateLimiterFallBackMethod")
	public BookingStatus bookingInitialization(@PathVariable Long screenId,@PathVariable Long seatId) throws BookMyShowException {
		return bookingService.bookingInitialization(screenId,seatId);
	}

	@GetMapping("/screen/{screenId}/book/{userName}")
	public String bookSeats(@PathVariable Long screenId,@PathVariable(value = "userName") String userName) throws BookMyShowException {
		return bookingService.bookSeats(screenId,userName);
	}

	@GetMapping("/payment/details")
	public Object getBookingDetails(){
		return bookingService.getAllPaymentDetails();
	}

	public BookingStatus rateLimiterFallBackMethod(Long screenId,Long seatId,Throwable t) throws BookMyShowException {
		Map<Seat,BookingDetail> seat = theatreService.getSeat(screenId,seatId);
		BookingStatus bookingStatus = null;
		for(Map.Entry entry:seat.entrySet()){
			BookingDetail bookingDetail = (BookingDetail) entry.getValue();
			bookingStatus = bookingDetail.getBookingStatus();
			break;
		}
		if(t instanceof BookMyShowException){
			log.error("Seat is already Locked.Please select another seat!, cause - {}",t.toString());
		}
		else{
			log.error("Inside rateLimiterFallBackMethod, cause - {}",t.toString());
		}
		return bookingStatus;
	}
}
