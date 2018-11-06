package com.justalex.grabber.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SizesPojo {

    private String thumbnail;

    @JsonProperty("thumbnail-width")
    private int thumbnail_width;

    @JsonProperty("thumbnail-height")
    private int thumbnail_height;

    private String medium;

    @JsonProperty("medium-width")
    private int medium_width;

    @JsonProperty("medium-height")
    private int medium_height;

    private String medium_large;

    @JsonProperty("medium_large-width")
    private int medium_large_width;

    @JsonProperty("medium_large-height")
    private int medium_large_height;

    private String large;

    @JsonProperty("large-width")
    private int large_width;

    @JsonProperty("large-height")
    private int large_height;
}
