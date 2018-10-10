package com.apap.tutorial5.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
public class DealerController{
	@Autowired
	private DealerService dealerService;
	
	@Autowired
	private CarService carService;
	
	@RequestMapping("/")
	private String home(Model model) {
		model.addAttribute("title", "Home");
		return "home";
	}
	
	@RequestMapping(value = "/dealer/add", method = RequestMethod.GET)
	private String add(Model model) {
		model.addAttribute("dealer", new DealerModel());
		model.addAttribute("title", "Add Dealer Succeed");
		return "addDealer";
	}
	
	@RequestMapping(value = "/dealer/delete/{dealerId}", method = RequestMethod.GET)
	private String delete(@PathVariable(value = "dealerId") Long dealerId, Model model) {
		DealerModel dealer = dealerService.getDealerDetailById(dealerId).get();
		List<CarModel> myCar = dealer.getListCar();
		if(!myCar.isEmpty()) {
			for (CarModel car : dealer.getListCar()) {
				carService.deleteCar(car);
			}
		}
		
		dealerService.deleteDealer(dealer);
		model.addAttribute("title", "Delete Car");
		return "delete";
	}
	
	
	@RequestMapping(value = "/dealer/view", method = RequestMethod.GET)
	private String viewDealer(@RequestParam(value = "dealerId") Long dealerId, Model model) {
		DealerModel dealer = dealerService.getDealerDetailById(dealerId).get();
		List<CarModel> myCar = dealer.getListCar();
		Collections.sort(myCar, CarComparatorByPrice);
		dealer.setListCar(myCar);
		model.addAttribute("dealer",dealer);
		model.addAttribute("title", "View Dealer");
		return "view-dealer";
	}
	
	
	@RequestMapping(value = "/dealer/view/all", method = RequestMethod.GET)
	private String viewAllDealer(Model model) {
		List<DealerModel> myDealer = dealerService.viewAllDealer();
		model.addAttribute("dealer", myDealer);
		model.addAttribute("title", "View All Dealer");
		return "all-dealer";
	}
	
	@RequestMapping(value = "/dealer/add", method = RequestMethod.POST)
	private String addDealerSubmit(@ModelAttribute DealerModel dealer, Model model) {
		dealerService.addDealer(dealer);
		model.addAttribute("title", "Add Dealer Succeed");
		return "add";
	}
	
	@RequestMapping(value = "/dealer/update/{dealerId}", method = RequestMethod.GET)
	private String update(@PathVariable(value = "dealerId") Long dealerId, Model model) {
		DealerModel dealer = dealerService.getDealerDetailById(dealerId).get();
		DealerModel newDealer = new DealerModel();
		newDealer.setId(dealer.getId());
		model.addAttribute("dealer",newDealer);
		model.addAttribute("oldDealer",dealer);
		model.addAttribute("title", "Update Dealer");
		return "updateDealer";
	}
	
	@RequestMapping(value = "/dealer/update", method = RequestMethod.POST)
	private String updateDealerSubmit(@ModelAttribute DealerModel dealer, Model model) {
		DealerModel real = dealerService.getDealerDetailById(dealer.getId()).get();
		real.setAlamat(dealer.getAlamat());
		real.setNoTelp(dealer.getNoTelp());
		dealerService.addDealer(real);
		model.addAttribute("title", "Update Dealer Succeed");
		return "update";
	}
	public static Comparator<CarModel> CarComparatorByPrice = new Comparator<CarModel>() {
		public int compare (CarModel o1, CarModel o2) {
			Long price1 = o1.getPrice();
			Long price2 = o2.getPrice();
			return price1.compareTo(price2);
		}
	};
	
}
