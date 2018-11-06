package com.justalex.grabber;

import com.justalex.grabber.pojos.cards.CardPojo;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Grabber {

    // https://warhammerunderworlds.com/wp-json/wp/v2/cards/?ver=13&per_page=2
    private final static int MAX_CARDS = 814;

    public static void main(String[] args) throws UnirestException, IOException {
        // mapper.getTypeFactory().constructCollectionType(IdentifiedList.class, pojoClass));
        CustomPojoMapper mapper = CustomPojoMapper.getInstance();
//        String respStr = Unirest.get("https://warhammerunderworlds.com/wp-json/wp/v2/cards/?ver=13&per_page=10").asString().getBody();
//        String respStr = Unirest.get("https://warhammerunderworlds.com/wp-json/wp/v2/cards/?ver=13&per_page=" + MAX_CARDS).asString().getBody();
//        String respStr = new String(Files.readAllBytes(Paths.get("./a_2_cards.txt")));
        String respStr = new String(Files.readAllBytes(Paths.get("./a_814_cards.txt")));
        List<CardPojo> cardsList = mapper.readValue(respStr, mapper.getTypeFactory().constructCollectionType(ArrayList.class, CardPojo.class));
        String cardNumber = "0001";
        CardPojo cardPojo = cardsList.stream().filter(it -> it.getAcf().getCard_number().equals(cardNumber)).findFirst().orElseThrow(() -> new IllegalArgumentException("Card with number" + cardNumber + "not found"));
        System.out.println("URL: " + cardPojo.getAcf().getCard_image().getUrl());
        /*
        System.out.println("cardsList.size() = " + cardsList.size());
        List<Integer> cardTypeIds = cardsList.stream().map(CardPojo::getCard_types).flatMap(List::stream).collect(Collectors.toList());
        System.out.println("cardTypeIds = " + cardTypeIds);
        Map<Integer, Long> collect = cardsList.stream().collect(Collectors.groupingBy(x -> x.getCard_types().get(0), Collectors.counting()));
        collect.entrySet().forEach(System.out::println);
        */
    }

   /*public static void main2(String[] args) throws IOException, UnirestException {
        String respStr = Unirest.get("https://warhammerunderworlds.com/wp-json/wp/v2/cards/?ver=13&per_page=100").asString().getBody();
        Files.write(Paths.get("./a_100_cards.txt"), respStr.getBytes());

    }*/
}
