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
import warehouse.storage.Sklad;
import warehouse.storage.SkladDao;

@RestController
public class SkladDaoController {

	SkladDao skladDao = DaoFactory.INSTANCE.getSkladDao();

	@RequestMapping("/sklad")
	public List<Sklad> getAll() {
		return skladDao.getAll();
	}

	@GetMapping("/sklad/{id}")
	public ResponseEntity<Sklad> getById(@PathVariable int id) {
		try {
			Sklad sklad = skladDao.getById(id);
			return new ResponseEntity<>(sklad, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(value = "/sklad")
	@ResponseStatus(HttpStatus.CREATED)
	public void addJednotka(@RequestBody Sklad jednotka) {
		skladDao.save(jednotka);
	}

	@PutMapping("/sklad")
	public ResponseEntity<Sklad> updateJednotka(@RequestBody Sklad sklad) {
		try {
			skladDao.save(sklad);
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/sklad")
	public ResponseEntity<Sklad> deleteJednotka(@RequestBody Sklad sklad) {
		try {
			skladDao.delete(sklad.getId());
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/sklad/{id}")
	public ResponseEntity<Sklad> deleteJednotkaById(@PathVariable int id) {
		try {
			skladDao.delete(id);
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
