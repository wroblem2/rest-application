package com.mwroblewski.controller;

import com.mwroblewski.bean.Category;
import com.mwroblewski.exception.Error;
import com.mwroblewski.exception.GeneralException;
import com.mwroblewski.exception.InfoException;
import com.mwroblewski.service.CategoryDTOService;
import com.mwroblewski.service.LoggerDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryDTOService categoryDTOService;

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping()
    public List<Category> getAll(){
        return categoryDTOService.getAllCategories();
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/getById")
    public Category getById(@RequestParam(name = "id", required = true) Long id){
        return categoryDTOService.getCategoryById(id);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/add")
    public void getById(@RequestBody Category category){
        categoryDTOService.addCategory(category);
    }


    @Autowired
    LoggerDTOService loggerDTOService;

    @ExceptionHandler(InfoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody Error infoException(InfoException e){
        loggerDTOService.persistLogger(e.getMessage(), e.getClass().toString());
        return new Error(e.getComment());
    }

    @ExceptionHandler(GeneralException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody Error generalException(GeneralException e){
        loggerDTOService.persistLogger(e.getComment(), e.getClass().toString());
        return new Error(e.getComment());
    }
}
