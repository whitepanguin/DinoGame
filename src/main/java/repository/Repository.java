package repository;

import domain.Dino;

import java.util.*;

public class Repository {
    private final List<Dino> dinoList = new ArrayList<>();

    public void save(Dino d) { dinoList.add(d); }
    public List<Dino> findAll() { return dinoList; }
}