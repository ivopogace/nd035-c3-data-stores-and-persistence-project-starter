package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.DTO.PetDTO;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final PetService petService;

    public PetController(ModelMapper modelMapper, UserService userService, PetService petService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.petService = petService;
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        try {
            if (petService.petExists(petDTO.getName(), petDTO.getType(), petDTO.getBirthDate()))
                throw new ValidationException(String
                        .format("Pet with name %s, type %s and birthdate %s already exists",
                                petDTO.getName(),
                                petDTO.getType(),
                                petDTO.getBirthDate()));
            return convertToPetDto(petService.addPet(convertToPetEntity(petDTO)));
        } catch (Exception ex){
            throw new ValidationException(ex.getMessage());
        }

    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        try {
            return convertToPetDto(petService.getPetById(petId));
        }catch (Exception ex){
            throw new EntityNotFoundException("Pet is not found with id: " + petId);
        }
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<Pet> petList = petService.getPetsList();
        List<PetDTO> petDTOList = new ArrayList<>();
        petList.forEach(pet -> petDTOList.add(convertToPetDto(pet)));
        return petDTOList;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> petList = petService.getPetsListByOwnerId(ownerId);
        if (petList.isEmpty())
            throw new EntityNotFoundException("Pets not found with owner id: " + ownerId);
        List<PetDTO> petDTOList = new ArrayList<>();
        petList.forEach(pet -> petDTOList.add(convertToPetDto(pet)));
        return petDTOList;
    }

    private Pet convertToPetEntity(PetDTO petDTO) {
        Pet pet = modelMapper.map(petDTO, Pet.class);
        pet.setType(petDTO.getType());
        pet.setName(petDTO.getName());
        pet.setCustomer(userService.getCustomerById(petDTO.getOwnerId()));
        pet.setBirthDate(petDTO.getBirthDate());
        pet.setNotes(petDTO.getNotes());
        return pet;
    }

    private PetDTO convertToPetDto(Pet pet) {
        PetDTO petDTO = modelMapper.map(pet, PetDTO.class);
        petDTO.setId(pet.getId());
        petDTO.setType(pet.getType());
        petDTO.setName(pet.getName());
        petDTO.setOwnerId(pet.getCustomer().getId());
        petDTO.setBirthDate(pet.getBirthDate());
        petDTO.setNotes(pet.getNotes());
        return petDTO;
    }
}
