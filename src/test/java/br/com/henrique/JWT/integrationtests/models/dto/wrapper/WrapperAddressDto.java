package br.com.henrique.JWT.integrationtests.models.dto.wrapper;

import br.com.henrique.JWT.integrationtests.models.vo.AddressEmbeddedVO;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WrapperAddressDto {

    @JsonProperty("_embedded")
    private AddressEmbeddedVO embedded;

    public WrapperAddressDto() {
    }

    public WrapperAddressDto(AddressEmbeddedVO embedded) {
        this.embedded = embedded;
    }

    public AddressEmbeddedVO getEmbedded() {
        return embedded;
    }

    public void setEmbedded(AddressEmbeddedVO embedded) {
        this.embedded = embedded;
    }
}
