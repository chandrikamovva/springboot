package com.example.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.Repository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.crud.model.Tutorial;
import com.example.crud.repository.TutorialRepository;
import java.util.*;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class TutorialController {
	
	@Autowired
	TutorialRepository tutorialRepository;
	
	@PostMapping("/tutorials")
	public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
		try {
			Tutorial _tutorial = tutorialRepository
					.save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), false));
			System.out.println("save is calling");
			return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@GetMapping("/tutorials")
	public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false)String title){
		try {
		   List<Tutorial> tutorials = new ArrayList<Tutorial>();
		   if(title == null) {
			   tutorialRepository.findAll().forEach(tutorials::add);
		   }else {
			   tutorialRepository.findByTitleContaining(title).forEach(tutorials::add);
		   }
		   if (tutorials.isEmpty()) {
		        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		      }
		
		      return new ResponseEntity<>(tutorials, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@GetMapping("/tutorials/{id}")
	public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") Long id){
		Optional<Tutorial> tutorialData = tutorialRepository.findById(id);
		if(tutorialData.isPresent()) {
			return new ResponseEntity<>(tutorialData.get(),HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/tutorials/published")
	public ResponseEntity<List<Tutorial>> findByPublished(){
		try {
		List<Tutorial> tutorial = new ArrayList<Tutorial>();
		tutorial = tutorialRepository.findByPublished(true);
			if(tutorial.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}else {
				return new ResponseEntity<>(tutorial,HttpStatus.OK);
			}
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/tutorials/{id}")
	public ResponseEntity<Tutorial> updateTutorialById(@PathVariable("id") Long id,@RequestBody Tutorial tutorial){
		Optional<Tutorial> tutorialData = tutorialRepository.findById(id);
		if(tutorialData.isPresent()) {
			Tutorial _tutorial = tutorialData.get();
			_tutorial.setTitle(tutorial.getTitle());
			_tutorial.setDescription(tutorial.getDescription());
			_tutorial.setPublished(tutorial.isPublished());
			return new ResponseEntity<>(tutorialRepository.save(_tutorial),HttpStatus.OK);
			
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/tutorials/{id}")
	public ResponseEntity<HttpStatus> deleteTutorialById(@PathVariable("id") Long id){
		try {
		      tutorialRepository.deleteById(id);
		      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		    } catch (Exception e) {
		      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		    }
	}
	
	@DeleteMapping("/tutorials")
	public ResponseEntity<HttpStatus> deleteAll(){
		try {
		tutorialRepository.deleteAll();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
		}catch(Exception e) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
}
