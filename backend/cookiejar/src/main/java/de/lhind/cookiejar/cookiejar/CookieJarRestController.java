package de.lhind.cookiejar.cookiejar;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.lhind.cookiejar.cookiejar.error.TimestampTooOldException;
import de.lhind.cookiejar.cookiejar.model.CookieJarWeight;
import de.lhind.cookiejar.cookiejar.model.CookieJarWeightRepository;
import de.lhind.cookiejar.cookiejar.model.CookieJarWeightVO;
import de.lhind.cookiejar.cookiejar.model.CookieJarWeightVisualData;
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
	public String getStatus() {
		//String lastValue = "";
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
		sb.append((CookieJarWeightVisualData.getAverageValue(repository)));
		sb.append("\n");
		sb.append("Durchschnitt der letzten 3 Werte minus Leergewicht mit Deckel: ");
		sb.append((CookieJarWeightVisualData.getAverageValueMinusEmptyWeightWithTop(repository)));
		sb.append("\n");
		sb.append("Durchschnitt der letzten 3 Werte minus Leergewicht ohne Deckel: ");
		sb.append((CookieJarWeightVisualData.getAverageValueMinusEmptyWeightWithoutTop(repository)));

//		repository.findTop3ByOrderByIdDesc().forEach(c -> {
//			sb.append(c.getWeight());
//			sb.append(" ");
//			sb.append(c.getTimeStamp());
//			sb.append("\n");
//		});
		return sb.toString();
	}
	
	// returns last 3 elements in json format
	@GetMapping("/last3elements")
	public List<CookieJarWeightVO> getLast3Elements() {
		log.info("last 3 elements called");
		return CookieJarWeightVisualData.mapCookieJarWeightAsList(repository.findTop3ByOrderByIdDesc());
	}
	
	// Durchschnitt der letzten 3 Werte minus Leergewicht mit Deckel
	@GetMapping("/averageminuswithtop")
	public Double getAverageDoubleValueTop() {  
		return CookieJarWeightVisualData.getAverageValueMinusEmptyWeightWithTop(repository);
	}
	
	// Durchschnitt der letzten 3 Werte minus Leergewicht ohne Deckel
	@GetMapping("/averageminuswithouttop")
	public Double getAverageDoubleValue() {  
		return CookieJarWeightVisualData.getAverageValueMinusEmptyWeightWithoutTop(repository);
	}
	
	// throws exception, other methods wont
	@GetMapping("/averageobject")
	public CookieJarWeightVO getAverageObject() {
		return CookieJarWeightVisualData.getAverageObject(repository);
	}
	
	// throws exception, other methods wont
	@GetMapping("/lastobject")
	public CookieJarWeightVO getLastObject() {
		return CookieJarWeightVisualData.getLastObject(repository);
	}
	
	@GetMapping("/throwexc")
	public Double throwExTest() {
		throw new TimestampTooOldException("test");
	}
}
