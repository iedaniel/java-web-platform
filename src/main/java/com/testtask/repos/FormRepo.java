package com.testtask.repos;

import com.testtask.domain.Form;
import org.springframework.data.repository.CrudRepository;

public interface FormRepo extends CrudRepository<Form, Integer> {
}