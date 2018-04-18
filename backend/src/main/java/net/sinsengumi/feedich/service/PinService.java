package net.sinsengumi.feedich.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import net.sinsengumi.feedich.model.Pin;
import net.sinsengumi.feedich.repository.PinRepository;

@Service
@AllArgsConstructor
public class PinService {

    private final PinRepository pinRepository;

    public int create(Pin pin) {
        // TODO: 重複チェック
        return pinRepository.create(pin);
    }

    public Pin findById(int id) {
        return pinRepository.findById(id);
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
