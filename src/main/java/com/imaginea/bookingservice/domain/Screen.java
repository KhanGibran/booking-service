package com.imaginea.bookingservice.domain;

import jdk.nashorn.internal.ir.LabelNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Screen
{
	private Long screenId;
	private String screenName;
	private String movieName;
	private Map<Seat, BookingDetail> seats = new LinkedHashMap<>();
}
