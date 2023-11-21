package org.caching.poc.controller.house;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.caching.poc.config.Constants;
import org.caching.poc.controller.ErrorResponse;
import org.caching.poc.controller.house.dto.HouseRequest;
import org.caching.poc.controller.house.dto.HouseResponse;
import org.caching.poc.exception.HouseNotFoundException;
import org.caching.poc.mapper.HouseMapper;
import org.caching.poc.model.Country;
import org.caching.poc.model.House;
import org.caching.poc.service.HouseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HouseController.class)
class HouseControllerTest {

    private static final String HOUSE_PATH = "/api/v1/houses";

    private static final House HOUSE = House.builder()
            .id(UUID.randomUUID())
            .name("TEST HOUSE")
            .country(Country.AUSTRIA)
            .city("Wien")
            .address("Some avenue 5")
            .buildYear(2012)
            .priceEuro(BigDecimal.valueOf(40000))
            .sizeSquareMeter(60)
            .build();

    private static final HouseResponse HOUSE_RESPONSE = HouseResponse.builder()
            .id(HOUSE.id())
            .name(HOUSE.name())
            .country(HOUSE.country())
            .city(HOUSE.city())
            .address(HOUSE.address())
            .buildYear(HOUSE.buildYear())
            .priceEuro(HOUSE.priceEuro())
            .sizeSquareMeter(HOUSE.sizeSquareMeter())
            .build();

    @MockBean
    private HouseService houseService;

    @MockBean
    private HouseMapper houseMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getHouses_returns200_withHouses() throws Exception {
       List<House> housesFromService = List.of(HOUSE);
       List<HouseResponse> expectedResponse = List.of(HOUSE_RESPONSE);

       when(houseService.getHouses()).thenReturn(housesFromService);
       when(houseMapper.modelsToResponses(housesFromService)).thenReturn(expectedResponse);

       mockMvc.perform(get(HOUSE_PATH)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().string(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    public void getHouseById_returns200_ifHouseExists() throws Exception {
        when(houseService.getHouseById(HOUSE.id())).thenReturn(HOUSE);
        when(houseMapper.modelToResponse(HOUSE)).thenReturn(HOUSE_RESPONSE);

        mockMvc.perform(get(HOUSE_PATH + "/" + HOUSE.id())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(HOUSE_RESPONSE)));
    }

    @Test
    public void getHouseById_returns404_ifHouseDoesNotExist() throws Exception {
        HouseNotFoundException exception = new HouseNotFoundException(HOUSE.id());
        when(houseService.getHouseById(HOUSE.id())).thenThrow(exception);

        ErrorResponse expectedResponse = ErrorResponse.singleError("house", exception.getMessage());

        mockMvc.perform(get(HOUSE_PATH + "/" + HOUSE.id())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    public void createHouse_returns201_ifRequestIsValid() throws Exception {
        HouseRequest validRequest = HouseRequest.builder()
                .name(HOUSE.name())
                .country(HOUSE.country())
                .city(HOUSE.city())
                .address(HOUSE.address())
                .buildYear(HOUSE.buildYear())
                .priceEuro(HOUSE.priceEuro().intValue())
                .sizeSquareMeter(HOUSE.sizeSquareMeter())
                .build();

        when(houseMapper.requestToModel(validRequest)).thenReturn(HOUSE);
        when(houseService.createHouse(HOUSE)).thenReturn(HOUSE);
        when(houseMapper.modelToResponse(HOUSE)).thenReturn(HOUSE_RESPONSE);

        mockMvc.perform(post(HOUSE_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(HOUSE_RESPONSE)));
    }

    @Test
    public void createHouse_returns400_ifRequestIsInvalid() throws Exception {
        HouseRequest invalidRequest = HouseRequest.builder()
                .name(null) //name must be provided
                .country(HOUSE.country())
                .city(HOUSE.city())
                .address(HOUSE.address())
                .buildYear(-2002) //build year must be after a set year
                .priceEuro(HOUSE.priceEuro().intValue())
                .sizeSquareMeter(HOUSE.sizeSquareMeter())
                .build();

        LinkedMultiValueMap<String, String> expectedErrors = new LinkedMultiValueMap<>();
        expectedErrors.put("name", List.of(Constants.NAME_MUST_BE_PROVIDED));
        expectedErrors.put("buildYear", List.of(Constants.BUILD_YEAR_MUST_BE_AFTER));
        ErrorResponse expectedResponse = new ErrorResponse(expectedErrors);

        mockMvc.perform(post(HOUSE_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    public void deleteHouse_returns204_ifHouseExists() throws Exception {
        mockMvc.perform(delete(HOUSE_PATH + "/" + HOUSE.id())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(houseService).deleteHouse(HOUSE.id());
    }

    @Test
    public void deleteHouse_returns404_ifHouseDoesNotExist() throws Exception {
        HouseNotFoundException exception = new HouseNotFoundException(HOUSE.id());
        doThrow(exception).when(houseService).deleteHouse(HOUSE.id());

        ErrorResponse expectedResponse = ErrorResponse.singleError("house", exception.getMessage());

        mockMvc.perform(delete(HOUSE_PATH + "/" + HOUSE.id())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

}