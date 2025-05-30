package service;

import domain.Dino;
import dto.DinoDTO;
import repository.Repository;
import repository.DinoRepository;

import java.util.List;
import java.util.stream.Collectors;

public class Service {
    private final DinoRepository dinoRepo = new DinoRepository();

    public List<DinoDTO> getAllDinoDTOs() {
        return dinoRepo.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public DinoDTO getDinoDTOById(int id) {
        Dino d = dinoRepo.findById(id);
        return (d != null) ? toDTO(d) : null;
    }

    public Dino getDinoById(int id) {
        return dinoRepo.findById(id);
    }

    private DinoDTO toDTO(Dino d) {
        return new DinoDTO(
                d.id,
                d.name,
                d.hp,
                d.maxHp,
                d.skillCount,
                d.maxSkillCount
        );
    }
}

