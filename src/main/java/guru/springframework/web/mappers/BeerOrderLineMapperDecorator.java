package guru.springframework.web.mappers;

import guru.springframework.domain.BeerOrderLine;
import guru.springframework.services.beer.BeerService;
import guru.springframework.common.model.BeerDto;
import guru.springframework.common.model.BeerOrderLineDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Optional;

public abstract class BeerOrderLineMapperDecorator implements BeerOrderLineMapper {

    private BeerService beerService;
    private BeerOrderLineMapper beerOrderLineMapper;

    @Autowired
    public void setBeerService(BeerService beerService) {
        this.beerService = beerService;
    }

    @Autowired
    @Qualifier("delegate")
    public void setBeerOrderLineMapper(BeerOrderLineMapper beerOrderLineMapper) {
        this.beerOrderLineMapper = beerOrderLineMapper;
    }

    @Override
    public BeerOrderLineDto beerOrderLineToDto(BeerOrderLine line) {
        BeerOrderLineDto orderLineDto = beerOrderLineMapper.beerOrderLineToDto(line);
        Optional<BeerDto> beerDtoOptional = beerService.getBeerByUpc(line.getUpc());

        beerDtoOptional.ifPresent(beerDto -> {
           orderLineDto.setBeerName(beerDto.getBeerName());
           orderLineDto.setBeerStyle(beerDto.getBeerStyle());
           orderLineDto.setPrice(beerDto.getPrice());
           orderLineDto.setBeerId(beerDto.getId());
        });

        return  orderLineDto;
    }
}
