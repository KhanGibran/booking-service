package com.imaginea.bookingservice.util;

import java.util.Random;

public class MovieBookingUtils
{
	private MovieBookingUtils(){

	}

	private static Random random = new Random();

	public static String getBookingId(){
		int leftLimit = 48;
		int rightLimit = 122;
		int targetStringLength = 10;

		return random.ints(leftLimit, rightLimit + 1)
				.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
				.limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();
	}

	public static Integer getLoggedInUserId(){
		int max = 5;
		int min = 1;
		return random.nextInt(max-min+1)+min;
	}
}
