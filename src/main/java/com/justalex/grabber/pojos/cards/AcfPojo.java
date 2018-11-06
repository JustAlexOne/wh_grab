package com.justalex.grabber.pojos.cards;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AcfPojo {
    String card_number;
    CardImagePojo card_image;
}
