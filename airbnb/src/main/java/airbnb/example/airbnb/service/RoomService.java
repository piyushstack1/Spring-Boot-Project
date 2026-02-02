package airbnb.example.airbnb.service;

import airbnb.example.airbnb.dto.RoomDto;

import java.util.List;

public interface RoomService {


     RoomDto createNewRoom(Long hotelId, RoomDto roomDto) ;

     RoomDto getRoomById(Long roomId);

     List<RoomDto> getAllRoomsInHotel(Long hotelId);

     void deleteRoomById(Long roomId);

    RoomDto updateRoomById(Long hotelId, Long roomId, RoomDto roomDto);
}
