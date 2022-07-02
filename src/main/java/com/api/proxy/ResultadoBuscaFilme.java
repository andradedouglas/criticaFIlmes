package com.api.proxy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ResultadoBuscaFilme {
    @JsonProperty("imdbID")
    private String imdbId;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Year")
    private Integer year;
    @JsonProperty("Poster")
    private String poster;
}
