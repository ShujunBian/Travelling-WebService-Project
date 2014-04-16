/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.tongji.cdi.soul.controller;

import cn.edu.tongji.cdi.soul.domain.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.tongji.cdi.soul.domain.Person;
import cn.edu.tongji.cdi.soul.service.PersonService;
import java.util.ArrayList;

/**
 *
 * @author bianshujun
 */
@Controller
@RequestMapping("webService/getHotel")
public class HotelController {

    PersonService personService;

    @Autowired
    public HotelController(PersonService personService) {
        this.personService = personService;
    }

    @RequestMapping("person/random")
    @ResponseBody
    public Person randomPerson() {
        return personService.getRandom();
    }

    @RequestMapping("person/{id}")
    @ResponseBody
    public Person getById(@PathVariable Long id) {
        return personService.getById(id);
    }

    /* same as above method, but is mapped to
     * /api/person?id= rather than /api/person/{id}
     */
    @RequestMapping(value = "person", params = "id")
    @ResponseBody
    public Person getByIdFromParam(@RequestParam("id") Long id) {
        return personService.getById(id);
    }

    /**
     * Saves new person. Spring automatically binds the name and age parameters
     * in the request to the person argument
     *
     * @param person
     * @return String indicating success or failure of save
     */
    @RequestMapping(value = "person", method = RequestMethod.POST)
    @ResponseBody
    public String savePerson(Person person) {
        personService.save(person);
        return "Saved person: " + person.toString();
    }

    @RequestMapping(value = "test", method = RequestMethod.GET)
    @ResponseBody
    public ArrayList<Hotel> test() {
        Hotel hotel = new Hotel();
        hotel.setCityName("shanghai");

        Hotel hotel1 = new Hotel();
        hotel.setCityName("beijing");
        
        ArrayList list = new ArrayList();
        list.add(hotel);
        list.add(hotel1);
        return list;
    }
}
