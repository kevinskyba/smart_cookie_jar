package de.cookiejar.cookiejar;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.cookiejar.cookiejar.model.CookieJarWeight;
import de.cookiejar.cookiejar.model.CookieJarWeightRepository;
import de.cookiejar.cookiejar.model.CookieJarWeightVO;
import de.cookiejar.cookiejar.model.CookieJarWeightVisualDelegate;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CookieJarRestController {

	@Autowired
	CookieJarWeightRepository repository;
	
	@Autowired
	CookieJarWeightVisualDelegate visualDelegate;

	@RequestMapping("/")
	public String index() {
		return "Welcome to Cookie Jar. Ask /status for information about weight.";
	}

	@RequestMapping("/status")
	public String getStatus() {
		StringBuilder sb = new StringBuilder();
		if (repository.findAll() != null && repository.findAll().iterator() != null
				&& repository.findAll().iterator().hasNext()) {
			CookieJarWeight next = repository.findAll().iterator().next();
			sb.append(next.getWeight());
			sb.append(" time: ");
			sb.append(next.getTimeStamp());
		}
		return sb.toString();
	}

	@RequestMapping("/getaverage")
	public String getAverage() {
		StringBuilder sb = new StringBuilder();
		sb.append("Durchschnitt der letzten 3 Werte: ");
		sb.append((visualDelegate.getAverageValue(repository)));
		return sb.toString();
	}
	
	// returns last 3 elements in json format
	@GetMapping("/last3elements")
	public List<CookieJarWeightVO> getLast3Elements() {
		log.info("last 3 elements called");
		return visualDelegate.mapCookieJarWeightAsList(repository.findTop3ByOrderByIdDesc());
	}
	
	// throws exception, other methods wont
	@GetMapping("/averageobject")
	public CookieJarWeightVO getAverageObject() {
		return visualDelegate.getAverageObject(repository);
	}
	
	// throws exception, other methods wont
	@GetMapping("/lastobject")
	public CookieJarWeightVO getLastObject() {
		return visualDelegate.getLastObject(repository);
	}
	
	@GetMapping("/tareweight")
	public Boolean tareWeight() {
		return visualDelegate.tareWeight(repository);
	}
}
