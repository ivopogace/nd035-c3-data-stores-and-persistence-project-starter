package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.PetType;

import java.time.LocalDate;
import java.util.List;

public interface PetService {
    List<Pet> getPetsList();
    Pet getPetById(Long petId);
    List<Pet> getPetsListByOwnerId(Long ownerId);
    Pet addPet(Pet newPet);
    boolean petExists(String name, PetType type, LocalDate birthDate);
}
