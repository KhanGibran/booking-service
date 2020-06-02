package com.imaginea.bookingservice.constants;

import com.imaginea.bookingservice.domain.BookingStatus;
import com.imaginea.bookingservice.domain.Screen;
import com.imaginea.bookingservice.domain.Seat;
import com.imaginea.bookingservice.domain.BookingDetail;

import java.util.*;

public class DatabaseConstant
{
	private DatabaseConstant() {
	}

	public static List<Screen> screens = new ArrayList<>();
	public static Map<Integer,String> loggedInUser = new LinkedHashMap<>();
	static{
		Screen screen1 = new Screen();
		screen1.setScreenId(1L);
		screen1.setScreenName("screenA");
		screen1.setMovieName("Terminator");
		Map<Seat, BookingDetail> screen1Seats = new LinkedHashMap<>();
		screen1Seats.put(new Seat(1L,"A1"),new BookingDetail(null,BookingStatus.FREE,null));
		screen1Seats.put(new Seat(2L,"A2"),new BookingDetail(null,BookingStatus.FREE,null));
		screen1Seats.put(new Seat(3L,"A3"),new BookingDetail(null,BookingStatus.FREE,null));
		screen1Seats.put(new Seat(4L,"A4"),new BookingDetail(null,BookingStatus.FREE,null));
		screen1Seats.put(new Seat(5L,"A5"),new BookingDetail(null,BookingStatus.FREE,null));
		screen1Seats.put(new Seat(6L,"A6"),new BookingDetail(null,BookingStatus.FREE,null));
		screen1Seats.put(new Seat(7L,"A7"),new BookingDetail(null,BookingStatus.FREE,null));
		screen1.setSeats(screen1Seats);

		Screen screen2 = new Screen();
		screen2.setScreenId(2L);
		screen2.setScreenName("screenB");
		screen2.setMovieName("Batman");
		Map<Seat, BookingDetail> screen2Seats = new LinkedHashMap<>();
		screen2Seats.put(new Seat(1L,"B1"),new BookingDetail(null,BookingStatus.FREE,null));
		screen2Seats.put(new Seat(2L,"B2"),new BookingDetail(null,BookingStatus.FREE,null));
		screen2Seats.put(new Seat(3L,"B3"),new BookingDetail(null,BookingStatus.FREE,null));
		screen2Seats.put(new Seat(4L,"B4"),new BookingDetail(null,BookingStatus.FREE,null));
		screen2Seats.put(new Seat(5L,"B5"),new BookingDetail(null,BookingStatus.FREE,null));
		screen2Seats.put(new Seat(6L,"B6"),new BookingDetail(null,BookingStatus.FREE,null));
		screen2Seats.put(new Seat(7L,"B7"),new BookingDetail(null,BookingStatus.FREE,null));
		screen2.setSeats(screen2Seats);

		screens.add(screen1);
		screens.add(screen2);

		loggedInUser.put(1, "Gibran");
		loggedInUser.put(2,"Manoj");
		loggedInUser.put(3,"Ritesh");
		loggedInUser.put(4,"Subash");
		loggedInUser.put(5,"Kayal");
	}
}
