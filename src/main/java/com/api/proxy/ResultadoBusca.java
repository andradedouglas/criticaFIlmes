package com.api.proxy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoBusca {

    @JsonProperty("Search")
    private List<ResultadoBuscaFilme> resultList;

    @JsonProperty("totalResults")
    private Integer total;

    @JsonProperty("Response")
    private Boolean response;

//    @JsonProperty("Response")
//    public void setResponse(String response) {
//        this.response = Boolean.valueOf(response);
//    }
//
//    @JsonProperty("totalResults")
//    public void setTotal(String total) {
//        this.total = Integer.parseInt(total);
//    }
}