package de.cookiejar.cookiejar;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import de.cookiejar.cookiejar.model.ConfigurationVO;
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
	
	@GetMapping("/")
	public String index() {
		return "Welcome to Smart Cookie Jar.";
	}
	
	@GetMapping("/lastobject")
	public CookieJarWeightVO getLastObject() {
		log.debug("getLastObject called");
		return visualDelegate.getLastObject(repository);
	}
	
	@GetMapping("/object/{id}")
	public CookieJarWeightVO findById(@PathVariable String id) {
		return visualDelegate.getObjectById(repository, id);
	}
	
	@GetMapping("/last500objects")
	public List<CookieJarWeightVO> getLast500Objects() {
		return visualDelegate.getLast500Objects(repository);
	}
	
	@GetMapping("/configure/emptyweight/{emptyweight}")
	public double setEmptyWeight(@PathVariable String emptyweight) {
		visualDelegate.setEmptyWeight(Double.valueOf(emptyweight).doubleValue());
		log.debug("Empty weight was set to: " + visualDelegate.getEmptyWeight());
		return visualDelegate.getEmptyWeight();
	}
	
	@GetMapping("/configure/tolerance/{tolerance}")
	public double setTolerance(@PathVariable String tolerance) {
		visualDelegate.setTolerance(Double.valueOf(tolerance).doubleValue());
		log.debug("Tolerance was set to: " + visualDelegate.getTolerance());
		return visualDelegate.getTolerance();
	}
	
	@GetMapping("/configure/maximumweight/{maximumweight}")
	public double setMaximumWeight(@PathVariable String maximumweight) {
		visualDelegate.setMaximumWeight(Double.valueOf(maximumweight).doubleValue());
		log.debug("Maximum weight was set to: " + visualDelegate.getMaximumWeight());
		return visualDelegate.getMaximumWeight();
	}
	
	@GetMapping("/getconfiguration")
	public ConfigurationVO getCOnfiguration() {
		return visualDelegate.getConfiguration();
	}
}
