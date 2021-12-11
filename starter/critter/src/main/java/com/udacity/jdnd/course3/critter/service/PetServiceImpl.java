package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.PetType;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PetServiceImpl implements PetService{
    private final PetRepository petRepository;

    public PetServiceImpl(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public List<Pet> getPetsList() {
        return petRepository.findAll();
    }

    @Override
    public Pet getPetById(Long petId) {
        return petRepository.findById(petId).get();
    }

    @Override
    public List<Pet> getPetsListByOwnerId(Long ownerId) {
        return petRepository.findAllByCustomer_Id(ownerId);
    }

    @Override
    public Pet addPet(Pet newPet) {
        return petRepository.save(newPet);
    }

    @Override
    public boolean petExists(String name, PetType type, LocalDate birthDate) {
        return petRepository.existsByNameAndTypeAndBirthDate(name, type, birthDate);
    }
}
