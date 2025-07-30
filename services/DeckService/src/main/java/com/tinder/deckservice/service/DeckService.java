package com.tinder.deckservice.service;

import com.tinder.deckservice.dto.DeckResponse;
import com.tinder.deckservice.dto.DeckUserDTO;
import com.tinder.deckservice.entity.Deck;
import com.tinder.deckservice.exception.ProfileDoesntExits;
import com.tinder.deckservice.mapper.DeckMapper;
import com.tinder.deckservice.repository.DeckRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeckService implements IDeckService {

    private static final Logger logger = LoggerFactory.getLogger(DeckService.class);

    private final DeckRepository deckRepository;
    private final IUserService userService;

    public DeckService(DeckRepository deckRepository, IUserService userService) {
        this.deckRepository = deckRepository;
        this.userService = userService;
    }

    @Override
    public DeckResponse getTopOfDeck(Long userId) {
        logger.info("Fetching top of deck for userId: {}", userId);

        if (!userService.exitsUserById(userId)) {
            logger.warn("User with id {} does not exist", userId);
            throw new ProfileDoesntExits("User doesn't exist with id: " + userId);
        }

        List<Deck> deckList = deckRepository.findByUserId(userId);
        logger.debug("Fetched {} deck entries from repository for userId {}", deckList.size(), userId);

        if (deckList.isEmpty()) {
            logger.info("No existing deck found for userId {}, generating new deck", userId);
            deckList = generateDeckForUser(userId);
        }

        List<DeckUserDTO> deckUserDTOList = new ArrayList<>();
        for (Deck deck : deckList) {
            Long potentialMatchId = deck.getPotentialMatch();
            logger.debug("Fetching user profile for potential match id: {}", potentialMatchId);
            DeckUserDTO deckUserDTO = userService.getUserById(potentialMatchId);
            deckUserDTOList.add(deckUserDTO);
        }

        DeckResponse deckResponse = DeckMapper.getDeckResponse(userId, deckUserDTOList);
        logger.info("DeckResponse generated successfully for userId {}", userId);
        return deckResponse;
    }

    private List<Deck> generateDeckForUser(Long userId) {
        logger.info("Generating deck for userId: {}", userId);
        // TODO: Implement logic to generate deck
        return new ArrayList<>();
    }
}
