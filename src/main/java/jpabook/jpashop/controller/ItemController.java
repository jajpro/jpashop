package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form) {
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId);

        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";

    }

    //url 에서 itemId 를 조작하는 경우가 있으므로 서버에서 제어 필요
    @PostMapping("items/{itemId}/edit")
    public String updateItem(@ModelAttribute("form") BookForm form) {

        //아래처럼 어설프게 Book 엔티티 만들지 말고 itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
        //한줄로 처리하는 것이 나음. 거기서, Item findItem = itemRepository.findOne(itemId); findItem.change(...); 하는 것이 최선
        //파라미터가 너무 많다 싶으면 updateItem(Long itemId, UpdateItemDto itemDto); 와 같이 DTO 객체 활용 가능.

        //cancel 과 비교
        //준영속 엔티티
        Book book = new Book();
        book.setId(form.getId());
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());
        //setter 보다는 setter 닫고 book.change(name, price, stock,...) 과 같은 메서드로 변경하는 것을 권장. set 을 여러군데에서 쓰면 추적이 어려움.

        //em.merge 로 넘어감. itemService.updateItem(book.getId(), book); 으로 쓰면 변경감지 방식
        itemService.saveItem(book); //merge 방식
        return "redirect:/items";
    }

}
