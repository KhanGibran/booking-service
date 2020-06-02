package com.imaginea.bookingservice.service;

import com.imaginea.bookingservice.domain.BookingDetail;
import com.imaginea.bookingservice.domain.BookingStatus;
import com.imaginea.bookingservice.domain.Screen;
import com.imaginea.bookingservice.domain.Seat;
import com.imaginea.bookingservice.dto.ScreenDTO;
import com.imaginea.bookingservice.exception.BookMyShowException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.imaginea.bookingservice.constants.DatabaseConstant.screens;

@Service
public class TheatreServiceImpl implements TheatreService {

	@Override
	public List<ScreenDTO> getScreens() {
		List<ScreenDTO> screenDTOS = new ArrayList<>();
		screens.forEach(screen -> {
			ScreenDTO screenDTO = new ScreenDTO();
			screenDTO.setScreenId(screen.getScreenId());
			screenDTO.setScreenName(screen.getScreenName());
			screenDTOS.add(screenDTO);
		});
		return screenDTOS;
	}

	public Map<Seat, BookingDetail> getSeats(Long screenId, BookingStatus bookingStatus) throws BookMyShowException {
		Screen screen =
				screens.stream()
						.filter(scr-> scr.getScreenId().equals(screenId)).findFirst().orElse(null);

		Map<Seat, BookingDetail> availableSeats = null;

		if(screen != null){
			if(bookingStatus==null){
				availableSeats = screen.getSeats();
				return availableSeats;
			}

			else{
				availableSeats = screen.getSeats().entrySet().stream()
						.filter(map->map.getValue().getBookingStatus().equals(bookingStatus))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

			}
		}
		else{
			throw new BookMyShowException("Screen Not Found!");
		}
		return availableSeats;
	}

	public Map<Seat,BookingDetail> getSeat(Long screenId,Long seatId) throws BookMyShowException {
		Map<Seat,BookingDetail> seats = getSeats(screenId,null);
		return seats.entrySet().stream()
				.filter(map-> map.getKey().getSeatId().equals(seatId))
				.collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
	}

	private Map<Seat,BookingDetail> getSeatsByUserNameAndStatus(Long screenId,String userName,BookingStatus bookingStatus) throws BookMyShowException {
		Map<Seat,BookingDetail> seats = getSeats(screenId,bookingStatus);
		return seats.entrySet().stream()
				.filter(map-> map.getValue().getBookedBy()!=null && map.getValue().getBookedBy().equalsIgnoreCase(userName))
				.collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
	}

	@Override
	public Map<Seat, BookingDetail> getAvailableSeats(Long screenId) throws BookMyShowException {
		return getSeats(screenId,BookingStatus.FREE);
	}

	@Override
	public Map<Seat, BookingDetail> getSeatsByBookingId(String bookingId) {
		Map<Seat,BookingDetail> bookedSeats = new LinkedHashMap<>();
		screens.forEach(screen -> {
			Map<Seat,BookingDetail> seats = screen.getSeats();
			for(Map.Entry entry:seats.entrySet()){
				Seat seat = (Seat) entry.getKey();
				BookingDetail bookingDetail = (BookingDetail) entry.getValue();
				if(bookingDetail.getBookingId() !=null && bookingDetail.getBookingId().equalsIgnoreCase(bookingId)){
					bookedSeats.put(seat,bookingDetail);
				}
			}
		});
		return bookedSeats;
	}
}
