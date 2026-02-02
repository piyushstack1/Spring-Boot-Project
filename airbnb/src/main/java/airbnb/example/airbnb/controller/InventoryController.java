package airbnb.example.airbnb.controller;


import airbnb.example.airbnb.dto.InventoryDto;
import airbnb.example.airbnb.dto.UpdateInventoryRequestDto;
import airbnb.example.airbnb.entity.Inventory;
import airbnb.example.airbnb.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class InventoryController {

    private  final InventoryService inventoryService;

    @GetMapping("/rooms/{roomsId}")
    @Operation(summary = "Get all inventory of a room", tags = {"Admin Inventory"})
    public ResponseEntity<List<InventoryDto>> getAllInventoryByRoom(@PathVariable Long roomsId){
        List<InventoryDto> lists = inventoryService.getAllInventoryByRoom(roomsId);

        return ResponseEntity.ok(lists);
    }

    @PatchMapping("/room/{roomId}")
    @Operation(summary = "Update the inventory of a room", tags = {"Admin Inventory"})
    public ResponseEntity<Void> updateInventory(@PathVariable Long roomId,
                                                @RequestBody UpdateInventoryRequestDto updateInventoryRequestDto) {
        inventoryService.updateInventory(roomId, updateInventoryRequestDto);
        return ResponseEntity.noContent().build();
    }
}
