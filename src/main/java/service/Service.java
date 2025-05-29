//package service;
//
//import domain.*;
//import dto.DinoDTO;
//import repository.Repository;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class Service {
//    private Repository repository = new Repository();
//
//    public Service() {
//        repository.save(new Parasaurolophus(1));
//        repository.save(new Ichthyosaurus(2));
//        repository.save(new Dimorphodon(3));
//    }
//
//    public List<DinoDTO> getAllDinoDTOs() {
//        return repository.findAll().stream()
//                .map(d -> new DinoDTO(d.id, d.name, d.hp, d.maxHp, d.skillCount, d.maxSkillCount))
//                .collect(Collectors.toList());
//    }
//
//    public Dino getDinoById(int id) {
//        return repository.findAll().stream().filter(d -> d.id == id).findFirst().map(this::copy).orElse(null);
//    }
//
//
//    private Dino copy(Dino d) {
//        if (d instanceof Parasaurolophus) return new Parasaurolophus(1);
//        if (d instanceof Ichthyosaurus) return new Ichthyosaurus(2);
//        return new Dimorphodon(3);
//    }
//}

package service;

import domain.Dino;
import domain.DinoData;
import dto.DinoDTO;
import repository.Repository;

import java.util.List;
import java.util.stream.Collectors;

public class Service {
    private Repository repository = new Repository();

    // ✅ 생성자 정리: DB를 사용하므로 직접 save 불필요
    public Service() {
        // 삭제됨
    }

    // ✅ 전체 공룡 목록 DTO로 반환 (유지)
    public List<DinoDTO> getAllDinoDTOs() {
        return repository.findAll().stream()
                .map(d -> new DinoDTO(d.id, d.name, d.hp, d.maxHp, d.skillCount, d.maxSkillCount))
                .collect(Collectors.toList());
    }

    // ✅ id로 공룡 1마리 가져오기 (DB 직접 조회)
    public Dino getDinoById(int id) {
        return repository.findById(id);
    }
}
