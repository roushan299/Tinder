package com.tinder.deckservice.service;

import com.tinder.deckservice.dto.DeckResponse;
import com.tinder.deckservice.dto.DeckUserDTO;
import com.tinder.deckservice.entity.Deck;
import com.tinder.deckservice.entity.User;
import com.tinder.deckservice.exception.DeckPersistenceException;
import com.tinder.deckservice.exception.ProfileDoesntExits;
import com.tinder.deckservice.mapper.DeckMapper;
import com.tinder.deckservice.repository.DeckRepository;
import com.tinder.deckservice.util.DeckUtil;
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
    private final DeckUtil deckUtil;

    public DeckService(DeckRepository deckRepository, IUserService userService, DeckUtil deckUtil) {
        this.deckRepository = deckRepository;
        this.userService = userService;
        this.deckUtil = deckUtil;
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
            persistsDeckList(deckList);
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

    private void persistsDeckList(List<Deck> deckList) {
        if (deckList == null || deckList.isEmpty()) {
            logger.info("No decks to persist, skipping DB save.");
            return;
        }

        try {
            deckRepository.saveAll(deckList);
            logger.info("Successfully persisted {} decks.", deckList.size());
        } catch (Exception e) {
            logger.error("Error persisting deck list: {}", e.getMessage(), e);
            throw new DeckPersistenceException("Error persisting decks to database", e);
        }
    }


    @Override
    public List<Deck> getAllDeckByUserId(long id) {
        logger.debug("Fetching decks for user ID: {}", id);
        return deckRepository.findByUserIdOrPotentialMatch(id, id);
    }

    @Override
    public void deleteDecks(List<Deck> deckList) {
        deckRepository.deleteAll(deckList);
        logger.info("Deleted {} decks", deckList.size());
    }

    private List<Deck> generateDeckForUser(Long userId) {
        logger.info("Generating deck for userId: {}", userId);
        List<User> deckUserList = deckUtil.generateDeckForUser(userId);
        List<Deck> deckList = DeckMapper.createDeckEntity(userId, deckUserList);
        return deckList;
    }

}
