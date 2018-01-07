package com.mwroblewski.service;

import com.mwroblewski.bean.Category;
import com.mwroblewski.bean.City;
import com.mwroblewski.exception.GeneralException;
import com.mwroblewski.exception.InfoException;
import com.mwroblewski.model.CityDTO;
import com.mwroblewski.repository.CityDTORepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CityDTOService {

    @Autowired
    CityDTORepository cityDTORepository;

    Logger logger = LoggerFactory.getLogger(CityDTOService.class);

    public CityDTO fromCityToCityDTO(City city){
        CityDTO cityDTO = cityDTORepository.findByName(city.getName().toUpperCase());

        return cityDTO;
    }

    public City fromCityDTOToCity(CityDTO cityDTO){
        City city = new City();

        city.setId(cityDTO.getId());
        city.setName(cityDTO.getName());

        return city;
    }

    @Transactional
    public List<City> getAllCities(){
        List<CityDTO> cityDTOS = cityDTORepository.findAll();

        List<City> cities = new ArrayList<>();
        for (CityDTO c : cityDTOS) {
            cities.add(fromCityDTOToCity(c));
        }

        if(cities.size() > 0)
            return cities;

        logger.info("Brak miasta do wyświtlenia.");
        throw new GeneralException("Brak miasta do wyświetlenia.");
    }

    @Transactional
    public City getCityById(Long id){
        CityDTO cityDTO = cityDTORepository.findById(id);

        if(cityDTO != null ){
            City city = fromCityDTOToCity(cityDTO);
            return city;
        }

        logger.info("Miasto zgodne z id: " + id + " nie istnieje.");
        throw new InfoException("Miasto zgodne z id: " + id + " nie istnieje.");

    }

    @Transactional
    public void addCity(City city){
        CityDTO cityDTO = cityDTORepository.findByName(city.getName().trim().toUpperCase());
        if(cityDTO == null){
            cityDTO = fromCityToCityDTO(city);
            cityDTORepository.save(cityDTO);
        }
        else{
            logger.info("Miasto " + city.getName() + " już istnieje.");
            throw new InfoException("Miasto " + city.getName() + " już istnieje.");
        }
    }
}
