package de.lhind.cookiejar.cookiejar.model;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface CookieJarWeightRepository extends PagingAndSortingRepository<CookieJarWeight, Long> {
	
	// TODO: method that delivers average of last three weights
	
	public List<CookieJarWeight> findTop3ByOrderByIdDesc();
//	public default String getAverage() {
//		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
//		
////		
////		if (findAll() != null) {
////			StreamSupport.stream(this.findAll().spliterator(), false).
////		}
////		return null;
//		return null;
//	}

}
