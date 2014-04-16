package cn.edu.tongji.cdi.soul.service;

import cn.edu.tongji.cdi.soul.domain.Person;

public interface PersonService {
	public Person getRandom();
	public Person getById(Long id);
	public void save(Person person);
}
