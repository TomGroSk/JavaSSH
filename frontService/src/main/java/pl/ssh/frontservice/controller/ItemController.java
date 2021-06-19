package pl.ssh.frontservice.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.ssh.frontservice.model.Item;
import pl.ssh.frontservice.service.CustomerService;
import pl.ssh.frontservice.service.ItemsService;

import java.util.UUID;

@Controller
@RequestMapping("/item")
public class ItemController {
    private final ItemsService itemsService;
    private final CustomerService customerService;

    public ItemController(ItemsService itemsService, CustomerService customerService) {
        this.itemsService = itemsService;
        this.customerService = customerService;
    }

    @PostMapping("/create")
    public String postItem(@ModelAttribute("itemId")String itemId, @ModelAttribute("itemType")String itemType, Authentication authentication){
        Item item = new Item();
        item.setItemType(itemType);
        item.setItemId(UUID.fromString(itemId));
        item.setCustomer(customerService.getCustomerByUsername(authentication.getName()));
        itemsService.createItem(item);
        return "redirect:/" + itemType + "/" + itemId;
    }

    @PostMapping("/remove")
    public String deleteItem(@ModelAttribute("itemId")String itemId, @ModelAttribute("itemType")String itemType, Authentication authentication){
        var customer = customerService.getCustomerByUsername(authentication.getName());
        var item = itemsService.getItem(customer.getId(), UUID.fromString(itemId), itemType);
        itemsService.removeItem(item);
        return "redirect:/" + itemType + "/" + itemId;
    }
}
