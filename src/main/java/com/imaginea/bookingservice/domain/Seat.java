package com.imaginea.bookingservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Seat{
	private Long seatId;
	private String seatNumber;

	@Override public String toString() {
		return "Seat{" + "seatId=" + seatId + ", seatNumber='" + seatNumber + '\'' + '}';
	}
}
