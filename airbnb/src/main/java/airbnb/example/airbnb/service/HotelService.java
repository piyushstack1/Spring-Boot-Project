package airbnb.example.airbnb.service;

import airbnb.example.airbnb.dto.HotelDto;
import airbnb.example.airbnb.dto.HotelInfoDto;
import airbnb.example.airbnb.dto.HotelInfoRequestDto;

import java.util.List;


public interface HotelService {
     HotelDto createNewHotel(HotelDto hotelDto);

    HotelDto getHotelById(Long hotelId);

    HotelDto updateHotelById(Long id, HotelDto hotelDto);

    void deleteHotelById(Long hotelId);

    void activateHotel(Long hotelId);

    HotelInfoDto getHotelInfoById(Long hotelId, HotelInfoRequestDto hotelInfoRequestDto);

    List<HotelDto> getAllHotels();
}
