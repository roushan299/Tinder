package com.tinder.deckservice.mapper;

import com.tinder.deckservice.dto.DeckResponse;
import com.tinder.deckservice.dto.DeckUserDTO;
import com.tinder.deckservice.entity.Deck;
import com.tinder.deckservice.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DeckMapper {


    public static DeckResponse getDeckResponse(Long userId, List<DeckUserDTO> deckUserDTOList) {
        DeckResponse deckResponse = DeckResponse.builder()
                .id(userId)
                .potentialUser(deckUserDTOList)
                .build();
        return deckResponse;
    }

    public static List<Deck> createDeckEntity(Long userId, List<User> deckUserList) {
        List<Deck> deckList = new ArrayList<>();
        for (User user : deckUserList) {
            Deck deck = Deck.builder()
                    .userId(userId)
                    .potentialMatch(user.getId())
                    .build();
            deckList.add(deck);
        }
        return deckList;
    }

}
