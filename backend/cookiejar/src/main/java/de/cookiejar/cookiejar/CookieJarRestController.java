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

	@RequestMapping("/")
	public String index() {
		return "Welcome to Cookie Jar. Ask /status for information about weight.";
	}

	@RequestMapping("/status")
	@Deprecated
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
	@Deprecated
	public String getAverage() {
		StringBuilder sb = new StringBuilder();
		sb.append("Durchschnitt der letzten 3 Werte: ");
		sb.append((CookieJarWeightVisualDelegate.getAverageValue(repository)));
		sb.append("\n");
		sb.append("Durchschnitt der letzten 3 Werte minus Leergewicht mit Deckel: ");
		sb.append((CookieJarWeightVisualDelegate.getAverageValueMinusEmptyWeightWithTop(repository)));
		sb.append("\n");
		sb.append("Durchschnitt der letzten 3 Werte minus Leergewicht ohne Deckel: ");
		sb.append((CookieJarWeightVisualDelegate.getAverageValueMinusEmptyWeightWithoutTop(repository)));
		return sb.toString();
	}
	
	// returns last 3 elements in json format
	@GetMapping("/last3elements")
	@Deprecated
	public List<CookieJarWeightVO> getLast3Elements() {
		log.info("last 3 elements called");
		return CookieJarWeightVisualDelegate.mapCookieJarWeightAsList(repository.findTop3ByOrderByIdDesc());
	}
	
	// Durchschnitt der letzten 3 Werte minus Leergewicht mit Deckel
	@GetMapping("/averageminuswithtop")
	@Deprecated
	public Double getAverageDoubleValueTop() {  
		return CookieJarWeightVisualDelegate.getAverageValueMinusEmptyWeightWithTop(repository);
	}
	
	// Durchschnitt der letzten 3 Werte minus Leergewicht ohne Deckel
	@GetMapping("/averageminuswithouttop")
	@Deprecated
	public Double getAverageDoubleValue() {  
		return CookieJarWeightVisualDelegate.getAverageValueMinusEmptyWeightWithoutTop(repository);
	}
	
	@GetMapping("/averageobject")
	@Deprecated
	public CookieJarWeightVO getAverageObject() {
		return CookieJarWeightVisualDelegate.getAverageObject(repository);
	}
	
	@GetMapping("/lastobject")
	public CookieJarWeightVO getLastObject() {
		return CookieJarWeightVisualDelegate.getLastObject(repository);
	}
}
