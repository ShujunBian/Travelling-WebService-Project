package cn.edu.tongji.cdi.soul.service;

import java.util.Random;

import org.springframework.stereotype.Service;

import cn.edu.tongji.cdi.soul.domain.Person;

@Service
public class PersonServiceImpl implements PersonService {

	String[] names = {"颜景豪", "Robert Ford", "Gottlieb Daimler", "Lt. General Masaharu Homma"};

	@Override
	public Person getRandom() {
		Person person = new Person();
		person.setName(randomName());
		person.setAge(randomAge());
		return person;
	}

	@Override
	public Person getById(Long id) {
		Person person = new Person();
		person.setName(names[id.intValue()]);
		person.setAge(50);
		return person;
	}
	
	@Override
	public void save(Person person) {
		// Save person to database ...
	}
	
	private Integer randomAge() {
		Random random = new Random();
		return 10 + random.nextInt(100);
	}

	private String randomName() {
		Random random = new Random();
		return names[random.nextInt(names.length)];
	}

}
