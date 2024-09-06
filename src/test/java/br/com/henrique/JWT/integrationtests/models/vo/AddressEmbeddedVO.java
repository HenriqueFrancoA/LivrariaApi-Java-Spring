package br.com.henrique.JWT.integrationtests.models.vo;

import br.com.henrique.JWT.integrationtests.models.dto.AddressDto;

import java.util.List;

public class AddressEmbeddedVO {

    private List<AddressDto> addressDtoList;

    public AddressEmbeddedVO() {
    }

    public AddressEmbeddedVO(List<AddressDto> addressDtoList) {
        this.addressDtoList = addressDtoList;
    }

    public List<AddressDto> getAddressDtoList() {
        return addressDtoList;
    }

    public void setAddressDtoList(List<AddressDto> addressDtoList) {
        this.addressDtoList = addressDtoList;
    }
}
