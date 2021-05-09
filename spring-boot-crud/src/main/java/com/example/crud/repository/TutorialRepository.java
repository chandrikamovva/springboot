package com.example.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.example.crud.model.Tutorial;
import java.util.List;



public interface TutorialRepository extends CrudRepository<Tutorial, Long>{
	List<Tutorial> findByPublished(boolean published);
	List<Tutorial> findByTitleContaining(String title);

}
