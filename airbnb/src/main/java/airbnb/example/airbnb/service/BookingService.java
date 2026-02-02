package airbnb.example.airbnb.service;

import airbnb.example.airbnb.dto.BookingDto;
import airbnb.example.airbnb.dto.BookingRequest;
import airbnb.example.airbnb.dto.GuestDto;
import airbnb.example.airbnb.dto.HotelReportDto;
import airbnb.example.airbnb.entity.enums.BookingStatus;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    BookingDto initialiseBooking(BookingRequest bookingRequest);

    BookingDto addGuests(Long bookingId, List<Long> guestIdList);



    BookingStatus getBookingStatus(Long bookingId);

    List<BookingDto> getMyBookings();

    List<BookingDto> getAllBookingsByHotelId(Long hotelId);

    HotelReportDto getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate);
}
