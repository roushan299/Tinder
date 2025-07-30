package com.tinder.deckservice.mapper;

import com.tinder.deckservice.dto.DeckResponse;
import com.tinder.deckservice.dto.DeckUserDTO;
import org.springframework.stereotype.Component;
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
}
