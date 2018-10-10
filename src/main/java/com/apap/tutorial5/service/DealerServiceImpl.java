package com.apap.tutorial5.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apap.tutorial5.model.CarModel;
import com.apap.tutorial5.model.DealerModel;
import com.apap.tutorial5.repository.CarDb;
import com.apap.tutorial5.repository.DealerDb;

@Service
@Transactional
public class DealerServiceImpl implements DealerService{
	
	private CarService carService;
	
	@Autowired
	private DealerDb dealerDb;

	@Override
	public Optional<DealerModel> getDealerDetailById(Long id) {
		return dealerDb.findById(id);
	}

	@Override
	public void addDealer(DealerModel dealer) {
		dealerDb.save(dealer);
		
	}

	@Override
	public void deleteDealer(DealerModel dealer) {
		System.out.println("masuk delete");
		System.out.println("size "+dealer.getListCar().size());
		
		dealerDb.delete(dealer);
		
	}

	@Override
	public List<DealerModel> viewAllDealer() {
		List<DealerModel> list = dealerDb.findAll();
		return list;
	}

	
	
}
