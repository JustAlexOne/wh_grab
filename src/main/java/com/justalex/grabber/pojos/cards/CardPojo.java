package com.justalex.grabber.pojos.cards;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CardPojo {

    int id;
    String slug;
    String status;
    String type;
    String link;
    Title title;
    String template;
    AcfPojo acf;
    List<Integer> card_types;
//    _links _linksObject;

    @Data
    private class Title {
        private String rendered;
    }

}
