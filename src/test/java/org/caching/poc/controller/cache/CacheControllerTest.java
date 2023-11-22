package org.caching.poc.controller.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.caching.poc.controller.ErrorResponse;
import org.caching.poc.exception.CacheNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("caching")
@WebMvcTest(controllers = CacheController.class)
class CacheControllerTest {

    public static final String CACHE_PATH = "/api/v1/cache";
    public static final String CACHE_NAME = "test-cache";

    @Mock
    private Cache cache;

    @MockBean
    private CacheManager cacheManager;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void clearCache_returns204_whenCacheExists() throws Exception {
        when(cacheManager.getCache(CACHE_NAME)).thenReturn(cache);

        mockMvc.perform(delete(CACHE_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("cacheName", CACHE_NAME))
                .andExpect(status().isNoContent());
    }

    @Test
    public void clearCache_returns404_whenCacheDoesNotExists_andCannotBeCreated() throws Exception {
        when(cacheManager.getCache(CACHE_NAME)).thenReturn(null);

        CacheNotFoundException exception = new CacheNotFoundException(CACHE_NAME);
        ErrorResponse expectedResponse = ErrorResponse.singleError("cache", exception.getMessage());

        mockMvc.perform(delete(CACHE_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("cacheName", CACHE_NAME))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

}