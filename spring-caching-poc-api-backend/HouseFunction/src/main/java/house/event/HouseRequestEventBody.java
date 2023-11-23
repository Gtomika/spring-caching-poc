package house.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseRequestEventBody {
    private String name;
    private String country;
    private String city;
    private String address;
    private int priceEuro;
    private int buildYear;
    private int sizeSquareMeter;
}
