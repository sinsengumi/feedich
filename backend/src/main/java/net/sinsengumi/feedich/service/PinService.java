package net.sinsengumi.feedich.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.model.Pin;
import net.sinsengumi.feedich.repository.PinRepository;

@Slf4j
@Service
@AllArgsConstructor
public class PinService {

    private final PinRepository pinRepository;

    public int create(Pin pin) {
        return pinRepository.create(pin);
    }

    public List<Pin> findByUserId(int userId) {
        return pinRepository.findByUserId(userId);
    }

    public int delete(int id) {
        return pinRepository.delete(id);
    }

    public int clear(int userId) {
        return pinRepository.clear(userId);
    }
}
