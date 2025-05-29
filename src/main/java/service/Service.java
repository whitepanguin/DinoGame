package service;

import domain.*;
import dto.DinoDTO;
import repository.Repository;

import java.util.List;
import java.util.stream.Collectors;

public class Service {
    private Repository repository = new Repository();

    public Service() {
        repository.save(new Parasaurolophus());
        repository.save(new Ichthyosaurus());
        repository.save(new Dimorphodon());
    }

    public List<DinoDTO> getAllDinoDTOs() {
        return repository.findAll().stream()
                .map(d -> new DinoDTO(d.id, d.name, d.hp, d.maxHp, d.skillCount, d.maxSkillCount))
                .collect(Collectors.toList());
    }

    public Dino getDinoById(int id) {
        return repository.findAll().stream().filter(d -> d.id == id).findFirst().map(this::copy).orElse(null);
    }

    private Dino copy(Dino d) {
        if (d instanceof Parasaurolophus) return new Parasaurolophus();
        if (d instanceof Ichthyosaurus) return new Ichthyosaurus();
        return new Dimorphodon();
    }
}