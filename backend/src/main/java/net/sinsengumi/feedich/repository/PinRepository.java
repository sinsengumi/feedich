package net.sinsengumi.feedich.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import net.sinsengumi.feedich.model.Pin;

@Repository
public interface PinRepository {

    int create(Pin pin);

    Pin findById(int id);

    List<Pin> findByUserId(int userId);

    int delete(int id);

    int clear(int userId);
}
