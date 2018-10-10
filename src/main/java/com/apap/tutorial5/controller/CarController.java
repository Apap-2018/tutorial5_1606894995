package com.apap.tutorial5.controller;


import java.util.ArrayList;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tutorial5.model.CarModel;
import com.apap.tutorial5.model.DealerModel;
import com.apap.tutorial5.service.CarService;
import com.apap.tutorial5.service.DealerService;

@Controller
public class CarController {
	@Autowired
	private CarService carService;
	
	@Autowired
	private DealerService dealerService;
	
	
	
	@RequestMapping(value = "/car/add/{dealerId}", method = RequestMethod.GET)
	private String add(@PathVariable(value = "dealerId") Long dealerId, Model model) {
		//CarModel car = new CarModel();
		DealerModel dealer = dealerService.getDealerDetailById(dealerId).get();
		ArrayList<CarModel> carList = new ArrayList<CarModel>();
		carList.add(new CarModel());
		dealer.setListCar(carList);

		model.addAttribute("dealer",dealer);
		model.addAttribute("title", "Add Car");
		return "addCar";
	}
	
	@RequestMapping(value = "/car/update/{carId}", method = RequestMethod.GET)
	private String update(@PathVariable(value = "carId") Long carId, Model model) {
		CarModel car = carService.getCarDetailById(carId).get();
		CarModel newCar = new CarModel();
		newCar.setId(car.getId());
		model.addAttribute("car",newCar);
		model.addAttribute("oldCar",car);
		model.addAttribute("title", "Update Car");
		return "updateCar";
	}
	
	@RequestMapping(value = "/car/update", method = RequestMethod.POST)
	private String updateCarSubmit(@ModelAttribute CarModel car, Model model) {
		CarModel realCar = carService.getCarDetailById(car.getId()).get();
		realCar.setAmount(car.getAmount());
		realCar.setBrand(car.getBrand());
		realCar.setPrice(car.getPrice());
		realCar.setType(car.getType());
		carService.addCar(realCar);
		model.addAttribute("title", "Update Succeed");
		return "update";
	}
	
	@RequestMapping(value = "/car/add/{dealerId}", method = RequestMethod.POST, params={"save"})
	private String addCarSubmit(@ModelAttribute DealerModel dealer, Model model) {
		DealerModel getDealer = dealerService.getDealerDetailById(dealer.getId()).get();
		for (CarModel car : dealer.getListCar()) {
			car.setDealer(getDealer);
			carService.addCar(car);
		}
		model.addAttribute("title", "Add succeed");
		return "add";
	}
	
	@RequestMapping(value="/car/add/{dealerId}", params= {"addRow"}, method = RequestMethod.POST)
	private String addRow (@ModelAttribute DealerModel dealer, Model model) {
		if (dealer.getListCar() ==  null) {
			dealer.setListCar(new ArrayList<CarModel>());
		}
		dealer.getListCar().add(new CarModel());
		model.addAttribute("dealer", dealer);
		return "addCar";
	}
	
	@RequestMapping(value="/car/add/{dealerId}", params= {"removeRow"},method = RequestMethod.POST)
	private String removeRow (@ModelAttribute DealerModel dealer, HttpServletRequest req, Model model) {
		Integer rowId = Integer.valueOf(req.getParameter("removeRow"));
		dealer.getListCar().remove(rowId.intValue());
		model.addAttribute("dealer", dealer);
		return "addCar";
	}
	
	@RequestMapping(value = "/car/delete", method = RequestMethod.POST)
	private String delete(@ModelAttribute DealerModel dealer, Model model) {
		for (CarModel car : dealer.getListCar()) {
			carService.deleteCar(car);
		}
		model.addAttribute("title", "Delete succeed");
		return "delete";
	}
	
}
