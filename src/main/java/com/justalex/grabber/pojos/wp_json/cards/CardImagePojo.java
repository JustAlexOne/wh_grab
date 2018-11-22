package com.justalex.grabber.pojos.wp_json.cards;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CardImagePojo {

    String card_number;
    private int ID;
    private int id;
    private String title;
    private String filename;
    private int filesize;
    private String url;
    private String link;
    private String alt;
    private String author;
    private String description;
    private String caption;
    private String name;
    private String status;
    private int uploaded_to;
    private String date;
    private String modified;
    private int menu_order;
    private String mime_type;
    private String type;
    private String subtype;
    private String icon;
    private int width;
    private int height;
    SizesPojo SizesObject;
}
