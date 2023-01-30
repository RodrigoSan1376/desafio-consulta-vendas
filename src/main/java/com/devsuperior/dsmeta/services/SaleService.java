package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public List<SaleSummaryDTO> searchSumary(String minDate, String maxDate){
		LocalDate min = this.validateMinDate(minDate);
		LocalDate max = this.validateMaxDate(maxDate);

		return repository.searchSumary(min, max).stream().map(x -> new SaleSummaryDTO(x)).toList();
	}

	public Page<SaleMinDTO> searchReport(String minDate, String maxDate, String name, Pageable pageable){
		LocalDate min = this.validateMinDate(minDate);
		LocalDate max = this.validateMaxDate(maxDate);

		return repository.searchReport(min, max, name, pageable);
	}

	private LocalDate validateMinDate(String minDate){
		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());

		if(minDate != null){
			return minDate.equals("") ? today.minusYears(1L) : LocalDate.parse(minDate);
		}else{
			return today.minusYears(1L);
		}
	}

	private LocalDate validateMaxDate(String maxDate){
		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());

		if(maxDate != null){
			return maxDate.equals("") ? today : LocalDate.parse(maxDate);
		}else{
			return today;
		}
	}
}
