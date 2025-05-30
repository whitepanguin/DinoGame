package repository;

import domain.Dino;
import java.util.*;

public class Repository {
    private final List<Dino> dinoList = new ArrayList<>();

    public void save(Dino d) {
        if (findById(d.id) == null) {
            dinoList.add(d);
        }
    }

    public List<Dino> findAll() {
        return dinoList;
    }

    public Dino findById(int id) {
        return dinoList.stream().filter(d -> d.id == id).findFirst().orElse(null);
    }
}
