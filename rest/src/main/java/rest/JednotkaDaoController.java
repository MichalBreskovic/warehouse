package rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import warehouse.storage.DaoFactory;
import warehouse.storage.EntityNotFoundException;
import warehouse.storage.Jednotka;
import warehouse.storage.JednotkaDao;

@RestController
public class JednotkaDaoController {

	JednotkaDao jednotkaDao = DaoFactory.INSTANCE.getJednotkaDao();

	@RequestMapping("/jednotka")
	public List<Jednotka> getAll() {
		return jednotkaDao.getAll();
	}

	@GetMapping("/jednotka/{id}")
	public ResponseEntity<Jednotka> getById(@PathVariable int id) {
		try {
			Jednotka jednotka = jednotkaDao.getById(id);
			return new ResponseEntity<>(jednotka, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(value = "/jednotka")
	@ResponseStatus(HttpStatus.CREATED)
	public void addJednotka(@RequestBody Jednotka jednotka) {
		jednotkaDao.save(jednotka);
	}
	
	@PutMapping("/jednotka")
	public ResponseEntity<Jednotka> updateJednotka(@RequestBody Jednotka jednotka) {
		try {
			jednotkaDao.save(jednotka);
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/jednotka")
	public ResponseEntity<Jednotka> deleteJednotka(@RequestBody Jednotka jednotka) {
		try {
			jednotkaDao.delete(jednotka.getId());
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	
	@DeleteMapping("/jednotka/{id}")
	public ResponseEntity<Jednotka> deleteJednotkaById(@PathVariable int id) {
		try {
			jednotkaDao.delete(id);
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
