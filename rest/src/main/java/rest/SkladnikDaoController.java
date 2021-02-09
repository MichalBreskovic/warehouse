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
import warehouse.storage.Skladnik;
import warehouse.storage.SkladnikDao;

@RestController
public class SkladnikDaoController {

	SkladnikDao skladnikDao = DaoFactory.INSTANCE.getSkladnikDao();

	@RequestMapping("/skladnik")
	public List<Skladnik> getAll() {
		return skladnikDao.getAll();
	}

	@GetMapping("/skladnik/{id}")
	public ResponseEntity<Skladnik> getById(@PathVariable int id) {
		try {
			Skladnik skladnik = skladnikDao.getById(id);
			return new ResponseEntity<>(skladnik, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(value = "/skladnik")
	@ResponseStatus(HttpStatus.CREATED)
	public void addJednotka(@RequestBody Skladnik jednotka) {
		skladnikDao.save(jednotka);
	}

	@PutMapping("/skladnik")
	public ResponseEntity<Skladnik> updateJednotka(@RequestBody Skladnik skladnik) {
		try {
			skladnikDao.save(skladnik);
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/skladnik")
	public ResponseEntity<Skladnik> deleteJednotka(@RequestBody Skladnik skladnik) {
		try {
			skladnikDao.delete(skladnik.getId());
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/skladnik/{id}")
	public ResponseEntity<Skladnik> deleteJednotkaById(@PathVariable int id) {
		try {
			skladnikDao.delete(id);
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
