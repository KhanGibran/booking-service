package com.imaginea.bookingservice.service;

import com.imaginea.bookingservice.domain.BookingDetail;
import com.imaginea.bookingservice.domain.BookingStatus;
import com.imaginea.bookingservice.domain.Seat;
import com.imaginea.bookingservice.dto.ScreenDTO;
import com.imaginea.bookingservice.exception.BookMyShowException;

import java.util.List;
import java.util.Map;

public interface TheatreService {
	List<ScreenDTO> getScreens();

	Map<Seat, BookingDetail> getAvailableSeats(Long screenId) throws BookMyShowException;

	Map<Seat,BookingDetail> getSeat(Long screenId,Long seatId) throws BookMyShowException;

	Map<Seat, BookingDetail> getSeats(Long screenId, BookingStatus bookingStatus) throws BookMyShowException;

	Map<Seat, BookingDetail> getSeatsByBookingId(String bookingId);
}
