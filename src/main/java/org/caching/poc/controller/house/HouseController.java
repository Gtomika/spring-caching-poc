package org.caching.poc.controller.house;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.caching.poc.controller.house.dto.HouseRequest;
import org.caching.poc.controller.house.dto.HouseResponse;
import org.caching.poc.mapper.HouseMapper;
import org.caching.poc.model.House;
import org.caching.poc.service.HouseService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/houses")
public class HouseController {

    private final HouseService houseService;
    private final HouseMapper houseMapper;

    @GetMapping
    public List<HouseResponse> getHouses() {
        return houseMapper.modelsToResponses(houseService.getHouses());
    }

    @GetMapping("/{id}")
    public HouseResponse getHouseById(@PathVariable UUID id) {
        return houseMapper.modelToResponse(houseService.getHouseById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HouseResponse createHouse(@RequestBody @Valid HouseRequest houseRequest) {
        House house = houseMapper.requestToModel(houseRequest);
        return houseMapper.modelToResponse(houseService.createHouse(house));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHouse(@PathVariable UUID id) {
        houseService.deleteHouse(id);
    }
}
